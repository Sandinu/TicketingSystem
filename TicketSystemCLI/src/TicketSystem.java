import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TicketSystem {
    private static Configuration config;
    private static TicketPool ticketPool;
    private static boolean running = false;
    private static boolean paused = false;
    public static Scanner sc = new Scanner(System.in);
    private static int totalTickets;
    private static int ticketReleaseRate;
    private static int customerRetrievalRate;
    private static int maxTicketCapacity;
    private static int Id=100;
    private static final List<Thread> allThreads = new ArrayList<>(); // Track all threads
    private static boolean waitingForLogs = false;
    private static final Lock lock = new ReentrantLock();
    protected static final Object pauseLock = new Object();




    public static void main(String[] args) {
        String command;

        System.out.println("Welcome to the Real-Time Ticketing System!");
        System.out.println(" ");

        configureSystem();

        help();

        while (true){
            if (waitingForLogs){
                continue;
            }
            System.out.println(" ");
            System.out.println("Command: ");
            command = sc.next();

            switch (command.toLowerCase()){
                case "help":
                    help();
                    break;
                case "mv":
                    manualVendor();
                    break;
                case "start":
                    startSystem();
                    break;
                case "stats":
                    ticketPool.getStats();
                    break;
                case "mc":
                    manualCustomer();
                    break;
                case "pause":
                    pauseSystem();
                    break;
                case "resume":
                    resumeSystem();
                    break;
                case "stop":
                    stopSystem();
                    return;
                default:
                    System.out.println("Invalid Command! Try again!");
            }

        }



    }

    //System Controlls
    private static void help(){
        if (paused && running){
            System.out.println("""
                    resume - Resume the system
                    stop - Stop the system
                    stats - Display stats
                    help - System Controllers
                    mv - Manual Vendor
                    mc - Manual Customer
                """);
        }else if(running){
            System.out.println("""
                    stop - Stop the system
                    pause - Pause the system
                    stats - Display stats
                    help - System Controllers
                    mv - Manual Vendor
                    mc - Manual Customer
                """);
        }else{
            System.out.println("""
                    start - Start the system
                    stop - Stop the system
                    stats - Display stats
                    help - System Controllers
                    mv - Manual Vendor
                    mc - Manual Customer
                """);
        }
    }

    //System configuring method
    private static void configureSystem(){
        System.out.println("Configure the system...");

        System.out.print("Enter total ticket sales: ");
        totalTickets = getIntInput();

        System.out.print("Enter ticket release rate (s): ");
        ticketReleaseRate = getIntInput();

        System.out.print("Enter customer retrieval rate (s): ");
        customerRetrievalRate = getIntInput();

        System.out.print("Enter maximum ticket capacity: ");
        maxTicketCapacity = getIntInput();


        //Creation of a configuration class to store the data
        config = new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
        config.logConfiguration();
        System.out.println();

        //Creation of a Ticketpool class with the configuration for the processes of the system
        ticketPool = new TicketPool(maxTicketCapacity, totalTickets);
    }

    //Error handled method to get integer inputs properly
    public static int getIntInput(){
        int value;
        while(true){
            try{
                value = sc.nextInt();
                if (value > 0 ){
                    return value;
                }else{
                    System.out.println("Value must be positive");
                }
            }catch (Exception e){
                System.out.println("Invalid Input. Please enter a valid value: ");
                sc.nextLine();
            }
        }
    }

    //Starting the automated system, generation of customers and vendors and simulation of the system
    private static void startSystem(){
        if (running){
            System.out.println("System already running...");
            return;
        }
        running = true;
        initializeLogFile(); //Check if the log.json file exist, if not creating a one! if exist overwrites it.

        if (running) {
            //generation of 100 vendors
            for (int i = 0; i < 100; i++) {
                Vendor vendor = new Vendor(i, ticketPool, config.getTicketReleaseRate());
                Thread vendorThread = new Thread(vendor);
                allThreads.add(vendorThread);
                vendorThread.start();
            }

            //generation of 100 customers
            for (int i = 0; i < 100; i++) {
                Customer customer = new Customer(i, ticketPool, config.getTicketReleaseRate());
                Thread customerThread = new Thread(customer);
                allThreads.add(customerThread);
                customerThread.start(); // starting the thread!
            }
        }

        System.out.println("Starting system operations...");
        if (!Console.isConsoleRunning()){
            Console.startConsole();
        } // Opening up the java swing panel to display real-time ticket transactions!
    }

    //check if log.json file exist, and if not create one, else overwrites it
    private static void initializeLogFile() {
        File logFile = new File("log.json");

        try (FileWriter writer = new FileWriter(logFile, false)) { // 'false' overwrites if file exists
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            writer.write("=== TICKET LOG ===");
            System.out.println("Initialized log file: log.json");
        } catch (IOException e) {
            System.err.println("Error initializing log file: " + e.getMessage());
        }
    }

    //method to pause the simulation (pausing all the threads!)
    private static void pauseSystem(){
        if (!running || paused){
            System.out.println("System is not running/ already paused!");
            return;
        }
        paused = true;
        System.out.println("System operations paused.");

        synchronized (pauseLock) {
            pauseLock.notifyAll(); // Notify any threads waiting on pauseLock
        }
    }

    //method to stop/end the system
    protected static void stopSystem(){
        if (!running){
            System.out.println("System is not running");
            return;
        }
        running = false;
        System.out.println("System operations stopped.");
        for (Thread thread : allThreads) {
            thread.interrupt(); // Sends an interrupt signal to each thread
        }

        // Indicate that the system is waiting to process and finalize logs
        waitingForLogs = true;

        // Acquire a lock to ensure the log-saving process completes without interruption
        lock.lock();
        try {
            // Save and process the ticket logs before system exit
            ticketPool.logfile();
        } finally {
            // Reset the waitingForLogs flag and release the lock
            waitingForLogs = false;
            lock.unlock();
        }

        // Terminate the program after completing the log saving
        System.exit(0);
    }

    //method to resume the system once its paused
    private static void resumeSystem(){
        if (!paused) {
            System.out.println("System is not paused!");
            return;
        }

        paused = false;
        System.out.println("System operations resumed.");

        synchronized (pauseLock) {
            pauseLock.notifyAll(); // Resume all threads waiting on pauseLock
        }
    }


    public static boolean isRunning(){
        return running;
    }

    public static boolean isPaused(){
        return paused;
    }

    //method to add a custom vendor and add tickets manually
    public static void manualVendor(){
        System.out.println("Manual vendor:");
        System.out.print("Enter the vendor name: ");
        String Name = sc.next();
        sc.nextLine();
        System.out.println("Enter the number of tickets to add: ");
        int vendorTicketCount = getIntInput();

        Vendor vendor = new Vendor(Name , ticketPool, config.getTicketReleaseRate(), Id);
        Id++;

        if (!Console.isConsoleRunning()){
            Console.startConsole();
        }

        Thread vendorThread = new Thread(() -> {
            try {
                vendor.performAction(vendorTicketCount);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Vendor action was interrupted");
            }
        });
        vendorThread.start();


    }

    //method to add a custom customer and buy tickets manually
    public static void manualCustomer(){
        System.out.println("Manual Customer:");
        System.out.print("Enter the customer name: ");
        String Name = sc.next();
        sc.nextLine();
        System.out.println("Enter the number of tickets to purchase: ");
        int customerTicketCount = getIntInput();

        Customer customer = new Customer(Name , ticketPool, config.getTicketReleaseRate(), Id);
        Id++;
        if (!Console.isConsoleRunning()){
            Console.startConsole();
        }
        Thread vendorThread = new Thread(() -> {
            try {
                customer.performAction(customerTicketCount);  // Call performAction with necessary arguments
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Vendor action was interrupted");
            }
        });
        vendorThread.start();


    }

    public static void setRunning(){
        running = false;
    }

}