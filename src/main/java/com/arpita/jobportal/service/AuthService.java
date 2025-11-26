package com.arpita.jobportal.service;

import com.arpita.jobportal.dto.LoginRequest;
import com.arpita.jobportal.dto.RegisterRequest;
import com.arpita.jobportal.model.User;

public interface AuthService {
    User register(RegisterRequest request);
    User login(LoginRequest request);
    void logout();
    User getCurrentUser();
}
