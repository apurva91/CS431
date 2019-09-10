package q3;

import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

class Constants {
    public static final int crossing_time = 5;
    public static final int time_per_signal = 10;
    public static final int per_signal_car = Constants.time_per_signal / Constants.crossing_time;
}

class Car {
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
        this.id = Main.get_new_car_id();
        this.time = time;
        this.type = type;
    }

    String getId() {
        return String.valueOf(id);
    }

    String getTime() {
        if (status == "Wait" || status == "Crossing")
            return String.valueOf(time - Main.current_time);
        else
            return "--";
    }

    void updateTime() {
        if ((time - Main.current_time) <= 0) {
            status = "Pass";
        } else {
            if (time - Main.current_time <= Constants.crossing_time)
                status = "Crossing";
            // if (time == Main.current_time)
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

class TrafficSignal {
    private String id;

    boolean is_on() {
        return ((id.charAt(1) - '1') == (Main.current_time / Constants.time_per_signal) % 3);
    }

    String[] getData() {
        if (is_on()) {
            String[] p = { id, "Green", String.valueOf(10 - (Main.current_time % Constants.time_per_signal)) };
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

class Main {

    public static List<Car> cars = new ArrayList<Car>();
    public static int current_time = 0;
    public static int current_car_id = 1;
    public static int next[] = { 0, Constants.time_per_signal, Constants.time_per_signal * 2 };
    public static TrafficSignal signals[] = { new TrafficSignal("T1"), new TrafficSignal("T2"),
            new TrafficSignal("T3") };

    public static int currentSignal(){
        return (Main.current_time / Constants.time_per_signal) % 3;
    }
    public static void updateNextTime(int type) {
        int temp = next[type];
        if(temp>current_time) return;
        if(temp%Constants.time_per_signal+1+Constants.crossing_time <= Constants.time_per_signal){
            next[type]+=1;
        }
        else{
            next[type] = (next[type] / Constants.time_per_signal + 3) * Constants.time_per_signal;
        }   
    }

    public static void updateNextTimeCar(int type){
        int temp = next[type];
        if(temp%Constants.time_per_signal+Constants.crossing_time*2 <= Constants.time_per_signal){
            next[type]+=Constants.crossing_time;
        }
        else{
            next[type] = (next[type] / Constants.time_per_signal + 3) * Constants.time_per_signal;
        }
    }
    public static Car add_new_car(int type, String source, String destination) {
        int time = next[type]+5;
        updateNextTimeCar(type);
        Car new_car = new Car(time, type, source, destination);
        cars.add(new_car);
        return new_car;
    }

    public static int get_new_car_id() {
        return current_car_id++;
    }

    public static void main(String[] args) {
        UserInterface ui = new UserInterface();
        ui.addEntry(signals[0]);
        ui.addEntry(signals[1]);
        ui.addEntry(signals[2]);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                current_time++;
                for (int i = 0; i < cars.size(); i++) {
                    cars.get(i).updateTime();
                }
                updateNextTime(currentSignal());
                System.out.println(String.format("%d, %d, %d",next[0],next[1],next[2]));
                ui.reDraw();
            }
        }, 0, 1000);
    }
}