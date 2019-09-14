package assignment1.q1;

// Class Inventory contains all the details about the inventory.
public class Inventory {
    // Array containing the quanity available of each item. It is a static variable
    // so that it remains the same across all the instances.
    private static Integer qty[] = {0,0,0,0};

    // These are the locks for each type of item in the inventory. Keeping it separate from the
    // inventory array gives the benifit of reading the value from inventory for printing purposes.
    private static Object lock[] = {new Object(), new Object(), new Object(), new Object()};

    Inventory(){
        // Empty Constructor.
    }
    Inventory(Integer[] quantity){
        qty = quantity;
    }

    // Method to return the quantity present in the inventory.
    int getQuantity(int type) {
        return qty[type];
    }

    // Function to print the inventory.
    void printInventoryWithMessage(String msg) {
        if (msg != null) {
            System.out.println(String.format("%s\nInventory:\n    S: %d, M: %d, L: %d, C: %d", msg, qty[0], qty[1],qty[2], qty[3]));
        } else {
            System.out.println(String.format("Inventory:\n    S: %d, M: %d, L: %d, C: %d", qty[0], qty[1], qty[2], qty[3]));
        }
    }

    // Check if the order is valid i.e., it contains illegal character other than
    // (S,M,L,C) return false.
    private boolean isValidOrder(Order details) {
        if (Constants.MAPPING.getOrDefault(details.getType(), Constants.DEFAULT_VALUE) == Constants.DEFAULT_VALUE)
            return false;
        else
            return true;
    }

    // Function to check whether the given order is available in the inventory.
    private boolean isAvailable(Order details) {
        // Get type of order using the MAPPING in Constants.
        int type = Constants.MAPPING.getOrDefault(details.getType(), Constants.DEFAULT_VALUE);

        // If sufficient quanity is available return true else return false.
        if (qty[type] >= details.getQuantity())
            return true;
        else
            return false;
    }

    // Function that is called by the thread that first checks if it can serve the
    // order using isAvailable if yes then it transacts and shows message successful
    // else it fails.
    void transact(Order details) {
        
        // Check if the order is correct if not reject the order.
        if (!isValidOrder(details)) {
            printInventoryWithMessage(String.format("Order %d failed.", details.getId()));
            return;
        }

        // Get type of the item.
        int type = Constants.MAPPING.get(details.getType());
        
        // If we use synchronized on entire inventory (qty[]) then it implies only one thread will
        // be able to access it at a time which will be same as serial execution.
        // Instead if we apply lock on individual type of items then multiple type of orders can be
        // processed parallely. Also the inventory is getting changed in only this particular thread
        // and the variable is private no one else can change the variable.
        synchronized (lock[type]){
            // Check if given order is available in the inventory.
            if (isAvailable(details)) {
                // It is available.
                // So decrease the quantity from the inventory followed by showing message order
                // successful and print the inventory.
                qty[type] -= details.getQuantity();
                printInventoryWithMessage(String.format("Order %d successful.", details.getId()));
            } else {
                // It is not available.
                // Shows a message informing order failed and print the inventory.
                printInventoryWithMessage(String.format("Order %d failed.", details.getId()));
            }
        }
        // Now after reaching here other locks can try to access qty.
    }
}
