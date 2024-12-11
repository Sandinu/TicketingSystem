export const fetchWithCors = async (url, options = {}) => {
    const defaultOptions = {
      mode: 'cors',
      headers: {
        'Content-Type': 'application/json',
      },
    };
  
    const mergedOptions = { ...defaultOptions, ...options };
  
    const response = await fetch(url, mergedOptions);
  
    if (!response.ok) {
      throw new Error(`Network response was not ok: ${response.statusText}`);
    }
  
    return response;
  };