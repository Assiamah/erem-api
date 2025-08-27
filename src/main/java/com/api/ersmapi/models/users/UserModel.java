package com.api.ersmapi.models.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel {

    public Connection con = null;

    public String loadUsers() throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM users.load_users()";
		Connection conn = con;
		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("load_users");
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
