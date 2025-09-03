package com.example.BusTicketReservation.service;

import com.example.BusTicketReservation.config.JwtUtil;
import com.example.BusTicketReservation.dto.AuthResponse;
import com.example.BusTicketReservation.dto.LoginRequest;
import com.example.BusTicketReservation.dto.RegisterRequest;
import com.example.BusTicketReservation.entity.User;
import com.example.BusTicketReservation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthResponse(null, "User already exists!");
        }

        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        String role = (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN"))
                ? "ROLE_ADMIN"
                : "ROLE_USER";

        User newUser = User.builder()
                .username(request.getUsername())
                .password(encryptedPassword)
                .role(role)
                .build();

        userRepository.save(newUser);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(newUser.getUsername())
                .password(newUser.getPassword())
                .authorities(newUser.getRole())
                .build();

        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token, "Registration successful!");
    }

    public AuthResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        if (auth.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);

            return new AuthResponse(token, "Login successful!");
        } else {
            return new AuthResponse(null, "Invalid credentials!");
        }
    }
}
