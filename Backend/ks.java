package com.example.eventmanagement.controller;

import com.example.eventmanagement.model.Vendor;
import com.example.eventmanagement.service.VendorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    // Create a new vendor
    @PostMapping("/create")
    public ResponseEntity<Vendor> createVendor(@RequestParam String name) {
        Vendor vendor = new Vendor();
        vendor.setName(name);
        return ResponseEntity.ok(vendorService.createVendor(vendor));
    }

    // Associate vendor with an event
    @PostMapping("/{vendorId}/associate-event")
    public ResponseEntity<Vendor> associateVendorWithEvent(
            @PathVariable String vendorId,
            @RequestParam String eventId) {
        Vendor vendor = vendorService.associateVendorWithEvent(vendorId, eventId);
        return ResponseEntity.ok(vendor);
    }

    // Get all vendors
    @GetMapping
    public ResponseEntity<List<Vendor>> getAllVendors() {
        return ResponseEntity.ok(vendorService.getAllVendors());
    }
}
