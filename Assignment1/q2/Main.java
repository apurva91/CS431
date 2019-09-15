package assignment1.q2;

// importing required packages.
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main {
    // global timer for the machine.
    private static int time = 0;

    public static void main(String[] args) throws IOException {
        // number of bottles of type 1.
        int noOfBottle1 = Integer.parseInt(args[0]);
        // number of bottles of type 2.
        int noOfBottle2 = Integer.parseInt(args[1]);
        // total time for machine to run.
        int totalTime = Integer.parseInt(args[2]);
        // unfinished tray object.
        UnfinishedTray unfinished = new UnfinishedTray(noOfBottle1, noOfBottle2);
        // finished tray object.
        Finished finished = new Finished();
        // packager object.
        Packager packager = new Packager();
        // sealer object.
        Sealer sealer = new Sealer();
        // iterating over totalTime
        while (time != totalTime + 1) {
            // thread for taking packaged bottles to sealer.
            PackagerToSealerThread t1 = new PackagerToSealerThread(packager, sealer, time, finished);
            // thread for taking sealed bottles to packager.
            SealerToPackagerThread t2 = new SealerToPackagerThread(packager, sealer, time, finished);
            // starting threads.
            t1.start();
            t2.start();
            // sleeping to let the threads complete.
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            // thread for taking input into packager from tray.
            TrayToPackagerThread t3 = new TrayToPackagerThread(packager, time, unfinished);
            // thread for taking input into sealer from tray.
            TrayToSealerThread t4 = new TrayToSealerThread(sealer, time, unfinished);
            // starting threads.
            t3.start();
            t4.start();
            // sleeping to let the threads complete.
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            // incrementing the time.
            time++;
        }

        // counting different packaged and sealed bottles.
        int noOfBottle1Packaged = 0, noOfBottle1Sealed = 0, noOfBottle1InGodown = 0, noOfBottle2Packaged = 0,
                noOfBottle2Sealed = 0, noOfBottle2InGodown = 0;
        // looking into tray 1 of packager.
        for (int i = 0; i < packager.tray1.size(); i++) {
            if (packager.tray1.get(i).isSealed == 1) {
                noOfBottle1Sealed++;
            }
        }
        // looking into tray 2 of packager.
        for (int i = 0; i < packager.tray2.size(); i++) {
            if (packager.tray2.get(i).isSealed == 1) {
                noOfBottle2Sealed++;
            }
        }
        if (packager.curr != null && packager.status == 1) {
            if (packager.curr.isSealed == 1) {
                if (packager.curr.type == 1) {
                    noOfBottle1Sealed++;
                } else {
                    noOfBottle2Sealed++;
                }
            }
        }
        // looking into tray of sealer.
        for (int i = 0; i < sealer.tray.size(); i++) {
            if (sealer.tray.get(i).type == 1) {
                if (sealer.tray.get(i).isPackaged == 1)
                    noOfBottle1Packaged++;
            } else {
                if (sealer.tray.get(i).isPackaged == 1)
                    noOfBottle2Packaged++;
            }
        }
        // looking into finished tray.
        noOfBottle1InGodown += finished.noOfBottle1;
        noOfBottle1Packaged += finished.noOfBottle1;
        noOfBottle1Sealed += finished.noOfBottle1;
        noOfBottle2InGodown += finished.noOfBottle2;
        noOfBottle2Packaged += finished.noOfBottle2;
        noOfBottle2Sealed += finished.noOfBottle2;
        if (sealer.curr != null && sealer.status == 1) {
            if (sealer.curr.isPackaged == 1) {
                if (sealer.curr.type == 1) {
                    noOfBottle1Packaged++;
                } else {
                    noOfBottle2Packaged++;
                }
            }
        }
        // printing the result.
        System.out.println("Number of Bottle 1 packaged:  " + noOfBottle1Packaged);
        System.out.println("Number of Bottle 1 sealed:    " + noOfBottle1Sealed);
        System.out.println("Number of Bottle 1 in godown: " + noOfBottle1InGodown);
        System.out.println("Number of Bottle 2 packaged:  " + noOfBottle2Packaged);
        System.out.println("Number of Bottle 2 sealed:    " + noOfBottle2Sealed);
        System.out.println("Number of Bottle 2 in godown: " + noOfBottle2InGodown);
    }
}