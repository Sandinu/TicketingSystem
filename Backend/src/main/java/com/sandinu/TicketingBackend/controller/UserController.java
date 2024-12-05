package com.sandinu.TicketingBackend.controller;

import com.sandinu.TicketingBackend.model.User;
import com.sandinu.TicketingBackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password
    ){
        return "Logged in";
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
