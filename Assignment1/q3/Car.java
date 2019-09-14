package assignment1.q3;

// Class Car for storing information about each car.
public class Car {
    // Data stored for each car includes id, source, destination, type(direction in
    // which it is going), time is the scheduled time for it to go and status is
    // (Wait, Crossing or Pass).
    private int id;
    private int time;
    private String source;
    private String destination;
    private String status;
    private int type;

    // Constructor of the class. Each car is initialized with the status Wait first,
    // a new car id is issued using getNewCarId function in Main Class.
    Car(int time, int type, String source, String destination) {
        this.status = "Wait";
        this.source = source;
        this.destination = destination;
        this.id = Main.getNewCarId();
        this.time = time;
        this.type = type;
    }

    // Return the id of the car
    String getId() {
        return String.valueOf(id);
    }

    // Return the time remaining for the car to complete crossing the road.
    String getTime() {
        // If the time stored is more than current time it has already crossed return
        // "--"
        if (status == "Wait" || status == "Crossing")
            return String.valueOf(time - Main.currentTime);
        else
            return "--";
    }

    // Updates the status of the car according to the time.
    void updateStatus() {
        if ((time - Main.currentTime) <= 0) {
            status = "Pass";
        } else {
            if (time - Main.currentTime <= Constants.CROSSING_TIME)
                status = "Crossing";
        }
    }

    // Returns the source string.
    String getSource() {
        return source;
    }

    // Returns the destination string.
    String getDestination() {
        return destination;
    }

    // Returns the status.
    String getStatus() {
        return status;
    }
}
