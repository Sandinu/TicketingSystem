"use client"
import React, { useContext } from 'react'
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import Link from 'next/link';
import { useState, useEffect } from 'react';
import { useToast } from '@/hooks/use-toast';



const page = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const {toast} = useToast();
  const [isVendor, setIsVendor] = useState(false);


  const handleVendor = async () => {
    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/users/register/vendor`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name: name,email: email , password: password}),
        mode: 'cors',
      });

      await new Promise(resolve => setTimeout(resolve, 1000));

      if (!response.ok) {
        toast({
          variant: "destructive",
          title: 'Registration failed',
          description: 'Please try again.',
        });
        return;      
      }
      const data = await response.text();
      console.log('Data:', data);
      toast({
        variant: "success",
        title: 'Registration successful',
      })
      await new Promise(resolve => setTimeout(resolve, 200));
      window.location.href = '/login';
    } catch (error) {
      console.log('Error login:', error);
    }
  };

  const handleCustomer = async () => {
    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/users/register/customer`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name: name,email: email , password: password}),
        mode: 'cors',
      });

      await new Promise(resolve => setTimeout(resolve, 1000));

      if (!response.ok) {
        toast({
          variant: "destructive",
          title: 'Registration failed',
          description: 'Please try again.',
        });
        return;      
      }
      const data = await response.text();
      console.log('Data:', data);
      toast({
        variant: "success",
        title: 'Registration successful',
      })
      await new Promise(resolve => setTimeout(resolve, 200));
      window.location.href = '/login';

    } catch (error) {
      console.log('Error login:', error);
    }
  };

  return (
    <div className='flex flex-row h-screen'>
      <div className="w-2/5 overflow-hidden flex items-center">
        <img src="/event2.jpg" className="h-full object-cover"/>
      </div>
      <div className='w-3/5 p-10 text-center text-white'>
        <img src="/Logo.png" className="w-16 float-end" />
        <h1 className='text-6xl text-or font-bold tracking-wider mt-32'>REGISTER NOW</h1>

        <div className='mt-8'>
            {
                !isVendor?
                <div>
                    <Button className="bg-transparent w-1/4 border border-or hover:bg-or" onClick={()=> setIsVendor(true)}>VENDOR</Button>
                    <Button className="bg-or w-1/4 border border-or hover:bg-or ml-3">CUSTOMER</Button>
                </div>
                :
                <div>
                    <Button className="bg-or w-1/4 border border-or hover:bg-or">VENDOR</Button>
                    <Button className="bg-transparent w-1/4 border border-or hover:bg-or ml-3" onClick={()=> setIsVendor(false)}>CUSTOMER</Button>
                </div>
            }
        </div>

        <div className='mt-10 w-full flex flex-col items-center'>
          <Input className='bg-blight border-none h-14 w-3/4 rounded-full text-center mb-6' placeholder='Email' onChange={(e) => setEmail(e.target.value)}></Input>

          <Input className='bg-blight border-none h-14 w-3/4 rounded-full text-center mb-6' placeholder='Full Name' onChange={(e) => setName(e.target.value)}></Input>

          <Input className='bg-blight border-none h-14 w-3/4 rounded-full text-center' placeholder='Password' type="password" onChange={(e) => setPassword(e.target.value)}></Input>

            {
                !isVendor?
                <Button className='mt-12 w-2/4 h-12 rounded-full bg-transparent border border-or hover:bg-or' onClick={handleCustomer} disabled={email == '' || password == '' || name == ''}>REGISTER AS A CUSTOMER</Button>
                :
                <Button className='mt-12 w-2/4 h-12 rounded-full bg-transparent border border-or hover:bg-or' onClick={handleVendor}  disabled={email == '' || password == '' || name == ''}>REGISTER AS A VENDOR</Button>

            }
        </div>

        <div className='text-center mt-20 tracking-wider text-sm'>
          <h5>Already registered? 
          <Link href="/login" className='text-or hover:text-white'>  Login</Link></h5>
        </div>
      </div>

    </div>
  )
}

export default page
