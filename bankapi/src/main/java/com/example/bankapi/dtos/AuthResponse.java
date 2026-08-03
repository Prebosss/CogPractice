package com.example.bankapi.dtos;

public record AuthResponse(
        String token,
        UserResponse user) {
}