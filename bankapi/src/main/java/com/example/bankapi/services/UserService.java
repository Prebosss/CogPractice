package com.example.bankapi.services;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.bankapi.models.User;
import com.example.bankapi.repos.UserRepo;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    public Optional<User> login(String username, String password) {
        return userRepo.findByUsername(username)
                .filter(user -> user.getPassword().equals(password));
    }

    public User createUser(User user) {
        userRepo.findByUsername(user.getUsername())
                .ifPresent(u -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
                });
        user.setPassword(
        passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public void deleteUser(String id) {
        userRepo.deleteById(id);
    }

    public User updateUser(String id, User updatedUser) {
        return userRepo.findById(id)
                .map(existingUser -> {
                    if (updatedUser.getUsername() != null) {
                        existingUser.setUsername(updatedUser.getUsername());
                    }
                    if (updatedUser.getPassword() != null) {
                        existingUser.setPassword(updatedUser.getPassword());
                    }
                    return userRepo.save(existingUser);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
    }

    public User authenticate(
    String username,
    String rawPassword
) {
    User user = userRepo
        .findByUsername(username)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "Incorrect username or password"
        ));

    if (!passwordEncoder.matches(
        rawPassword,
        user.getPassword()
    )) {
        throw new ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "Incorrect username or password"
        );
    }

    return user;
}
}
