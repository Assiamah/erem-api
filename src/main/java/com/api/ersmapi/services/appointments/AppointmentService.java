package com.api.ersmapi.services.appointments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

public class AppointmentService {

    public Connection con = null;

    public String loadSlots(Connection conn) throws Exception {
        if (conn == null || conn.isClosed()) {
            throw new Exception("Database connection is not established");
        }

        String result = null;

        String SQL = "SELECT * FROM appointments.get_calendar_slots()";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            ResultSet rs = pstmt.executeQuery();

            JSONArray slots = new JSONArray(); // use org.json
            while (rs.next()) {
                JSONObject slot = new JSONObject();
                slot.put("id", rs.getInt("id"));
                slot.put("title", rs.getString("title"));
                slot.put("start", rs.getTimestamp("start_time").toLocalDateTime().toString()); 
                slot.put("end", rs.getTimestamp("end_time").toLocalDateTime().toString());
                slot.put("color", rs.getString("color"));
                slot.put("is_available", rs.getBoolean("is_available"));
                slot.put("current_bookings", rs.getInt("current_bookings"));
                slot.put("max_capacity", rs.getInt("max_capacity"));
                slot.put("appointment_type_name", rs.getString("appointment_type_name"));

                slots.put(slot);
            }
            result = slots.toString();
        } catch (SQLException e) {
            System.out.println("Error loading slots: " + e.getMessage());
            throw e;
        }

        return (result != null) ? result : "[]";
    }

    public String getSlotsSummary(Connection conn) throws Exception {
        if (conn == null || conn.isClosed()) {
            throw new Exception("Database connection is not established");
        }

        String SQL = "SELECT * FROM appointments.get_slots_summary()";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int totalSlots = rs.getInt("total_slots");
                int availableSlots = rs.getInt("available_slots");
                int bookedSlots = rs.getInt("booked_slots");
                int fullSlots = rs.getInt("full_slots");

                // Build JSON manually (or use a library like Jackson/Gson)
                return String.format(
                    "{\"total_slots\": %d, \"available_slots\": %d, \"booked_slots\": %d, \"full_slots\": %d}",
                    totalSlots, availableSlots, bookedSlots, fullSlots
                );
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error getting slots summary: " + e.getMessage());
            throw e;
        }

        return "{}";
    }

    public String createSlot(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM appointments.create_single_slot(?::json)";
        Connection conn = con;
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, jsonReq);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result = rs.getString("create_single_slot");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        return result;
    }

    public String createBatchSlots(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM appointments.create_batch_slots(?::json)";
        Connection conn = con;
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, jsonReq);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result = rs.getString("create_batch_slots");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        return result;
    }

    public String toggleSlotAvailability(Integer slotId, String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM appointments.toggle_slot_availability(?, ?::json)";
        Connection conn = con;
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, slotId);
            pstmt.setString(2, jsonReq);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result = rs.getString("toggle_slot_availability");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        return result;
    }

    public String deleteSlot(Integer slotId, String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM appointments.delete_slot(?, ?::json)";
        Connection conn = con;
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, slotId);
            pstmt.setString(2, jsonReq);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result = rs.getString("delete_slot");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        return result;
    }

    public String getAppointmentTypes() throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }

        String result = null;
        Connection conn = con;

        String SQL = "SELECT * FROM appointments.get_appointment_types() AS result";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString("result");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error getting appointment types: " + e.getMessage());
            throw e;
        }

        return (result != null) ? result : "[]";
    }

    public String bookAppointment(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM appointments.book_appointment(?::json)";
        Connection conn = con;
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, jsonReq);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result = rs.getString("book_appointment");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        return result;
    }

    public String cancelAppointment(Integer appointmentId, String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM appointments.cancel_appointment(?, ?::json)";
        Connection conn = con;
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, appointmentId);
            pstmt.setString(2, jsonReq);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result = rs.getString("cancel_appointment");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        return result;
    }

    public String getUserAppointments(Integer userId, Integer page, Integer size, Boolean isProvider) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }

        String result = null;
        Connection conn = con;

        String SQL = "SELECT * FROM appointments.get_user_appointments(?, ?, ?, ?) AS result";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, page);
            pstmt.setInt(3, size);
            pstmt.setBoolean(4, isProvider);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString("result");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error getting user appointments: " + e.getMessage());
            throw e;
        }

        return (result != null) ? result : "[]";
    }

    public String getAvailableSlots(String date, Integer appointmentTypeId) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }

        String result = null;
        Connection conn = con;

        String SQL = "SELECT * FROM appointments.get_available_slots(?, ?) AS result";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, date);
            pstmt.setObject(2, appointmentTypeId, java.sql.Types.INTEGER);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString("result");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error getting available slots: " + e.getMessage());
            throw e;
        }

        return (result != null) ? result : "[]";
    }

    public String getSlotDetails(Integer slotId) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }

        String result = null;
        Connection conn = con;

        String SQL = "SELECT * FROM appointments.get_slot_details(?) AS result";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, slotId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString("result");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error getting slot details: " + e.getMessage());
            throw e;
        }

        return (result != null) ? result : "{}";
    }

    public String updateSlot(Integer slotId, String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM appointments.update_slot(?, ?::json)";
        Connection conn = con;
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, slotId);
            pstmt.setString(2, jsonReq);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result = rs.getString("update_slot");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        return result;
    }

    public String getAppointmentDetails(Integer appointmentId) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }

        String result = null;
        Connection conn = con;

        String SQL = "SELECT * FROM appointments.get_appointment_details(?) AS result";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, appointmentId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString("result");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error getting appointment details: " + e.getMessage());
            throw e;
        }

        return (result != null) ? result : "{}";
    }

    public String rescheduleAppointment(Integer appointmentId, String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM appointments.reschedule_appointment(?, ?::json)";
        Connection conn = con;
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, appointmentId);
            pstmt.setString(2, jsonReq);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result = rs.getString("reschedule_appointment");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        return result;
    }

    public String getProviderSlots(Integer providerId, String startDate, String endDate) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }

        String result = null;
        Connection conn = con;

        String SQL = "SELECT * FROM appointments.get_provider_slots(?, ?, ?) AS result";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, providerId);
            pstmt.setString(2, startDate);
            pstmt.setString(3, endDate);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString("result");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error getting provider slots: " + e.getMessage());
            throw e;
        }

        return (result != null) ? result : "[]";
    }
}