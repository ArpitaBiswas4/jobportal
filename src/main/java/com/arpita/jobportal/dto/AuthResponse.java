package com.arpita.jobportal.dto;

import com.arpita.jobportal.model.User.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private Long userId;
    private String email;
    private String fullName;
    private Role role;
    private String message;
}
