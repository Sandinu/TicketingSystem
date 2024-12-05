package com.sandinu.TicketingBackend.controller;

import com.sandinu.TicketingBackend.model.User;
import com.sandinu.TicketingBackend.model.UserDeets;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {
    HttpSession session;

    @GetMapping("/check-session")
    public ResponseEntity<String> checkSession(){;
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser"){
            UserDeets ux = (UserDeets) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return ResponseEntity.ok("User logged in: " + ux.getEmail());
        }else{
            return ResponseEntity.ok("No user logged in");
        }
    }
}
