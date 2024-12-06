package com.sandinu.TicketingBackend.service;

import com.sandinu.TicketingBackend.model.Event;
import org.springframework.scheduling.annotation.Async;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class VendorTask implements Runnable{
    private final EventService eventService;
    private final String eventId;
    private final String vendorId;
    private final Random random = new Random();
    private volatile boolean isRunning = true;


    private final ReentrantLock lock = new ReentrantLock();

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
            while(isRunning){
                lock.lock(); {  // Synchronize on the event object
                    synchronized (eventService.getPauseLock()) {
                        while (eventService.isPaused()) {
                            eventService.getPauseLock().wait();
                        }
                    }
                    if (event.getTotalTicketsSold() >= event.getTotalTickets()) {
                        System.out.println("Ticket limit reached for event " + eventId + ", stopping customer task.");
                        Thread.currentThread().interrupt();
                        break;  // Stop the loop if total tickets sold reaches the limit
                    }

                    eventService.addTickets(eventId, random.nextInt(10) + 1, vendorId);
                    Thread.sleep(event.getTicketReleaseRate() * 1000);
                }
            }
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }finally {
            lock.unlock();
        }
    }
}
