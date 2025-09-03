package com.example.BusTicketReservation.service;

import com.example.BusTicketReservation.dto.BookingRequest;
import com.example.BusTicketReservation.dto.BookingResponse;
import com.example.BusTicketReservation.entity.Booking;
import com.example.BusTicketReservation.entity.Bus;
import com.example.BusTicketReservation.entity.User;
import com.example.BusTicketReservation.repository.BookingRepository;
import com.example.BusTicketReservation.repository.BusRepository;
import com.example.BusTicketReservation.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BusRepository busRepository;

    public BookingService(BookingRepository bookingRepository,
            UserRepository userRepository,
            BusRepository busRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.busRepository = busRepository;
    }

    public BookingResponse bookTicket(BookingRequest req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Bus bus = busRepository.findById(req.getBusId())
                .orElseThrow(() -> new RuntimeException("Bus not found"));

        if (bus.getAvailableSeats() < req.getSeats()) {
            throw new RuntimeException("Not enough seats available");
        }

        double totalAmount = req.getSeats() * 100.0;

        bus.setAvailableSeats(bus.getAvailableSeats() - req.getSeats());
        busRepository.save(bus);

        Booking b = Booking.builder()
                .ticketNumber("TICKET-" + UUID.randomUUID())
                .fromCity(req.getFromCity())
                .toCity(req.getToCity())
                .seats(req.getSeats())
                .bookingDate(new Date())
                .totalAmount(totalAmount)
                .status("CONFIRMED")
                .user(user)
                .bus(bus)
                .build();

        bookingRepository.save(b);
        return toResponse(b);
    }

    public List<BookingResponse> getMyBookings(String username) {
        return bookingRepository.findByUserUsername(username)
                .stream().map(this::toResponse).toList();
    }

    public Optional<BookingResponse> findById(Long id) {
        return bookingRepository.findById(id).map(this::toResponse);
    }

    public String cancelBooking(Long id) {
        Booking b = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if ("CANCELLED".equalsIgnoreCase(b.getStatus())) {
            return "Booking is already cancelled";
        }

        b.setStatus("CANCELLED");

        Bus bus = b.getBus();
        if (bus != null) {
            bus.setAvailableSeats(bus.getAvailableSeats() + b.getSeats());
            busRepository.save(bus);
        }

        bookingRepository.save(b);
        return "Booking cancelled successfully";
    }

    private BookingResponse toResponse(Booking b) {
        Bus bus = b.getBus();
        return new BookingResponse(
                b.getId(),
                b.getTicketNumber(),
                b.getFromCity(),
                b.getToCity(),
                b.getSeats(),
                b.getTotalAmount(),
                b.getStatus(),
                bus != null ? bus.getBusNumber() : null,
                bus != null ? bus.getBusType() : null,
                bus != null ? bus.getAvailableSeats() : 0);
    }
}
