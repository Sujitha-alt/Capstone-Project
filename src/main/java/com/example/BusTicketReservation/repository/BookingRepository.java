package com.example.BusTicketReservation.repository;

import com.example.BusTicketReservation.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserUsername(String username);
}
