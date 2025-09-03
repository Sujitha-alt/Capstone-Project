package com.example.BusTicketReservation.dto;

import lombok.Data;

@Data
public class BookingRequest {
    private Long userId;
    private Long busId;
    private String fromCity;
    private String toCity;
    private int seats;
}
