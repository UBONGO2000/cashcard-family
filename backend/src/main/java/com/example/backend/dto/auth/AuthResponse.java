package com.example.backend.dto.auth;


import com.example.backend.dto.user.UserResponse;

public record AuthResponse(
        String token,
        UserResponse user
) {
    public static AuthResponse of(String token, UserResponse user) {
        return new AuthResponse(token, user);
    }
}