'use client'
import React, { useContext, useEffect, useState } from 'react'
import DashNav from './../../dashboard/[eventId]/_components/DashNav';
import EventDeets from './../../dashboard/[eventId]/_components/EventDeets';
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Button } from '@/components/ui/button';
import { useToast } from '@/hooks/use-toast';
import { UserContext } from '@/UserContext';

const page = ({params}) => {
  const [event, setEvent] = useState(true);
  const [loading, setLoading] = useState(true);
  const [buyTicketDialog, setBuyTicketDialog] = useState(false);
  const {toast} = useToast();
  const {user} = useContext(UserContext);
  const [ticketsToBuy, setTicketToBuy] = useState(0);



  useEffect(() => {
    const fetchParams = async () => {
        const unwrappedParams = await params;
        const eventId = unwrappedParams.eventId;
        if (eventId) {
            fetchEvent(`${eventId}`);
        }
    };
    fetchParams();
}, [params]);

  const fetchEvent = async (eventId) => {
      try {
          const url = `${process.env.NEXT_PUBLIC_API_URL}/api/events/${eventId}`;
          console.log(`Fetching event from URL: ${url}`);
          const response = await fetch(url);
          if (!response.ok) {
              throw new Error(`Network response was not ok: ${response.statusText}`);
          }
          const data = await response.json();
          console.log('Fetched event data:', data);
          setEvent(data);

      } catch (error) {
          console.error('Error fetching event:', error);
      } finally {
          setLoading(false);
      }
  };


  const handleBuyTickets = async () => {
    if(ticketsToBuy <= 0 ){
      toast({
        variant: "destructive",
        title: 'Value Error!',
        description: 'Only positive values are allowed',
      });
      return;
    }else if(ticketsToBuy > 10){
      toast({
        variant: "destructive",
        title: 'Value Error!',
        description: 'Maximum 10 tickets can be purchased at a time',
      });
      return;
    }

    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/events/${event.eventId}/purchase-tickets-admin`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ ticketCount: ticketsToBuy , cusotmerId: "AdminVendor"},),
        //mode: 'no-cors',
      });

      if (!response.ok) {
        throw new Error(`Network response was not ok: ${response.statusText}`);
        toast({
          variant: "destructive",
          title: 'Error with ticket purchase!',
          description: 'Try again later',
        });
      }

      const data = await response.text();
      toast({
        variant: "success",
        title: 'Tickets Purchased!',
        description: 'Tickets Purchased successfully',
      })
    } catch (error) {
      console.log('Error starting simulation:', error);
      toast({
        variant: "destructive",
        title: 'Error with ticket purchase!',
        description: 'Try again later',
      });
    }
  };



  if (loading) {
    return <div>
        <img src="/loader.gif" className="fixed left-0 right-0 bottom-0 top-0 m-auto w-32"/>
    </div>
}


  return (
    <div className=" pt-5">
      <div className="px-10">
        <DashNav />
      </div>
      <div className='h-[450px] w-full overflow-hidden relative justify-center items-center text-center'>
        <div ><img src={event.eventImageUrl} className="opacity-90 w-full blur-md z-5"/></div>
        <img src={event.eventImageUrl} className="z-10 absolute top-0 h-[450px] right-0 left-0 m-auto border"/>
        <div className="absolute bottom-5 z-10 right-0 left-0 mx-auto"><Button className='w-80 py-2 bg-white text-or rounded-full hover:bg-or hover:text-bdark' onClick={()=>{setBuyTicketDialog(true)}}>BUY TICKETS</Button></div>
      </div>
      <div className="text-center mt-8 text-white bg-blight py-5 rounded-xl mx-32 mb-8">
        <h2 className='text-4xl uppercase font-bold tracking-wider mb-4'>{event.eventName}</h2>
        <hr/>
        <p className="mt-5 mb-5">{event.eventDescription}</p>
        <div className="justify-center items-center flex flex-row">
            <h4 className="w-1/5 bg-or text-bdark py-2 rounded-full mx-4 text-lg">{event.eventDate}</h4>
            <h4 className="w-1/5 bg-or text-bdark py-2 rounded-full mx-4 text-lg">{event.eventStartTime}</h4>
            <h4 className="w-1/5 bg-or text-bdark py-2 rounded-full mx-4 text-lg">{event.eventLocation}</h4>
        </div>
      </div>


      <Dialog open={buyTicketDialog} onOpenChange={setBuyTicketDialog}>
      <DialogContent>
          <DialogHeader>
          <DialogTitle className="mb-5">Edit Event Details</DialogTitle>
          <DialogDescription>

                  <Label htmlFor='tickets'>Enter the number of tickets to buy:</Label>
                  <Input type='number' id='tickets' value={ticketsToBuy}  onChange={(e) => setTicketToBuy(e.target.value)} className="mt-2 mb-3"/>

              <Button className="mt-5 bg-or hover:bg-orange" onClick={handleBuyTickets} disabled={ticketsToBuy <= 0 || ticketsToBuy == null}>BUY TICKETS</Button>
          </DialogDescription>
          </DialogHeader>
          </DialogContent>
      </Dialog>

    </div>
  )
}

export default page;
