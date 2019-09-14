package assignment1.q1;

// Class Order is data structre used for storing the order details for an order.
public class Order {
    // ID of the order.
    private int id;
    // Type of order
    private char type;
    // Quantity required for the order.
    private int qty;

    //Method to return the order id
    int getId() {
        return id;
    }
    
    //Method to return the order type
    char getType() {
        return type;
    }
    
    //Method to return the order quantity
    int getQuantity() {
        return qty;
    }

    // Initializer function for the Order class.
    Order(int id, char type, int qty) {
        this.id = id;
        this.type = type;
        this.qty = qty;
    }
}
