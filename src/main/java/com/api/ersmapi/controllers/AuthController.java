package com.api.ersmapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.ersmapi.config.DBConnection;
import com.api.ersmapi.services.auth.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/auth_service")
@Tag(name = "Authentication Service", description = "Authentication Service for TerraFinder Application")
public class AuthController {

    AuthService authService = new AuthService();

    @Autowired
    private DBConnection dbConnection;

    @PostMapping("/user_login")
    public ResponseEntity<?> userLogin(@RequestBody String jsonReq)  throws Exception {
        authService.con = dbConnection.getConnection();
        String result = authService.userLogin(jsonReq);
        authService.con.close();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/verify_otp")
    public ResponseEntity<?> verifyOtp(@RequestBody String jsonReq)  throws Exception {
        authService.con = dbConnection.getConnection();
        String result = authService.verifyOtp(jsonReq);
        authService.con.close();
        return ResponseEntity.ok(result);
    }

    
}
