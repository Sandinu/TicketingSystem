package com.sandinu.TicketingBackend.service;

import com.sandinu.TicketingBackend.model.Customer;
import com.sandinu.TicketingBackend.model.User;
import com.sandinu.TicketingBackend.model.Vendor;
import com.sandinu.TicketingBackend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByEmail(email);

        if (user.isPresent()){
            var userObj = user.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(userObj.getEmail())
                    .password(userObj.getPassword())
                    .roles(userObj.getRole())
                    .build();
        }else{
            throw new UsernameNotFoundException(email);
        }
    }

//    public User login(String email, String password){
//        Optional<User> user = userRepo.findByEmail(email);
//        if (user != null && user.getPassword().equals(password)){
//            return user;
//        }
//        throw new RuntimeException("Invalid email or password");
//    }

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
