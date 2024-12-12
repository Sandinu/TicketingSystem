import java.io.FileWriter;
import java.io.IOException;

public class Configuration {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    private FileWriter fileWriter;
    private String configLogs = "config.json";


    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
        initializeFileWriter();
    }

    private void initializeFileWriter() {
        try {
            fileWriter = new FileWriter(configLogs, false); // Overwrite at start
            fileWriter.write("=== CONFIGURATIONS LOG ==="); // Initial log structure
            fileWriter.flush();
        } catch (IOException e) {
            System.err.println("Error initializing log file: " + e.getMessage());
        }
    }

    public void logConfiguration(){
        try{
            StringBuilder jsonBuilder = new StringBuilder("{");
            jsonBuilder.append("\"Total Tickets\": \"").append(totalTickets).append("\", ");
            jsonBuilder.append("\"Ticket Release Rate\": \"").append(ticketReleaseRate).append("\", ");
            jsonBuilder.append("\"Customer Retrieval Rate\": \"").append(customerRetrievalRate).append("\", ");
            jsonBuilder.append("\"Max Ticket Capacity\": \"").append(maxTicketCapacity).append("\"},\n");
            fileWriter.write(jsonBuilder.toString());
            fileWriter.flush(); // Flush to ensure immediate write
        } catch (IOException e) {
            System.err.println("Error logging transaction: " + e.getMessage());
        }

    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        if (totalTickets > 0) this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        if (ticketReleaseRate > 0) this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        if (customerRetrievalRate > 0) this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        if (maxTicketCapacity >= totalTickets) this.maxTicketCapacity = maxTicketCapacity;
    }


}
