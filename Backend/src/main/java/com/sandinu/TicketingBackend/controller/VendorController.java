package com.sandinu.TicketingBackend.controller;

import com.sandinu.TicketingBackend.model.Vendor;
import com.sandinu.TicketingBackend.service.VendorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {
    private final VendorService vendorService;

    public VendorController(VendorService vendorService){
        this.vendorService = vendorService;
    }

    @PostMapping("/create")
    public ResponseEntity<Vendor> createVendor{
        @RequestParam

    }

    //name, email, password
}
