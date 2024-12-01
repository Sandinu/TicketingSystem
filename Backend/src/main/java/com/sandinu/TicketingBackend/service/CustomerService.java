package com.sandinu.TicketingBackend.service;

import com.sandinu.TicketingBackend.model.Customer;
import com.sandinu.TicketingBackend.repo.CustomerRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepo customerRepo;

    public CustomerService(CustomerRepo customerRepo){
        this.customerRepo = customerRepo;
    }

    public Customer createCustomer(String name, String email, String password){
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPassword(password);

        return customerRepo.save(customer);
    }

    public Customer getCustomerById(String id){
        return customerRepo.findById(id).orElseThrow(()->new RuntimeException("Customer not found"));
    }

    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }
}
