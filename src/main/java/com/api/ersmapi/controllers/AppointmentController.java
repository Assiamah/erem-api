package com.api.ersmapi.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.ersmapi.config.DBConnection;
import com.api.ersmapi.services.appointments.AppointmentService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/appointment_service")
@Tag(name = "Appointment Service", description = "Appointment Service for managing appointments and time slots")
public class AppointmentController {
    
    AppointmentService appointmentService = new AppointmentService();

    @Autowired
    private DBConnection dbConnection;

    @GetMapping("/load_slots")
    public ResponseEntity<?> loadSlots() throws Exception {
        appointmentService.con = dbConnection.getConnection();
        String result = appointmentService.loadSlots();
        appointmentService.con.close();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/get_slots_summary")
    public ResponseEntity<?> getSlotsSummary(@RequestParam Map<String, Object> params) throws Exception {
        
        appointmentService.con = dbConnection.getConnection();
        String result = appointmentService.getSlotsSummary();
        appointmentService.con.close();

        return ResponseEntity.ok(result);
    }

    @PostMapping("/create_slot")
    public ResponseEntity<?> createSlot(@RequestBody String jsonReq) throws Exception {
        appointmentService.con = dbConnection.getConnection();
        String result = appointmentService.createSlot(jsonReq);
        appointmentService.con.close();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/create_batch_slots")
    public ResponseEntity<?> createBatchSlots(@RequestBody String jsonReq) throws Exception {
        appointmentService.con = dbConnection.getConnection();
        String result = appointmentService.createBatchSlots(jsonReq);
        appointmentService.con.close();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/toggle_slot_availability/{slotId}")
    public ResponseEntity<?> toggleSlotAvailability(@PathVariable Integer slotId, @RequestBody String jsonReq) throws Exception {
        appointmentService.con = dbConnection.getConnection();
        String result = appointmentService.toggleSlotAvailability(slotId, jsonReq);
        appointmentService.con.close();
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete_slot/{slotId}")
    public ResponseEntity<?> deleteSlot(@PathVariable Integer slotId, @RequestBody String jsonReq) throws Exception {
        appointmentService.con = dbConnection.getConnection();
        String result = appointmentService.deleteSlot(slotId, jsonReq);
        appointmentService.con.close();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get_appointment_types")
    public ResponseEntity<?> getAppointmentTypes() throws Exception {
        appointmentService.con = dbConnection.getConnection();
        String result = appointmentService.getAppointmentTypes();
        appointmentService.con.close();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/book_appointment")
    public ResponseEntity<?> bookAppointment(@RequestBody String jsonReq) throws Exception {
        appointmentService.con = dbConnection.getConnection();
        String result = appointmentService.bookAppointment(jsonReq);
        appointmentService.con.close();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/cancel_appointment/{appointmentId}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Integer appointmentId, @RequestBody String jsonReq) throws Exception {
        appointmentService.con = dbConnection.getConnection();
        String result = appointmentService.cancelAppointment(appointmentId, jsonReq);
        appointmentService.con.close();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get_user_appointments")
    public ResponseEntity<?> getUserAppointments(@RequestParam Map<String, Object> params) throws Exception {
        Integer userId = params.get("user_id") != null ? Integer.parseInt(params.get("user_id").toString()) : null;
        Integer page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 0;
        Integer size = params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10;
        Boolean isProvider = params.get("is_provider") != null ? Boolean.parseBoolean(params.get("is_provider").toString()) : false;
        
        appointmentService.con = dbConnection.getConnection();
        String result = appointmentService.getUserAppointments(userId, page, size, isProvider);
        appointmentService.con.close();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/get_available_slots")
    public ResponseEntity<?> getAvailableSlots(@RequestParam Map<String, Object> params) throws Exception {
        String date = params.get("date") != null ? params.get("date").toString() : null;
        Integer appointmentTypeId = params.get("appointment_type_id") != null ? Integer.parseInt(params.get("appointment_type_id").toString()) : null;
        
        appointmentService.con = dbConnection.getConnection();
        String result = appointmentService.getAvailableSlots(date, appointmentTypeId);
        appointmentService.con.close();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/get_slot_details/{slotId}")
    public ResponseEntity<?> getSlotDetails(@PathVariable Integer slotId) throws Exception {
        appointmentService.con = dbConnection.getConnection();
        String result = appointmentService.getSlotDetails(slotId);
        appointmentService.con.close();
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update_slot/{slotId}")
    public ResponseEntity<?> updateSlot(@PathVariable Integer slotId, @RequestBody String jsonReq) throws Exception {
        appointmentService.con = dbConnection.getConnection();
        String result = appointmentService.updateSlot(slotId, jsonReq);
        appointmentService.con.close();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get_appointment_details/{appointmentId}")
    public ResponseEntity<?> getAppointmentDetails(@PathVariable Integer appointmentId) throws Exception {
        appointmentService.con = dbConnection.getConnection();
        String result = appointmentService.getAppointmentDetails(appointmentId);
        appointmentService.con.close();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/reschedule_appointment/{appointmentId}")
    public ResponseEntity<?> rescheduleAppointment(@PathVariable Integer appointmentId, @RequestBody String jsonReq) throws Exception {
        appointmentService.con = dbConnection.getConnection();
        String result = appointmentService.rescheduleAppointment(appointmentId, jsonReq);
        appointmentService.con.close();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get_provider_slots")
    public ResponseEntity<?> getProviderSlots(@RequestParam Map<String, Object> params) throws Exception {
        Integer providerId = params.get("provider_id") != null ? Integer.parseInt(params.get("provider_id").toString()) : null;
        String startDate = params.get("start_date") != null ? params.get("start_date").toString() : null;
        String endDate = params.get("end_date") != null ? params.get("end_date").toString() : null;
        
        appointmentService.con = dbConnection.getConnection();
        String result = appointmentService.getProviderSlots(providerId, startDate, endDate);
        appointmentService.con.close();

        return ResponseEntity.ok(result);
    }
}