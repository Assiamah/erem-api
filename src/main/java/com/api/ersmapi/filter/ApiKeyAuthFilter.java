package com.api.ersmapi.filter;

import java.io.IOException;
import java.util.List;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(ApiKeyAuthFilter.class);
    private final String apiKeyHeaderName;
    private final String validApiKey;

    public ApiKeyAuthFilter(
            @Value("${api.key.header.name}") String apiKeyHeaderName,
            @Value("${api.key}") String encryptedValidApiKey,
            @Value("${jasypt.encryptor.password}") String jasyptPassword,
            @Value("${jasypt.encryptor.algorithm}") String jasyptAlgorithm) {

        this.apiKeyHeaderName = apiKeyHeaderName;
        this.validApiKey = decryptApiKey(encryptedValidApiKey, jasyptPassword, jasyptAlgorithm);

        if (this.validApiKey == null || this.validApiKey.isEmpty()) {
            throw new IllegalStateException("Failed to decrypt API key. Check: " +
                    "1. Jasypt password is correct\n" +
                    "2. Encryption algorithm matches\n" +
                    "3. The encrypted value is correct");
        }

        logger.info("API Key Filter initialized successfully");
    }

    private String decryptApiKey(String encryptedText, String password, String algorithm) {
        try {
            logger.debug("Attempting decryption with algorithm: {}", algorithm);

            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setPassword(password);
            encryptor.setAlgorithm(algorithm);

            return encryptor.decrypt(encryptedText);
        } catch (Exception e) {
            logger.error("Decryption failed. Details:", e);
            logger.error("Password used: [REDACTED]");
            logger.error("Algorithm used: [REDACTED]");
            logger.error("Encrypted text length: {}", encryptedText != null ? encryptedText.length() : 0);
            return null;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Skip auth for whitelisted endpoints
        if (shouldSkipAuthentication(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader(apiKeyHeaderName);

        if (!isValidApiKey(apiKey)) {
            logFailedAttempt(request, apiKey);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key");
            return;
        }

        Authentication auth =
                new UsernamePasswordAuthenticationToken("apiKeyUser", null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);

        logger.info("Valid API Key Attempt - IP: {}, URI: {}, Method: {}, Key Provided: {}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                request.getMethod(),
                apiKey != null ? "[REDACTED]" : "NULL");

        filterChain.doFilter(request, response);
    }

    private boolean shouldSkipAuthentication(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/swagger") ||
               uri.startsWith("/v3/api-docs") ||
               uri.startsWith("/actuator");
    }

    private boolean isValidApiKey(String apiKey) {
        //System.out.println(validApiKey);
        return apiKey != null && apiKey.equals(validApiKey);
    }

    private void logFailedAttempt(HttpServletRequest request, String apiKey) {
        logger.warn("Invalid API Key Attempt - IP: {}, URI: {}, Method: {}, Key Provided: {}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                request.getMethod(),
                apiKey != null ? "[REDACTED]" : "NULL");
    }
}