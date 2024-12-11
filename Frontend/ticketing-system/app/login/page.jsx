"use client"
import React, { useContext } from 'react'
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import Link from 'next/link';
import { useState, useEffect } from 'react';
import { useToast } from '@/hooks/use-toast';
import { UserContext } from '@/UserContext';



const page = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const {toast} = useToast();
  const {user,setUser} = useContext(UserContext)


  const handleLogin = async () => {
    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/users/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email: email , password: password}),
        mode: 'cors',
      });

      await new Promise(resolve => setTimeout(resolve, 1000));

      if (!response.ok) {
        toast({
          variant: "destructive",
          title: 'Login failed',
          description: 'Please check your email and password and try again.',
        });
        return;      
      }
      const data = await response.text();
      console.log('Data:', data);
      setUser(JSON.parse(data));
      toast({
        variant: "success",
        title: 'Login successful',
      })
    } catch (error) {
      console.log('Error login:', error);
    }
  };
  useEffect(() => {
    if (user) {
      console.log('Updated user:', user);
    }
  }, [user]);


  return (
    <div className='flex flex-row h-screen'>
      <div className='w-3/5 p-10 text-center text-white'>
        <img src="/Logo.png" className="w-16" />
        <h1 className='text-6xl text-or font-bold tracking-wider mt-20'>LOGIN</h1>

        <div className='mt-14 w-full flex flex-col items-center'>
          <Input className='bg-blight border-none h-14 w-3/4 rounded-full text-center mb-8' placeholder='Email' onChange={(e) => setEmail(e.target.value)}></Input>

          <Input className='bg-blight border-none h-14 w-3/4 rounded-full text-center' type="password" placeholder='Password' onChange={(e) => setPassword(e.target.value)}></Input>

          <Button className='mt-12 w-2/4 h-12 rounded-full bg-transparent border border-or hover:bg-or' onClick={handleLogin} disabled={email == '' || password == ''}>LOGIN</Button>
        </div>

        <div className='text-center mt-32 tracking-wider text-sm'>
          <h5>Don't have an account? 
          <Link href="/signup" className='text-or hover:text-white'>  Sign Up</Link></h5>
        </div>
      </div>
      <div className="w-2/5 overflow-hidden flex items-center">
        <img src="/event.jpg" className="h-full object-cover"/>
      </div>
    </div>
  )
}

export default page
