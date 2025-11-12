package com.api.ersmapi.services.bills;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BillService {

    public Connection con = null;

    public String generateApplicationFee(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM payments.generate_advanced_bill(?::json)";
		Connection conn = con;
		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setString(1, jsonReq);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("generate_advanced_bill");
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

    public String getCompleteBillDetails(int billId) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM payments.get_complete_bill_details(?)";
		Connection conn = con;
		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setInt(1, billId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("get_complete_bill_details");
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

	public String getUserBills(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM payments.get_user_bills(?::json)";
		Connection conn = con;
		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setString(1, jsonReq);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("get_user_bills");
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

	public String getUserBillsStats(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM payments.get_user_bills_stats(?::json)";
		Connection conn = con;
		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setString(1, jsonReq);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("get_user_bills_stats");
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

	public String getBillDetails(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM payments.get_bill_details(?::json)";
		Connection conn = con;
		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setString(1, jsonReq);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("get_bill_details");
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