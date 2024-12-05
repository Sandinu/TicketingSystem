package com.sandinu.TicketingBackend.repo;


import com.sandinu.TicketingBackend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepo extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    User getByEmail(String email);
}
