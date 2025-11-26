package com.arpita.jobportal.controller;

import com.arpita.jobportal.dto.AuthResponse;
import com.arpita.jobportal.dto.LoginRequest;
import com.arpita.jobportal.dto.RegisterRequest;
import com.arpita.jobportal.model.User;
import com.arpita.jobportal.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);

        AuthResponse response = new AuthResponse(
                user.getUserId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                "Registration successful"
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = authService.login(request);

        AuthResponse response = new AuthResponse(
                user.getUserId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                "Login successful"
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        authService.logout();
        return ResponseEntity.ok("Logout successful");
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurrentUser() {
        User user = authService.getCurrentUser();

        AuthResponse response = new AuthResponse(
                user.getUserId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                "User retrieved successfully"
        );

        return ResponseEntity.ok(response);
    }
}