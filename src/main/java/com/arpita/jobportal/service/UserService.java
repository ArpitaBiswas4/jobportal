package com.arpita.jobportal.service;

import com.arpita.jobportal.dto.RegisterRequest;
import com.arpita.jobportal.model.User;

import java.util.Optional;

public interface UserService {
    User registerUser(RegisterRequest request);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long userId);
    User updateUser(Long userId, User user);
    void deleteUser(Long userId);
    boolean existsByEmail(String email);
}
