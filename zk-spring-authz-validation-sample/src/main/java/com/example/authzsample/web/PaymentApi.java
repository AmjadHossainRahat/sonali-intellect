package com.example.authzsample.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentApi {

    @PostMapping
    @PreAuthorize("hasPermission('PAYMENT','CREATE')")
    public ResponseEntity<String> createPayment() {
        return ResponseEntity.ok("payment-created");
    }
}
