'use client'
import React, { useState, useEffect } from 'react';
import DashNav from './_components/DashNav';
import EventPost from './_components/EventPost';
import EventDeets from './_components/EventDeets';
import { Button } from '@/components/ui/button';
import SimulationPanel from './_components/SimulationPanel';
import { Chart } from './_components/Chart';
import TicketLog from './_components/Ticketlog';
import VendorList from './_components/VendorList';
import useEventWebSocket from '../../../hooks/EventDeetsSocket';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faDownload } from '@fortawesome/free-solid-svg-icons'
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { useToast } from '@/hooks/use-toast';
import { Textarea } from '@/components/ui/textarea';
import { DatePickerDemo } from './_components/DatePicker';
import { useParams, usePathname, useRouter } from 'next/navigation';


const Dashboard = ({params}) => {

    const [event, setEvent] = useState(true);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [runSimPanel, setRunSimPanel] = useState(false);

    const [totalTicketsAdded, setTotalTicketsAdded] = useState(0);
    const [totalTicketsSold, setTotalTicketsSold] = useState(0);

    const [chartData, setChartData] = useState([]);

    const [totalTickets, setTotalTickets] = useState(event.totalTickets);
    const [ticketReleaseRate, setTicketReleaseRate] = useState(event.ticketReleaseRate);
    const [customerRetrievalRate, setCustomerRetrievalRate] = useState(event.customerRetrievalRate);

    const [ticketAddDialog, setTicketAddDialog] = useState(false);
    const [eventDetailsDialog, setEventDetailsDialog] = useState(false);

    const [eventDesc, setEventDesc] = useState(event.eventDesc);
    const [eventStartTime, setEventStartTime] = useState(event.eventStartTime);
    const [eventLocation, setEventLocation] = useState(event.eventLocation);
    const [eventDate, setEventDate] = useState(event.eventDate);

    const [addTicketCount, setAddTicketCount] = useState(0);

    const {toast} = useToast();

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
            setTotalTicketsAdded(data.totalTicketsAdded);
            setTotalTicketsSold(data.totalTicketsSold);
            setTotalTickets(data.totalTickets);
            setTicketReleaseRate(data.ticketReleaseRate);
            setCustomerRetrievalRate(data.customerRetrievalRate);
            setEventDesc(data.eventDescription);
            setEventStartTime(data.eventStartTime);
            setEventLocation(data.eventLocation);
            setEventDate(data.eventDate);

            const initialChartData = data.ticketLogs.map(log => ({
                totalTicketsAdded: log.totalTicketsAdded,
                totalTicketsSold: log.totalTicketsSold,
              }));
              setChartData(initialChartData);
        } catch (error) {
            console.error('Error fetching event:', error);
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };


    // useEffect(() => {
    //     const eventId = '6754214e77d2145538124927'; // Replace with actual event ID
    //     fetchEvent(eventId);
    // }, []);

    const {messages, connectionStatus} = useEventWebSocket('wss://ticketing-system-sp-c52b1ee3dbb7.herokuapp.com/ws/event-stats')

    useEffect(() => {
        if (messages.length > 0) {
          const { eventId, totalTicketsAdded, totalTicketsSold } = JSON.parse(messages[0]);
          setTotalTicketsAdded(totalTicketsAdded);
          setTotalTicketsSold(totalTicketsSold);

          setChartData((prevChartData) => [
            ...prevChartData,
            { totalTicketsAdded, totalTicketsSold }
          ]);
        }
      }, [messages]);

    // const event = {
    //     "eventId": "6754214e77d2145538124927",
    //     "eventName": "Holi",
    //     "eventDescription": "Holi",
    //     "maxCapacity": 100,
    //     "totalTickets": 1000,
    //     "totalTicketsSold": 325,
    //     "totalTicketsAdded": 384,
    //     "customerRetrievalRate": 5,
    //     "ticketReleaseRate": 3,
    //     "eventDate": "2024-12-12",
    //     "eventStartTime": "13:40:23",
    //     "eventLocation": "Test"}

    // useEffect(() => {
    //     const eventId = '6754214e77d2145538124927'; // Replace with actual event ID
    //     fetchEvent(eventId);
    // }, []);

    //gets the csv format from the backend and create the file and download it
    const handleLogDownload = async () => {
        try {
          const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/events/ticket-logs`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({ eventId: event.eventId }),
          });
    
          if (!response.ok) {
            throw new Error(`Network response was not ok: ${response.statusText}`);
          }
    
          const blob = await response.blob();
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = `${event.eventName}-ticket-logs.csv`;
          document.body.appendChild(a);
          a.click();
          a.remove();
          window.URL.revokeObjectURL(url);
        } catch (error) {
        console.log('Error downloading logs:', error);
        }
      };

      //updates the event configurations
      const handleSubmit = async () => {
        if(ticketReleaseRate<0 || customerRetrievalRate<0 || totalTickets<0){
            toast({
                variant: "destructive",
                title: 'Value Error!',
                description: 'Only positive values are allowed',
              })
            return;
        }

        try {
          const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/events/config-update`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({ 
                eventId: event.eventId,
                ticketReleaseRate: ticketReleaseRate,
                customerRetrievalRate: customerRetrievalRate,
                totalTickets: totalTickets
             }),
          });
    
          if (!response.ok) {
            throw new Error(`Network response was not ok: ${response.statusText}`);
          }
    
          const updatedEvent = await response.json();
            setEvent(updatedEvent);
            console.log('Event updated:', updatedEvent);
            window.location.reload();
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
          const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/events/${event.eventId}/add-tickets`, {
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

      const handleDateChange = (newDate) => {
        setEventDate(newDate);
      };

      const handleEventDeets = async () => {
        try {
          const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/events/event-update`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({ 
                eventId: event.eventId,
                eventDesc: eventDesc,
                eventDate: eventDate,
                eventStartTime: eventStartTime,
                eventLocation: eventLocation
             }),
          });
    
          if (!response.ok) {
            throw new Error(`Network response was not ok: ${response.statusText}`);
          }
    
          const updatedEvent = await response.json();
            setEvent(updatedEvent);
            toast({
                variant: "success",
                title: 'Event Updated!',
            })
            console.log('Event updated:', updatedEvent);
            window.location.reload();
        } catch (error) {
          console.log('Error starting simulation:', error);
        }
      };


    if (loading) {
        return <div>
            <img src="/loader.gif" className="fixed left-0 right-0 bottom-0 top-0 m-auto w-32"/>
        </div>
    }

    if (error) {
        return <div>Error: {error}</div>;
    }

    return (
        <div className='px-10 py-3'>
            <DashNav/>
            <div className='flex gap-8 flex-row mt-4'>
                <div className='w-1/4'><EventPost imgsrc={event.eventImageUrl}/></div>
                <div className='w-3/4 flex-col gap-5'>
                    <div className='h-3/4'>
                        <EventDeets event={event}/>
                    </div>
                    <div className='h-1/4 align-middle w-full justify-center flex gap-6 mt-3'>
                        <Button className='w-1/4 h-3/5 border-or bg-transparent border text-or rounded-full text-md uppercase hover:bg-or hover:text-white' onClick={() => setEventDetailsDialog(true)}>Edit Event</Button>
                        <Button className='w-1/4 h-3/5  border-or bg-transparent border text-or rounded-full text-md uppercase hover:bg-or hover:text-white' disabled>Add vendors</Button>
                        <Button className='w-1/4 h-3/5  border-or bg-transparent border text-or rounded-full text-md uppercase hover:bg-or hover:text-white' onClick={() => setTicketAddDialog(true)}>Add tickets</Button>
                        <Button onClick={() => setRunSimPanel(!runSimPanel)} className='w-1/4 h-3/5  border-or bg-transparent border text-or rounded-full text-md uppercase hover:bg-or hover:text-white'>Run simulation</Button>
                    </div>
                </div>
            </div>
            {runSimPanel && (
                <div className='w-full justify-center flex transition-all ease-in-out duration-500 mt-5'>
                    <SimulationPanel eventid={event.eventId}/>
                </div>
            )}

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

            <div className='flex gap-8 flex-row mt-5 align-middle'>
                <div className='w-2/6 flex flex-col gap-5 align-middle justify-between'>
                    <div className='bg-blight text-center align-middle py-5 rounded-xl h-full'>
                        <h1 className='text-white text-5xl tracking-wider font-bold mb-2'>{event.totalTickets}</h1>
                        <h5 className="text-or">TOTAL TICKETS</h5>
                    </div>
                    <div className='bg-blight text-center align-middle py-5 rounded-xl h-full'>
                        <h1 className='text-white text-5xl tracking-wider font-bold mb-2'>{totalTicketsAdded}</h1>
                        <h5 className="text-or">TOTAL TICKETS ADDED</h5>
                    </div>
                    <div className='bg-blight text-center align-middle py-5 rounded-xl h-full'>
                        <h1 className='text-white text-5xl tracking-wider font-bold mb-2'>{totalTicketsSold}</h1>
                        <h5 className="text-or">TOTAL TICKETS SOLD</h5>
                    </div>
                </div>
                <div className='w-2/6'>
                    <Chart eventname={event.eventName} chartData={chartData}/>
                </div>
                <div className='w-2/6 flex flex-col gap-5'>
                    <div className='bg-blight text-center align-middle py-5 rounded-xl h-full flex-col flex'>
                        <h1 className="text-white text-xl font-semibold mt-1">EVENT CONFIGURATIONS</h1>
                        <div className="text-center mt-3 flex flex-row justify-center">
                            <div className='flex-col gap-5 flex justify-between'>
                                <h5 className="text-white text-xl border rounded-xl px-4 w-full py-2">Max Capacity : <span className="text-or font-semibold">{event.maxCapacity}</span></h5>
                                <h5 className="text-white text-xl border rounded-xl px-4  w-full py-2">Total Tickets : <span className="text-or font-semibold">{event.totalTickets}</span></h5>
                                <h5 className="text-white text-xl border rounded-xl px-4 w-full py-2">Ticket Release Rate : <span className="text-or font-semibold">{event.ticketReleaseRate}</span></h5>
                                <h5 className="text-white text-xl border rounded-xl px-4 w-full py-2">Customer Retrieval Rate : <span className="text-or font-semibold">{event.customerRetrievalRate}</span></h5>
                                <Dialog>
                                    <DialogTrigger className='bg-white text-bdark hover:bg-or py-3 rounded-full font-medium tracking-wide'>EDIT CONFIGURATION</DialogTrigger>
                                    <DialogContent>
                                        <DialogHeader>
                                        <DialogTitle className="mb-4">Edit Event Configurations</DialogTitle>
                                        <DialogDescription>

                                                <Label htmlFor='maxCapacity'>Total Tickets</Label>
                                                <Input type='number' id='totalTickets' value={totalTickets} onChange={(e) => setTotalTickets(e.target.value)} className="mt-2 mb-3"/>

                                                <Label htmlFor='maxCapacity'>Ticket Release Rate</Label>
                                                <Input type='number' id='ticketRate' value={ticketReleaseRate} onChange={(e) => setTicketReleaseRate(e.target.value)} className="mt-2"/>

                                                <Label htmlFor='maxCapacity'>Customer Retrieval Rate</Label>
                                                <Input type='number' id='customerRate' value={customerRetrievalRate} onChange={(e) => setCustomerRetrievalRate(e.target.value)} className="mt-2"/>

                                            <Button className="mt-5 bg-or hover:bg-orange" onClick={handleSubmit}>SUBMIT</Button>
                                        </DialogDescription>
                                        </DialogHeader>
                                    </DialogContent>
                                </Dialog>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div className='flex flex-row mt-5 align-middle gap-8'>
                <div className='w-3/4'>
                    <TicketLog/>
                </div>
                <div className='w-1/4 flex flex-col gap-5'>
                    <div className='h-3/6'>
                        <VendorList/>
                    </div>
                    <div className='justify-center align-middle h-1/6'>
                        <Button className='w-full h-full bg-or  hover:bg-white hover:text-or' onClick={handleLogDownload}><FontAwesomeIcon icon={faDownload}/> TICKET LOGS</Button>
                    </div>
                </div>
            </div>

            <Dialog open={eventDetailsDialog} onOpenChange={setEventDetailsDialog}>
                <DialogContent>
                    <DialogHeader>
                    <DialogTitle className="mb-5">Edit Event Details</DialogTitle>
                    <DialogDescription>

                            <Label htmlFor='location'>Event Location</Label>
                            <Input type='text' id='location' value={eventLocation}  onChange={(e) => setEventLocation(e.target.value)} className="mt-2 mb-3"/>

                            <Label htmlFor='starttime'>Event Start Time (HH:MM:SS)</Label>
                            <Input type='text' id='starttime' value={eventStartTime}  onChange={(e) => setEventStartTime(e.target.value)} className="mt-2 mb-3"/>

                            <Label htmlFor='starttime'>Event Start Time (HH:MM:SS)</Label>
                            <DatePickerDemo eventDate={eventDate} onDateChange={handleDateChange} className="mt-2 mb-3"/> <br/>

                            <Label htmlFor='eventDesc'>Event Description</Label>
                            <Textarea type='text' id='eventDesc' value={eventDesc}  onChange={(e) => setEventDesc(e.target.value)} />

                        <Button className="mt-5 bg-or hover:bg-orange" onClick={handleEventDeets}>EDIT DETAILS</Button>
                    </DialogDescription>
                    </DialogHeader>
                </DialogContent>
            </Dialog>

        </div>
    );
};

export default Dashboard;