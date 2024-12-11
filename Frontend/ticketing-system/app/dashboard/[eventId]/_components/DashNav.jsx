import { Avatar } from '@/components/ui/avatar'
import { Button } from '@/components/ui/button'
import { UserContext } from '@/UserContext'
import { AvatarFallback, AvatarImage } from '@radix-ui/react-avatar'
import Link from 'next/link'
import React, { useContext } from 'react'

const DashNav = () => {
  const {user, setUser} = useContext(UserContext);
  const handleLogOut = async () => {
    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/users/logout`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        }
      });

      if (!response.ok) {
        throw new Error(`Network response was not ok: ${response.statusText}`);
      }
      setUser(null);
      localStorage.removeItem('user');
      window.location.href = '/login';
    } catch (error) {
    console.log('Error Logging out logs:', error);
    }
  };
  return (
    <div>
      <nav>
      <div className="py-5 flex justify-between items-center">
            <div className="flex flex-row items-center">
                <img src={'/logo.png'} alt="logo" width={40} height={25}/>
                <span className="text-or text-xl font-bold ml-5">BOOK-IT</span>
            </div>
                
                <div className="flex gap-3 items-center">
                  { user == null ?
                    (<div>
                      <Link href="/signup"> <Button className='rounded-full mx-4 w-40 bg-transparent border border-white text-white hover:bg-white hover:text-or'>SIGNUP</Button></Link>
                      <Link href="/login"><Button className='rounded-full w-40 bg-or text-white hover:bg-white hover:text-or' onClick={handleLogOut}>LOGIN</Button></Link>
                    </div>) :
                    user.roles == "ROLE_VENDOR" ?
                      (<div>
                        <Link href="/create-event"> <Button className='rounded-full mx-4 w-40 bg-white text-or hover:bg-or hover:text-white'>Create Event</Button></Link>
                        <Button className='rounded-full w-40 bg-or text-white hover:bg-white hover:text-or' onClick={handleLogOut}>LOGOUT</Button>
                      </div>) : user.roles == "ROLE_CUSTOMER"
                     (<Button className='rounded-full w-40 bg-or text-white hover:bg-white hover:text-or' onClick={handleLogOut}>LOGOUT</Button>)
                      
                  }
                </div>

        </div>
      </nav>
    </div>
  )
}

export default DashNav
