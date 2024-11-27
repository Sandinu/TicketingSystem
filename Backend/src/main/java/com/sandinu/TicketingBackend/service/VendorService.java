package com.sandinu.TicketingBackend.service;

import com.sandinu.TicketingBackend.model.Vendor;
import com.sandinu.TicketingBackend.repo.VendorRepo;
import org.springframework.stereotype.Service;

@Service
public class VendorService {
    private final VendorRepo vendorRepo;

    public VendorService(VendorRepo vendorRepo){
        this.vendorRepo = vendorRepo;
    }

    public Vendor createVendor(Vendor)

}
