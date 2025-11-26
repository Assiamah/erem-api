package com.api.ersmapi.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.api.ersmapi.security.ApiKeyAuthToken;
import com.api.ersmapi.services.ApiKeyService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(ApiKeyAuthFilter.class);
    private static final String API_KEY_HEADER = "X-API-KEY";
    private final ApiKeyService apiKeyService;

    // List of paths to exclude from API key authentication
    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
        "/swagger-ui/**",
        "/api/v1/auth/token",
        "/api/v1/auth/api-key",
        "/api/v1/payment-service/process-payment-callback"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        String apiKey = getApiKeyFromRequest(request);

        // Skip API key validation for excluded paths
        if (isExcludedPath(requestURI)) {

            logger.info("No API Key Attempt - IP: {}, URI: {}, Method: {}, Key Provided: {}",
                    request.getRemoteAddr(),
                    request.getRequestURI(),
                    request.getMethod(),
                    apiKey != null ? "[REDACTED]" : "NULL");

            filterChain.doFilter(request, response);
            return;
        }

        // System.out.println(apiKey);

        if (apiKey != null) {
            var apiKeyEntity = apiKeyService.validateApiKey(apiKey);
            
            if (apiKeyEntity.isPresent()) {
                var authentication = new ApiKeyAuthToken(apiKey);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.info("Valid API Key Attempt - IP: {}, URI: {}, Method: {}, Key Provided: {}",
                    request.getRemoteAddr(),
                    request.getRequestURI(),
                    request.getMethod(),
                    apiKey != null ? "[REDACTED]" : "NULL");
            } else {
                logFailedAttempt(request, apiKey);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key");
                return;
            }
        } else {
            // No API key provided for protected endpoint
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "API Key required");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isExcludedPath(String requestURI) {
        return EXCLUDED_PATHS.stream().anyMatch(requestURI::startsWith);
    }

    private String getApiKeyFromRequest(HttpServletRequest request) {
        String apiKey = request.getHeader(API_KEY_HEADER);
        
        if (apiKey == null) {
            apiKey = request.getParameter("api_key");
        }
        
        return apiKey;
    }

    private void logFailedAttempt(HttpServletRequest request, String apiKey) {
        logger.warn("Invalid API Key Attempt - IP: {}, URI: {}, Method: {}, Key Provided: {}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                request.getMethod(),
                apiKey != null ? "[REDACTED]" : "NULL");
    }
}