package com.api.ersmapi.services.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    public Connection con = null;

    public String loadUsers(int page, int limit, String search) throws Exception {
		if (con == null) {
			throw new Exception("Database connection is not established");
		}

		String result = null;
		Connection conn = con;

		String SQL = "SELECT users.load_users(?, ?, ?) AS result";

		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setInt(1, page);
			pstmt.setInt(2, limit);
			pstmt.setString(3, search);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getString("result"); // JSON from function
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("Error loading users: " + e.getMessage());
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

		return (result != null) ? result : "{}";
	}

    public String addUser(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM users.add_user(?::json)";
		Connection conn = con;
		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setString(1, jsonReq);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("add_user");
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

    public String getUserById(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM users.get_user_by_id(?::json)";
		Connection conn = con;
		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setString(1, jsonReq);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("get_user_by_id");
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
	
	public String updateUser(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM users.update_user(?::json)";
		Connection conn = con;
		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setString(1, jsonReq);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("update_user");
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

    public String updatePortalUser(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM users.update_portal_user(?::json)";
		Connection conn = con;
		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setString(1, jsonReq);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("update_portal_user");
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



	    public String getUserSelect() throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }

        String result = null;
        Connection conn = con;

        String SQL = "SELECT * FROM users.load_users_for_select() AS result";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString("result");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error getting Users: " + e.getMessage());
            throw e;
        }

        return (result != null) ? result : "[]";
    }

	public String saveApplicant(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM services.save_applicant(?::jsonb)";
		Connection conn = con;
		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setString(1, jsonReq);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("save_applicant");
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
