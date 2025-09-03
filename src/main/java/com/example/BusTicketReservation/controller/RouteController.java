package com.example.BusTicketReservation.controller;

import com.example.BusTicketReservation.entity.Route;
import com.example.BusTicketReservation.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/routes")
public class RouteController {

    private final RouteService service;

    public RouteController(RouteService service) {
        this.service = service;
    }

    @Operation(summary = "Creates a new route (Admin).")
    @PostMapping
    public ResponseEntity<Route> create(@RequestBody Route req) {
        return ResponseEntity.ok(service.create(req));
    }

}
