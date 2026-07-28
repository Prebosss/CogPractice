package com.example.bankapi.repos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.bankapi.models.Customer;

@Repository
public class CustomerRepo {
    private final List<Customer> customers = new ArrayList<>();
    public CustomerRepo() {
        customers.add(new Customer(1, "John Doe"));
        customers.add(new Customer(2, "Jane Smith"));
        customers.add(new Customer(3, "Alice Johnson"));
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public Optional<Customer> getCustomerById(Integer id) {
        List<Customer> customers = getCustomers();
        return customers.stream().filter(c -> c.getId() == id).findFirst();
    }

    public Customer createCustomer(Customer customer) {
        customer.setId(customers.size() + 1);
        customers.add(customer);
        return customer;
    }

    public void deleteCustomer(int id) {
        customers.removeIf(c -> c.getId() == id);
    }

    public Customer updateCustomer(int id, Customer updatedCustomer) {
            Customer existingCustomer = getCustomerById(id).get();
            if (updatedCustomer.getName() != null) {
                existingCustomer.setName(updatedCustomer.getName());
            }
            if (updatedCustomer.getBalance() != 0.0) {
                existingCustomer.setBalance(existingCustomer.getBalance() + updatedCustomer.getBalance());
            }
            return existingCustomer;
    }
}



    /*
    Optional<Customer> existingCustomerOpt = getCustomerById(id);
        if (existingCustomerOpt.isPresent()) {
            Customer existingCustomer = existingCustomerOpt.get();
            Customer existingCustomer = getCustomerById(id).get();
            existingCustomer.setName(updatedCustomer.getName());
            return existingCustomer;
        } else {
            return null; // or throw an exception
        }
     */
