package com.sandinu.TicketingBackend.controller;

import com.sandinu.TicketingBackend.model.Event;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate = messagingTemplate;
    }

    public void sendUpdateToClients(Event event){
        messagingTemplate.convertAndSend("/topic/event", event);
    }

    @MessageMapping("/sendMessage")
    public void recieveMessage(String message){
        System.out.println("Message recieved: " + message);
    }
}
