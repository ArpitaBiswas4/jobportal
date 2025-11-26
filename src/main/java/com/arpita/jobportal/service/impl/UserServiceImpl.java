package com.arpita.jobportal.service.impl;

import com.arpita.jobportal.dto.RegisterRequest;
import com.arpita.jobportal.model.User;
import com.arpita.jobportal.repository.UserRepository;
import com.arpita.jobportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        user.setEnabled(true);

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User updateUser(Long userId, User updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(updatedUser.getFullName());
        user.setPhone(updatedUser.getPhone());

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
