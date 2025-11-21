package com.api.ersmapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.api.ersmapi.config.DBConnection;
import com.api.ersmapi.services.payments.PaymentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/v1/payment_service")
@Tag(name = "Payment Service", description = "Payment Service for DiLAP Application")
public class PaymentController {

    PaymentService paymentService = new PaymentService();

    @Autowired
    private DBConnection dbConnection;

    @PostMapping("/process_payment_callback")
    public ResponseEntity<?> processPaymentCallback(@RequestBody String jsonReq) throws Exception {
        paymentService.con = dbConnection.getConnection();
        String result = paymentService.processPaymentCallback(jsonReq);
        paymentService.con.close();

        return ResponseEntity.ok(result);
    }

    @PostMapping("/initiate_payment_callback")
    public ResponseEntity<?> initiatePaymentCallback(@RequestBody String jsonReq) throws Exception {
        paymentService.con = dbConnection.getConnection();
        String result = paymentService.initiatePaymentCallback(jsonReq);
        paymentService.con.close();

        return ResponseEntity.ok(result);
    }

    @PostMapping("/verify_payment_by_current_bill")
    public ResponseEntity<?> verifyPaymentByCurrentBill(@RequestBody String jsonReq) throws Exception {
        paymentService.con = dbConnection.getConnection();
        String result = paymentService.verifyPaymentByCurrentBill(jsonReq);
        paymentService.con.close();

        return ResponseEntity.ok(result);
    }

}
