package com.example.BusTicketReservation.repository;

import com.example.BusTicketReservation.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
}
