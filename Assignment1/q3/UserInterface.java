package assignment1.q3;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.*;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;


public class UserInterface extends JFrame implements ActionListener {

    JRadioButton r1, r2, r3;
    JButton b;
    JTable carTable;
    JTable trafficTable;

    static DefaultTableModel carTableModel;
    static DefaultTableModel trafficTableModel;

    public static void updateTrafficData(int i) {
        String[] data = Main.signals[i].getData();
        trafficTableModel.setValueAt(data[1], i, 1);
        trafficTableModel.setValueAt(data[2], i, 2);
    }

    public static void updateCarData(int i) {
        if (carTableModel.getValueAt(i, 3) == "Pass"){
            Main.isPassed.set(i,true);
            return;
        }
        carTableModel.setValueAt(Main.cars.get(i).getStatus(), i, 3);
        carTableModel.setValueAt(Main.cars.get(i).getTime(), i, 4);
    }

    public void reDraw() {
        List<TableThread> threads = new ArrayList<TableThread>();
        for (int i = 0; i < Main.cars.size(); i++) {
            if(Main.isPassed.get(i)) continue;
            threads.add(new TableThread(1, i));
        }
        for (int i = 0; i < Main.signals.length; i++) {
            threads.add(new TableThread(0, i));
        }
        for (int i = 0; i < threads.size(); i++) {
            threads.get(i).start();
        }
    }

    public static void addEntry(int i) {
        Car details = Main.cars.get(i);
        String[] data = new String[5];
        data[0] = details.getId();
        data[1] = details.getSource();
        data[2] = details.getDestination();
        data[3] = details.getStatus();
        data[4] = details.getTime();
        carTableModel.addRow(data);
    }

    private void addEntry(TrafficSignal details) {
        trafficTableModel.addRow(details.getData());
    }

    private void getCarJTable() {
        String[] colName = { "ID", "Source", "Destination", "Status", "Remaining Time" };
        if (carTable == null) {
            carTable = new JTable() {
                public boolean isCellEditable(int nRow, int nCol) {
                    return false;
                }
            };
        }
        carTableModel = (DefaultTableModel) carTable.getModel();
        carTableModel.setColumnIdentifiers(colName);
    }

    private void getTrafficJTable() {
        String[] colName = { "Traffic Light", "Status", "Time" };
        if (trafficTable == null) {
            trafficTable = new JTable() {
                public boolean isCellEditable(int nRow, int nCol) {
                    return false;
                }
            };
        }
        trafficTableModel = (DefaultTableModel) trafficTable.getModel();
        trafficTableModel.setColumnIdentifiers(colName);
    }

    UserInterface() {

        setLayout(new GridLayout(3, 0));
        r1 = new JRadioButton("South to East");
        r2 = new JRadioButton("West to South");
        r3 = new JRadioButton("East to West");
        ButtonGroup radioGroup = new ButtonGroup();
        JPanel selector = new JPanel();
        radioGroup.add(r1);
        radioGroup.add(r2);
        radioGroup.add(r3);

        r1.setBounds(75, 50, 200, 30);
        r2.setBounds(75, 80, 200, 30);
        r3.setBounds(75, 110, 200, 30);

        selector.add(r1);
        selector.add(r2);
        selector.add(r3);

        JButton b = new JButton("Add a new car");// creating instance of JButton
        b.setBounds(75, 140, 200, 30);// x axis, y axis, width, height
        b.addActionListener(this);
        selector.add(b);// adding button in JFrame
        getCarJTable();
        getTrafficJTable();
        add(selector);
        JScrollPane sp1 = new JScrollPane(trafficTable);
        add(sp1);
        // pack();

        JScrollPane sp2 = new JScrollPane(carTable);
        add(sp2);
        pack();
        addEntry(Main.signals[0]);
        addEntry(Main.signals[1]);
        addEntry(Main.signals[2]);
        setTitle("Simulation");
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (r1.isSelected()) {
            TableThread thread = new TableThread(2,Main.addNewCar(0, "South", "East"));
            thread.start();
        } else if (r2.isSelected()) {
            TableThread thread = new TableThread(2,Main.addNewCar(1, "West", "South"));
            thread.start();
        } else if (r3.isSelected()) {
            TableThread thread = new TableThread(2,Main.addNewCar(2, "East", "West"));
            thread.start();
        } else {
            JOptionPane.showMessageDialog(this, "Please Select a option to add a car.");
        }
    }

}