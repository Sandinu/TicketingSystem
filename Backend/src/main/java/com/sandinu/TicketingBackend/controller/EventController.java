package com.sandinu.TicketingBackend.controller;

import com.sandinu.TicketingBackend.model.Event;
import com.sandinu.TicketingBackend.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService){
        this.eventService = eventService;
    }

    @PostMapping("/create")
    public ResponseEntity<Event> createEvent(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam int maxCapacity,
            @RequestParam int totalTickets,
            @RequestParam int customerRetrievalRate,
            @RequestParam int ticketReleaseRate)

            {
                Event event = eventService.createEvent(name, description, maxCapacity, totalTickets, customerRetrievalRate, ticketReleaseRate);
                return ResponseEntity.ok(event);
            }
}
