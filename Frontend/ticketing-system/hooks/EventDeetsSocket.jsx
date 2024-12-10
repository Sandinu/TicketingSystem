'use client'
import { useState, useEffect } from "react";

const useEventWebSocket = (url) => {
  const [messages, setMessages] = useState([]);
  const [connectionStatus, setConnectionStatus] = useState('Disconnected');

  useEffect(() => {
    // Create a WebSocket connection
    const socket = new WebSocket(url);

    // Handle the WebSocket connection open event
    socket.onopen = () => {
      setConnectionStatus('Connected');
      console.log("WebSocket connection established.");
    };

    // Handle incoming WebSocket messages
    socket.onmessage = (event) => {
      const newMessage = event.data;
      setMessages((prevMessages) => [newMessage, ...prevMessages]);
      console.log("Received message:", newMessage);
    };

    // Handle WebSocket connection close event
    socket.onclose = () => {
      setConnectionStatus('Disconnected');
      console.log("WebSocket connection closed.");
    };

    // Handle WebSocket errors
    socket.onerror = (error) => {
      console.log("WebSocket error:", error);
    };

    // Cleanup on component unmount
    return () => {
      socket.close();
    };
  }, [url]);

  return { messages, connectionStatus };
};

export default useEventWebSocket;