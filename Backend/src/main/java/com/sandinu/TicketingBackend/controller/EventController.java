package com.sandinu.TicketingBackend.controller;

import com.sandinu.TicketingBackend.model.Event;
import com.sandinu.TicketingBackend.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService){
        this.eventService = eventService;
    }

    //Create new event
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

    //Get event by ID
    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable String eventId){
        Event event = eventService.getEventById(eventId);
        return ResponseEntity.ok(event);
    }

    @PostMapping("/{eventId}/add-tickets")
    public ResponseEntity<Event> addTickets(
            @PathVariable String eventId,
            @RequestParam int count,
            @RequestParam String vendorId
    ){
        Event event = eventService.addTickets(eventId, count, vendorId);
        return ResponseEntity.ok(event);
    }

}
