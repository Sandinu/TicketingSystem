package com.sandinu.TicketingBackend.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateEventDTO {
    private String name;
    private String description;
    private int maxCapacity;
    private int totalTickets;
    private int customerRetrievalRate;
    private int ticketReleaseRate;
    private LocalDate eventDate;
    private LocalTime eventStartTime;
    private String eventLocation;
    private String eventImageUrl;
    private String vendorId;
}
