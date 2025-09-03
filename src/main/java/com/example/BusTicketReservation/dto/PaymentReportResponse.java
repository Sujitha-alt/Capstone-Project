package com.example.BusTicketReservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class PaymentReportResponse {
    private Long paymentId;
    private Long bookingId;
    private String passenger;
    private double amount;
    private String paymentMethod;
    private String status;
    private Date paymentDate;
}
