package com.sandinu.TicketingBackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

@Data
public class UserDeets {
    private String username;
    private String id;
    private String email;
    private String password;
    private String roles;

    public UserDeets(String username,String id, String email, String password, String roles) {
        this.username = username;
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public UserDeets getCurrentUser() {
        return (UserDeets) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String toString() {
        return "UserDeets{" +
                "username='" + username + '\'' +
                ", id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles='" + roles + '\'' +
                '}';
    }

}
