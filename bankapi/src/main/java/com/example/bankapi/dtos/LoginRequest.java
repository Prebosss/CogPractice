package com.example.bankapi.dtos;

public record LoginRequest(
    String username,
    String password
) {
}