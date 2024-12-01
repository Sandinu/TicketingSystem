package com.example.eventmanagement.controller;

import com.example.eventmanagement.model.Customer;
import com.example.eventmanagement.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Create a new customer
    @PostMapping("/create")
    public ResponseEntity<Customer> createCustomer(@RequestParam String name, @RequestParam boolean isVip) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setVip(isVip);
        return ResponseEntity.ok(customerService.createCustomer(customer));
    }

    // Get all customers
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }
}
