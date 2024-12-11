'use client'
import React, { useEffect, useState } from 'react'
import DashNav from './../../dashboard/[eventId]/_components/DashNav';
import EventDeets from './../../dashboard/[eventId]/_components/EventDeets';
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Button } from '@/components/ui/button';

const page = ({params}) => {
  const [event, setEvent] = useState(true);
  const [loading, setLoading] = useState(true);
  const [buyTicketDialog, setBuyTicketDialog] = useState(false);



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
        <div className="absolute bottom-5 z-10 right-0 left-0 mx-auto"><Button className='w-80 py-2 bg-or rounded-full hover:bg-white hover:text-or' onClick={()=>{setBuyTicketDialog(true)}}>BUY TICKETS</Button></div>
      </div>
      <div className="w-3/4 mt-8 justify-center text-center mx-auto ">
        <EventDeets event={event}/>
      </div>
      <Dialog open={buyTicketDialog} onOpenChange={setBuyTicketDialog}>
      <DialogContent>
          <DialogHeader>
          <DialogTitle className="mb-5">Edit Event Details</DialogTitle>
          <DialogDescription>

                  <Label htmlFor='location'>Event Location</Label>
                  <Input type='text' id='location' value={"eventLocation"}  onChange={(e) => setEventLocation(e.target.value)} className="mt-2 mb-3"/>

              <Button className="mt-5 bg-or hover:bg-orange" >EDIT DETAILS</Button>
          </DialogDescription>
          </DialogHeader>
          </DialogContent>
      </Dialog>

    </div>
  )
}

export default page
