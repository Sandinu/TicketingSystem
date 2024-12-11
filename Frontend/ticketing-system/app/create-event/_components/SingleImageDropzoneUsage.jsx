import React, { useState } from 'react';
import { useEdgeStore } from '@/lib/edgestore';
import { SingleImageDropzone } from '@/components/SingleImageDropzone';

const SingleImageDropzoneUsage = () => {
  const [file, setFile] = useState(null);
  const { edgestore } = useEdgeStore();

  const handleUpload = async () => {
    if (file) {
      try {
        const res = await edgestore.publicFiles.upload({
          file,
          onProgressChange: (progress) => {
            console.log('Upload progress:', progress);
          },
        });
        console.log('Upload response:', res);
      } catch (error) {
        console.error('Upload failed:', error);
      }
    } else {
      console.error('No file selected');
    }
  };

  return (
    <div>
      <SingleImageDropzone onDrop={(acceptedFiles) => setFile(acceptedFiles[0])} />
      <button onClick={handleUpload}>Upload</button>
    </div>
  );
};

export default SingleImageDropzoneUsage;