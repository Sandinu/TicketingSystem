package com.sandinu.TicketingBackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
public class TicketLog {
    private String action;
    private String customerId;
    private String vendorId;
    private Date timestamp;
    private int ticketCount;
    private int totalTicketsAdded;
    private int totalTicketsSold;
}
