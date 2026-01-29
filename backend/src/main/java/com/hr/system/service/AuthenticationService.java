package com.hr.system.service;

import com.hr.system.controller.AuthenticationRequest;
import com.hr.system.controller.AuthenticationResponse;
import com.hr.system.controller.RegisterRequest;
import com.hr.system.entity.User;
import com.hr.system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        repository.save(user);
        var jwtToken = jwtService.generateToken(new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                java.util.Collections
                        .singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                "ROLE_" + user.getRole()))));
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        System.out.println("Authenticating user: " + request.getUsername());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));
            System.out.println("Authentication successful for: " + request.getUsername());
        } catch (Exception e) {
            System.err.println("AuthenticationManager failed: " + e.getMessage());
            throw e;
        }

        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        System.out.println("User found in DB: " + user.getUsername() + ", generating token...");
        var jwtToken = jwtService.generateToken(new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                java.util.Collections
                        .singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                "ROLE_" + user.getRole()))));
        System.out.println("Token generated successfully");
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
