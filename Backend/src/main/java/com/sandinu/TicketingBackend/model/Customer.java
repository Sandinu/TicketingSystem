package com.sandinu.TicketingBackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "customers")
public class Customer extends User{
    private boolean isVip;
    private List<Ticket> ticketHistory = new ArrayList<>();
}
