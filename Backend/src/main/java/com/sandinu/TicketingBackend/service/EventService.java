package com.sandinu.TicketingBackend.service;

import com.sandinu.TicketingBackend.model.Event;
import com.sandinu.TicketingBackend.model.Ticket;
import com.sandinu.TicketingBackend.model.TicketLog;
import com.sandinu.TicketingBackend.repo.EventRepo;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EventService {
    private final EventRepo eventRepo;

    public EventService(EventRepo eventRepo){
        this.eventRepo = eventRepo;
    }

    public Event createEvent(String name, String description, int maxCapacity, int totalTickets, int customerRetrievalRate, int ticketReleaseRate) {
        if (totalTickets > maxCapacity) {
            throw new IllegalArgumentException("Total tickets cannot exceed the maximum capacity!");
        }

        Event event = new Event();
        event.setEventName(name);
        event.setEventDescription(description);
        event.setMaxCapacity(maxCapacity);
        event.setTotalTickets(totalTickets);
        event.setCustomerRetrievalRate(customerRetrievalRate);
        event.setTicketReleaseRate(ticketReleaseRate);

        return eventRepo.save(event);
    }

    public Event getEventById(String id){
        return eventRepo.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public synchronized Event addTickets(String eventId, int ticketCount, String vendorId){
        Event event = getEventById(eventId);

        if (event.getTotalTicketsAdded() + ticketCount > event.getTotalTickets()){
            throw new RuntimeException("Cannot add more tickets! Total ticket count exceeded!");
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
        event.getTicketLogs().add(log);

        return eventRepo.save(event);
    }

    public synchronized Event purchaseTickets(String eventId, int count, String customerId){
        Event event = getEventById(eventId);

        if (event.getTicketpool().size() < count){
            throw new RuntimeException("Not enough tickets available!");
        }

        
    }
}
