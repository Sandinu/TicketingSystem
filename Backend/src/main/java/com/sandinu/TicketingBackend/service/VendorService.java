package com.sandinu.TicketingBackend.service;

import com.sandinu.TicketingBackend.model.Vendor;
import com.sandinu.TicketingBackend.repo.VendorRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService {
    private final VendorRepo vendorRepo;

    public VendorService(VendorRepo vendorRepo){
        this.vendorRepo = vendorRepo;
    }

    //create a new vendor
    public Vendor createVendor(String name, String Email, String password){
        Vendor vendor = new Vendor();
        vendor.setName(name);
        vendor.setEmail(Email);
        vendor.setPassword(password);

        return vendorRepo.save(vendor);
    }

    //Retrieve a vendor by Id
    private Vendor getVendorById(String Id){
        return vendorRepo.findById(Id).orElseThrow(() -> new RuntimeException("Vendor Not Found!"));
    }

    //Add events for vendor's event list
    public Vendor addCollaboratedEvents(String vendorId, String eventId){
        Vendor vendor = getVendorById(vendorId);

        //Add the event to the associated event list of the vendor
        if (!vendor.getAssociatedEvents().contains(eventId)){
            vendor.getAssociatedEvents().add(eventId);
        }

        return vendorRepo.save(vendor);
    }

    //Get all vendors
    public List<Vendor> getAllVendors(){
        return vendorRepo.findAll();
    }

}
