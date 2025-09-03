package com.example.BusTicketReservation.controller;

import com.example.BusTicketReservation.dto.AuthResponse;
import com.example.BusTicketReservation.dto.LoginRequest;
import com.example.BusTicketReservation.dto.RegisterRequest;
import com.example.BusTicketReservation.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
