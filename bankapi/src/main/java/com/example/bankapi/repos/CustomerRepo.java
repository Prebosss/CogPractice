package com.example.bankapi.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.bankapi.models.Customer;

@Repository
public interface CustomerRepo extends MongoRepository<Customer, String> {
}

