package com.example.bankapi.repos;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.bankapi.models.User;

public interface UserRepo extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
