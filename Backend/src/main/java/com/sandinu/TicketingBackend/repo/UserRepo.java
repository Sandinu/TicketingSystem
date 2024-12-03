package com.sandinu.TicketingBackend.repo;


import com.sandinu.TicketingBackend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<User, String> {
    User findByEmail(String email);
}
