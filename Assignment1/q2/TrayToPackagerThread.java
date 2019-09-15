package assignment1.q2;

//	thread for moving bottles into packager from trays.
public class TrayToPackagerThread extends Thread {
    // packager object.
    Packager packager;
    // global time.
    int time;
    // unfinished tray object.
    UnfinishedTray unfinished;

    // initializing the thread.
    TrayToPackagerThread(Packager packager, int time, UnfinishedTray unfinished) {
        this.packager = packager;
        this.time = time;
        this.unfinished = unfinished;
    }

    @Override
    public void run() {
        // packager is idle and contains no bottle in it.
        if (packager.status == 0) {
            // atleast one of the trays is non-empty.
            if (packager.tray1.size() > 0 || packager.tray2.size() > 0) {
                // input from tray 1.
                if (packager.flip2 == 1) {
                    if (packager.tray1.size() > 0) {
                        packager.curr = packager.tray1.get(0);
                        packager.tray1.remove(0);
                        packager.status = 1;
                    } else {
                        packager.curr = packager.tray2.get(0);
                        packager.tray2.remove(0);
                        packager.status = 1;
                    }
                    packager.flip2 = 2;
                }
                // input from tray 2;
                else {
                    if (packager.tray2.size() > 0) {
                        packager.curr = packager.tray2.get(0);
                        packager.tray2.remove(0);
                        packager.status = 1;
                    } else {
                        packager.curr = packager.tray1.get(0);
                        packager.tray1.remove(0);
                        packager.status = 1;
                    }
                    packager.flip2 = 1;
                }
            }
            // taking input from unfinished tray.
            else {
                // take bottle of type 1.
                if (packager.flip1 == 1) {
                    Bottle x = unfinished.getBottle(1);
                    if (x != null) {

                        packager.curr = x;
                        packager.status = 1;
                    } else {
                        x = unfinished.getBottle(2);
                        if (x != null) {
                            packager.curr = x;
                            packager.status = 1;
                        }
                    }
                    packager.flip1 = 2;
                }
                // take bottle of type 2.
                else {
                    Bottle x = unfinished.getBottle(2);
                    if (x != null) {
                        packager.curr = x;
                        packager.status = 1;
                    } else {
                        x = unfinished.getBottle(1);
                        if (x != null) {
                            packager.curr = x;
                            packager.status = 1;
                        }
                    }
                    packager.flip1 = 1;
                }
            }
            // increasing timer of packager by 2.
            packager.time = packager.time + 2;
        }
    }
}