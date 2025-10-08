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
import com.api.ersmapi.services.bills.BillService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/bill_service")
@Tag(name = "Bill Service", description = "Bill Service for TerraFinder Application")
public class BillController {
    BillService billService = new BillService();

    @Autowired
    private DBConnection dbConnection;
    
    @PostMapping("/generate_application_fee")
    public ResponseEntity<?> generateApplicationFee(@RequestBody String jsonReq)  throws Exception {
        billService.con = dbConnection.getConnection();
        String result = billService.generateApplicationFee(jsonReq);
        billService.con.close();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/get_complete_bill_details")
    public ResponseEntity<?> getCompleteBillDetails(@RequestBody Map<String, Integer> request) throws Exception {
        billService.con = dbConnection.getConnection();
        int billId = request.get("bill_id");
        String result = billService.getCompleteBillDetails(billId);
        billService.con.close();
        return ResponseEntity.ok(result);
    }
}
