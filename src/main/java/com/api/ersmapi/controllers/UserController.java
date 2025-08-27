package com.api.ersmapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.ersmapi.config.DBConnection;
import com.api.ersmapi.models.users.UserModel;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/user_service")
@Tag(name = "User Service", description = "User Service for TerraFinder Application")
public class UserController {
    UserModel userModel = new UserModel();

    @Autowired
    private DBConnection dbConnection;

    @GetMapping("/load_users")
    public String loadUsers()  throws Exception {
        userModel.con = dbConnection.getConnection();
        String result = userModel.loadUsers();
        userModel.con.close();
        return result;
    }
}