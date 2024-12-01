package com.sandinu.TicketingBackend.service;

import com.sandinu.TicketingBackend.model.Event;
import org.springframework.scheduling.annotation.Async;

import java.util.Random;

public class VendorTask implements Runnable{
    private final EventService eventService;
    private final String eventId;
    private final String vendorId;
    private final Random random = new Random();

    public VendorTask(EventService eventService, String eventId, String vendorId){
        this.eventService = eventService;
        this.eventId = eventId;
        this.vendorId = vendorId;
    }


    @Override
    @Async
    public void run(){
        Event event = eventService.getEventById(eventId);
        try{
            while(true){
                synchronized (event) {  // Synchronize on the event object
                    if (event.getTotalTicketsSold() >= event.getTotalTickets()) {
                        System.out.println("Ticket limit reached for event " + eventId + ", stopping customer task.");
                        break;  // Stop the loop if total tickets sold reaches the limit
                    }

                    eventService.addTickets(eventId, random.nextInt(10) + 1, vendorId);
                    Thread.sleep(event.getTicketReleaseRate() * 1000);
                }
            }
        }catch (InterruptedException e){
            //Thread.currentThread().interrupt();
        }
    }
}
