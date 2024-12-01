package com.sandinu.TicketingBackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "customers")
public class Customer {
    @Id
    private String customerId;
    private String name;
    private boolean isVip;
    private String email;
    private String password;
}
