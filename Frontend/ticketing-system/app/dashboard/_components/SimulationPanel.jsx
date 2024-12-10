'use client'
import React from 'react'
import { Button } from '@/components/ui/button';
import { useState } from 'react';



const SimulationPanel = ({eventid}) => {
    const vendors = 50;
    const customers = 50;
    const [vipCustomers, setVipCustomers] = useState(0);
    const [simRunning, setSimRunnoing] = useState(false);

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
        <Button className='w-1/6 py-10 rounded-2xl bg-bdark hover:bg-blight uppercase'>Add <br /> customer</Button>
        <Button className='w-1/6 py-10 rounded-2xl bg-bdark hover:bg-blight uppercase'>Add <br/> vendor</Button>
        <Button className='w-1/6 py-10 rounded-2xl bg-bdark hover:bg-blight uppercase' onClick={handleVipCustomerRequest}>Add VIP <br/> CUSTOMER</Button>
        <Button className='w-1/6 py-10 rounded-2xl bg-bdark hover:bg-blight uppercase'>MANUAL <br/> ticket purchase</Button>
        <Button className='w-1/6 py-10 rounded-2xl bg-bdark hover:bg-blight uppercase'>MANUAL <br/> ticket addition</Button>
      </div>
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
