package com.example.bankapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankapi.dtos.AuthResponse;
import com.example.bankapi.dtos.LoginRequest;
import com.example.bankapi.dtos.RegisterRequest;
import com.example.bankapi.dtos.UserResponse;
import com.example.bankapi.models.User;
import com.example.bankapi.services.JwtService;
import com.example.bankapi.services.UserService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(
        UserService userService,
        JwtService jwtService
    ) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
        @RequestBody RegisterRequest request
    ) {
        User newUser = new User(
            null,
            request.username(),
            request.password()
        );

        User savedUser = userService.createUser(newUser);

        String token = jwtService.generateToken(savedUser);

        AuthResponse response = new AuthResponse(
            token,
            new UserResponse(
                savedUser.getId(),
                savedUser.getUsername()
            )
        );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
        @RequestBody LoginRequest request
    ) {
        User user = userService.authenticate(
            request.username(),
            request.password()
        );

        String token = jwtService.generateToken(user);

        AuthResponse response = new AuthResponse(
            token,
            new UserResponse(
                user.getId(),
                user.getUsername()
            )
        );

        return ResponseEntity.ok(response);
    }
}