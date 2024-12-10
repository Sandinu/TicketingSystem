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
import useEventWebSocket from './../../hooks/EventDeetsSocket';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faDownload } from '@fortawesome/free-solid-svg-icons'

const Dashboard = () => {
    const [event, setEvent] = useState(true);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [runSimPanel, setRunSimPanel] = useState(false);

    const [totalTicketsAdded, setTotalTicketsAdded] = useState(0);
    const [totalTicketsSold, setTotalTicketsSold] = useState(0);

    const [chartData, setChartData] = useState([]);


    const fetchEvent = async (eventId) => {
        try {
            const url = `http://localhost:8080/api/events/${eventId}`;
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


    useEffect(() => {
        const eventId = '6754214e77d2145538124927'; // Replace with actual event ID
        fetchEvent(eventId);
    }, []);

    const {messages, connectionStatus} = useEventWebSocket('ws://localhost:8080/ws/event-stats')

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

    useEffect(() => {
        const eventId = '6754214e77d2145538124927'; // Replace with actual event ID
        fetchEvent(eventId);
    }, []);

    //gets the csv format from the backend and create the file and download it
    const handleLogDownload = async () => {
        try {
          const response = await fetch('http://localhost:8080/api/events/ticket-logs', {
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
                <div className='w-1/4'><EventPost/></div>
                <div className='w-3/4 flex-col gap-5'>
                    <div className='h-3/4'>
                        <EventDeets event={event}/>
                    </div>
                    <div className='h-1/4 align-middle w-full justify-center flex gap-6 mt-3'>
                        <Button className='w-1/4 h-3/5 border-or bg-transparent border text-or rounded-full text-md uppercase hover:bg-or hover:text-white'>Edit Event</Button>
                        <Button className='w-1/4 h-3/5  border-or bg-transparent border text-or rounded-full text-md uppercase hover:bg-or hover:text-white'>Add vendors</Button>
                        <Button className='w-1/4 h-3/5  border-or bg-transparent border text-or rounded-full text-md uppercase hover:bg-or hover:text-white'>Add tickets</Button>
                        <Button onClick={() => setRunSimPanel(!runSimPanel)} className='w-1/4 h-3/5  border-or bg-transparent border text-or rounded-full text-md uppercase hover:bg-or hover:text-white'>Run simulation</Button>
                    </div>
                </div>
            </div>
            {runSimPanel && (
                <div className='w-full justify-center flex transition-all ease-in-out duration-500 mt-5'>
                    <SimulationPanel eventid={event.eventId}/>
                </div>
            )}
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
                                <h5 className="text-white text-2xl border rounded-xl px-4 w-full py-2">Max Capacity : <span className="text-or font-semibold">{event.maxCapacity}</span></h5>
                                <h5 className="text-white text-2xl border rounded-xl px-4  w-full py-2">Total Tickets : <span className="text-or font-semibold">{event.totalTickets}</span></h5>
                                <h5 className="text-white text-2xl border rounded-xl px-4 w-full py-2">Ticket Release Rate : <span className="text-or font-semibold">{event.ticketReleaseRate}</span></h5>
                                <h5 className="text-white text-2xl border rounded-xl px-4 w-full py-2">Customer Retrieval Rate : <span className="text-or font-semibold">{event.customerRetrievalRate}</span></h5>
                                <Button className='bg-white text-bdark hover:bg-or'>EDIT CONFIGURATION</Button>
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
        </div>
    );
};

export default Dashboard;