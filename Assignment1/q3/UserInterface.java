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
    JRadioButton sourceRadioButton1, sourceRadioButton2, sourceRadioButton3, destinationRadioButton1,
            destinationRadioButton2, destinationRadioButton3;
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
        if (!isInitialized)
            return;
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

        // Create three radio buttons for selecting which direction the new car is
        // coming from.
        sourceRadioButton1 = new JRadioButton("South");
        sourceRadioButton2 = new JRadioButton("West");
        sourceRadioButton3 = new JRadioButton("East");

        // Create a button group for the radio buttons so that only one of them can be
        // selected at a time.
        ButtonGroup sourceRadioGroup = new ButtonGroup();
        sourceRadioGroup.add(sourceRadioButton1);
        sourceRadioGroup.add(sourceRadioButton2);
        sourceRadioGroup.add(sourceRadioButton3);

        // Label to be displayed for the set of radio buttons.
        JLabel sourceLabel = new JLabel("Source:");

        // Create a panel and add all of the buttons and label to it..
        JPanel sourceSelector = new JPanel();
        sourceSelector.add(sourceLabel);
        sourceSelector.add(sourceRadioButton1);
        sourceSelector.add(sourceRadioButton2);
        sourceSelector.add(sourceRadioButton3);

        // Create three radio buttons for selecting which direction the new car is
        // going to.
        destinationRadioButton1 = new JRadioButton("South");
        destinationRadioButton2 = new JRadioButton("West");
        destinationRadioButton3 = new JRadioButton("East");

        // Create a button group for the Radio buttons so that only one of them can be
        // selected at a time.
        ButtonGroup destinationRadioGroup = new ButtonGroup();
        destinationRadioGroup.add(destinationRadioButton1);
        destinationRadioGroup.add(destinationRadioButton2);
        destinationRadioGroup.add(destinationRadioButton3);

        // Label to be displayed for the set of radio buttons.
        JLabel destinationLabel = new JLabel("Destination:");

        // Create a panel and add all of the buttons and label to it..
        JPanel destinationSelector = new JPanel();
        destinationSelector.add(destinationLabel);
        destinationSelector.add(destinationRadioButton1);
        destinationSelector.add(destinationRadioButton2);
        destinationSelector.add(destinationRadioButton3);

        // Create a button for submiting the user selection.
        JButton submitButton = new JButton("Add a new car");
        submitButton.setBounds(0, 0, 200, 30);

        // Add a action listener on the button.
        submitButton.addActionListener(this);

        // Create a panel for holding the buttons.
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);

        // Create a panel for keeping all the panels into it.
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.add(sourceSelector);
        inputPanel.add(destinationSelector);
        inputPanel.add(buttonPanel);

        // Calling the function to create table of traffic signals.
        getTrafficJTable();
        // Creating a scrollable Pane for it in case of overflows.
        JScrollPane scrollingPane1 = new JScrollPane(trafficTable);

        // Calling the function to create table of cars.
        getCarJTable();
        // Creating a scrollable Pane for it in case of overflows.
        JScrollPane scrollingPane2 = new JScrollPane(carTable);

        // Add all the panels to the Main Frame.
        add(inputPanel);
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
        if (sourceRadioButton1.isSelected() && destinationRadioButton3.isSelected()) {
            addEntry(Main.addNewCar(0, "South", "East"));
        } else if (sourceRadioButton2.isSelected() && destinationRadioButton1.isSelected()) {
            addEntry(Main.addNewCar(1, "West", "South"));
        } else if (sourceRadioButton3.isSelected() && destinationRadioButton2.isSelected()) {
            addEntry(Main.addNewCar(2, "East", "West"));
        } else if (sourceRadioButton1.isSelected() && destinationRadioButton2.isSelected()) {
            addEntry(Main.addNewCar(3, "South", "West"));
        } else if (sourceRadioButton2.isSelected() && destinationRadioButton3.isSelected()) {
            addEntry(Main.addNewCar(4, "West", "East"));
        } else if (sourceRadioButton3.isSelected() && destinationRadioButton1.isSelected()) {
            addEntry(Main.addNewCar(5, "East", "South"));
        } else {
            JOptionPane.showMessageDialog(this, "Please select valid options to add a car.");
        }
    }

}