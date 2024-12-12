 //Abstract User class that implements Runnable, and the base class for the customer and vendor classes

public abstract class User implements Runnable {
    protected final TicketPool ticketpool;
    protected int Id;
    protected String Name = ""; // Name of the user, optional

    //constructor for the simulation without a username
    public User(int Id, TicketPool ticketpool) {
        this.ticketpool = ticketpool;
        this.Id = Id;
    }

    //overloaded constructor for manual vendors/customers with a name
    public User(String Name, TicketPool ticketpool, int Id) {
        this.ticketpool = ticketpool;
        this.Name = Name;
        this.Id = Id;
    }


    //abstract method to perform the manual ticket additions/purchases
    protected abstract void performAction(int ticketCount) throws InterruptedException;
}
