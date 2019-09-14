package assignment1.q1;

// Class Order is data structre used for storing the order details for an order.
public class Order {
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
