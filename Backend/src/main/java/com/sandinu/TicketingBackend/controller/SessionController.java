package com.sandinu.TicketingBackend.controller;

import com.sandinu.TicketingBackend.config.JwtUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    private final JwtUtil jwtUtil;

    public SessionController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/check-session")
    public ResponseEntity<String> checkSession(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok("User logged in: " + authentication.getName());
        } else {
            return ResponseEntity.ok("No user logged in");
        }
    }
}
