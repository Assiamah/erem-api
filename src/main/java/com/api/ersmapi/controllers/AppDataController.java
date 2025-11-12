package com.api.ersmapi.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.ersmapi.config.DBConnection;
import com.api.ersmapi.services.app_data.AppDataService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/app_data_service")
@Tag(name = "App Data Service", description = "App Data Service for TerraFinder Application")
public class AppDataController {
    
    AppDataService appDataService = new AppDataService();

    @Autowired
    private DBConnection dbConnection;

    @PostMapping("/submit_lease_application")
    public ResponseEntity<?> submitLeaseApplication(@RequestBody String jsonReq)  throws Exception {
        appDataService.con = dbConnection.getConnection();
        String result = appDataService.submitLeaseApplication(jsonReq);
        appDataService.con.close();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/get_user_applications")
    public ResponseEntity<?> getUserApplications(@RequestBody String jsonReq)  throws Exception {
        appDataService.con = dbConnection.getConnection();
        String result = appDataService.getUserApplications(jsonReq);
        appDataService.con.close();

        ObjectMapper mapper = new ObjectMapper();
        Object json = mapper.readValue(result, Object.class);
        return ResponseEntity.ok(json);
    }

    @PostMapping("/get_user_applications_stats")
    public ResponseEntity<?> getUserApplicationsStats(@RequestBody String jsonReq)  throws Exception {
        appDataService.con = dbConnection.getConnection();
        String result = appDataService.getUserApplicationsStats(jsonReq);
        appDataService.con.close();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/get_application_details")
    public ResponseEntity<?> getApplicationDetails(@RequestBody String jsonReq)  throws Exception {
        appDataService.con = dbConnection.getConnection();
        String result = appDataService.getApplicationDetails(jsonReq);
        appDataService.con.close();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get_submitted_applications")
    public ResponseEntity<?> getSubmittedApplications()  throws Exception {
        appDataService.con = dbConnection.getConnection();
        String result = appDataService.getSubmittedApplications();
        appDataService.con.close();
        return ResponseEntity.ok(result);
    }
    
}