package com.sandinu.TicketingBackend.repo;

import com.sandinu.TicketingBackend.model.Vendor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VendorRepo extends MongoRepository<Vendor, String> {
}
