package com.api.ersmapi.filter;

import java.io.IOException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain)
            throws ServletException, IOException {
        
        if (logger.isDebugEnabled()) {
            logger.debug("Request: {} {}", request.getMethod(), request.getRequestURI());
            // Sanitize headers before logging
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String header = headerNames.nextElement();
                if (!header.equalsIgnoreCase("Authorization") && 
                    !header.equalsIgnoreCase("X-API-KEY")) {
                    logger.debug("Header: {} = {}", header, request.getHeader(header));
                }
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
}
