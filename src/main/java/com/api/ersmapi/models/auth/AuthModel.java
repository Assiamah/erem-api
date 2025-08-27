package com.api.ersmapi.models.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthModel {
    
    public Connection con = null;

    public String userLogin(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM security.user_login(?)";
		Connection conn = con;
		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setString(1, jsonReq);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("user_login");
			}
			rs.close();
		} catch (SQLException e) {
			// Print Errors in console.
			System.out.println(e.getMessage());
			throw e;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}
		return result;
    }
}
