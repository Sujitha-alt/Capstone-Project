package com.example.BusTicketReservation.service;

import com.example.BusTicketReservation.dto.TripCreateRequest;
import com.example.BusTicketReservation.entity.Route;
import com.example.BusTicketReservation.entity.Bus;
import com.example.BusTicketReservation.entity.Trip;
import com.example.BusTicketReservation.repository.RouteRepository;
import com.example.BusTicketReservation.repository.BusRepository;
import com.example.BusTicketReservation.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TripService {

    private final TripRepository repo;
    private final RouteRepository routeRepo;
    private final BusRepository busRepo;

    public TripService(TripRepository repo, RouteRepository routeRepo, BusRepository busRepo) {
        this.repo = repo;
        this.routeRepo = routeRepo;
        this.busRepo = busRepo;
    }

    public Trip create(TripCreateRequest req) {
        Route route = routeRepo.findById(req.getRouteId())
                .orElseThrow(() -> new RuntimeException("Route not found"));
        Bus bus = busRepo.findById(req.getBusId())
                .orElseThrow(() -> new RuntimeException("Bus not found"));

        Trip t = new Trip();
        t.setRoute(route);
        t.setBus(bus);

        Date tripDate = (req.getTripDate() != null)
                ? toDate(req.getTripDate())
                : new Date(); // today
        t.setTripDate(tripDate);

        t.setPricePerSeat(req.getPricePerSeat());
        return repo.save(t);
    }

    public List<Trip> findAll() {
        return repo.findAll();
    }

    public Optional<Trip> findById(Long id) {
        return repo.findById(id);
    }

    public Trip update(Long id, Trip req) {
        Trip t = repo.findById(id).orElseThrow(() -> new RuntimeException("Trip not found"));

        if (req.getRoute() != null) {
            t.setRoute(req.getRoute());
        }
        if (req.getBus() != null) {
            t.setBus(req.getBus());
        }
        if (req.getTripDate() != null) {
            t.setTripDate(req.getTripDate());
        }
        if (req.getPricePerSeat() != 0) {
            t.setPricePerSeat(req.getPricePerSeat());
        }
        return repo.save(t);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<Trip> search(String source, String destination, LocalDate date) {
        return repo.findAll();
    }

    private Date toDate(LocalDate ld) {
        return Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
