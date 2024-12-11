'use client'
import React, { useState } from 'react'
import supabase from './../../supabase/Supabase';

const page = () => {
    const [file,setFile] = useState(null);
    const [uploading, setUploading] = useState(null);

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    }

    const handleUpload = async () => {
        if (!file) return;
        setUploading(true);

        const {data, error} = await supabase.storage
        .from('oow-cw')
        .upload(`public/${file.name}`, file);

        setUploading(false);

        if (error) {
            console.log('Error uploading:', error);
            return;
        }else{
            console.log('Uploaded:', data);
        }
    }
  return (
    <div>
          <div>
      <h1>Upload Image</h1>
      <input type="file" onChange={handleFileChange} />
      <button onClick={handleUpload} disabled={uploading}>
        {uploading ? 'Uploading...' : 'Upload'}
      </button>
    </div>
    </div>
  )
}

export default page
