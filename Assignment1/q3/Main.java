package assignment1.q3;

import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    public static List<Car> cars = new ArrayList<Car>();
    public static List<Boolean> isPassed = new ArrayList<Boolean>();
    public static int currentTime = 0;
    public static int currentCarIndex = 1;
    public static int next[] = { 0, Constants.TIME_PER_SIGNAL, Constants.TIME_PER_SIGNAL * 2 };
    public static TrafficSignal signals[] = { new TrafficSignal("T1"), new TrafficSignal("T2"),
            new TrafficSignal("T3") };

    public static int currentSignal(){
        return (Main.currentTime / Constants.TIME_PER_SIGNAL) % 3;
    }
    public static void updateNextTime(int type) {
        int temp = next[type];
        if(temp>currentTime) return;
        if(temp%Constants.TIME_PER_SIGNAL+1+Constants.CROSSING_TIME <= Constants.TIME_PER_SIGNAL){
            next[type]+=1;
        }
        else{
            next[type] = (next[type] / Constants.TIME_PER_SIGNAL + 3) * Constants.TIME_PER_SIGNAL;
        }   
    }

    public static void updateNextTimeCar(int type){
        int temp = next[type];
        if(temp%Constants.TIME_PER_SIGNAL+Constants.CROSSING_TIME*2 <= Constants.TIME_PER_SIGNAL){
            next[type]+=Constants.CROSSING_TIME;
        }
        else{
            next[type] = (next[type] / Constants.TIME_PER_SIGNAL + 3) * Constants.TIME_PER_SIGNAL;
        }
    }
    public static int addNewCar(int type, String source, String destination) {
        int time = next[type]+5;
        updateNextTimeCar(type);
        Car new_car = new Car(time, type, source, destination);
        cars.add(new_car);
        isPassed.add(false);
        return Integer.parseInt(new_car.getId())-1;
    }

    public static int getNewCarId() {
        return currentCarIndex++;
    }

    public static void main(String[] args) {
        UserInterface ui = new UserInterface();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentTime++;
                for (int i = 0; i < cars.size(); i++) {
                    cars.get(i).updateTime();
                }
                updateNextTime(currentSignal());
                ui.reDraw();
            }
        }, 0, 1000);
    }
}