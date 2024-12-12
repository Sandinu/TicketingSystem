
# Event Ticketing Application🎟️

## Overview
This is a full-stack Event Ticketing Application designed for two types of users: Vendors and Customers. Vendors can create and manage events, while customers can explore events and purchase tickets. The application is built with a focus on Object-Oriented Programming (OOP) concepts and incorporates concurrent programming patterns using multithreading, specifically the Producer-Consumer pattern.

The project is designed to provide real-time ticket transaction tracking, event management, and analytics, all backed by a robust and scalable architecture.



## Features
### Vendor Features :
- Create Events: Vendors can create new events and specify event details.
- Add Tickets: Vendors can add tickets to events with different categories and prices.
- Edit Event Details: Vendors can modify event information such as name, description, and date.
- Event Configuration: Vendors can edit event configurations like ticket availability and pricing.
- Simulation: Vendors can run a simulation with generated customers and vendors to test the event's capacity, sales, and user interactions.
- VIP Customers: Add special VIP customers to the simulation for testing.
- Real-Time Analytics: View real-time ticket transactions and event statistics with both numerical and graphical representations.
- Start/Stop/Pause Simulation: Control the simulation to analyze various scenarios.

### Customer Features :
- Explore Events: Customers can browse available events and view details.
- Purchase Tickets: Customers can buy tickets for the events of their choice.

## Screenshots

![App aslkm;](https://www.google.com/url?sa=i&url=https%3A%2F%2Fpixlr.com%2Fimage-generator%2F&psig=AOvVaw3in-SXY_mH5t8F91FQ-DQi&ust=1734046118252000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCJiK4ajvoIoDFQAAAAAdAAAAABAY)

## Technologies Used
- Frontend:
Next.js: React framework for building the user interface.
Tailwind CSS: Utility-first CSS framework for responsive design.
- Backend:

 Spring Boot: Java-based framework for building the REST API.
WebSockets: Real-time communication for ticket transactions and event updates.
- Database:

MongoDB: NoSQL database for storing event and ticket data.
- Cloud Storage:

Cloudinary: Cloud-based storage solution for handling media files (e.g., event images).
- Deployment:

Amazon EC2: Hosting the application on Amazon EC2 for scalability and availability.
- Concurrency:

Implemented Producer-Consumer patterns using multithreading to handle concurrent ticket purchases and simulations.

## Installation and Setup
### Prerequisites:
- Java 11 or later
- Node.js (for frontend)
- MongoDB (local or remote instance)
- Cloudinary account for media storage

### Frontend Setup
1. Clone the repository
```bash
 git clone <repo_url>
 cd frontend
```
2. Install dependancies
```bash
 npm install
```
3. Run the nextJs app
```bash
 npm run dev
```

### Backend Setup
1. Clone the repository
```bash
 git clone <repo_url>
 cd backend
```
2. Install dependencies using maven
```bash
 mvn install
```
3. Configure MongoDB connection and Cloudinary API keys in application.properties.
4. Run the SpringBoot Application

## Usage
### For Vendors🛒 :
- Log in to the system (create an account if you haven't).
Create and manage events.
- Simulate ticket sales, add VIP customers, and view real-time analytics.
### For Customers🕴️ :
- Browse events and buy tickets.

## Concurrency & Multithreading
This project makes use of multithreading to simulate multiple users interacting with the system concurrently. The Producer-Consumer design pattern is used to manage ticket addition and purchases efficiently.

## Real-Time Features
The application provides real-time updates for ticket transactions using WebSockets, enabling vendors and customers to see live updates on ticket availability and sales statistics.


![Logo](/fron)


## 🔗 Links

[![linkedin](https://img.shields.io/badge/linkedin-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/)


