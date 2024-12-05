package com.sandinu.TicketingBackend.service;

import com.sandinu.TicketingBackend.model.Customer;
import com.sandinu.TicketingBackend.model.User;
import com.sandinu.TicketingBackend.model.Vendor;
import com.sandinu.TicketingBackend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public User login(String email, String password){
        User user = userRepo.findByEmail(email);
        if (user != null && user.getPassword().equals(password)){
            return user;
        }
        throw new RuntimeException("Invalid email or password");
    }

    public User registerCustomer(String name, String email, String password){
        if (userRepo.findByEmail(email) != null){
            throw new RuntimeException("Email already registered");
        }
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPassword(password);
        customer.setRole("ROLE_CUSTOMER");
        customer.setVip(false);

        return userRepo.save(customer);
    }

    public User registerVendor(String name, String email, String password){
        if (userRepo.findByEmail(email) != null){
            throw new RuntimeException("Email already registered");
        }
        Vendor vendor = new Vendor();
        vendor.setName(name);
        vendor.setEmail(email);
        vendor.setRole("ROLE_VENDOR");
        vendor.setPassword(password);

        return userRepo.save(vendor);
    }
}
