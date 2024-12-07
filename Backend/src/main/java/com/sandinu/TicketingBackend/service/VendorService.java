package com.sandinu.TicketingBackend.service;

import com.sandinu.TicketingBackend.model.Customer;
import com.sandinu.TicketingBackend.model.User;
import com.sandinu.TicketingBackend.model.Vendor;
import com.sandinu.TicketingBackend.repo.UserRepo;
import com.sandinu.TicketingBackend.repo.VendorRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService {
    private final UserRepo vendorRepo;

    public VendorService(UserRepo vendorRepo){
        this.vendorRepo = vendorRepo;
    }

    //create a new vendor
//    public Vendor createVendor(String name, String Email, String password){
//        Vendor vendor = new Vendor();
//        vendor.setName(name);
//        vendor.setEmail(Email);
//        vendor.setPassword(password);
//
//        return vendorRepo.save(vendor);
//    }

    //Retrieve a vendor by Id
    private Vendor getVendorById(String Id){
        User user = vendorRepo.findById(Id).orElseThrow(() -> new RuntimeException("Vendor Not Found!"));

        if (user instanceof Vendor){
            return (Vendor) user;
        } else {
            throw new RuntimeException("Vendor Not Found!");
        }
    }

    //Add events for vendor's event list
    public Vendor addCollaboratedEvents(String vendorId, String eventId){
        Vendor vendor = getVendorById(vendorId);

        //Add the event to the associated event list of the vendor
        if (!vendor.getAssociatedEvents().contains(eventId)){
            vendor.getAssociatedEvents().add(eventId);
        }

        vendorRepo.save(vendor);
        return vendor;
    }

    public Vendor findVendorById(String vendorId){
        User user = vendorRepo.findById(vendorId).orElseThrow(()->new RuntimeException("Vendor not found"));

        if (user instanceof Vendor){
            return (Vendor) user;
        } else {
            throw new RuntimeException("Vendor not found");
        }
    }

    //Get all vendors
    public List<Vendor> getAllVendors(){
        List<User> users = vendorRepo.findAll();
        return users.stream().filter(user -> user instanceof Vendor)
                .map(user -> (Vendor) user)
                .toList();
    }

}
