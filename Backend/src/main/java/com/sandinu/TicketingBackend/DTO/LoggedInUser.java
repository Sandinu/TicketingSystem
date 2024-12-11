package com.sandinu.TicketingBackend.DTO;

import lombok.Data;

@Data
public class LoggedInUser {
    private String username;
    private String id;
    private String email;
    private String password;
    private String roles;
}
