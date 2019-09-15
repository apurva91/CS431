package assignment1.q2;

// importing required packages.
import java.util.*;

// class for sealing module.
public class Sealer {
    // tray for sealer.
    List<Bottle> tray;
    // internal timer for sealer.
    int time;
    // status of operation of sealer.
    int status;
    // current bottle getting sealed.
    Bottle curr;
    // next type of bottle to be taken from unfinished tray.
    int count;

    // initializing sealer object.
    Sealer() {
        this.status = 0;
        this.time = 0;
        this.count = 2;
        this.tray = new ArrayList<Bottle>();
        this.curr = null;
    }

}
