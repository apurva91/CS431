package assignment1.q3;

// Importing the required packages
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.awt.Image;

// SwingUtilities Invoke Later creates a new Runnable that does the job in the background. The Swing data structures are not thread-safe so Swing library has its own implementation. Swing has a rule that whatever accesses to the UI needs to be done should be done on the same thread.

// Class UserInterface extends JFrame which is responsible for creating and displaying the user interface. It uses Swing and AWT widgets to create the User Interface.
public class UserInterface extends JFrame implements ActionListener {

    // The radio buttons for taking input from user.
    JRadioButton radioButton1, radioButton2, radioButton3;
    // Submit button for letting the user submit their choice.
    JButton submitButton;
    // Two tables one for car and one for traffic.
    JTable carTable, trafficTable;
    // Table Models for the tables.
    static DefaultTableModel carTableModel, trafficTableModel;
    // Boolean indicating if the class has been initialized completely once.
    static Boolean isInitialized = false;

    // For row number i, update the remaining time and status of the traffic signal.
    public static void updateTrafficData(int i) {
        String[] data = Main.signals[i].getData();
        trafficTableModel.setValueAt(data[1], i, 1);
        trafficTableModel.setValueAt(data[2], i, 2);
    }

    // For a row number i, it sets the new value for cells updates time and status.
    // Once the status is pass it sets a boolean corresponding to the row to be
    // true, so it wont be updated next time.
    public static void updateCarData(int i) {
        if (carTableModel.getValueAt(i, 3) == "Pass") {
            Main.isPassed.set(i, true);
            return;
        }
        carTableModel.setValueAt(Main.cars.get(i).getStatus(), i, 3);
        carTableModel.setValueAt(Main.cars.get(i).getTime(), i, 4);
    }

    // This method is invoked every one second, it refreshes the table data of both
    // cars as well as traffic signals.
    public static void reDraw() {
        // If the window is not yet initialized there is nothing to redraw.
        if(!isInitialized) return;
        // Updating Car Data. It only updates those which are either in Waiting or
        // Crossing state.
        for (int i = 0; i < Main.cars.size(); i++) {
            if (Main.isPassed.get(i))
                continue;
            final int rowNumber = i;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    updateCarData(rowNumber);
                }
            });
        }
        // Update the traffic signals.
        for (int i = 0; i < Main.signals.length; i++) {
            final int rowNumber = i;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    updateTrafficData(rowNumber);
                }
            });
        }
    }

    // Add Entry to Cars Table. Fetch the details from ith index of Array List of
    // Cars.
    public static void addEntry(int i) {
        final int rowNumber = i;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Car details = Main.cars.get(rowNumber);
                String[] data = new String[5];
                data[0] = details.getId();
                data[1] = details.getSource();
                data[2] = details.getDestination();
                data[3] = details.getStatus();
                data[4] = details.getTime();
                carTableModel.addRow(data);
            }
        });
    }

    // Add Entry to Traffic Signal Table.
    private void addEntry(TrafficSignal details) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                trafficTableModel.addRow(details.getData());
            }
        });
    }

    // Initialize the table for cars.
    private void getCarJTable() {
        // Header columns.
        String[] colName = { "ID", "Source", "Destination", "Status", "Remaining Time" };

        // Create the table.
        carTable = new JTable() {
            public boolean isCellEditable(int nRow, int nCol) {
                return false;
            }
        };

        // Create the model for the table.
        carTableModel = (DefaultTableModel) carTable.getModel();
        carTableModel.setColumnIdentifiers(colName);
    }

    // Initialize the table for traffic signals.
    private void getTrafficJTable() {
        // Header columns.
        String[] colName = { "Traffic Light", "Status", "Time" };

        // Create the table.
        trafficTable = new JTable() {
            public boolean isCellEditable(int nRow, int nCol) {
                return false;
            }
        };

        // Create the model for the table.
        trafficTableModel = (DefaultTableModel) trafficTable.getModel();
        trafficTableModel.setColumnIdentifiers(colName);
    }

    // Constructor of the class.
    UserInterface() {
        // Create the layout. Using GridLayout(row, columns)
        // Three rows basically are for taking user input, showing traffic signals
        // table, showing cars table.
        setLayout(new GridLayout(3, 0));

        // Create three radio buttons for selecting which road the new car is coming on.
        radioButton1 = new JRadioButton("South to East");
        radioButton2 = new JRadioButton("West to South");
        radioButton3 = new JRadioButton("East to West");

        // Create a button group for the radio buttons so that only one of them can be
        // selected at a time.
        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(radioButton1);
        radioGroup.add(radioButton2);
        radioGroup.add(radioButton3);

        // Set font size for each of the radio button. As we will be using pack() later
        // on position doesn't matter only height and width matters in setBounds(x axis,
        // y axis, width, height).
        radioButton1.setBounds(0, 0, 200, 30);
        radioButton2.setBounds(0, 0, 200, 30);
        radioButton3.setBounds(0, 0, 200, 30);

        // Create a button for submiting the user selection.
        JButton submitButton = new JButton("Add a new car");
        submitButton.setBounds(0, 0, 200, 30);

        // Add a action listener on the button.
        submitButton.addActionListener(this);

        JPanel selector = new JPanel();
        selector.add(radioButton1);
        selector.add(radioButton2);
        selector.add(radioButton3);
        selector.add(submitButton);
        // Create a panel for adding the radio buttons and the submit button into it.

        // Calling the function to create table of traffic signals.
        getTrafficJTable();
        // Creating a scrollable Pane for it in case of overflows.
        JScrollPane scrollingPane1 = new JScrollPane(trafficTable);

        // Calling the function to create table of cars.
        getCarJTable();
        // Creating a scrollable Pane for it in case of overflows.
        JScrollPane scrollingPane2 = new JScrollPane(carTable);

        // Add all the panels to the Main Frame.
        add(selector);
        add(scrollingPane1);
        add(scrollingPane2);
        // Pack it all together
        pack();

        // Add rows for each traffic signal to the traffic signal table.
        for (int i = 0; i < Main.signals.length; i++) {
            addEntry(Main.signals[i]);
        }

        // Set the title of the window.
        setTitle("Simulation");
        // Make it visible.
        setVisible(true);
        // Add action so that on close it exits the program.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Set isInitialized true as window is created.
        isInitialized = true;
    }

    // The event listener for the submit button.
    public void actionPerformed(ActionEvent e) {
        // Check which radio button was selected at the time of click and accordingly
        // add the car using the addNewCar method in Main Class and then adding it to
        // the user interface using addEntry method. If none of the radio buttons were
        // selected display a popup telling select a option to add a car.
        if (radioButton1.isSelected()) {
            addEntry(Main.addNewCar(0, "South", "East"));
        } else if (radioButton2.isSelected()) {
            addEntry(Main.addNewCar(1, "West", "South"));
        } else if (radioButton3.isSelected()) {
            addEntry(Main.addNewCar(2, "East", "West"));
        } else {
            JOptionPane.showMessageDialog(this, "Please select a option to add a car.");
        }
    }

}