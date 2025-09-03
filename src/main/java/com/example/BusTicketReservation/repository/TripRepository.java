package com.example.BusTicketReservation.repository;

import com.example.BusTicketReservation.entity.Bus;
import com.example.BusTicketReservation.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    @Query("select distinct t.bus from Trip t where t.route.id = :routeId")
    List<Bus> findBusesByRouteId(Long routeId);
}
