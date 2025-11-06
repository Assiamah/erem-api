package com.api.ersmapi.services.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {
    
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
		System.out.println(result);
		return result;
    }

    public String verifyOtp(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM security.verify_otp(?)";
		Connection conn = con;
		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setString(1, jsonReq);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("verify_otp");
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

    public String checkRegistrationEmail(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM security.check_registration_email(?)";
		Connection conn = con;
		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setString(1, jsonReq);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("check_registration_email");
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

	public String verifyRegistrationEmail(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM security.verify_registration_email(?)";
		Connection conn = con;
		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setString(1, jsonReq);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("verify_registration_email");
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

	public String createPortalUserPassword(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM security.create_portal_user_password(?)";
		Connection conn = con;
		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setString(1, jsonReq);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("create_portal_user_password");
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
