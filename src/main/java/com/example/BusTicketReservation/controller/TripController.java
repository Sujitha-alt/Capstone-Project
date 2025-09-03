package com.example.BusTicketReservation.controller;

import com.example.BusTicketReservation.dto.TripCreateRequest;
import com.example.BusTicketReservation.entity.Trip;
import com.example.BusTicketReservation.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/trips")
public class TripController {

    private final TripService service;

    public TripController(TripService service) {
        this.service = service;
    }

    @Operation(summary = "Schedules a new trip for a specific bus and route.")
    @PostMapping
    public ResponseEntity<Trip> create(@RequestBody TripCreateRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @Operation(summary = "Search trips by source/destination/date or directly by tripId.")
    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(value = "tripId", required = false) Long tripId,
            @RequestParam(value = "source", required = false) String source,
            @RequestParam(value = "destination", required = false) String destination,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(value = "date", required = false) LocalDate date) {
        if (tripId != null) {
            return service.findById(tripId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.ok(service.search(source, destination, date));
    }

}
