package com.sandinu.TicketingBackend.DTO;

import lombok.Data;

@Data
public class ConfigDTO {
    private String eventId;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int totalTickets;
}
