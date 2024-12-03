package com.sandinu.TicketingBackend.controller;

import com.sandinu.TicketingBackend.model.Customer;
import com.sandinu.TicketingBackend.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

//    @PostMapping("/create")
//    public ResponseEntity<Customer> createCustomer(
//            @RequestParam String name,
//            @RequestParam String email,
//            @RequestParam String password
//    ){
//        Customer customer = customerService.createCustomer(name, email, password);
//        return ResponseEntity.ok(customer);
//    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }
}
