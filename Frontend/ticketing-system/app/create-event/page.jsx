'use client'
import { useEdgeStore } from '@/lib/edgestore';
import React, { useState } from 'react'
import { SingleImageDropzoneUsage } from './_components/SingleImageDropzoneUsage';

const page = () => {
    const [file,setFile] = useState();
    const {edgestore} = useEdgeStore();

  return (
    <div>
        <SingleImageDropzoneUsage/>
    </div>
  )
}

export default page
