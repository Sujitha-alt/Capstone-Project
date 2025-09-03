package com.example.BusTicketReservation.service;

import com.example.BusTicketReservation.dto.PaymentRequest;
import com.example.BusTicketReservation.dto.PaymentResponse;
import com.example.BusTicketReservation.entity.Booking;
import com.example.BusTicketReservation.entity.Payment;
import com.example.BusTicketReservation.repository.BookingRepository;
import com.example.BusTicketReservation.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PaymentService {

    private final PaymentRepository repo;
    private final BookingRepository bookingRepo;

    public PaymentService(PaymentRepository repo, BookingRepository bookingRepo) {
        this.repo = repo;
        this.bookingRepo = bookingRepo;
    }

    // POST /payments/process
    public PaymentResponse process(PaymentRequest body) {
        Booking booking = bookingRepo.findById(body.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Payment p = Payment.builder()
                .booking(booking)
                .amount(body.getAmount())
                .paymentMethod(body.getPaymentMethod())
                .paymentDate(new Date())
                .status("SUCCESS")
                .build();

        repo.save(p);
        return toResponse(p);
    }

    // GET /payments/process?paymentId=...
    public PaymentResponse processById(Long paymentId) {
        Payment p = repo.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        // idempotent confirm
        p.setStatus("SUCCESS");
        p.setPaymentDate(new Date());
        repo.save(p);
        return toResponse(p);
    }

    private PaymentResponse toResponse(Payment p) {
        return new PaymentResponse(p.getId(), p.getAmount(), p.getPaymentMethod(), p.getStatus());
    }
}
