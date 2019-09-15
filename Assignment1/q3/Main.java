package assignment1.q3;

// Importing the required packages
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

// Class Main initiates the simulation. Turns on the UI and maintains the Time for various events.
public class Main {

    // List of all the cars entered till now.
    public static List<Car> cars = new ArrayList<Car>();
    // For all the cars mentioned above a boolean variable indicating whether the
    // car has already crossed the road.
    public static List<Boolean> isPassed = new ArrayList<Boolean>();
    // Global Time Counter
    public static int currentTime = 0;
    // Current Car ID to be given to the new car that comes.
    public static int currentCarIndex = 1;
    // Array containing variable for each signal telling when the road will be open
    // for crossing again.
    public static int next[] = { 0, Constants.TIME_PER_SIGNAL, Constants.TIME_PER_SIGNAL * 2 , 0,0,0};
    // Array containing the objects for each signal.
    public static TrafficSignal signals[] = { new TrafficSignal("T1"), new TrafficSignal("T2"), new TrafficSignal("T3") };

    // Returns the signal which is active currently by using current time.
    public static int currentSignal() {
        return (Main.currentTime / Constants.TIME_PER_SIGNAL) % Constants.NUMBER_OF_SIGNALS;
    }

    // Updates the next time for the given signal.
    public static void updateNextTime(int signal) {
        // If already the time when next car can come is more than the current time, we
        // dont need to increment the next time.
        if (next[signal] > currentTime)
            return;
        // When the above is not the case, we need to increment the time.
        // So overhere we check if a new car comes at that instant will it be able to
        // cross or will it have to wait for two cycles.
        // As mentioned earlier next is when the road will be free. So we will check
        // whether Current Signal Time + Crossing Time + 1 is in the same signal if yes
        // then increment by 1 otherwise we need to set the next time to the time when
        // the current signal will be green again.
        if (next[signal] % Constants.TIME_PER_SIGNAL + 1 + Constants.CROSSING_TIME <= Constants.TIME_PER_SIGNAL) {
            next[signal] += 1;
        } else {
            next[signal] = (next[signal] / Constants.TIME_PER_SIGNAL + Constants.NUMBER_OF_SIGNALS)
                    * Constants.TIME_PER_SIGNAL;
        }
    }

    // Updates the next time once a car arrives.
    public static void updateNextTimeCar(int signal) {
        // So overhere we check if a new car comes at this instant will it be able to
        // cross or will it have to wait for two cycles.
        // As mentioned earlier next is when the road will be free. So we will check
        // whether Current Signal Time + Crossing Time + Crossing Time is in the same
        // signal if yes
        // then increment by 1 otherwise we need to set the next time to the time when
        // the current signal will be green again.
        if (next[signal] % Constants.TIME_PER_SIGNAL
                + Constants.CROSSING_TIME * (Constants.NUMBER_OF_SIGNALS - 1) <= Constants.TIME_PER_SIGNAL) {
            next[signal] += Constants.CROSSING_TIME;
        } else {
            next[signal] = (next[signal] / Constants.TIME_PER_SIGNAL + Constants.NUMBER_OF_SIGNALS)
                    * Constants.TIME_PER_SIGNAL;
        }
    }

    // Handles updating the time for the cars which need not wait for signal.
    public static void updateNextTimeCarDirect(int signal){
        if(next[signal]<currentTime){
            next[signal]=currentTime+Constants.CROSSING_TIME;
        }
        else{
            next[signal]=next[signal]+Constants.CROSSING_TIME;
        }
    }

    // Adds new car in the List, Updates the next time, returns the index of the car
    // in the list.
    public static int addNewCar(int type, String source, String destination) {
        // Time at which the car will cross the road.
        int time = next[type] + Constants.CROSSING_TIME;
        
        // Update the next time for the current signal.
        if(type>=3){
            updateNextTimeCarDirect(type);
            time = next[type];
        }
        else{
            updateNextTimeCar(type);
        }
        // Create a new Car object.
        Car new_car = new Car(time, type, source, destination);
        // Add it in the list.
        cars.add(new_car);
        // Mark it as not yet passed.
        isPassed.add(false);
        // Return the index of the car.
        return Integer.parseInt(new_car.getId()) - 1;
    }

    // Gets a new car ID and increments currentCarIndex.
    public static int getNewCarId() {
        return currentCarIndex++;
    }

    // Method main.
    public static void main(String[] args) {

        // Use InvokeLater to run the UserInterface.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create a new instance of the UserInterface Class.
                new UserInterface();
            }
        });

        // Create a Timer object.
        Timer timer = new Timer();

        // Schedule it to run at every 1 second with delay of 0 seconds.
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // increment the current time.
                currentTime++;
                // Update next time for the current signal.
                updateNextTime(currentSignal());
                // Refresh the user interface by updating all values in the tables.
                UserInterface.reDraw();
            }
        }, 0, 1000);
    }
}