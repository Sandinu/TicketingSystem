import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

/*
  The TicketPool class manages ticket operations, including adding, purchasing, and logging transactions.
  It handles the synchronization and capacity constraints for ticket handling within the ticketing system.
 */
public class TicketPool {
    private final Queue<String> tickets; // Queue to store ticket codes
    private final int maxTicketCount; // Maximum capacity of the ticket pool
    private final String ticketLogs = "log.json"; // Log file path for ticket operations
    private FileWriter fileWriter;
    private int code = 000001; // Initial code for ticket generation
    private int totalTickets; // Total number of tickets configured for the system
    private int totalTicketsAdded = 0; // Counter for tickets added
    private int totalTicketsSold = 0; // Counter for tickets sold
    private int endMessage = 0; // Flag to ensure the final message is printed once

    // Constructor initializes the ticket pool and sets up the log writer
    public TicketPool(int maxTicketCount, int totalTickets) {
        this.maxTicketCount = maxTicketCount;
        this.tickets = new LinkedList<>();
        this.totalTickets = totalTickets;
        initializeFileWriter();
    }

    // Initializes the log file writer, creating or overwriting the log file
    private void initializeFileWriter() {
        try {
            fileWriter = new FileWriter(ticketLogs, false); // Overwrite at start
            fileWriter.write("=== TICKET LOG ==="); // Initial log structure
            fileWriter.flush();
        } catch (IOException e) {
            System.err.println("Error initializing log file: " + e.getMessage());
        }
    }

    // Logs ticket transactions to the log file in JSON format
    private void logTransactions(String type, int id, int count, String tickets) {
        try {
            Map<String, Object> logEntry = new LinkedHashMap<>();
            logEntry.put("Type", type);
            logEntry.put("Id", id);
            logEntry.put("Ticket Count", count);
            logEntry.put("No of Tickets", tickets);

            StringBuilder jsonBuilder = new StringBuilder("{");
            logEntry.forEach((key, value) -> jsonBuilder.append("\"").append(key).append("\": \"").append(value).append("\", "));
            jsonBuilder.delete(jsonBuilder.length() - 2, jsonBuilder.length()).append("},\n");

            fileWriter.write(jsonBuilder.toString());
            fileWriter.flush(); // Flush to ensure immediate write
        } catch (IOException e) {
            System.err.println("Error logging transaction: " + e.getMessage());
        }
    }

    // Adds tickets to the pool simulation, checking for capacity and overall ticket limit
    public synchronized void addTicket(int count, int vendorId) throws InterruptedException {
        if (totalTicketsAdded + count > totalTickets) {
            if (processEnd()) {
                return;
            }
            Console.writeToConsole("Vendor-" + vendorId + " Total ticket limit reached! No further tickets can be added.");
            return;
        }

        if (tickets.size() + count > maxTicketCount) {
            Console.writeToConsole("Max capacity reached! Vendor" + vendorId + " waiting...");
            wait();
        }

        StringBuilder addedTickets = new StringBuilder();
        for (int i = 0; i < count; i++) {
            String ticketCode = "T-" + code;
            tickets.offer(ticketCode);
            addedTickets.append(ticketCode).append(" ");
            code++;
        }

        totalTicketsAdded += count;
        Console.writeToConsole("Vendor " + vendorId + " added " + count + " tickets (" + addedTickets.toString().trim() + ")");
        logTransactions("Add", vendorId, count, addedTickets.toString().trim());
        notifyAll();
    }

    // Adds tickets with vendor name and custom ID manually. Method overload
    public synchronized void addTicket(int count, String vendorId, int Id) throws InterruptedException {
        if (totalTicketsAdded + count > totalTickets) {
            if (processEnd()) {
                System.out.println("Sorry the total limit reached! Cannot add tickets!");
                return;
            }
            System.out.println("Total ticket limit reached! No further tickets can be added.");
            Console.writeToConsole("Vendor-" + vendorId + " Total ticket limit reached! No further tickets can be added.");
            return;
        }

        if (tickets.size() > maxTicketCount) {
            System.out.println("Max capacity reached! Vendor" + vendorId + " waiting...");
            Console.writeToConsole("Max capacity reached! Vendor" + vendorId + " waiting...");
            wait();
        }

        StringBuilder addedTickets = new StringBuilder();
        for (int i = 0; i < count; i++) {
            String ticketCode = "T-" + code;
            tickets.offer(ticketCode);
            addedTickets.append(ticketCode).append(" ");
            code++;
        }

        totalTicketsAdded += count;
        String logMessage = "Vendor " + vendorId + " added " + count + " tickets (" + addedTickets.toString().trim() + ")";
        System.out.println(logMessage);
        Console.writeToConsole(logMessage);
        logTransactions("Add", Id, count, addedTickets.toString().trim());
        notifyAll();
    }

    // Purchases tickets for a customer simulation
    public synchronized void purchaseTicket(int count, int customerId) throws InterruptedException {
        while (tickets.size() < count) {
            if (processEnd()) {
                return;
            }
            Console.writeToConsole("Not enough tickets available! Customer " + customerId + " waiting...");
            wait();
        }

        StringBuilder purchasedTickets = new StringBuilder();
        for (int i = 0; i < count; i++) {
            String ticketCode = tickets.poll();
            purchasedTickets.append(ticketCode).append(" ");
        }

        totalTicketsSold += count;
        Console.writeToConsole("Customer " + customerId + " purchased " + count + " tickets (" + purchasedTickets.toString().trim() + ")");
        logTransactions("Purchase", customerId, count, purchasedTickets.toString().trim());
        notifyAll();
    }

    // Purchases tickets for a customer by name and custom ID manually, Method overload
    public synchronized void purchaseTicket(int count, String customerId, int Id) throws InterruptedException {
        while (tickets.size() < count) {
            if (processEnd()) {
                return;
            }
            System.out.println("Not enough tickets available! Please wait...");
            Console.writeToConsole("Customer-" + customerId + " Not enough tickets available! Please wait...");
            wait();
        }

        StringBuilder purchasedTickets = new StringBuilder();
        for (int i = 0; i < count; i++) {
            String ticketCode = tickets.poll();
            purchasedTickets.append(ticketCode).append(" ");
        }

        totalTicketsSold += count;
        String logMessage = "Customer-" + customerId + " purchased " + count + " tickets (" + purchasedTickets.toString().trim() + ")";
        System.out.println(logMessage);
        Console.writeToConsole(logMessage);
        logTransactions("Purchase", Id, count, purchasedTickets.toString().trim());
        notifyAll();
    }

    // Returns the number of tickets currently in the pool
    public int getTicketCount() {
        return tickets.size();
    }

    // Checks if the ticket pool has reached the end state and stops the system
    public boolean processEnd() {
        if (tickets.isEmpty() && totalTicketsSold == totalTickets) {
            if (endMessage == 0) {
                System.out.println("Final Ticket was sold now!");
                endMessage++;
                TicketSystem.stopSystem();
            }
            TicketSystem.setRunning();
            Thread.currentThread().interrupt();
            return true;
        }
        return false;
    }

    // Displays ticket statistics
    public void getStats() {
        System.out.println("Total Tickets Limit Configured: " + totalTickets);
        System.out.println("Total Tickets Added: " + totalTicketsAdded);
        System.out.println("Total Tickets Sold: " + totalTicketsSold);
    }

    // Handles log file saving or discarding based on user input
    public synchronized void logfile() {
        System.out.flush();
        String saveCommand;
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Would you like to save the ticket logs? y/n");
            saveCommand = sc.nextLine().trim().toLowerCase();
            if (saveCommand.equals("y")) {
                break;
            } else if (saveCommand.equals("n")) {
                break;
            } else {
                System.out.println("Invalid command! Try again!");
            }
        }

        if (saveCommand.equals("y")) {
            System.out.println("Enter a name for the log file: ");
            String fileName = sc.nextLine().trim();

            File logfile = new File("log.json");
            File file = new File(fileName + ".json");

            try {
                Files.copy(logfile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                System.out.println("Error saving file! " + e.getMessage());
            }
        } else {
            System.out.println("Log file not saved!");
        }
    }
}
