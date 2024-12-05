package com.sandinu.TicketingBackend.controller;

import com.sandinu.TicketingBackend.model.User;
import com.sandinu.TicketingBackend.model.UserDeets;
import com.sandinu.TicketingBackend.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession httpSession){
        UserDeets user = userService.login(email, password, httpSession);
        UserDeets ux = (UserDeets) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(ux);
        return ResponseEntity.ok(user.getEmail());
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
