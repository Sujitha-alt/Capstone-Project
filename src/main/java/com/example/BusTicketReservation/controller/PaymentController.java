package com.example.BusTicketReservation.controller;

import com.example.BusTicketReservation.dto.PaymentRequest;
import com.example.BusTicketReservation.dto.PaymentResponse;
import com.example.BusTicketReservation.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> process(@RequestBody PaymentRequest body) {
        return ResponseEntity.ok(service.process(body));
    }

}
