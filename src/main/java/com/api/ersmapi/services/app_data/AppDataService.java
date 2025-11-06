package com.api.ersmapi.services.app_data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AppDataService {

    public Connection con = null;

    public String submitLeaseApplication(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM services.submit_lease_application(?::jsonb)";
		Connection conn = con;
		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setString(1, jsonReq);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("submit_lease_application");
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

    public String getUserApplications(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }

        String SQL = "SELECT * FROM services.get_user_applications(?::jsonb)";
        String result = null;

        try (PreparedStatement pstmt = con.prepareStatement(SQL)) {
            pstmt.setString(1, jsonReq);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    result = rs.getString("get_user_applications");
                }
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            throw e;
        }

        return result;
    }

    public String getUserApplicationsStats(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM services.get_user_applications_stats(?::jsonb)";

        try (PreparedStatement pstmt = con.prepareStatement(SQL)) {
            pstmt.setString(1, jsonReq);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    result = rs.getString("get_user_applications_stats");
                }
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            throw e;
        }

        return result;
    }

    public String getApplicationParcels(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM services.get_application_parcels(?::jsonb)";
        Connection conn = con;
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, jsonReq);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result = rs.getString("get_application_parcels");
            }
            rs.close();
        } catch (SQLException e) {
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

    public String getApplicationDetails(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM services.get_application_details(?::jsonb)";
        Connection conn = con;
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, jsonReq);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result = rs.getString("get_application_details");
            }
            rs.close();
        } catch (SQLException e) {
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