package com.sandinu.TicketingBackend.service;

import java.util.concurrent.ThreadFactory;

public class PriorityThreadFactory implements ThreadFactory {
    private final int priority;

    public PriorityThreadFactory(int priority){
        this.priority = priority;
    }

    @Override
    public Thread newThread(Runnable r){
        Thread t = new Thread(r);
        t.setPriority(priority);
        return t;
    }
}
