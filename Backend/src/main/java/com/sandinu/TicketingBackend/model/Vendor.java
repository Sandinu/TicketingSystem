package com.sandinu.TicketingBackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "vendors")
public class Vendor extends User{
    private List<String> associatedEvents = new ArrayList<>(); //associated events


}
