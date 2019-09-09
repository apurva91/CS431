import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.HashMap;

class Constants{
	public static final String input_file = "input.txt";
	public static final int default_value = -1;
	public static final Map<Character, Integer> mapping = new HashMap<Character, Integer>();
    static {
        mapping.put('S',0);
        mapping.put('M',1);
        mapping.put('L',2);
		mapping.put('C',3);
    }
}

class Order {
	int id;
	char type;
	int qty;

	Order(int id, char type, int qty) {
		this.id = id;
		this.type = type;
		this.qty = qty;
	}
}

class Inventory{
	private static int qty[] = {10,10,10,10};

	void printInventory() {
		System.out.println("Inventory:");
		System.out.println(String.format("    S: %d, M: %d, L: %d, C: %d", qty[0], qty[1], qty[2], qty[3]));
	}

	private boolean is_available(Order details){
		int int_type = Constants.mapping.getOrDefault(details.type,Constants.default_value);
		if(int_type==-1) return false;
		if(qty[int_type]>=details.qty) return true;
		else return false;
	}

	void transact(Order details){
		synchronized(qty){
			if(is_available(details)){
				qty[Constants.mapping.get(details.type)]-= details.qty;
				System.out.println(String.format("Order %d is successful (%c,%d)", details.id, details.type,details.qty));
			}
			else{
				System.out.println(String.format("Order %d failed (%c,%d)", details.id, details.type, details.qty));
			}
			printInventory();
		}
	}	
}

class Serve extends Thread {
	public Order details;
	@Override
	public void run(){
		Inventory inventory = new Inventory();
		inventory.transact(details);
	}
}

class Main{
	private static Serve[] threads; 
	public static void main(String[] args) throws IOException{
		File file = new File(Constants.input_file);

		BufferedReader reader = new BufferedReader(new FileReader(file));

		int total_orders = Integer.parseInt(reader.readLine());
		threads = new Serve[total_orders];

		for (int i = 0; i < total_orders; i++) {
			String[] details = (reader.readLine().trim()).split("\\s+");
			threads[i] = new Serve();
			threads[i].details = new Order(Integer.parseInt(details[0]), details[1].charAt(0),Integer.parseInt(details[2]));
		}

		Inventory inventory = new Inventory();
		inventory.printInventory();
		
		for (int i = 0; i < total_orders; i++) {
			threads[i].start();
		}

	}
}