package com.example.BusTicketReservation.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TripCreateRequest {
    private Long routeId;
    private Long busId;
    private LocalDate tripDate;
    private double pricePerSeat;
}
