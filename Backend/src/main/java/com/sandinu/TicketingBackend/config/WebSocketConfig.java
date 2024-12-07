package com.sandinu.TicketingBackend.config;

import com.sandinu.TicketingBackend.service.EventWebSocketHandler;
import com.sandinu.TicketingBackend.service.TicketLogWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final EventWebSocketHandler eventWebSocketHandler;
    private final TicketLogWebSocketHandler ticketLogWebSocketHandler;

    public WebSocketConfig(EventWebSocketHandler eventWebSocketHandler, TicketLogWebSocketHandler ticketLogWebSocketHandler) {
        this.eventWebSocketHandler = eventWebSocketHandler;
        this.ticketLogWebSocketHandler = ticketLogWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(eventWebSocketHandler, "/ws/event-stats" ).setAllowedOrigins("*");
        registry.addHandler(ticketLogWebSocketHandler, "/ws/event-ticketlogs" ).setAllowedOrigins("*");
    }
}

//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.setApplicationDestinationPrefixes("/app");
//        registry.enableSimpleBroker("/topic");
//    }
//}