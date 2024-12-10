import React from 'react'

const EventPost = ({imgsrc}) => {
  return (
    <div>
      <div className='w-full overflow-hidden rounded-xl border border-bblack'>
        <img src={imgsrc ? imgsrc : "/event.jpg"} className='size-s'  alt='logo'/>
      </div>
    </div>
  )
}

export default EventPost
