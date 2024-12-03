package com.sandinu.TicketingBackend.service;

import com.sandinu.TicketingBackend.model.Event;
import org.springframework.scheduling.annotation.Async;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class CustomerTask implements Runnable{
    private final EventService eventService;
    private final String eventId;
    private final String customerId;
    private final Random random = new Random();
    private final ReentrantLock lock = new ReentrantLock();

    private volatile boolean isRunning = true;

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
            while (isRunning){
                lock.lock();{
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
        }finally {
            lock.unlock();
        }
    }
}