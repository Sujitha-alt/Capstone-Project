package com.example.BusTicketReservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResponse {
    private Long paymentId;
    private double amount;
    private String paymentMethod;
    private String status;
}
