package com.api.ersmapi.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.google.common.util.concurrent.RateLimiter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitInterceptor implements HandlerInterceptor  {

    private final Map<String, RateLimiter> limiters = new ConcurrentHashMap<>();
    
    @Value("${rate.limit}")
    private int rateLimit;

    @Override
    public boolean preHandle(HttpServletRequest request, 
                           HttpServletResponse response, 
                           Object handler) throws Exception {
        
        String apiKey = request.getHeader("X-API-KEY");
        if (apiKey == null) {
            return true; // Let other security filters handle this
        }
        
        RateLimiter rateLimiter = limiters.computeIfAbsent(
            apiKey, key -> RateLimiter.create(rateLimit));
        
        if (!rateLimiter.tryAcquire()) {
            response.sendError(429, "Rate limit exceeded");
            return false;
        }
        
        return true;
    }
}
