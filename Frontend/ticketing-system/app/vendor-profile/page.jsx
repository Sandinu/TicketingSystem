'use client'
import React, { useContext, useEffect, useState } from 'react'
import DashNav from '../dashboard/[eventId]/_components/DashNav'
import { UserContext } from '@/UserContext'
import Event from './_components/Event';
import { Button } from '@/components/ui/button';

const page = () => {
    const {user} = useContext(UserContext);
    const [loading, setLoading] = useState(true);
    const [userDetails, setUserDetails] = useState();

    useEffect(() => {
        fetchUser();
    },[])

    const fetchUser = async () => {
        try {
            const url = `${process.env.NEXT_PUBLIC_API_URL}/api/vendors/${user.id}`;

            const response = await fetch(url);

            if (!response.ok) {
                throw new Error(`Network response was not ok: ${response.statusText}`);
            }

            const data = await response.json();
            console.log('Fetched event data:', data);
            setUserDetails(data);

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

    if(user.roles == "ROLE_CUSTOMER"){
        return <div className='text-center justify-center pt-10'>
            <h1 className="text-white text-6xl font-semibold text-center mt-10 mb-5">Unauthorized Access</h1>
            <Button className="bg-or rounded-full text-white hover:bg-white hover:text-or">BACK TO HOME PAGE</Button>
        </div>
    }


  return (
    <div className='px-10 pt-5'>
      <DashNav/>
      <div className="text-center">
        <h1 className="text-white text-6xl font-semibold">{user.username}</h1>
        <h3 className="text-white text-xl mt-2">{user.email}</h3>
      </div>
      <h4 className="mt-10 text-blue-800 text-center">VENDOR EVENTS:</h4>
      <div className="grid grid-cols-4 gap-6 mt-5">
                {userDetails?.associatedEvents.map(eventId => (
                    <Event key={eventId} eventId={eventId} />
                ))}
      </div>
    </div>
  )
}

export default page
