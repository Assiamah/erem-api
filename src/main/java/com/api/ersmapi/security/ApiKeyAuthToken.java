package com.api.ersmapi.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class ApiKeyAuthToken extends AbstractAuthenticationToken {

    private final String apiKey;

    public ApiKeyAuthToken(String apiKey) {
        super(Collections.emptyList());
        this.apiKey = apiKey;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null; // don’t expose raw key
    }

    @Override
    public Object getPrincipal() {
        return apiKey; // or return managerId if you have it
    }
}
