package com.hr.system.controller;

import com.hr.system.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        System.out.println("Login attempt for user: " + request.getUsername());
        try {
            return ResponseEntity.ok(service.authenticate(request));
        } catch (Exception e) {
            System.err.println("Authentication failed for user " + request.getUsername() + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
