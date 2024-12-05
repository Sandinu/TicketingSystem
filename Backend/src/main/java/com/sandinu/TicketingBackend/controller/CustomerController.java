package com.sandinu.TicketingBackend.controller;

import com.sandinu.TicketingBackend.model.Customer;
import com.sandinu.TicketingBackend.model.Ticket;
import com.sandinu.TicketingBackend.model.UserDeets;
import com.sandinu.TicketingBackend.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/ticket-history")
    public ResponseEntity<List<Ticket>> getTicketHistory(
            HttpSession session
    ){
        UserDeets userDeets = (UserDeets) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Ticket> ticketHistory = customerService.getTicketHistory(customerService.getCustomerByEmail(userDeets.getEmail()).getUserId());
        return ResponseEntity.ok(ticketHistory);
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }
}
