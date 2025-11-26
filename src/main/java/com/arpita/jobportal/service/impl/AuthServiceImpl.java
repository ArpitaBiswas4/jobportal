package com.arpita.jobportal.service.impl;

import com.arpita.jobportal.dto.LoginRequest;
import com.arpita.jobportal.dto.RegisterRequest;
import com.arpita.jobportal.model.User;
import com.arpita.jobportal.repository.UserRepository;
import com.arpita.jobportal.service.AuthService;
import com.arpita.jobportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Override
    public User register(RegisterRequest request) {
        return userService.registerUser(request);
    }

    @Override
    public User login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("No authenticated user found");
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }
}