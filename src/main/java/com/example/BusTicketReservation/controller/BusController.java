package com.example.BusTicketReservation.controller;

import com.example.BusTicketReservation.entity.Bus;
import com.example.BusTicketReservation.service.BusService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/buses")
public class BusController {

    private final BusService service;

    public BusController(BusService service) {
        this.service = service;
    }

    @Operation(summary = "Creates a new bus (Admin).")
    @PostMapping
    public ResponseEntity<Bus> create(@RequestBody Bus req) {
        return ResponseEntity.ok(service.create(req));
    }

}
