package com.sandinu.TicketingBackend.controller;

import com.sandinu.TicketingBackend.DTO.*;
import com.sandinu.TicketingBackend.model.Event;
import com.sandinu.TicketingBackend.service.*;
import com.sun.java.accessibility.util.EventID;
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

    @PostMapping("/{eventId}/add-tickets")
    public ResponseEntity<Event> addTickets(
            @PathVariable String eventId,
            @RequestBody AddTicketDTO addTicketDTO
            ){
        try {
            Event event = eventService.addTickets(eventId, addTicketDTO.getTicketCount(), addTicketDTO.getVendorId());
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

    @PostMapping("/{eventId}/purchase-tickets-admin")
    public ResponseEntity<Event> purchaseTicketsAdmin(
            @PathVariable String eventId,
            @RequestBody PurchaseTicketDTO purchaseTicketDTO
    ){
        try {
            Event event = eventService.purchaseTickets(eventId, purchaseTicketDTO.getTicketCount(), purchaseTicketDTO.getCustomerId());
            return ResponseEntity.ok(event);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupt flag
            return ResponseEntity.status(500).body(null); // Internal Server Error
        }
    }

    @PostMapping("/sim")
    public ResponseEntity<String> runSimulation(
            @RequestBody String eventId
    ){

        if (executor.isShutdown()) {
            executor = Executors.newFixedThreadPool(100, new PriorityThreadFactory(Thread.MIN_PRIORITY));
        }

        for (int i = 0; i <= 50; i++){
            executor.submit(new VendorTask(eventService, eventId, "simVendor"+i));
        }
        for (int i = 0; i <= 50; i++){
            executor.submit(new CustomerTask(eventService, eventId, "simCustomer"+i, false));
        }

            return ResponseEntity.ok("simulation Started!");
    }

    @PostMapping("/vip")
    public ResponseEntity<String> addVipCustomer(
            @RequestBody VipCustomerDTO vipCustomerDTO
            ){
        VipExecutors = Executors.newFixedThreadPool(vipCustomerDTO.getCount(), new PriorityThreadFactory(Thread.MAX_PRIORITY));

        for (int i = 0; i <= vipCustomerDTO.getCount(); i++){
            VipExecutors.submit(new CustomerTask(eventService, vipCustomerDTO.getEventId(), "VIPCustomer"+i, true));
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
        if (VipExecutors != null){
            VipExecutors.shutdownNow();
        }
        System.out.println("Simulation Stopped!");

        return ResponseEntity.ok("Simulation Stopped!");
    }

    @PostMapping("/sim-reset")
    public ResponseEntity<?> resetEvent(@RequestBody UserEventId userEventId){
        try {
            Event eve = eventService.resetEvent(userEventId.getEventId());
            return ResponseEntity.ok(eve);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Event not found");
        }
    }

    @GetMapping("/threads")
    public ResponseEntity<Integer> getThreadCount(){
        return ResponseEntity.ok(Thread.activeCount());
    }

    @PostMapping("/ticket-logs")
    public ResponseEntity<byte[]> downloadTicketLogs(@RequestBody UserEventId userEventId ){
        CsvExport csvExport = new CsvExport(eventService);
        return csvExport.exportToCsv(userEventId.getEventId());
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents(){
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @PostMapping("/config-update")
    public ResponseEntity<Event> updateReleaseRate(
            @RequestBody ConfigDTO configDTO
            ){
        Event event = eventService.updateConfiguration(configDTO.getEventId(), configDTO.getTicketReleaseRate(), configDTO.getCustomerRetrievalRate(), configDTO.getTotalTickets());
        System.out.println(event);
        return ResponseEntity.ok(event);
    }


    @PostMapping("/date-update")
    public ResponseEntity<Event> updateEventDate(
            @RequestParam String eventId,
            @RequestParam LocalDate eventDate
    ){
        Event event = eventService.updateEventDate(eventId, eventDate);
        return ResponseEntity.ok(event);
    }

    @PostMapping("/start-time-update")
    public ResponseEntity<Event> updateEventStartTime(
            @RequestParam String eventId,
            @RequestParam LocalTime eventStartTime
    ){
        Event event = eventService.updateEventStartTime(eventId, eventStartTime);
        return ResponseEntity.ok(event);
    }

    @PostMapping("/location-update")
    public ResponseEntity<Event> updateEventLocation(
            @RequestParam String eventId,
            @RequestParam String eventLocation
    ){
        Event event = eventService.updateEventLocation(eventId, eventLocation);
        return ResponseEntity.ok(event);
    }

    @PostMapping("/description-update")
    public ResponseEntity<Event> updateEventDescription(
            @RequestParam String eventId,
            @RequestParam String description
    ){
        Event event = eventService.updateEventDescription(eventId, description);
        return ResponseEntity.ok(event);
    }
}
