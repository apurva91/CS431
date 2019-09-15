package assignment1.q2;

// importing required packages
import java.util.*;

// class for packaging module.
public class Packager {
    // tray 1 for packager storing bottles of type 1.
    List<Bottle> tray1;
    // tray 2 for packager storing bottles of type 2.
    List<Bottle> tray2;
    // internal timer for packager.
    int time;
    // status of operation of packager.
    int status;
    // next type of bottle to be taken from unfinished tray.
    int flip1;
    // next type of bottle to be taken from packager's tray.
    int flip2;
    // current bottle getting packaged.
    Bottle curr;

    // initializing packager object.
    Packager() {

        this.time = 0;
        this.tray1 = new ArrayList<Bottle>();
        this.tray2 = new ArrayList<Bottle>();
        this.status = 0;
        this.flip1 = 1;
        this.flip2 = 1;
        this.curr = null;
    }
}