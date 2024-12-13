import React from "react";
import useWebSocket from '../../../../hooks/WebSocket';
import './Ticketlog.css';

const TicketLog = () => {
  const { messages, connectionStatus } = useWebSocket("ws://ec2-16-171-162-202.eu-north-1.compute.amazonaws.com:8080/ws/event-ticketlogs");

  return (
    <div className="w-full bg-bblack rounded-xl max-h-[400px] min-h-[400px] text-gray-600 p-5 overflow-hidden">

    <h1 className="text-or text-center uppercase text-3xl font-bold tracking-wider">Ticket Log</h1>
      <div className="max-h-[370px] overflow-y-scroll custom-scrollbar p-5">
        <ul className="text-sm text-blight">
          {messages.map((message, index) => (
            <li key={index} className="py-1">{message}</li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default TicketLog;
