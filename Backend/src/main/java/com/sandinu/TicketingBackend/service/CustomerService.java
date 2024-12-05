package com.sandinu.TicketingBackend.service;

import com.sandinu.TicketingBackend.model.Customer;
import com.sandinu.TicketingBackend.model.Ticket;
import com.sandinu.TicketingBackend.model.User;
import com.sandinu.TicketingBackend.repo.CustomerRepo;
import com.sandinu.TicketingBackend.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final UserRepo customerRepo;

    public CustomerService(UserRepo customerRepo){
        this.customerRepo = customerRepo;
    }

//    public Customer createCustomer(String name, String email, String password){
//        Customer customer = new Customer();
//        customer.setName(name);
//        customer.setEmail(email);
//        customer.setPassword(password);
//        customer.setVip(false);
//
//        return customerRepo.save(customer);
//    }

    public Customer getCustomerById(String id){
        User user = customerRepo.findById(id).orElseThrow(()->new RuntimeException("Customer not found"));

        if (user instanceof Customer){
            return (Customer) user;
        } else {
            throw new RuntimeException("Customer not found");
        }
    }

    public Customer getCustomerByEmail(String email){
        User user = customerRepo.findByEmail(email).orElseThrow(()->new RuntimeException("Customer not found"));
        if (user instanceof Customer){
            return (Customer) user;
        } else {
            throw new RuntimeException("Customer not found");
        }
    }

    public List<Ticket> getTicketHistory(String id){
        Customer customer = getCustomerById(id);

        return customer.getTicketHistory().stream().toList();
    }

    public List<Customer> getAllCustomers() {
        List<User> users = customerRepo.findAll();
        return users.stream().filter(user -> user instanceof Customer)
                .map(user -> (Customer) user)
                .toList();
    }
}
