package assignment1.q2;

// importing required packages.
import java.util.concurrent.*;

// unfinished tray class
public class UnfinishedTray {
    // semaphore lock for unfinished tray.
    Semaphore sema = new Semaphore(1);
    // no. of bottles of type 1 in tray.
    int noOfBottle1;
    // no. of bottles of type 2 in tray.
    int noOfBottle2;

    // initializer function for UnfinishedTray class.
    UnfinishedTray(int noOfBottle1, int noOfBottle2) {
        this.noOfBottle1 = noOfBottle1;
        this.noOfBottle2 = noOfBottle2;
    }

    // method to return specific type of bottle from unfinished class.
    Bottle getBottle(int i) {
        try {
            sema.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (i == 1) {
            if (noOfBottle1 > 0) {
                Bottle bottle = new Bottle(1);
                // decreasing number of bottle 1.
                noOfBottle1--;
                sema.release();
                return bottle;
            }
            sema.release();
            // no bottle left.
            return null;
        } else {
            if (noOfBottle2 > 0) {
                Bottle bottle = new Bottle(2);
                // decreasing number of bottle 2.
                noOfBottle2--;
                sema.release();
                return bottle;
            }
            sema.release();
            // no bottle left.
            return null;
        }

    }
}
