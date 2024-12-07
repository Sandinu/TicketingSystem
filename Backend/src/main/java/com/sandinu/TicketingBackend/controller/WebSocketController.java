package com.sandinu.TicketingBackend.controller;

import com.sandinu.TicketingBackend.model.Event;
import com.sandinu.TicketingBackend.service.EventWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

//    private final SimpMessagingTemplate messagingTemplate;
//
//    public WebSocketController(SimpMessagingTemplate messagingTemplate){
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    public void sendEventUpdate(String eventId, int totalTicketsAdded, int totalTicketsSold){
//        messagingTemplate.convertAndSend("/topic/event/" + eventId, new EventUpdate(totalTicketsAdded, totalTicketsSold));
//    }
//
//    public static class EventUpdate{
//        private int totalTicketsAdded;
//        private int totalTicketsSold;
//
//        public EventUpdate(int totalTicketsAdded, int totalTicketsSold){
//            this.totalTicketsAdded = totalTicketsAdded;
//            this.totalTicketsSold = totalTicketsSold;
//        }
//
//    }

    private final EventWebSocketHandler eventWebSocketHandler;

    @Autowired
    public WebSocketController(EventWebSocketHandler eventWebSocketHandler){
        this.eventWebSocketHandler = eventWebSocketHandler;
    }

    public void sendEventUpdate(String eventId, int totalTicketsAdded, int totalTicketsSold){
        eventWebSocketHandler.sendEventUpdate(eventId, totalTicketsAdded, totalTicketsSold);
    }


}
