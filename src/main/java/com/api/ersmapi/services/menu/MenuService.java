package com.api.ersmapi.services.menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Service;

@Service
public class MenuService {
    
    public String getAllMenus(Connection conn) throws Exception {
        if (conn == null || conn.isClosed()) {
            throw new Exception("Database connection is not established or is closed");
        }
        
        String result = null;
        String SQL = "SELECT * FROM system.get_all_menus()";
        
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString("get_all_menus");
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error in getAllMenus: " + e.getMessage());
            throw e;
        }
        
        return result;
    }

    public String getUserMenus(Connection conn, String jsonReq) throws Exception {
        if (conn == null || conn.isClosed()) {
            throw new Exception("Database connection is not established or is closed");
        }
        
        String result = null;
        String SQL = "SELECT * FROM system.get_user_menus(?::json)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, jsonReq);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString("get_user_menus");
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error in getUserMenus: " + e.getMessage());
            throw e;
        }
        
        return result;
    }
    
    public String saveUserMenus(Connection conn, String jsonReq) throws Exception {
        if (conn == null || conn.isClosed()) {
            throw new Exception("Database connection is not established or is closed");
        }
        
        String result = null;
        String SQL = "SELECT * FROM system.save_user_menus(?::json)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, jsonReq);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString("save_user_menus");
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error in saveUserMenus: " + e.getMessage());
            throw e;
        }
        
        return result;
    }
}
