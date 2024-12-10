package com.sandinu.TicketingBackend.controller;

import com.sandinu.TicketingBackend.DTO.LoginData;
import com.sandinu.TicketingBackend.model.User;
import com.sandinu.TicketingBackend.model.UserDeets;
import com.sandinu.TicketingBackend.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> login(
           @RequestBody LoginData loginData,
            HttpSession httpSession){
        UserDeets user = userService.login(loginData.getEmail(), loginData.getPassword(), httpSession);
        System.out.println(loginData.getEmail());
        return ResponseEntity.ok(user.getEmail());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession httpSession){
        httpSession.invalidate();
        return ResponseEntity.ok("Logged out successfully!");
    }

    @PostMapping("/register/customer")
    public ResponseEntity<User> registerCustomer(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password
    ){
        User user = userService.registerCustomer(name, email, password);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register/vendor")
    public ResponseEntity<User> registerVendor(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password
    ){
        User user = userService.registerVendor(name, email, password);
        return ResponseEntity.ok(user);
    }

}
