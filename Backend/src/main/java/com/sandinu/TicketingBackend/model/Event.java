package com.sandinu.TicketingBackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    private Queue<Ticket> ticketpool = new LinkedList<>(); //Shared ticketpool specific for the event
    private List<String> vendorId; //list of vendors for each event
    private List<TicketLog> ticketLogs = new ArrayList<>(); //Ticket transaction logs

    private boolean isSoldOut(){
        return totalTicketsSold >= totalTickets;
    }

}
