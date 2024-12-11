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

    ExecutorService executor = Executors.newFixedThreadPool(200, new PriorityThreadFactory(Thread.MIN_PRIORITY));
    ExecutorService VipExecutors = Executors.newFixedThreadPool(50, new PriorityThreadFactory(Thread.MAX_PRIORITY));;
    private int manualVendors = 1;
    private int manualCustomers = 1;

    public EventController(EventService eventService){
        this.eventService = eventService;
    }

    //Create new event
    @PostMapping("/create")
    public ResponseEntity<Event> createEvent(
            @RequestBody CreateEventDTO createEventDTO
            )
            {
                Event event = eventService.createEvent(createEventDTO.getName(), createEventDTO.getDescription(), createEventDTO.getMaxCapacity(), createEventDTO.getTotalTickets(), createEventDTO.getCustomerRetrievalRate(),createEventDTO.getTicketReleaseRate(), createEventDTO.getEventDate(), createEventDTO.getEventStartTime(), createEventDTO.getEventLocation(),createEventDTO.getEventImageUrl(),createEventDTO.getVendorId());
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
            executor = Executors.newFixedThreadPool(200, new PriorityThreadFactory(Thread.MIN_PRIORITY));
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
        if (VipExecutors.isShutdown()) {
            VipExecutors = Executors.newFixedThreadPool(50, new PriorityThreadFactory(Thread.MAX_PRIORITY));
        }
        for (int i = 0; i <= vipCustomerDTO.getCount(); i++){
            VipExecutors.submit(new CustomerTask(eventService, vipCustomerDTO.getEventId(), "VIPCustomer"+i, true));
        }
        return ResponseEntity.ok("VIP Customers Added!");
    }

    @PostMapping("/add-vendor")
    public ResponseEntity<String> addVendor(
            @RequestBody UserEventId userEventId
    ){
        executor.submit(new VendorTask(eventService, userEventId.getEventId(), "manualVendor" + manualVendors));
        manualVendors++;
        return ResponseEntity.ok("Vendor Added!");
    }

    @PostMapping("/add-customer")
    public ResponseEntity<String> addCustomer(
            @RequestBody UserEventId userEventId
    ){
        executor.submit(new CustomerTask(eventService, userEventId.getEventId(), "simCustomer"+ manualCustomers, false));
        manualCustomers++;
        return ResponseEntity.ok("Vendor Added!");
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

    @GetMapping("/all")
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


    @PostMapping("/event-update")
    public ResponseEntity<Event> updateEventDate(
            @RequestBody UpdateEventDTO updateEventDTO
    ){
        Event event = eventService.updateEventDetails(updateEventDTO.getEventId(), updateEventDTO.getEventDesc(), updateEventDTO.getEventDate(), updateEventDTO.getEventStartTime(), updateEventDTO.getEventLocation());
        return ResponseEntity.ok(event);
    }
}

