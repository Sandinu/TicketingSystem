package com.sandinu.TicketingBackend.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    @GetMapping("/check-session")
    public ResponseEntity<String> checkSession(HttpSession session){
        Object loggedUser = session.getAttribute("user");

        if (loggedUser != null){
            return ResponseEntity.ok("User logged in: " + loggedUser.toString());
        }else{
            return ResponseEntity.ok("No user logged in");
        }
    }
}
