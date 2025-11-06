package com.api.ersmapi.services.documents;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.codehaus.jettison.json.JSONObject;

public class DocumentService {
    
    public Connection con = null;

    public String saveSupportingDocuments(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }

        String result = null;
        String SQL = "SELECT documents.save_supporting_documents(?::jsonb) AS result";

        try (PreparedStatement pstmt = con.prepareStatement(SQL)) {
            pstmt.setString(1, jsonReq);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                result = rs.getString("result");
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println("Error saving data capture documents: " + e.getMessage());
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", false);
            errorResponse.put("message", "Database error: " + e.getMessage());
            result = errorResponse.toString();
        } finally {
            if (con != null) {
                con.close();
            }
        }

        System.out.println("DB Response: " + result);
        return result;
    }

    public String saveApplicationDocuments(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }

        String result = null;
        String SQL = "SELECT documents.save_application_documents(?::jsonb) AS result";

        try (PreparedStatement pstmt = con.prepareStatement(SQL)) {
            pstmt.setString(1, jsonReq);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                result = rs.getString("result");
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println("Error saving application documents: " + e.getMessage());
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", false);
            errorResponse.put("message", "Database error: " + e.getMessage());
            result = errorResponse.toString();
        } finally {
            if (con != null) {
                con.close();
            }
        }

        System.out.println("DB Response: " + result);
        return result;
    }
}
