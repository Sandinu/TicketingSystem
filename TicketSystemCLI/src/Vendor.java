import java.util.Random;

/*
  Vendor class representing a vendor in the ticketing system.
  Extends the User class and implements behavior specific to ticket adding.
 */

public class Vendor extends User {
    private final int ticketReleaseRate; // Rate at which the vendor releases tickets (in seconds)
    private final Random random = new Random();

    // Constructor for vendor for simulation
    public Vendor(int Id, TicketPool ticketPool, int ticketReleaseRate) {
        super(Id, ticketPool);
        this.ticketReleaseRate = ticketReleaseRate;
    }

    // Constructor for vendor for manual ticket additions
    public Vendor(String Name, TicketPool ticketPool, int ticketReleaseRate, int Id) {
        super(Name, ticketPool, Id);
        this.ticketReleaseRate = ticketReleaseRate;
    }

    /*
      Simulates adding tickets periodically, considering system running state and pause conditions.
     */
    @Override
    public void run() {
        try {
            while (TicketSystem.isRunning() && !Thread.currentThread().isInterrupted()) {
                // Check for pause condition
                synchronized (TicketSystem.pauseLock) {
                    while (TicketSystem.isPaused()) {
                        TicketSystem.pauseLock.wait(); // Wait if the system is paused
                    }
                }

                // Sleep based on the ticket release rate before adding tickets
                Thread.sleep(ticketReleaseRate * 1000);

                // Generate a random number of tickets to add and add them to the pool
                int ticketsToAdd = random.nextInt(10) + 1;
                ticketpool.addTicket(ticketsToAdd, Id);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupt status
        }
    }

    /*
      Performs the ticket addition action manually. Override the abstract method in the User class.
     */
    @Override
    protected void performAction(int ticketCount) throws InterruptedException {
        ticketpool.addTicket(ticketCount, this.Name, this.Id);
    }
}
