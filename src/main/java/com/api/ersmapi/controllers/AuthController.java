package com.api.ersmapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.ersmapi.config.DBConnection;
import com.api.ersmapi.models.auth.AuthModel;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/auth_service")
@Tag(name = "Authentication Service", description = "Authentication Service for TerraFinder Application")
public class AuthController {

    AuthModel authModel = new AuthModel();

    @Autowired
    private DBConnection dbConnection;

    @PostMapping("/user_login")
    public String userLogin(@RequestBody String jsonReq)  throws Exception {
        authModel.con = dbConnection.getConnection();
        String result = authModel.userLogin(jsonReq);
        authModel.con.close();
        return result;
    }
    
}
