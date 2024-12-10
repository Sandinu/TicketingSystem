import React from 'react'



const EventDeets = ({event}) => {
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
  return (
    <div>
      <div className=' relative align-middle text-center justify-center'>
        <div className='w-[90%] bg-or h-70 z-0 absolute top-0 block m-auto right-0 left-0 rounded-xl h-12'></div>
            <div className='w-full h-50 bg-blight py-8 z-10 absolute top-5 rounded-xl align-middle justify-center border'>
                <div className=''>
                    <h1 className='text-white text-6xl uppercase font-extrabold tracking-widest'>{event.eventName}</h1>
                    <h2 className='text-white font-light text-xl mt-1'>@vendor name</h2>
                </div>
                <div className='flex flex-row w-full justify-center mt-6 gap-4 px-20'>
                    <div className='bg-or py-2 w-1/3 rounded-full text-bdark font-medium text-xl'>{event.eventDate}</div>
                    <div className='bg-or py-2 w-1/3 rounded-full text-bdark font-medium text-xl'>{event.eventStartTime}</div>
                    <div className='bg-or py-2 w-1/3 rounded-full text-bdark font-medium text-xl'>{event.eventLocation}</div>
                </div>
            </div>
      </div>
    </div>
  )
}

export default EventDeets
