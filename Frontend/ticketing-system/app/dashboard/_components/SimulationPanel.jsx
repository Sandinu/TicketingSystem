'use client'
import React from 'react'
import { Button } from '@/components/ui/button';
import { useState } from 'react';
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { useToast } from '@/hooks/use-toast';


const SimulationPanel = ({eventid}) => {
    const [vendors, setVendors] = useState(50);
    const [customers, setCustomers] = useState(50);
    const [vipCustomers, setVipCustomers] = useState(0);
    const [simRunning, setSimRunnoing] = useState(false);
    const [ticketAddDialog, setTicketAddDialog] = useState(false);
    const [ticketBuyDialog, setTicketBuyDialog] = useState(false);

    const [addTicketCount, setAddTicketCount] = useState(0);
    const [buyTicketCount, setBuyTicketCount] = useState(0);
    const {toast} = useToast();

    const handleStartClick = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/events/sim', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: eventid,
        });
  
        if (!response.ok) {
          throw new Error(`Network response was not ok: ${response.statusText}`);
        }
  
        const data = await response.text();
        setSimRunnoing(true);
      } catch (error) {
        console.log('Error starting simulation:', error);
      }
    };

    const handleStopClick = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/events/sim-stop', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: eventid,
        });
  
        if (!response.ok) {
          throw new Error(`Network response was not ok: ${response.statusText}`);
        }
  
        const data = await response.text();
        setSimRunnoing(false);
        setVipCustomers(0);
        setCustomers(50);
        setVendors(50);
      } catch (error) {
        console.log('Error stopping simulation:', error);
      }
    };

    const handleResetClick = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/events/sim-reset', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ eventId: eventid }),
        });
  
        if (!response.ok) {
          throw new Error(`Network response was not ok: ${response.statusText}`);
        }
  
        const data = await response.text();
        window.location.reload();
      } catch (error) {
        console.log('Error starting simulation:', error);
      }
    };

    const handleVipCustomerRequest = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/events/vip', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ eventId: eventid , count: 1}),
        });
  
        if (!response.ok) {
          throw new Error(`Network response was not ok: ${response.statusText}`);
        }
  
        const data = await response.text();
        setVipCustomers(vipCustomers + 1);
        setSimRunnoing(true);
        console.log('VIP customer requested:', data);
      } catch (error) {
        console.log('Error starting simulation:', error);
      }
    };
    
    const handleAddTickets = async () => {
      if(addTicketCount <= 0 ){
        toast({
          variant: "destructive",
          title: 'Value Error!',
          description: 'Only positive values are allowed',
        });
        return;
      }else if(addTicketCount > 10){
        toast({
          variant: "destructive",
          title: 'Value Error!',
          description: 'Maximum 10 tickets can be added at a time',
        });
        return;
      }

      try {
        const response = await fetch(`http://localhost:8080/api/events/${eventid}/add-tickets`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ ticketCount: addTicketCount , vendorId: "AdminVendor"}),
        });
  
        if (!response.ok) {
          throw new Error(`Network response was not ok: ${response.statusText}`);
        }
  
        const data = await response.text();
        toast({
          variant: "success",
          title: 'Tickets Added!',
          description: 'Tickets added successfully',
        })
        setTicketAddDialog(false);
      } catch (error) {
        console.log('Error starting simulation:', error);
      }
    };

    const handleBuyTickets = async () => {
      if(buyTicketCount <= 0 ){
        toast({
          variant: "destructive",
          title: 'Value Error!',
          description: 'Only positive values are allowed',
        });
        return;
      }else if(buyTicketCount > 10){
        toast({
          variant: "destructive",
          title: 'Value Error!',
          description: 'Maximum 10 tickets can be purchased at a time',
        });
        return;
      }

      try {
        const response = await fetch(`http://localhost:8080/api/events/${eventid}/purchase-tickets-admin`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ ticketCount: buyTicketCount , cusotmerId: "AdminVendor"}),
        });
  
        if (!response.ok) {
          throw new Error(`Network response was not ok: ${response.statusText}`);
        }
  
        const data = await response.text();
        toast({
          variant: "success",
          title: 'Tickets Purchased!',
          description: 'Tickets Purchased successfully',
        })
        setTicketBuyDialog(false);
      } catch (error) {
        console.log('Error starting simulation:', error);
      }
    };

    const handleAddVendors = async () => {
      try {
        const response = await fetch("http://localhost:8080/api/events/add-vendor", {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ eventId: eventid}),
        });
  
        if (!response.ok) {
          throw new Error(`Network response was not ok: ${response.statusText}`);
        }
  
        const data = await response.text();
        toast({
          variant: "success",
          title: 'Manual Vendor Added!',
        })
        setVendors(vendors + 1)
        setSimRunnoing(true);
      } catch (error) {
        console.log('Error starting simulation:', error);
      }
    };

    const handleAddCustomer = async () => {
      try {
        const response = await fetch("http://localhost:8080/api/events/add-customer", {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ eventId: eventid}),
        });
  
        if (!response.ok) {
          throw new Error(`Network response was not ok: ${response.statusText}`);
        }
  
        const data = await response.text();
        toast({
          variant: "success",
          title: 'Manual Customer Added!',
        })
        setCustomers(customers + 1)
        setSimRunnoing(true);
      } catch (error) {
        console.log('Error starting simulation:', error);
      }
    };

  return (
    <div className='w-3/4 bg-or justify-center py-6 rounded-xl'>
      <div className="w-full justify-center flex gap-5">
            {!simRunning?
            <Button className='w-1/6 py-6 rounded-full bg-bdark hover:bg-blight' onClick={handleStartClick}>START</Button>
            : <Button className='w-1/6 py-6 rounded-full bg-bdark hover:bg-blight' onClick={handleStopClick}>STOP</Button>
            }
          <Button className='w-1/6 py-6 rounded-full bg-bdark hover:bg-blight' onClick={handleStopClick} disabled={!simRunning}>PAUSE</Button>
          <Button className='w-1/6 py-6 rounded-full bg-bdark hover:bg-blight' onClick={handleResetClick}>RESET</Button>
      </div>
      <div className="w-full justify-center flex gap-5 mt-5 uppercase">
        <Button className='w-1/6 py-10 rounded-2xl bg-bdark hover:bg-blight uppercase' onClick={handleAddCustomer}>Add <br /> customer</Button>
        <Button className='w-1/6 py-10 rounded-2xl bg-bdark hover:bg-blight uppercase' onClick={handleAddVendors}>Add <br/> vendor</Button>
        <Button className='w-1/6 py-10 rounded-2xl bg-bdark hover:bg-blight uppercase' onClick={handleVipCustomerRequest}>Add VIP <br/> CUSTOMER</Button>
        <Button className='w-1/6 py-10 rounded-2xl bg-bdark hover:bg-blight uppercase' onClick={() => setTicketAddDialog(true)}>MANUAL <br/> ticket addition</Button>
        <Button className='w-1/6 py-10 rounded-2xl bg-bdark hover:bg-blight uppercase' onClick={() => setTicketBuyDialog(true)}>MANUAL <br/> ticket purchase</Button>
      </div>

      <Dialog open={ticketAddDialog} onOpenChange={setTicketAddDialog}>
        <DialogContent>
              <DialogHeader>
              <DialogTitle className="mb-5">Add Tickets</DialogTitle>
              <DialogDescription>

                      <Label htmlFor='maxCapacity'>Number of Tickets</Label>
                      <Input type='number' id='totalTickets'  onChange={(e) => setAddTicketCount(e.target.value)} className="mt-2"/>

                  <Button className="mt-5 bg-or hover:bg-orange" onClick={handleAddTickets}>ADD</Button>
              </DialogDescription>
              </DialogHeader>
          </DialogContent>
      </Dialog>

      <Dialog open={ticketBuyDialog} onOpenChange={setTicketBuyDialog}>
        <DialogContent>
              <DialogHeader>
              <DialogTitle className="mb-5">Buy Tickets</DialogTitle>
              <DialogDescription>

                      <Label htmlFor='maxCapacity'>Number of Tickets</Label>
                      <Input type='number' id='totalTickets'  onChange={(e) => setBuyTicketCount(e.target.value)} className="mt-2"/>

                  <Button className="mt-5 bg-or hover:bg-orange" onClick={handleBuyTickets}>BUY</Button>
              </DialogDescription>
              </DialogHeader>
          </DialogContent>
      </Dialog>




      <div className='flex justify-center'>
        <div className="justify-center text-center flex mt-5 gap-5 w-2/3 border py-3 border-bdark rounded-xl">
            <h3>Total Vendors : {vendors}</h3>
            <h3>|</h3>
            <h3>Total Customers : {customers}</h3>
            <h3>|</h3>
            <h3>Total VIP Customers : {vipCustomers}</h3>
        </div>
      </div>
    </div>
  )
}

export default SimulationPanel
