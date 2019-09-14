package assignment1.q3;

public class TrafficSignal {
    private String id;

    boolean isOn() {
        return ((id.charAt(1) - '1') == (Main.currentTime / Constants.TIME_PER_SIGNAL) % 3);
    }

    String[] getData() {
        if (isOn()) {
            String[] p = { id, "Green",
                    String.valueOf(Constants.TIME_PER_SIGNAL - (Main.currentTime % Constants.TIME_PER_SIGNAL)) };
            return p;
        } else {
            String[] p = { id, "Red", "--" };
            return p;
        }
    }

    TrafficSignal(String id) {
        this.id = id;
    }
}
