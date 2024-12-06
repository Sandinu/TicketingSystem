package com.sandinu.TicketingBackend.controller;

import com.sandinu.TicketingBackend.model.Event;
import com.sandinu.TicketingBackend.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    ExecutorService executor = Executors.newFixedThreadPool(100, new PriorityThreadFactory(Thread.MIN_PRIORITY));
    ExecutorService VipExecutors;

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
            @RequestParam int ticketReleaseRate,
            @RequestParam LocalDate eventDate,
            @RequestParam LocalTime eventStartTime,
            @RequestParam String eventLocation)

            {
                Event event = eventService.createEvent(name, description, maxCapacity, totalTickets, customerRetrievalRate, ticketReleaseRate, eventDate, eventStartTime, eventLocation);
                return ResponseEntity.ok(event);
            }

    //Get event by ID
    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable String eventId){
        Event event = eventService.getEventById(eventId);
        return ResponseEntity.ok(event);
    }

    @PreAuthorize("hasRole('ROLE_VENDOR')")
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
            HttpSession session
    ){
        try {
            Event event = eventService.purchaseTickets(eventId, count);
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

        for (int i = 0; i <= 50; i++){
            executor.submit(new VendorTask(eventService, eventId, "simVendor"+i));
        }
        for (int i = 0; i <= 50; i++){
            executor.submit(new CustomerTask(eventService, eventId, "simCustomer"+i, false));
        }

            return ResponseEntity.ok("simulation Started!");
    }

    @PostMapping("/VIP")
    public ResponseEntity<String> addVipCustomer(
            @RequestParam String eventId,
            @RequestParam int count
    ){
        VipExecutors = Executors.newFixedThreadPool(count, new PriorityThreadFactory(Thread.MAX_PRIORITY));

        for (int i = 0; i <= count; i++){
            VipExecutors.submit(new CustomerTask(eventService, eventId, "VIPCustomer"+i, true));
        }
        return ResponseEntity.ok("VIP Customers Added!");
    }

    @PostMapping("/sim-pause")
    public ResponseEntity<String> pauseSimulation(){
        if (eventService.isPaused()){
            return ResponseEntity.status(500).body("Simulation already paused!");
        }else{
            eventService.pauseSimulation();
        }
        return ResponseEntity.ok("Simulation Paused!");
    }

    @PostMapping("/sim-resume")
    public ResponseEntity<String> resumeSimulation(){
        if (!eventService.isPaused()){
            return ResponseEntity.status(500).body("Simulation already running!");
        }else{
            eventService.resumeSimulation();
        }
        return ResponseEntity.ok("Simulation Resumed!");
    }

    @PostMapping("/sim-stop")
    public ResponseEntity<String> stopSimulation(){
        executor.shutdownNow();
        VipExecutors.shutdownNow();
        System.out.println("Simulation Stopped!");
        return ResponseEntity.ok("Simulation Stopped!");
    }

    @PostMapping("/sim-reset")
    public ResponseEntity<Event> resetSimulation(
            @RequestParam String eventId
    ){
        Event eve = eventService.resetEvent(eventId);
        return ResponseEntity.ok(eve);
    }

    @GetMapping("/ticket-logs")
    public ResponseEntity<byte[]> downloadTicketLogs(
            @RequestParam String eventId
    ){
        CsvExport csvExport = new CsvExport(eventService);
        return csvExport.exportToCsv(eventId);
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents(){
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }


}
