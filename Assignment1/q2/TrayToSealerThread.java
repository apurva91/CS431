package assignment1.q2;

//	thread for moving bottles into sealer from trays.
public class TrayToSealerThread extends Thread {
    // sealer object.
    Sealer sealer;
    // global time.
    int time;
    // unfinished tray object.
    UnfinishedTray unfinished;

    // initialzing the thread.
    TrayToSealerThread(Sealer sealer, int time, UnfinishedTray unfinished) {
        this.sealer = sealer;
        this.time = time;
        this.unfinished = unfinished;
    }

    @Override
    public void run() {
        // sealer is idle and contains no bottle in it.
        if (sealer.status == 0) {
            // sealer's tray is non-empty.
            if (sealer.tray.size() > 0) {
                sealer.curr = sealer.tray.get(0);
                sealer.tray.remove(0);
                sealer.status = 1;
            } else {
                // taking bottle of type 1 from unfinished tray.
                if (sealer.count == 1) {
                    Bottle x = unfinished.getBottle(1);
                    if (x != null) {
                        sealer.curr = x;
                        sealer.status = 1;
                    } else {
                        x = unfinished.getBottle(2);
                        if (x != null) {
                            sealer.curr = x;
                            sealer.status = 1;
                        }
                    }
                    sealer.count = 2;

                }
                // taking bottle of type 2 from unfinished tray.
                else {
                    Bottle x = unfinished.getBottle(2);
                    if (x != null) {
                        sealer.curr = x;
                        sealer.status = 1;
                    } else {
                        x = unfinished.getBottle(1);
                        if (x != null) {
                            sealer.curr = x;
                            sealer.status = 1;
                        }
                    }
                    sealer.count = 1;
                }
            }
            sealer.time = sealer.time + 3;
        }
    }
}
