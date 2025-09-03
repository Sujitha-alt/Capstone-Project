package com.example.BusTicketReservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class BookingReportResponse {
    private Long bookingId;
    private String ticketNumber;
    private String passenger;
    private String fromCity;
    private String toCity;
    private int seats;
    private double totalAmount;
    private String status;
    private String busNumber;
    private String busType;
    private Date bookingDate;
}
