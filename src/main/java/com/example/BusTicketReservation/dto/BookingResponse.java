package com.example.BusTicketReservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingResponse {
    private Long bookingId;
    private String ticketNumber;
    private String fromCity;
    private String toCity;
    private int seats;
    private double totalAmount;
    private String status;
    private String busNumber;
    private String busType;
    private int remainingSeats;
}
