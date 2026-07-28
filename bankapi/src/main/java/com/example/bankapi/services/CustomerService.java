package com.example.bankapi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.bankapi.models.Customer;
import com.example.bankapi.repos.CustomerRepo;

@Service
public class CustomerService {
    private final CustomerRepo customerRepo;

    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public List<Customer> getCustomers() {
        return customerRepo.findAll();
    }

    public Optional<Customer> getCustomerById(String id) {
        return customerRepo.findById(id);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepo.save(customer);
    }

    public void deleteCustomer(String id) {
        customerRepo.deleteById(id);
    }

    public Customer updateCustomer(String id, Customer updatedCustomer) {
        return customerRepo.findById(id)
                .map(existingCustomer -> {
                    if (updatedCustomer.getName() != null) {
                        existingCustomer.setName(updatedCustomer.getName());
                    }
                    if (updatedCustomer.getBalance() != 0.0) {
                        existingCustomer.setBalance(existingCustomer.getBalance() + updatedCustomer.getBalance());
                    }
                    return customerRepo.save(existingCustomer);
                })
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }
}
