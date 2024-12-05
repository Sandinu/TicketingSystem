package com.sandinu.TicketingBackend.controller;

import com.sandinu.TicketingBackend.config.JwtUtil;
import com.sandinu.TicketingBackend.model.User;
import com.sandinu.TicketingBackend.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam String email,
            @RequestParam String password
    ){
        User user = userService.login(email, password);
        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(token);
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
