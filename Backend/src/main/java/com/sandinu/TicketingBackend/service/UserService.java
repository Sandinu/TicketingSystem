package com.sandinu.TicketingBackend.service;

import com.sandinu.TicketingBackend.model.Customer;
import com.sandinu.TicketingBackend.model.User;
import com.sandinu.TicketingBackend.model.UserDeets;
import com.sandinu.TicketingBackend.model.Vendor;
import com.sandinu.TicketingBackend.repo.UserRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public UserDeets login(String email, String password, HttpSession session){
        Optional<User> user = userRepo.findByEmail(email);
        if (user != null && user.get().getPassword().equals(password)){
            UserDeets userDeets = new UserDeets(user.get().getName(),user.get().getUserId(),email, password, user.get().getRole());

            Authentication auth = new UsernamePasswordAuthenticationToken(userDeets, null, null);
            SecurityContextHolder.getContext().setAuthentication(auth);

            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

            return (UserDeets) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        throw new RuntimeException("Invalid email or password");
    }


    public User registerCustomer(String name, String email, String password){
        if (userRepo.getByEmail(email) != null){
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
        if (userRepo.findByEmail(email).isPresent()){
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
