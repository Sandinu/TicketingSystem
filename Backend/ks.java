package com.example.eventmanagement.service;

import com.example.eventmanagement.model.Event;
import com.example.eventmanagement.model.Ticket;
import com.example.eventmanagement.model.TicketLog;
import com.example.eventmanagement.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Add tickets to the event's ticket pool
    public synchronized Event addTickets(String eventId, int count, String vendorId) {
        Event event = getEventById(eventId);

        // Check if adding tickets exceeds the total limit
        if (event.getCurrentTickets() + count > event.getTotalTickets()) {
            throw new RuntimeException("Cannot add tickets. Total ticket limit reached!");
        }

        // Generate and add tickets to the pool
        for (int i = 0; i < count; i++) {
            Ticket ticket = new Ticket();
            ticket.setTicketId("T-" + (event.getCurrentTickets() + i + 1));
            ticket.setEventId(eventId);
            event.getTicketPool().offer(ticket);
        }

        event.setCurrentTickets(event.getCurrentTickets() + count);

        // Log the transaction
        TicketLog log = new TicketLog();
        log.setAction("Add");
        log.setVendorId(vendorId);
        log.setTimestamp(new Date());
        log.setTicketCount(count);
        event.getTicketLogs().add(log);

        return eventRepository.save(event);
    }

    // Purchase tickets for a customer
    public synchronized Event purchaseTickets(String eventId, int count, String customerId) {
        Event event = getEventById(eventId);

        // Check if tickets are available
        if (event.getCurrentTickets() < count) {
            throw new RuntimeException("Not enough tickets available!");
        }

        // Remove tickets from the pool
        for (int i = 0; i < count; i++) {
            event.getTicketPool().poll();
        }

        event.setCurrentTickets(event.getCurrentTickets() - count);
        event.setTotalTicketsSold(event.getTotalTicketsSold() + count);

        // Log the transaction
        TicketLog log = new TicketLog();
        log.setAction("Purchase");
        log.setCustomerId(customerId);
        log.setTimestamp(new Date());
        log.setTicketCount(count);
        event.getTicketLogs().add(log);

        return eventRepository.save(event);
    }

    // Helper method to retrieve an event by ID
    private Event getEventById(String id) {
        return eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
    }
}
