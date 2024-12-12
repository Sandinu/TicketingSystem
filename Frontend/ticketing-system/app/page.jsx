'use client'
import { useEffect, useState } from 'react';
import DashNav from './dashboard/[eventId]/_components/DashNav';
import { Button } from '@/components/ui/button';
import Event from './vendor-profile/_components/Event';
export default function Home() {
  const [loading, setLoading] = useState(true);
  const [events, setEvents] = useState();

  useEffect(() => {
    fetchEvents();
  },[])

  const fetchEvents = async () => {
      try {
          const url = `${process.env.NEXT_PUBLIC_API_URL}/api/events/all`;

          const response = await fetch(url);

          if (!response.ok) {
              throw new Error(`Network response was not ok: ${response.statusText}`);
          }

          const data = await response.json();
          console.log('Fetched event data:', data);
          setEvents(data);

      } catch (error) {
          //console.error('Error fetching event:', error);
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
    <div className="relative px-10 pt-5 justify-center text-center  overflow-hidden">
      <DashNav/>
      <div className='rounded-full bg-blight blur-[180px] absolute -z-10 left-0 right-0 m-auto -top-20 content h-[700px] w-[700px]'></div>
      <div className="mt-40">
        <h1 className="text-white text-7xl font-bold tracking-wide leading-tight">Experience the Best <br /> Events Near You</h1>
        <h4 className="text-or text-2xl mt-5">Find, Book, and Enjoy – It’s That Simple!</h4>
        <a href="#s2"><Button className="bg-transparent border border-white text-white mt-5 hover:bg-white hover:text-or">EXPLORE THE EVENTS</Button></a>
      </div>
        <img src="/el.svg" className='absolute -left-36 top-40' />
        <img src="/el.svg" className='absolute -right-32 top-[600px] w-[250px]' />
      

      <section id='s2' className=" mt-60 justify-center">
        <h4 className="text-white text-3xl uppercase font-semibold">Our <span className="text-or">Events</span></h4>
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 mt-8">
        {loading ? (
          <p>Loading...</p>
        ) : (
          events.map(event => (
            <Event key={event.eventId} eventId={event.eventId} />
          ))
        )}
      </div>
      </section>
      
    </div>
  );
}
