package com.sandinu.TicketingBackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "vendors")
public class Vendor {
    @Id
    private String vendorId;
    private String name;
    private List<String> associatedEvents = new ArrayList<>(); //associated events
    private String email;
    private String password;
}
