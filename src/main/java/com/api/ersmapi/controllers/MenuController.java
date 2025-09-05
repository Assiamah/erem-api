package com.api.ersmapi.controllers;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.ersmapi.config.DBConnection;
import com.api.ersmapi.services.menu.MenuService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/menu_service")
@Tag(name = "Menu Service", description = "Menu Service for TerraFinder Application")
public class MenuController {

    @Autowired
    private MenuService menuService;
    
    @Autowired
    private DBConnection dbConnection;

    @GetMapping("/get_all_menus")
    public ResponseEntity<?> getAllMenus() {
        Connection conn = null;
        try {
            conn = dbConnection.getConnection();
            String result = menuService.getAllMenus(conn);
            //System.out.println("get_all_menus: " + result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("Error in getAllMenus controller: " + e.getMessage());
            return ResponseEntity.status(500).body("{\"status\": \"error\", \"message\": \"Failed to get menus: " + e.getMessage() + "\"}");
        } finally {
            closeConnection(conn);
        }
    }

    @PostMapping("/get_user_menus")
    public ResponseEntity<?> getUserMenus(@RequestBody String jsonReq) {
        Connection conn = null;
        try {
            conn = dbConnection.getConnection();
            String result = menuService.getUserMenus(conn, jsonReq);
            //System.out.println("get_user_menus: " + result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("Error in getUserMenus controller: " + e.getMessage());
            return ResponseEntity.status(500).body("{\"status\": \"error\", \"message\": \"Failed to get user menus: " + e.getMessage() + "\"}");
        } finally {
            closeConnection(conn);
        }
    }

    @PostMapping("/save_user_menus")
    public ResponseEntity<?> saveUserMenus(@RequestBody String jsonReq) {
        Connection conn = null;
        try {
            conn = dbConnection.getConnection();
            String result = menuService.saveUserMenus(conn, jsonReq);
            //System.out.println("save_user_menus: " + result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("Error in saveUserMenus controller: " + e.getMessage());
            return ResponseEntity.status(500).body("{\"status\": \"error\", \"message\": \"Failed to save user menus: " + e.getMessage() + "\"}");
        } finally {
            closeConnection(conn);
        }
    }
    
    // Helper method to safely close connection
    private void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}