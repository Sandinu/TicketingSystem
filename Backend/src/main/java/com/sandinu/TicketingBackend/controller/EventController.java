package com.sandinu.TicketingBackend.controller;

import com.sandinu.TicketingBackend.model.Event;
import com.sandinu.TicketingBackend.service.CustomerTask;
import com.sandinu.TicketingBackend.service.EventService;
import com.sandinu.TicketingBackend.service.VendorTask;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        System.out.println(eventId);
        return ResponseEntity.ok(event);
    }

    @PostMapping("/{eventId}/add-tickets")
    public ResponseEntity<Event> addTickets(
            @PathVariable String eventId,
            @RequestParam int count,
            @RequestParam String vendorId
    ){
        try {
            Event event = eventService.addTickets(eventId, count, vendorId);
            return ResponseEntity.ok(event);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupt flag
            return ResponseEntity.status(500).body(null); // Internal Server Error
        }
    }

    @PostMapping("/{eventId}/purchase-tickets")
    public ResponseEntity<Event> purchaseTickets(
            @PathVariable String eventId,
            @RequestParam int count,
            @RequestParam String customerId
    ){
        try {
            Event event = eventService.purchaseTickets(eventId, count, customerId);
            return ResponseEntity.ok(event);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupt flag
            return ResponseEntity.status(500).body(null); // Internal Server Error
        }
    }

    @PostMapping("/sim")
    public ResponseEntity<String> runSimulation(
            @RequestParam String eventId
    ){
        ExecutorService executor = Executors.newFixedThreadPool(100);

        for (int i = 0; i <= 50; i++){
            executor.submit(new VendorTask(eventService, eventId, "simVendor"+i));
        }
        for (int i = 0; i <= 50; i++){
            executor.submit(new CustomerTask(eventService, eventId, "simCustomer"+i));
        }

        return ResponseEntity.ok("simulation Done!");
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents(){
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }
}
