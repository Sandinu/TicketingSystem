package com.sandinu.TicketingBackend.service;

import com.sandinu.TicketingBackend.model.Event;
import org.springframework.scheduling.annotation.Async;

import java.util.Random;

public class CustomerTask implements Runnable{
    private final EventService eventService;
    private final String eventId;
    private final String customerId;
    private final Random random = new Random();

    public CustomerTask(EventService eventService, String eventId, String customerId){
        this.eventService = eventService;
        this.eventId = eventId;
        this.customerId = customerId;
    }


    @Override
    @Async
    public void run(){
        Event event = eventService.getEventById(eventId);
        try{
            while (true){
                synchronized (event){
                    if (event.getTotalTicketsSold() >= event.getTotalTickets()) {
                        System.out.println("Ticket limit reached");
                        break;
                    }
                    eventService.purchaseTickets(eventId, random.nextInt(10) + 1, customerId);
                    Thread.sleep(event.getCustomerRetrievalRate() * 1000);
                }
            }
        }catch (InterruptedException e){
            //Thread.currentThread().interrupt();
        }
    }
}
