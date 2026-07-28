package com.example.bankapi.controllers;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankapi.models.Customer;
import com.example.bankapi.services.CustomerService;


@RestController
@RequestMapping("/api/v1")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getCustomers(){
	   return ResponseEntity.ok(this.customerService.getCustomers());
   }

   @GetMapping("/customers/{id}")
   public ResponseEntity<Customer> getCustomerById(@PathVariable int id) {
	   return this.customerService.getCustomerById(id)
		   .map(ResponseEntity::ok)
		   .orElseGet(() -> ResponseEntity.notFound().build());
   }

   @PostMapping("/customers")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.createCustomer(customer));
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable int id) {
        return customerService.getCustomerById(id)
                .map(c -> {
                    customerService.deleteCustomer(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable int id, @RequestBody Customer updatedCustomer) {
        return customerService.getCustomerById(id)
                .map(c -> {
                    return ResponseEntity.ok(customerService.updateCustomer(id, updatedCustomer));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

