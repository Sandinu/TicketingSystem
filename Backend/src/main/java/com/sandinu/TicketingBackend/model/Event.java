package com.sandinu.TicketingBackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Data
@Document(collection = "events")
public class Event {
    @Id
    private String eventId;
    private String eventName;
    private String eventDescription;
    private int maxCapacity;
    private int totalTickets;
    private int totalTicketsSold = 0;
    private int totalTicketsAdded = 0;
    private int customerRetrievalRate; // Rate at which customers retrieve tickets
    private int ticketReleaseRate; // Rate at which vendors add tickets
    //private List<Vendor> vendorList = new ArrayList<>(); //List of vendors for each event

    private LocalDate eventDate;  // Date of the event (e.g., 2024-12-15)
    private LocalTime eventStartTime; // Start time of the event (e.g., 15:30)
    private String eventLocation; // Location of the event (e.g., "Conference Hall A")

    private ConcurrentLinkedQueue<Ticket> ticketpool = new ConcurrentLinkedQueue<>(); //Shared ticketpool specific for the event
    //thread-safe, non-blocking, FIFO queue that allows multiple threads to safely access and modify it concurrently.

    private List<Vendor> vendorId = new ArrayList<>(); //list of vendors for each event
    private List<TicketLog> ticketLogs = new ArrayList<>(); //Ticket transaction logs
    private String eventImageUrl;

    private boolean isSoldOut(){
        return totalTicketsSold >= totalTickets;
    }

}
