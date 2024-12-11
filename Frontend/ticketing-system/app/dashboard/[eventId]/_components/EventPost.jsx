import React from 'react'

const EventPost = ({imgsrc}) => {
  return (
    <div>
      <div className='w-full overflow-hidden rounded-xl border border-bblack h-full aspect-square'>
        <img src={imgsrc ? imgsrc : "/event.jpg"} className='w-full h-full object-cover' alt='logo'/>
      </div>
    </div>
  )
}

export default EventPost
