package com.example.BusTicketReservation.service;

import com.example.BusTicketReservation.entity.Route;
import com.example.BusTicketReservation.repository.RouteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RouteService {

    private final RouteRepository repo;

    public RouteService(RouteRepository repo) {
        this.repo = repo;
    }

    public Route create(Route req) {
        return repo.save(req);
    }

    public List<Route> findAll() {
        return repo.findAll();
    }

    public Optional<Route> findById(Long id) {
        return repo.findById(id);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Route update(Long id, Route req) {
        Route r = repo.findById(id).orElseThrow(() -> new RuntimeException("Route not found"));
        if (req.getSource() != null)
            r.setSource(req.getSource());
        if (req.getDestination() != null)
            r.setDestination(req.getDestination());
        if (req.getDistance() != 0)
            r.setDistance(req.getDistance());
        if (req.getDuration() != null)
            r.setDuration(req.getDuration());
        return repo.save(r);
    }
}
