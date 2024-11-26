package com.sandinu.TicketingBackend.repo;

import com.sandinu.TicketingBackend.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepo extends MongoRepository<Customer, String> {
}
