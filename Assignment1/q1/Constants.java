package assignment1.q1;

//Importing required packages.
import java.util.Map;
import java.util.HashMap;

// Class Constants contains all the constants used in the application.
public class Constants {
    // Specify the input file
    public static final String INPUT_FILE = "input.txt";

    // MAPPINGs Do Not Change
    public static final int DEFAULT_VALUE = -1;
    public static final Map<Character, Integer> MAPPING = new HashMap<Character, Integer>();
    static {
        MAPPING.put('S', 0);
        MAPPING.put('M', 1);
        MAPPING.put('L', 2);
        MAPPING.put('C', 3);
    }
}
