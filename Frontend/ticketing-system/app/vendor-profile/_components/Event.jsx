'use client'
import Link from 'next/link';
import React, { useEffect, useState } from 'react'

const Event = ({eventId}) => {
    const [eventData, setEventData] = useState();
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if(eventId){
            fetchEvent(eventId);
        }
    },[])

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
            setEventData(data);
    
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
    <Link href={`/dashboard/${eventId}`}>
        <div>
          <div className='w-full m-3 pb-6 bg-blight rounded-xl overflow-hidden text-center h-min-[350px] h-max-[350px]'>
            {
                eventData.eventImageUrl ? <img src={eventData.eventImageUrl} className="object-cover h-[250px] overflow-y-hidden w-full" /> : <img src='/event.jpg' className="object-cover h-[250px] overflow-y-hidden w-full" />
            }
            <h3 className="text-2xl text-white font-semibold mt-5 uppercase">{eventData.eventName}</h3>
            <h5 className='text-md font-medium text-or'>{eventData.eventDate}</h5>
          </div>
        </div>
    </Link>
  )
}

export default Event
