package com.example.BusTicketReservation.repository;

import com.example.BusTicketReservation.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {}
