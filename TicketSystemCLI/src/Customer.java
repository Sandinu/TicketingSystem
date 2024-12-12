import java.util.Random;

/*
  Customer class representing a customer in the ticketing system.
  Extends the User class and implements behavior specific to ticket purchasing.
 */

public class Customer extends User {
    private final int retrievalRate; // Rate at which the customer retrieves tickets (in seconds)
    private final Random random = new Random();

    //constructor for customer for simulation
    public Customer(int id, TicketPool ticketPool, int retrievalRate) {
        super(id, ticketPool);
        this.retrievalRate = retrievalRate;
    }

    //constructor for customer for manual purchases
    public Customer(String Name, TicketPool ticketPool, int retrievalRate, int id) {
        super(Name, ticketPool, id);
        this.retrievalRate = retrievalRate;
    }


     //Simulates purchasing tickets periodically, considering system running state and pause conditions.

    @Override
    public void run() {
        try {
            while (TicketSystem.isRunning() && !Thread.currentThread().isInterrupted()) {
                // Check for pause condition and wait if paused
                synchronized (TicketSystem.pauseLock) {
                    while (TicketSystem.isPaused()) {
                        TicketSystem.pauseLock.wait();
                    }
                }

                // Sleep based on the retrieval rate before attempting a purchase
                Thread.sleep(retrievalRate * 1000);

                // Generate a random number of tickets to purchase and attempt the purchase
                int ticketsToPurchase = random.nextInt(10) + 1;
                ticketpool.purchaseTicket(ticketsToPurchase, Id);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupt status
        }
    }


     //Performs the ticket purchase action manually. Override the abstract method in the USER Class
    @Override
    protected void performAction(int ticketCount) throws InterruptedException {
        ticketpool.purchaseTicket(ticketCount, this.Name, this.Id);
    }
}
