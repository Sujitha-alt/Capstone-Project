package com.example.BusTicketReservation.service;

import com.example.BusTicketReservation.entity.Bus;
import com.example.BusTicketReservation.repository.BusRepository;
import com.example.BusTicketReservation.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusService {

    private final BusRepository busRepo;
    private final TripRepository tripRepo;

    public BusService(BusRepository busRepo, TripRepository tripRepo) {
        this.busRepo = busRepo;
        this.tripRepo = tripRepo;
    }

    public Bus create(Bus req) {

        if (req.getAvailableSeats() == 0) {
            req.setAvailableSeats(req.getTotalSeats());
        }
        return busRepo.save(req);
    }

    public void delete(Long id) {
        busRepo.deleteById(id);
    }

    public Bus update(Long id, Bus req) {
        Bus b = busRepo.findById(id).orElseThrow(() -> new RuntimeException("Bus not found"));
        if (req.getBusNumber() != null)
            b.setBusNumber(req.getBusNumber());
        if (req.getBusType() != null)
            b.setBusType(req.getBusType());
        if (req.getTotalSeats() != 0)
            b.setTotalSeats(req.getTotalSeats());
        if (req.getAvailableSeats() != 0)
            b.setAvailableSeats(req.getAvailableSeats());
        return busRepo.save(b);
    }

    public List<Bus> findByRouteId(Long routeId) {
        return tripRepo.findBusesByRouteId(routeId);
    }
}
