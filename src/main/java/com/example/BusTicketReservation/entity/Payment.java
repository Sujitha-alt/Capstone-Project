package com.example.BusTicketReservation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;
    private String paymentMethod;
    private Date paymentDate;
    private String status;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
}
