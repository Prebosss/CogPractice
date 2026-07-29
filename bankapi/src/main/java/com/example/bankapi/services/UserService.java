package com.example.bankapi.services;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.bankapi.models.User;
import com.example.bankapi.repos.UserRepo;

@Service
public class UserService {
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> getUsers() {
        return userRepo.getUsers();
    }

    public Optional<User> getUserById(int id) {
        return userRepo.getUserById(id);
    }

    public User createUser(User user) {
        return userRepo.createUser(user);
    }

    public void deleteUser(int id) {
        userRepo.deleteUser(id);
    }

    public User updateUser(int id, User updatedUser) {
        return userRepo.updateUser(id, updatedUser);
    }
}
