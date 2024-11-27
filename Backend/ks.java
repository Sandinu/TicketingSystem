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




package com.example.eventmanagement.service;

import com.example.eventmanagement.model.Vendor;
import com.example.eventmanagement.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    // Create a new vendor
    public Vendor createVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    // Retrieve a vendor by ID
    public Vendor getVendorById(String id) {
        return vendorRepository.findById(id).orElseThrow(() -> new RuntimeException("Vendor not found"));
    }

    // Associate a vendor with an event
    public Vendor associateVendorWithEvent(String vendorId, String eventId) {
        Vendor vendor = getVendorById(vendorId);

        // Add the event ID to the vendor's associated events
        if (!vendor.getAssociatedEvents().contains(eventId)) {
            vendor.getAssociatedEvents().add(eventId);
        }

        return vendorRepository.save(vendor);
    }

    // Retrieve all vendors
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }
}
