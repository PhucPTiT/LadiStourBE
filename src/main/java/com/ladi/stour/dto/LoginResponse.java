package com.ladi.stour.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String id;
    private String username;
    private String email;
    private String fullName;
    private String role;
    private String token;
    private String message;

    // Backward compatibility constructor without token
    public LoginResponse(String id, String username, String email, String fullName, String role, String message) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.token = null;
        this.message = message;
    }
}
