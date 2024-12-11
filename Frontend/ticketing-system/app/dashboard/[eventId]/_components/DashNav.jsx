import { Avatar } from '@/components/ui/avatar'
import { Button } from '@/components/ui/button'
import { AvatarFallback, AvatarImage } from '@radix-ui/react-avatar'
import Link from 'next/link'
import React from 'react'

const DashNav = () => {
  return (
    <div>
      <nav>
      <div className="p-5 flex justify-between items-center">
            <div className="flex flex-row items-center">
                <img src={'/logo.png'} alt="logo" width={40} height={25}/>
                <span className="text-or text-xl font-bold ml-5">BOOK-IT</span>
            </div>
                
                <div className="flex gap-3 items-center">
                    {/* <Link href='/dashboard'>
                        <Button variant="outline" className="rounded-full">Dashboard</Button>
                    </Link>
                    <Link href='/dashboard'>
                        <Button className="rounded-full bg-blue-800">Get Started</Button>
                    </Link> */}
                    <Link href="#">
                        <Avatar>
                            <AvatarImage src='/user.png'/>
                            <AvatarFallback>CN</AvatarFallback>
                        </Avatar>
                    </Link>
                </div>

        </div>
      </nav>
    </div>
  )
}

export default DashNav
