package com.example.eventmanagement.controller;

import com.example.eventmanagement.model.Event;
import com.example.eventmanagement.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // Create a new event
    @PostMapping("/create")
    public ResponseEntity<Event> createEvent(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam int maxCapacity,
            @RequestParam int totalTickets,
            @RequestParam int customerRetrievalRate,
            @RequestParam int ticketReleaseRate) {
        Event event = eventService.createEvent(name, description, maxCapacity, totalTickets, customerRetrievalRate, ticketReleaseRate);
        return ResponseEntity.ok(event);
    }

    // Get event by ID
    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable String eventId) {
        Event event = eventService.getEventById(eventId);
        return ResponseEntity.ok(event);
    }

    // Add tickets to an event's pool
    @PostMapping("/{eventId}/add-tickets")
    public ResponseEntity<Event> addTickets(
            @PathVariable String eventId,
            @RequestParam int count,
            @RequestParam String vendorId) {
        Event event = eventService.addTickets(eventId, count, vendorId);
        return ResponseEntity.ok(event);
    }

    // Purchase tickets from an event's pool
    @PostMapping("/{eventId}/purchase-tickets")
    public ResponseEntity<Event> purchaseTickets(
            @PathVariable String eventId,
            @RequestParam int count,
            @RequestParam String customerId) {
        Event event = eventService.purchaseTickets(eventId, count, customerId);
        return ResponseEntity.ok(event);
    }

    // Get all events
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }
}
