package com.sandinu.TicketingBackend.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class UpdateEventDTO {
    private String eventId;
    private String eventDesc;
    private LocalDate eventDate;
    private LocalTime eventStartTime;
    private String eventLocation;
}
