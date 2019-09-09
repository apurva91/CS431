// Importing required packages.
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.HashMap;

// Class Constants contains all the constants used in the application.
class Constants{
	// Specify the input file
	public static final String input_file = "input.txt";
	
	// Quantity of individual items available.
	public static final int small_qty = 20;
	public static final int medium_qty = 20;
	public static final int large_qty = 20;
	public static final int cap_qty = 20;
	
	// Mappings Do Not Change
	public static final int default_value = -1;
	public static final Map<Character, Integer> mapping = new HashMap<Character, Integer>();
    static {
        mapping.put('S',0);
        mapping.put('M',1);
        mapping.put('L',2);
		mapping.put('C',3);
    }
}

// Class Order is data structre used for storing the order details for an order.
class Order {
	// ID of the order.
	int id;
	// Type of order
	char type;
	// Quantity required for the order.
	int qty;

	// Initializer function for the Order class.
	Order(int id, char type, int qty) {
		this.id = id;
		this.type = type;
		this.qty = qty;
	}
}

// Class Inventory contains all the details about the inventory.
class Inventory{
	// Array containing the quanity available of each item. It is a static variable so that it remains the same across all the instances.
	private static int qty[] = {Constants.small_qty,Constants.medium_qty,Constants.large_qty,Constants.cap_qty};

	// Function to print the inventory.
	void printInventory() {
		System.out.println("Inventory:");
		System.out.println(String.format("    S: %d, M: %d, L: %d, C: %d", qty[0], qty[1], qty[2], qty[3]));
	}

	// Function to check whether the given order is available in the inventory.
	private boolean is_available(Order details){
		// Get type of order using the mapping in Constants.
		int int_type = Constants.mapping.getOrDefault(details.type,Constants.default_value);

		// If contains illegal character i.e., other than (S,M,L,C) return false.
		if(int_type==Constants.default_value) return false;

		// If sufficient quanity is available return true else return false.
		if(qty[int_type]>=details.qty) return true;
		else return false;
	}

	// Function that is called by the thread that first checks if it can serve the order using is_available if yes then it transacts and shows message successful else it fails.
	void transact(Order details){
		// If we use synchronized on qty then it implies only one thread will be able to access it. As it is a private variable it cannot be accessed from outside. So once a thread gets access to qty it executes this function.
		synchronized(qty){			
			// Check if given order is available in the inventory.
			if(is_available(details)){
				// It is available.
				// So decrease the quantity from the inventory followed by showing message order successful.
				qty[Constants.mapping.get(details.type)]-= details.qty;
				System.out.println(String.format("Order %d is successful (%c,%d)", details.id, details.type,details.qty));
			}
			else{
				// It is not available.
				// Shows a message informing order failed.
				System.out.println(String.format("Order %d failed (%c,%d)", details.id, details.type, details.qty));
			}
			// Once completed print the inventory so that next user is able to see it.
			printInventory();
		}
		// Now after reaching here other locks can try to access qty.
	}	
}


// Class Serve extends Class Thread overriding its run function. It also stores the details of the order. 
class Serve extends Thread {
	// Details of the order that this thread is going to serve
	public Order details;
	
	// Overriding the default run function by Thread Class.
	@Override
	public void run(){
		// Initialize an instance of inventory.
		Inventory inventory = new Inventory();
		// Call the transact function of the inventory for the order.
		inventory.transact(details);
	}
}

// Class Main contains the method main which is executed in the beginning of the application. It takes the input and creates and runs the threads.
class Main{
	// Array of the threads. 
	private static Serve[] threads; 

	// Main function.
	public static void main(String[] args) throws IOException{

		// From the file mentioned in Constants create a file object for it.
		File file = new File(Constants.input_file);

		// Read the file using the file object created above.
		BufferedReader reader = new BufferedReader(new FileReader(file));

		// The first number of the input is the number of total orders. 
		int total_orders = Integer.parseInt(reader.readLine());

		// Initialize all the threads.
		threads = new Serve[total_orders];

		for (int i = 0; i < total_orders; i++) {
			// Read the next line contains the data in the format "id type qty" so will split it and create a new order object and add it to the respective thread.
			String[] details = (reader.readLine().trim()).split("\\s+");
			threads[i] = new Serve();
			threads[i].details = new Order(Integer.parseInt(details[0]), details[1].charAt(0),Integer.parseInt(details[2]));
		}

		// Initally print the inventory details for the first user to see it.
		Inventory inventory = new Inventory();
		inventory.printInventory();
		
		// Start all the created threads. 
		for (int i = 0; i < total_orders; i++) {
			threads[i].start();
		}

	}
}