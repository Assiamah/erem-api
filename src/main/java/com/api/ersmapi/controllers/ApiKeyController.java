package com.api.ersmapi.controllers;

import com.api.ersmapi.services.ApiKeyService;
import com.api.ersmapi.utils.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/v1/auth")
@Tag(name = "ApiKey Service", description = "ApiKey Service for DiLAP Application")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;
    private final JwtUtil jwtUtil;

    @PostMapping("/token")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        try {
            ApiKeyService.ManagerResult manager = apiKeyService.checkExistingManagerForToken(
                    request.getUsername(),
                    request.getPassword()
            );

            if (manager == null || !manager.success()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                    .body("Invalid username or password");
            }

            String token = jwtUtil.generateToken(manager.managerId(), manager.username());
            return ResponseEntity.ok(new AuthResponse(token, "Bearer"));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Database error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error processing request: " + e.getMessage());
        }
    }

    @PostMapping("/api-key")
    public ResponseEntity<?> apiKey(@RequestBody String jsonReq) {
        try {
            String result = apiKeyService.checkExistingManagerForApiKey(jsonReq);
            return ResponseEntity.ok(result);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Database error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error generating API key: " + e.getMessage());
        }
    }

    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
        private String email;
    }

    @Data
    @RequiredArgsConstructor
    public static class AuthResponse {
        private final String accessToken;
        private final String tokenType;
    }
}