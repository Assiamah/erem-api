package com.api.ersmapi.controllers;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.ersmapi.config.DBConnection;
import com.api.ersmapi.services.map.MapService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/map_service")
@Tag(name = "Menu Service", description = "Menu Service for DiLAP Application")
public class MapController {

    @Autowired
    private MapService mapService;

    @Autowired
    private DBConnection dbConnection;

    @PostMapping("/get_parcel_details_by_id")
    public ResponseEntity<?> getUserMenus(@RequestBody String jsonReq) {
        Connection conn = null;
        try {
            conn = dbConnection.getConnection();
            String result = mapService.getParcelById(conn, jsonReq);
            //System.out.println("get_user_menus: " + result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("Error in getParcelById controller: " + e.getMessage());
            return ResponseEntity.status(500).body("{\"status\": \"error\", \"message\": \"Failed to get parcel details: " + e.getMessage() + "\"}");
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
