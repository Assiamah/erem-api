package com.api.ersmapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.api.ersmapi.utils.RateLimitInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor);
    }

    // Add CORS filter
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        config.addAllowedOriginPattern("*"); // Allows all origins
        config.addAllowedHeader("*");        // Allows all headers
        config.addAllowedMethod("*");        // Allows all methods (GET, POST, etc.)
        config.setAllowCredentials(true);    // Allow cookies/auth headers
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
