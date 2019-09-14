package assignment1.q3;

import javax.swing.*;

public class TableThread extends Thread {
    public int mode;
    public int rowNumber;
    // public static int thread_number_count;
    // int thread_number;

    TableThread(int mode, int rowNumber) {
        this.mode = mode;
        // this.thread_number = thread_number_count++;
        this.rowNumber = rowNumber;
    }

    @Override
    public void run() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (mode == 0) {
                    UserInterface.updateTrafficData(rowNumber);
                } else if (mode == 1) {
                    UserInterface.updateCarData(rowNumber);
                } else if (mode == 2) {
                    UserInterface.addEntry(rowNumber);
                }
                // System.out.println(String.format("Thread Number %d completed.",
                // thread_number));
            }
        });
    }
}