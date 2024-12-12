'use client'
import React, { useContext, useState } from 'react'
import {CldUploadWidget} from 'next-cloudinary'
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Textarea } from '@/components/ui/textarea';
import { DatePickerD } from './_components/DatePicker';
import { Button } from '@/components/ui/button';
import { UserContext } from '@/UserContext';
import { useToast } from '@/hooks/use-toast';
import DashNav from './../dashboard/[eventId]/_components/DashNav';



const page = () => {
    const {user} = useContext(UserContext);
    const {toast} = useToast();
    const [resource, setResource] = useState(null);
    const [eventName, setEventName] = useState('');
    const [eventDate, setEventDate] = useState('');
    const [eventImageUrl, setEventImageUrl] = useState('');
    const [eventDescription, setEventDescription] = useState('');
    const [maxCapacity, setMaxCapacity] = useState('');
    const [totalTickets, setTotalTickets] = useState('');
    const [customerRetrievalRate, setCustomerRetrievalRate] = useState('');
    const [ticketReleaseRate, setTicketReleaseRate] = useState('');
    const [eventStartTime, setEventStartTime] = useState('');
    const [eventLocation, setEventLocation] = useState('');      
    const [loading, setLoading] = useState(false);
       
    

    const handleDateChange = (newDate) => {
        setEventDate(newDate);
      };

      const handleCreateEvent = async () => {
        if(user.roles != "ROLE_VENDOR"){
          toast({
            variant: "destructive",
            title: 'Unauthorized Access',
            description: 'Only vendors can create events.',
          });
          return;
        }

        if(!eventName ||!eventDate ||!maxCapacity ||!totalTickets ||!customerRetrievalRate ||!ticketReleaseRate ||!eventStartTime ||!eventLocation ){
            toast({
              variant: "destructive",
              title: 'Missing Fields',
              description: 'Please fill in all the fields',
            });
            return;
        }

        if(maxCapacity <= 0 || totalTickets <= 0 || customerRetrievalRate <= 0 ||  ticketReleaseRate <= 0){
          toast({
            variant: "destructive",
            title: 'Value Error!',
            description: 'Only positive values are allowed',
          });
          return;
        }

        if(eventDate < new Date()){
          toast({
            variant: "destructive",
            title: 'Invalid Date',
            description: 'Event date cannot be in the past.',
          });
          return;
        }


        try {
          const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/events/create`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({ 
                name: eventName , 
                description: eventDescription,
                maxCapacity: maxCapacity,
                totalTickets: totalTickets,
                customerRetrievalRate: customerRetrievalRate,
                ticketReleaseRate: ticketReleaseRate,
                eventDate: eventDate,
                eventStartTime: eventStartTime,
                eventLocation: eventLocation,
                eventImageUrl: eventImageUrl,
                vendorId: user.id,
            }),
            mode: 'cors',
          });
    
          await new Promise(resolve => setTimeout(resolve, 1000));
    
          if (!response.ok) {
            toast({
              variant: "destructive",
              title: 'Event Creation failed',
              description: 'Please try again.',
            });
            return;      
          }
          const data = await response.text();
          console.log('Event creation response:', data);
          toast({
            variant: "success",
            title: 'Event creation successful',
          })
          window.location.href = `/vendor-profile`;
        } catch (error) {
          console.log('Error login:', error);
        }
      };

      
      if(user.roles == "ROLE_CUSTOMER"){
        return <div className='text-center justify-center pt-10'>
            <h1 className="text-white text-6xl font-semibold text-center mt-10 mb-5">Unauthorized Access</h1>
            <Button className="bg-or rounded-full text-white hover:bg-white hover:text-or">BACK TO HOME PAGE</Button>
        </div>
    }

    if (loading) {
      return <div>
          <img src="/loader.gif" className="fixed left-0 right-0 bottom-0 top-0 m-auto w-32"/>
      </div>
  }

    
  return (
    <div className='px-10 pt-5'><DashNav/>
    <div className="text-center p-10 flex flex-col items-center justify-center">
        
        <div>
            <h1 className="text-white text-4xl font-bold">CREATE EVENT</h1>
        </div>
        <div className="w-full flex flex-col items-center justify-center mt-6">
                <div className="w-2/5 flex flex-col items-center">
                    <Label htmlFor='eventName' className="text-gray-300 mb-3">Event Name*</Label>
                    <Input className='bg-blight  text-white border-none h-12 w-full rounded-full text-center mb-8' id="eventName" placeholder='Event Name' onChange={(e) => setEventName(e.target.value)} required={true}></Input>
                    
                    <Label htmlFor='eventDescription' className="text-gray-300 mb-3">Event Description</Label>
                    <Textarea className='bg-blight text-white border-none h-12 w-full rounded-lg text-center mb-8' id="eventDescription" placeholder='Event Description' onChange={(e) => setEventDescription(e.target.value)}></Textarea>
                </div>

                <div className="w-2/5 flex flex-col items-center">
                    <Label htmlFor='maxCapacity' className="text-gray-300 mb-3">Max Capacity*</Label>
                    <Input className='bg-blight text-white border-none h-12 w-full rounded-full text-center mb-8' type='number' id="maxCapacity" placeholder='Max Capacity' onChange={(e) => setMaxCapacity(e.target.value)} required></Input>
                </div>

                <div className="w-2/5 flex flex-col items-center">
                    <Label htmlFor='maxCapacity' className="text-gray-300 mb-3">Total Tickets*</Label>
                    <Input className='bg-blight text-white border-none h-12 w-full rounded-full text-center mb-8' type='number' id="maxCapacity" placeholder='Total Tickets' onChange={(e) => setTotalTickets(e.target.value)} required></Input>
                </div>

                <div className="w-2/5 flex flex-col items-center">
                    <Label htmlFor='maxCapacity' className="text-gray-300 mb-3">Customer Retrieval Rate*</Label>
                    <Input className='bg-blight text-white border-none h-12 w-full rounded-full text-center mb-8' type='number' id="maxCapacity" placeholder='Customer Retrieval Rate' onChange={(e) => setCustomerRetrievalRate(e.target.value)} required></Input>
                </div>

                <div className="w-2/5 flex flex-col items-center">
                    <Label htmlFor='maxCapacity' className="text-gray-300 mb-3">Ticket Release Rate*</Label>
                    <Input className='bg-blight text-white border-none h-12 w-full rounded-full text-center mb-8' type='number' id="maxCapacity" placeholder='Ticket Release Rate' onChange={(e) => setTicketReleaseRate(e.target.value)} required></Input>
                </div>

                <div className="w-2/5 flex flex-col items-center">
                    <Label htmlFor='maxCapacity' className="text-gray-300 mb-3">Event Date*</Label>
                    <DatePickerD eventDate={null} onDateChange={handleDateChange} className="mt-2 mb-3"/> <br/>
                </div>

                <div className="w-2/5 flex flex-col items-center">
                    <Label htmlFor='maxCapacity' className="text-gray-300 mb-3">Event Start Time* (HH:MM:SS)</Label>
                    <Input className='bg-blight text-white border-none h-12 w-full rounded-full text-center mb-8' id="maxCapacity" placeholder='Event Start Time (HH:MM:SS)' onChange={(e) => setEventStartTime(e.target.value)} required></Input>
                </div>

                <div className="w-2/5 flex flex-col items-center">
                    <Label htmlFor='maxCapacity' className="text-gray-300 mb-3">Event Location*</Label>
                    <Input className='bg-blight text-white border-none h-12 w-full rounded-full text-center mb-8' id="maxCapacity" placeholder='Event Location' onChange={(e) => setEventLocation(e.target.value)} required></Input>
                </div>

                <div className="w-2/5 flex flex-col items-center">
 
                    <CldUploadWidget uploadPreset="OOP_CW"   onSuccess={(results) => {setEventImageUrl(results.info.secure_url)}} cropping={true} croppingAspectRatio={1} options={{cropping:true, croppingAspectRatio:1, }}>
                    {({ open }) => {
                        return (
                        <Button onClick={() => open()} className="w-full rounded-full h-12 bg-white text-blight hover:bg-or hover:text-white">UPLOAD AN EVENT FLYER</Button>
                        );
                    }}
                    </CldUploadWidget>
                </div>
                <div className='w-2/5 mt-8'>
                    <Button onClick={handleCreateEvent} className="w-full rounded-full h-12 bg-or text-white hover:bg-white hover:text-or">ADD EVENT</Button>
                </div>
            </div>
    </div></div>
  )
}

export default page