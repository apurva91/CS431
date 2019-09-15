package assignment1.q2;

// class for thread moving bottle from sealer to packager's tray.
public class SealerToPackagerThread extends Thread {
    // sealer object.
    Sealer sealer;
    // global time.
    int time;
    // packager object.
    Packager packager;
    // finished tray object.
    Finished finished;

    // initializing the thread.
    SealerToPackagerThread(Packager packager, Sealer sealer, int time, Finished finished) {
        this.sealer = sealer;
        this.time = time;
        this.packager = packager;
        this.finished = finished;
    }

    @Override
    public void run() {
        // if sealer has stopped processing on or before global time.
        if (sealer.time <= this.time) {
            // if sealer is carrying newly sealed bottle.
            if (sealer.status == 1) {
                // turning isSealed to true for the bottle.
                sealer.curr.isSealed = 1;
                // if bottle is also packaged, then move to godown.
                if (sealer.curr.isPackaged == 1) {

                    if (sealer.curr.type == 1) {
                        finished.send(1);
                    } else {
                        finished.send(2);
                    }
                    sealer.status = 0;
                } else if (sealer.curr.type == 1) {

                    if (packager.tray1.size() < 2) {

                        packager.tray1.add(sealer.curr);
                        sealer.status = 0;
                    }

                }
                // move the bottle to packager's tray.
                else {

                    if (packager.tray2.size() < 3) {

                        packager.tray2.add(sealer.curr);
                        sealer.status = 0;
                    }
                }
            }
        }
    }
}