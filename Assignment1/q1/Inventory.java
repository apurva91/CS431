package assignment1.q1;

// Class Inventory contains all the details about the inventory.
public class Inventory {
    // Array containing the quanity available of each item. It is a static variable
    // so that it remains the same across all the instances.
    private static int qty[] = { Constants.small_qty, Constants.medium_qty, Constants.large_qty, Constants.cap_qty };

    // Function to print the inventory.
    void printInventory() {
        System.out.println("Inventory:");
        System.out.println(String.format("    S: %d, M: %d, L: %d, C: %d", qty[0], qty[1], qty[2], qty[3]));
    }

    // Function to check whether the given order is available in the inventory.
    private boolean isAvailable(Order details) {
        // Get type of order using the MAPPING in Constants.
        int int_type = Constants.MAPPING.getOrDefault(details.type, Constants.DEFAULT_VALUE);

        // If contains illegal character i.e., other than (S,M,L,C) return false.
        if (int_type == Constants.DEFAULT_VALUE)
            return false;

        // If sufficient quanity is available return true else return false.
        if (qty[int_type] >= details.qty)
            return true;
        else
            return false;
    }

    // Function that is called by the thread that first checks if it can serve the
    // order using isAvailable if yes then it transacts and shows message successful
    // else it fails.
    void transact(Order details) {
        // If we use synchronized on qty then it implies only one thread will be able to
        // access it. As it is a private variable it cannot be accessed from outside. So
        // once a thread gets access to qty it executes this function.
        synchronized (qty) {
            // Check if given order is available in the inventory.
            if (isAvailable(details)) {
                // It is available.
                // So decrease the quantity from the inventory followed by showing message order
                // successful.
                qty[Constants.MAPPING.get(details.type)] -= details.qty;
                System.out.println(
                        String.format("Order %d is successful (%c,%d)", details.id, details.type, details.qty));
            } else {
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
