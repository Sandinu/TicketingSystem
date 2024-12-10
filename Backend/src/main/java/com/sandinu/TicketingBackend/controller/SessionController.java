package com.sandinu.TicketingBackend.controller;

import com.sandinu.TicketingBackend.model.User;
import com.sandinu.TicketingBackend.model.UserDeets;
import com.sandinu.TicketingBackend.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/session")
public class SessionController {
    UserService userService;
    HttpSession session;

    @GetMapping("/check-session")
    public ResponseEntity<String> checkSession(){;
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser"){
            UserDeets ux = (UserDeets) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return ResponseEntity.ok(ux.getId());
        }else{
            return ResponseEntity.ok(null);
        }
    }
}
