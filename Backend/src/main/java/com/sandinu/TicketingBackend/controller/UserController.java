package com.sandinu.TicketingBackend.controller;

import com.sandinu.TicketingBackend.DTO.LoggedInUser;
import com.sandinu.TicketingBackend.DTO.LoginData;
import com.sandinu.TicketingBackend.DTO.RegisterUserDTO;
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
public ResponseEntity<?> login(
       @RequestBody LoginData loginData,
        HttpSession httpSession){
    try {
        UserDeets user = userService.login(loginData.getEmail(), loginData.getPassword(), httpSession);
        LoggedInUser loggedInUser = new LoggedInUser();
        loggedInUser.setEmail(user.getEmail());
        loggedInUser.setUsername(user.getUsername());
        loggedInUser.setRoles(user.getRoles());
        loggedInUser.setId(user.getId());

        System.out.println(loginData.getEmail());
        return ResponseEntity.ok(loggedInUser);
    } catch (RuntimeException e) {
        return ResponseEntity.status(404).body("User not found");
    }
}

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession httpSession){
        httpSession.invalidate();
        return ResponseEntity.ok("Logged out successfully!");
    }

    @PostMapping("/register/customer")
    public ResponseEntity<User> registerCustomer(
            @RequestBody RegisterUserDTO registerUserDTO
    ){
        User user = userService.registerCustomer(registerUserDTO.getName(), registerUserDTO.getEmail(), registerUserDTO.getPassword());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register/vendor")
    public ResponseEntity<User> registerVendor(
            @RequestBody RegisterUserDTO registerUserDTO
            ){
        User user = userService.registerVendor(registerUserDTO.getName(), registerUserDTO.getEmail(), registerUserDTO.getPassword());
        return ResponseEntity.ok(user);
    }

}
