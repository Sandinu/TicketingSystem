package com.sandinu.TicketingBackend.DTO;

import lombok.Data;

@Data
public class RegisterUserDTO {
    private String name;
    private String email;
    private String password;
}
