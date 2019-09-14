package assignment1.q3;

public class Car {
    private int id;
    private int time;
    private String source;
    private String destination;
    private String status;
    private int type;

    Car(int time, int type, String source, String destination) {
        this.status = "Wait";
        this.source = source;
        this.destination = destination;
        this.id = Main.getNewCarId();
        this.time = time;
        this.type = type;
    }

    String getId() {
        return String.valueOf(id);
    }

    String getTime() {
        if (status == "Wait" || status == "Crossing")
            return String.valueOf(time - Main.currentTime);
        else
            return "--";
    }

    void updateTime() {
        if ((time - Main.currentTime) <= 0) {
            status = "Pass";
        } else {
            if (time - Main.currentTime <= Constants.CROSSING_TIME)
                status = "Crossing";
        }
    }

    String getSource() {
        return source;
    }

    String getDestination() {
        return destination;
    }

    String getStatus() {
        return status;
    }
}
