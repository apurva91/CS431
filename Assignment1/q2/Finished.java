package assignment1.q2;

// importing required packages.
import java.util.concurrent.*;

//	class for tray of finished bottles.
public class Finished {
    // semaphore lock for access to finished tray.
    Semaphore sema = new Semaphore(1);
    // no. of finished bottles of type 1.
    int noOfBottle1;
    // no. of finished bottles of type 2.
    int noOfBottle2;

    // initializing tray of finished bottles.
    Finished() {
        this.noOfBottle1 = 0;
        this.noOfBottle2 = 0;
    }

    // adding to finished tray.
    void send(int i) {
        try {
            sema.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (i == 1) {
            this.noOfBottle1++;
        } else {
            this.noOfBottle2++;
        }
        sema.release();
    }
}
