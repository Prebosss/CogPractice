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
        return customerRepo.getCustomers();
    }

    public Optional<Customer> getCustomerById(int id) {
        return customerRepo.getCustomerById(id);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepo.createCustomer(customer);
    }

    public void deleteCustomer(int id) {
        customerRepo.deleteCustomer(id);
    }

    public Customer updateCustomer(int id, Customer updatedCustomer) {
        return customerRepo.updateCustomer(id, updatedCustomer);
    }
}
