import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/*
  The Console class creates a Java Swing based panel to display real-time logs of the ticketing system.
*/
public class Console {
    private static JTextArea textArea; // Text area for displaying console output
    private static boolean consoleRunning = false;
     //Initializes and displays the console in a JFrame window.

    public static void startConsole() {
        JFrame frame = new JFrame("Real-Time Console");
        frame.setSize(800, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setEditable(false); //text-area is set to view only
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.WHITE);
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textArea);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setLocation(700, 100); // Positioning the frame on the screen
        frame.setAlwaysOnTop(true); // Ensure the frame stays on top initially
        frame.setVisible(true);
        frame.setAlwaysOnTop(false); // Allow it to be managed normally afterward
        consoleRunning = true;
    }

    //Appends a message to the console window. Automatically scrolls to the latest entry.

    public static void writeToConsole(String message) {
        textArea.append(message + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength()); // Auto-scroll to the bottom
    }

    public static boolean isConsoleRunning() {
        return consoleRunning;
    }
}
