package assignment1.q1;

// Class Serve extends Class Thread overriding its run function. It also stores
// the details of the order.
public class Serve extends Thread {
    // Details of the order that this thread is going to serve
    public Order details;

    // Overriding the default run function by Thread Class.
    @Override
    public void run() {
        // Initialize an instance of inventory.
        Inventory inventory = new Inventory();
        // Call the transact function of the inventory for the order.
        inventory.transact(details);
    }
}