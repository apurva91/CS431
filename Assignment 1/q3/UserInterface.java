package q3;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.*;
import java.awt.GridLayout;

public class UserInterface extends JFrame implements ActionListener {

    JRadioButton r1, r2, r3;
    JButton b;
    JTable carTable;
    JTable trafficTable;

    DefaultTableModel carTableModel;
    DefaultTableModel trafficTableModel;

    public void reDraw() {
        carTableModel.setRowCount(0);
        for (int i = 0; i < Main.cars.size(); i++) {
            String[] data = new String[5];
            data[0] = Main.cars.get(i).getId();
            data[1] = Main.cars.get(i).getSource();
            data[2] = Main.cars.get(i).getDestination();
            data[3] = Main.cars.get(i).getStatus();
            data[4] = Main.cars.get(i).getTime();
            carTableModel.addRow(data);
        }
        carTableModel.fireTableDataChanged();

        trafficTableModel.setRowCount(0);
        for (int i = 0; i < Main.signals.length; i++) {
            trafficTableModel.addRow(Main.signals[i].getData());
        }
        trafficTableModel.fireTableDataChanged();
    }

    private void addEntry(Car details) {
        String[] data = new String[5];
        data[0] = details.getId();
        data[1] = details.getSource();
        data[2] = details.getDestination();
        data[3] = details.getStatus();
        data[4] = details.getTime();
        carTableModel.addRow(data);
        // carTable.setModel(carTableModel);
        carTableModel.fireTableDataChanged();

    }

    public void addEntry(TrafficSignal details) {
        trafficTableModel.addRow(details.getData());
        // trafficTable.setModel(trafficTableModel);
        trafficTableModel.fireTableDataChanged();

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
        String[] colName = { "Traffic Light", "Status", "Time"};
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

        setLayout(new GridLayout(3,0));
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

        setTitle("Simulation");
        // setLayout(null);// using no layout managers
        setVisible(true);// making the frame visible
    }

    public void actionPerformed(ActionEvent e) {
        if (r1.isSelected()) {
            addEntry(Main.add_new_car(0,"South", "East"));
        } else if (r2.isSelected()) {
            addEntry(Main.add_new_car(1,"West", "South"));
        } else if (r3.isSelected()) {
            addEntry(Main.add_new_car(2,"East", "West"));
        } else {
            JOptionPane.showMessageDialog(this, "Please Select a option to add a car.");
        }
    }

}