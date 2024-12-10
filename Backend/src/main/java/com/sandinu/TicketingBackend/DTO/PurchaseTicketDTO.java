package com.sandinu.TicketingBackend.DTO;

import lombok.Data;

@Data
public class PurchaseTicketDTO {
    private int ticketCount;
    private String customerId;
}
