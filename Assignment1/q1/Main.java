package assignment1.q1;

// Importing required packages.
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

// Class Main contains the method main which is executed in the beginning of the application. It takes the input and creates and runs the threads.
public class Main {
    // Array of the threads.
    private static Serve[] threads;

    // Main function.
    public static void main(String[] args) throws IOException {

        // From the file mentioned in Constants create a file object for it.
        File file = new File(Constants.INPUT_FILE);

        // Read the file using the file object created above.
        BufferedReader reader = new BufferedReader(new FileReader(file));

        // The first number of the input is the number of total orders.
        int totalOrders = Integer.parseInt(reader.readLine());

        // Initialize all the threads.
        threads = new Serve[totalOrders];

        for (int i = 0; i < totalOrders; i++) {
            // Read the next line contains the data in the format "id type qty" so will
            // split it and create a new order object and add it to the respective thread.
            String[] details = (reader.readLine().trim()).split("\\s+");
            threads[i] = new Serve();
            threads[i].details = new Order(Integer.parseInt(details[0]), details[1].charAt(0),
                    Integer.parseInt(details[2]));
        }

        // Initally print the inventory details for the first user to see it.
        Inventory inventory = new Inventory();
        inventory.printInventory();

        // Start all the created threads.
        for (int i = 0; i < totalOrders; i++) {
            threads[i].start();
        }

    }
}