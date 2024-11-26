package com.sandinu.TicketingBackend.repo;

import com.sandinu.TicketingBackend.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventRepo extends MongoRepository<Event, String> {
    List<Event> findByVendorIdContains(String vendorId);
}
