package com.api.ersmapi.services.map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Service;

@Service
public class MapService {

    public String getParcelById(Connection conn, String jsonReq) throws Exception {
        if (conn == null || conn.isClosed()) {
            throw new Exception("Database connection is not established or is closed");
        }
        
        String result = null;
        String SQL = "SELECT * FROM maps.get_parcel_details_by_id(?::json)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, jsonReq);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString("get_parcel_details_by_id");
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error in getUserMenus: " + e.getMessage());
            throw e;
        }
        
        return result;
    }
}
