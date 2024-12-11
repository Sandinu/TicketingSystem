package com.sandinu.TicketingBackend.controller;

import com.sandinu.TicketingBackend.model.Vendor;
import com.sandinu.TicketingBackend.service.VendorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {
    private final VendorService vendorService;

    public VendorController(VendorService vendorService){
        this.vendorService = vendorService;
    }

//    @PostMapping("/create")
//    public ResponseEntity<Vendor> createVendor(
//        @RequestParam String name,
//        @RequestParam String email,
//        @RequestParam String password
//    ){
//        Vendor vendor = vendorService.createVendor(name, email, password);
//        return ResponseEntity.ok(vendor);
//    }


    @PostMapping("/{vendorId}/associate-event")
    public ResponseEntity<Vendor> addCollabEvents(
            @PathVariable String vendorId,
            @RequestParam String eventId
    ){
        Vendor vendor = vendorService.addCollaboratedEvents(vendorId,eventId);
        return ResponseEntity.ok(vendor);
    }

    @GetMapping("/{vendorId}")
    public ResponseEntity<Vendor> getVendor(@PathVariable String vendorId){
        Vendor vendor = vendorService.findVendorById(vendorId);
        return ResponseEntity.ok(vendor);
    }

    @GetMapping
    public ResponseEntity<List<Vendor>> getAllVendors(){
        return ResponseEntity.ok(vendorService.getAllVendors());
    }
}
