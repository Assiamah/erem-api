package com.api.ersmapi.services.appointments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;

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
                slot.put("className", rs.getString("color"));
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

    public String toggleSlotAvailability(Integer slotId, Integer userId) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM appointments.toggle_slot_availability(?, ?)";
        Connection conn = con;
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, slotId);
            pstmt.setInt(2, userId);
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

    public String deleteSlot(Integer slotId, Integer userId) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM appointments.delete_slot(?, ?)";
        Connection conn = con;
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, slotId);
            pstmt.setInt(2, userId);
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

    
    public String bookSelfAppointment(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM appointments.book_self_appointment(?::json)";
        Connection conn = con;
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, jsonReq);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result = rs.getString("book_self_appointment");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        return result;
    }



    public String cancelAppointment(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM appointments.cancel_appointment(?)";       
        Connection conn = con;
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, jsonReq);
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

        // @PostMapping("/{appointmentId}/cancel")


    // public String getUserAppointments(Integer userId, Integer page, Integer size, Boolean isProvider) throws Exception {
    //     if (con == null) {
    //         throw new Exception("Database connection is not established");
    //     }

    //     String result = null;
    //     Connection conn = con;

    //     String SQL = "SELECT * FROM appointments.get_user_appointments(?, ?, ?, ?) AS result";

    //     try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
    //         pstmt.setInt(1, userId);
    //         pstmt.setInt(2, page);
    //         pstmt.setInt(3, size);
    //         pstmt.setBoolean(4, isProvider);

    //         ResultSet rs = pstmt.executeQuery();
    //         if (rs.next()) {
    //             result = rs.getString("result");
    //         }
    //         rs.close();
    //     } catch (SQLException e) {
    //         System.out.println("Error getting user appointments: " + e.getMessage());
    //         throw e;
    //     }

    //     return (result != null) ? result : "[]";
    // }

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



        public String loadAppointments() throws Exception {
            if (con == null) {
                throw new Exception("Database connection is not established");
            }

            String result = null;
            Connection conn = con;

            String SQL = "SELECT * FROM appointments.load_appointments() AS result";

            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    result = rs.getString("load_appointments");
                }
                rs.close();
            } catch (SQLException e) {
                System.out.println("Error loading slots: " + e.getMessage());
                throw e;
            }

            return (result != null) ? result : "[]";
        }



       public String appointmentById(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM appointments.get_appointment_by_id(?::json)";
		Connection conn = con;
		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setString(1, jsonReq);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("get_appointment_by_id");
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

    public String getUserAppointments(Map<String, Object> params) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }

        String result = null;
        Connection conn = con;

        String SQL = """
            SELECT COALESCE(json_agg(t), '[]'::json) AS result
            FROM appointments.get_user_appointments_with_filters(?, ?, ?, ?, ?, ?, ?) t
        """;

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            Integer userId = params.get("user_id") != null 
                ? Integer.parseInt(params.get("user_id").toString()) 
                : null;

            Integer page = params.get("page") != null 
                ? Integer.parseInt(params.get("page").toString()) 
                : 0;

            Integer size = params.get("size") != null 
                ? Integer.parseInt(params.get("size").toString()) 
                : 10;

            Boolean isProvider = params.get("is_provider") != null 
                ? Boolean.parseBoolean(params.get("is_provider").toString()) 
                : false;
            String status = (String) params.get("status");
            String startDate = (String) params.get("start_date");
            String endDate = (String) params.get("end_date");

            pstmt.setInt(1, userId);
            pstmt.setInt(2, page != null ? page : 0);
            pstmt.setInt(3, size != null ? size : 10);
            pstmt.setBoolean(4, isProvider != null ? isProvider : false);
            
            // Handle nullable parameters
            if (status != null) {
                pstmt.setString(5, status);
            } else {
                pstmt.setNull(5, java.sql.Types.VARCHAR);
            }
            
            if (startDate != null) {
                pstmt.setString(6, startDate);
            } else {
                pstmt.setNull(6, java.sql.Types.VARCHAR);
            }
            
            if (endDate != null) {
                pstmt.setString(7, endDate);
            } else {
                pstmt.setNull(7, java.sql.Types.VARCHAR);
            }

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString("result");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error getting user appointments: " + e.getMessage());
            throw e;
        }

        return (result != null) ? result : "{\"appointments\": [], \"total_count\": 0}";
    }

    public String getUpcomingAppointments(Integer userId, Integer daysAhead) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }

        String result = null;
        Connection conn = con;

        String SQL = """
            SELECT COALESCE(json_agg(t), '[]'::json) AS result
            FROM appointments.get_upcoming_appointments(?, ?) t
        """;

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, daysAhead != null ? daysAhead : 30);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString("result");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error getting upcoming appointments: " + e.getMessage());
            throw e;
        }

        return (result != null) ? result : "[]";
    }

    public String getAppointmentDetailsForClient(Integer appointmentId, Integer clientId) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }

        String result = null;
        Connection conn = con;

        String SQL = "SELECT * FROM appointments.get_appointment_details_for_client(?, ?) AS result";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, appointmentId);
            pstmt.setInt(2, clientId);

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

    public String getProviderAppointments(Map<String, Object> params) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }

        String result = null;
        Connection conn = con;

        String SQL = "SELECT * FROM appointments.get_provider_appointments_with_filters(?, ?, ?, ?, ?) AS result";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            Integer providerId = (Integer) params.get("provider_id");
            Integer page = (Integer) params.get("page");
            Integer size = (Integer) params.get("size");
            String status = (String) params.get("status");
            String startDate = (String) params.get("start_date");
            String endDate = (String) params.get("end_date");

            pstmt.setInt(1, providerId);
            pstmt.setInt(2, page != null ? page : 0);
            pstmt.setInt(3, size != null ? size : 10);
            
            // Handle nullable parameters
            if (status != null) {
                pstmt.setString(4, status);
            } else {
                pstmt.setNull(4, java.sql.Types.VARCHAR);
            }
            
            if (startDate != null) {
                pstmt.setString(5, startDate);
            } else {
                pstmt.setNull(5, java.sql.Types.VARCHAR);
            }

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString("result");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error getting provider appointments: " + e.getMessage());
            throw e;
        }

        return (result != null) ? result : "{\"appointments\": [], \"total_count\": 0}";
    }

    public String getAppointmentDetailsForProvider(Integer appointmentId, Integer providerId) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }

        String result = null;
        Connection conn = con;

        String SQL = "SELECT * FROM appointments.get_appointment_details_for_provider(?, ?) AS result";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, appointmentId);
            pstmt.setInt(2, providerId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString("result");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error getting provider appointment details: " + e.getMessage());
            throw e;
        }

        return (result != null) ? result : "{}";
    }


    public String getAvailableSlotByDate(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM appointments.get_available_slots(?::json)";
		Connection conn = con;
		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setString(1, jsonReq);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("get_available_slots");
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


    public String rescheduleAppointment(String jsonReq) throws Exception {
        if (con == null) {
            throw new Exception("Database connection is not established");
        }
        String result = null;
        String SQL = "SELECT * FROM appointments.reschedule_appointment(?::json)";
        Connection conn = con;
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, jsonReq);
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


}