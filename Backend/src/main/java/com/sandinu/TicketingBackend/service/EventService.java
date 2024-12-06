package com.sandinu.TicketingBackend.service;

import com.sandinu.TicketingBackend.controller.EventController;
import com.sandinu.TicketingBackend.controller.WebSocketController;
import com.sandinu.TicketingBackend.model.*;
import com.sandinu.TicketingBackend.repo.CustomerRepo;
import com.sandinu.TicketingBackend.repo.EventRepo;
import com.sandinu.TicketingBackend.repo.UserRepo;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

@Data
@Service
public class EventService {
    private final EventRepo eventRepo;
    private final Random random = new Random();
    private final CustomerService customerService;
    private final CustomerRepo customerRepo;
    private final UserRepo userRepo;
    private final WebSocketController webSocketController;
    private EventController eventController;

    private final ReentrantLock lock = new ReentrantLock();
    private final Object pauseLock = new Object();
    private volatile boolean isPaused = false;


    public EventService(EventRepo eventRepo, CustomerService customerService, CustomerRepo customerRepo, UserRepo userRepo, WebSocketController webSocketController){
        this.eventRepo = eventRepo;
        this.customerService = customerService;
        this.customerRepo = customerRepo;
        this.userRepo = userRepo;
        this.webSocketController = webSocketController;
    }



    public Event createEvent(String name, String description, int maxCapacity, int totalTickets, int customerRetrievalRate, int ticketReleaseRate, LocalDate eventDate, LocalTime eventStartTime, String eventLocation){
//        if (totalTickets > maxCapacity) {
//            throw new IllegalArgumentException("Total tickets cannot exceed the maximum capacity!");
//        }

        Event event = new Event();
        event.setEventName(name);
        event.setEventDescription(description);
        event.setMaxCapacity(maxCapacity);
        event.setTotalTickets(totalTickets);
        event.setCustomerRetrievalRate(customerRetrievalRate);
        event.setTicketReleaseRate(ticketReleaseRate);
        event.setEventDate(eventDate);
        event.setEventStartTime(eventStartTime);
        event.setEventLocation(eventLocation);
        //event.getVendorList().add()

        return eventRepo.save(event);
        }



    public Event getEventById(String id){
        return eventRepo.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
    }



    public synchronized Event addTickets(String eventId, int ticketCount, String vendorId) throws InterruptedException{
        Event event = getEventById(eventId);

        if (event.getTotalTickets() == event.getTotalTicketsAdded()){
            Thread.currentThread().interrupt();
            return event;
        }

        if (event.getTotalTickets() < event.getTotalTicketsAdded() + ticketCount){
            System.out.println("Vendor Limit reached");
            return event;
        }

        while (event.getTicketpool().size() + ticketCount > event.getMaxCapacity() && event.getTotalTickets() > event.getTotalTicketsAdded() + ticketCount){
            System.out.println("Not enough space to add tickets, Vendor Waiting...");
            wait();
        }

        for (int i=0; i < ticketCount; i++){
            Ticket ticket = new Ticket();
            ticket.setTicketId(event.getEventId() + (event.getTotalTicketsAdded() + 1));
            ticket.setEventId(eventId);
            event.getTicketpool().offer(ticket);
        }

        event.setTotalTicketsAdded(event.getTotalTicketsAdded() + ticketCount);

        TicketLog log = new TicketLog();
        log.setAction("Add");
        log.setVendorId(vendorId);
        log.setTimestamp(new Date());
        log.setTicketCount(ticketCount);
        System.out.println(log);
        event.getTicketLogs().add(log);

        webSocketController.sendUpdateToClients(event);
        notifyAll();

        return eventRepo.save(event);
    }



    public synchronized Event purchaseTickets(String eventId, int count, String customerId) throws InterruptedException{
        Event event = getEventById(eventId);

        if (event.getTotalTickets() == event.getTotalTicketsSold()){
            Thread.currentThread().interrupt();
            eventController.stopSimulation();
            return event;
        }

        if (event.getTotalTickets() < event.getTotalTicketsSold() + count){
            System.out.println("Customer Limit reached");
            return event;
        }

        while (event.getTicketpool().size() < count && event.getTotalTickets() > event.getTotalTicketsSold() + count){
            System.out.println("No tickets available! " +customerId+ " waiting...");
            wait();
        }

        for (int i = 0; i < count; i++){
            event.getTicketpool().poll();
        }

        event.setTotalTicketsSold(event.getTotalTicketsSold() + count);

        TicketLog log = new TicketLog();
        log.setAction("Purchase");
        log.setCustomerId(customerId);
        log.setTimestamp(new Date());
        log.setTicketCount(count);
        System.out.println(log);
        event.getTicketLogs().add(log);

        webSocketController.sendUpdateToClients(event);
        notifyAll();

        return eventRepo.save(event);
    }

    public synchronized Event purchaseTickets(String eventId, int count) throws InterruptedException{
        Event event = getEventById(eventId);
        UserDeets userDeets = (UserDeets) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Customer customer = customerService.getCustomerById(userDeets.getId());

        while (event.getTicketpool().size() < count && event.getTotalTickets() > event.getTotalTicketsSold() + count){
            System.out.println("No tickets available! Customer waiting...");
            wait();
        }

        if (event.getTotalTickets() < event.getTotalTicketsSold() + count){
            System.out.println("Customer Limit reached");
            return event;
        }

        for (int i = 0; i < count; i++){
            Ticket ticket = event.getTicketpool().poll();
            customer.getTicketHistory().add(ticket);
            userRepo.save(customer);
            System.out.println(customer);
            System.out.println(ticket);
        }

        event.setTotalTicketsSold(event.getTotalTicketsSold() + count);

        TicketLog log = new TicketLog();
        log.setAction("Purchase");
        log.setCustomerId(customer.getUserId());
        log.setTimestamp(new Date());
        log.setTicketCount(count);
        System.out.println(log);
        event.getTicketLogs().add(log);
        notifyAll();

        return eventRepo.save(event);
    }

    public boolean pauseSimulation(){
        isPaused = true;
        System.out.println("System Paused");

        return isPaused;
    }

    public boolean resumeSimulation(){
        isPaused = false;

        synchronized (pauseLock){
            pauseLock.notifyAll();
        }
        System.out.println("System Resumed");
        return isPaused;
    }

    public Event resetEvent(String eventId){
        Event event = getEventById(eventId);
        event.setTotalTicketsSold(0);
        event.setTotalTicketsAdded(0);
        event.getTicketpool().clear();
        event.getTicketLogs().clear();
        return eventRepo.save(event);
    }

    public List<Event> getAllEvents(){
        return eventRepo.findAll();
    }


}
