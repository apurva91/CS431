package assignment1.q3;

// Class TrafficSignal is created for each traffic signal.
public class TrafficSignal {
    // The id of the traffic signal.
    private String id;

    // Returns a boolean value indicating whether the traffic signal is active.
    boolean isOn() {
        return ((id.charAt(1) - '1') == ((Main.currentTime) / Constants.TIME_PER_SIGNAL) % Constants.NUMBER_OF_SIGNALS);
    }

    // Returns the current data about the signal i.e, ID, Color and Remaining Time.
    String[] getData() {
        // If signal is active return Green and remaining time otherwise return Red with
        // "--" as the time.
        if (isOn()) {
            String[] p = { id, "Green",
                    String.valueOf(Constants.TIME_PER_SIGNAL - ((Main.currentTime) % Constants.TIME_PER_SIGNAL)) };
            return p;
        } else {
            String[] p = { id, "Red", "--" };
            return p;
        }
    }

    // Constructor of the class.
    TrafficSignal(String id) {
        this.id = id;
    }
}
