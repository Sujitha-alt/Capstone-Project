package com.example.BusTicketReservation.controller;

import com.example.BusTicketReservation.dto.BookingRequest;
import com.example.BusTicketReservation.dto.BookingResponse;
import com.example.BusTicketReservation.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/book")
    public BookingResponse book(@RequestBody BookingRequest request) {
        return bookingService.bookTicket(request);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<String> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }
}
