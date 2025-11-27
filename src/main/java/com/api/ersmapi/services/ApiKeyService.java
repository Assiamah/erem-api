package com.api.ersmapi.services;

import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.security.SecureRandom;
import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApiKeyService {

    private final DataSource dataSource;

    public ApiKeyService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static final int API_KEY_LENGTH = 64;
    private static final int PREFIX_LENGTH = 8;
    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public Optional<ApiKeyEntity> validateApiKey(String apiKey) {
        // System.out.println(apiKey);
        try (Connection con = dataSource.getConnection()) {
            String SQL = "SELECT * FROM security.validate_api_key(?)";
            
            try (PreparedStatement pstmt = con.prepareStatement(SQL)) {
                pstmt.setString(1, apiKey);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next() && rs.getBoolean("valid")) {
                        ApiKeyEntity entity = new ApiKeyEntity(
                                (UUID) rs.getObject("api_key_id"),
                                (UUID) rs.getObject("manager_id"),
                                rs.getString("name"),
                                (String[]) rs.getArray("scopes").getArray(),
                                rs.getInt("rate_limit"),
                                rs.getTimestamp("expires_at") != null ? 
                                    rs.getTimestamp("expires_at").toInstant() : null
                        );
                        return Optional.of(entity);
                    }
                }
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error validating API key: " + e.getMessage(), e);
        }
    }

    public ManagerResult checkExistingManagerForToken(String username, String password) throws SQLException {
        try (Connection con = dataSource.getConnection()) {
            String SQL = "SELECT * FROM security.check_existing_manager_for_token(?, ?)";
            
            try (PreparedStatement pstmt = con.prepareStatement(SQL)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new ManagerResult(
                            rs.getBoolean("success"),
                            (UUID) rs.getObject("manager_id"),
                            rs.getString("username")
                        );
                    }
                }
            }
            return null;
        }
    }

    public String checkExistingManagerForApiKey(String jsonReq) throws SQLException {
        try (Connection con = dataSource.getConnection()) {
            String SQL = "SELECT * FROM security.check_existing_manager_for_api_key(?::json)";
            
            try (PreparedStatement pstmt = con.prepareStatement(SQL)) {
                pstmt.setString(1, jsonReq);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        boolean success = rs.getBoolean("success");
                        UUID managerId = (UUID) rs.getObject("manager_id");
                        String managerName = rs.getString("manager_name");

                        if (success && managerId != null) {
                            String[] scopes = {"read", "write"};
                            return generateAndSaveApiKey(
                                managerId,
                                managerName,
                                scopes,
                                1000,
                                Duration.ofDays(30)
                            );
                        } else {
                            return "Manager not found";
                        }
                    }
                }
            }

            return "No result from database";
        }
    }

    public String generateAndSaveApiKey(UUID managerId, String name, String[] scopes,
                                    Integer rateLimit, Duration validity) throws SQLException {
        
        try (Connection con = dataSource.getConnection()) {
            String rawApiKey = generateRandomString(API_KEY_LENGTH);
            String prefix = generateRandomString(PREFIX_LENGTH);
            String apiKeyWithPrefix = prefix + "_" + rawApiKey;

            String apiKeyHash = hashApiKey(rawApiKey);
            Instant expiresAt = (validity != null ? Instant.now().plus(validity) : null);

            String SQL = "SELECT security.save_api_key(?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = con.prepareStatement(SQL)) {
                pstmt.setString(1, apiKeyHash);
                pstmt.setObject(2, managerId);
                pstmt.setString(3, name);
                pstmt.setString(4, prefix);
                pstmt.setArray(5, con.createArrayOf("text", scopes != null ? scopes : new String[0]));
                pstmt.setInt(6, rateLimit != null ? rateLimit : 1000);
                
                if (expiresAt != null) {
                    pstmt.setTimestamp(7, Timestamp.from(expiresAt));
                } else {
                    pstmt.setNull(7, Types.TIMESTAMP);
                }

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        UUID generatedId = (UUID) rs.getObject(1);
                        System.out.println("Saved API key with id: " + generatedId);
                    }
                }
            }

            return apiKeyWithPrefix;
        }
    }

    private String generateRandomString(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    private String hashApiKey(String apiKey) {
        return Base64.getEncoder().encodeToString(apiKey.getBytes());
    }

    public static record ManagerResult(boolean success, UUID managerId, String username) {}
    
    public static record ApiKeyEntity(
            UUID apiKeyId,
            UUID managerId,
            String name,
            String[] scopes,
            Integer rateLimit,
            Instant expiresAt
    ) {}

    public boolean checkExistingWhitelistedIP(String requestIp) throws SQLException {
        try (Connection con = dataSource.getConnection()) {
            String SQL = "SELECT security.check_existing_whitelisted_ip(?)";
            
            try (PreparedStatement pstmt = con.prepareStatement(SQL)) {
                pstmt.setString(1, requestIp);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getBoolean(1);
                    }
                }
            }
            return false;
        }
    }
}