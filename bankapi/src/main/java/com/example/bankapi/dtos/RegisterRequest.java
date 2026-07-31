package com.example.bankapi.dtos;

public record RegisterRequest(
    String username,
    String password
) {
}