// Inventory System with Permanent File Storage
// Data persists even after closing VS Code

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

class Item {
    int id;
    String name;
    int qty;
    double price;
    String category;

    public Item(int id, String name, int qty, double price, String category) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.price = price;
        this.category = category;
    }
}

public class InventorySystem extends JFrame {

    ArrayList<Item> items = new ArrayList<>();

    JTextField txtId, txtName, txtQty, txtPrice, txtSearch;
    JComboBox<String> cbCategory;
    DefaultTableModel model;
    JTable table;

    File saveFile = new File("inventory_data.txt");

    public InventorySystem() {

        setTitle("Inventory Management System");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 240));

        // ----- HEADER -----
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 150, 243));
        JLabel header = new JLabel("Inventory Management System");
        header.setFont(new Font("SansSerif", Font.BOLD, 26));
        header.setForeground(Color.WHITE);
        headerPanel.add(header);
        add(headerPanel, BorderLayout.NORTH);

        // ----- LEFT PANEL -----
        JPanel form = new JPanel(new GridLayout(6, 2, 12, 12));
        form.setBorder(BorderFactory.createTitledBorder("Item Details"));
        form.setBackground(Color.WHITE);
        Font inputFont = new Font("SansSerif", Font.PLAIN, 16);

        txtId = new JTextField(); txtId.setFont(inputFont);
        txtName = new JTextField(); txtName.setFont(inputFont);
        txtQty = new JTextField(); txtQty.setFont(inputFont);
        txtPrice = new JTextField(); txtPrice.setFont(inputFont);

        cbCategory = new JComboBox<>(new String[]{"Electronics", "Clothing", "Food", "Furniture", "Misc"});
        cbCategory.setFont(inputFont);

        form.add(new JLabel("ID:")); form.add(txtId);
        form.add(new JLabel("Name:")); form.add(txtName);
        form.add(new JLabel("Quantity:")); form.add(txtQty);
        form.add(new JLabel("Price:")); form.add(txtPrice);
        form.add(new JLabel("Category:")); form.add(cbCategory);

        add(form, BorderLayout.WEST);

        // ----- SEARCH BAR -----
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblSearch = new JLabel("Search:");
        txtSearch = new JTextField(25);
        txtSearch.setFont(inputFont);
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        add(searchPanel, BorderLayout.NORTH);

        // ----- TABLE -----
        String[] columns = {"ID", "Name", "Qty", "Price", "Category"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 15));
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // ----- BUTTON PANEL -----
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 15, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        styleButton(btnAdd);
        styleButton(btnUpdate);
        styleButton(btnDelete);
        styleButton(btnClear);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        add(buttonPanel, BorderLayout.SOUTH);

        // ----- TABLE CLICK LOAD -----
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                txtId.setText(model.getValueAt(row, 0).toString());
                txtName.setText(model.getValueAt(row, 1).toString());
                txtQty.setText(model.getValueAt(row, 2).toString());
                txtPrice.setText(model.getValueAt(row, 3).toString());
                cbCategory.setSelectedItem(model.getValueAt(row, 4).toString());
            }
        });

        // ----- SEARCH FUNCTION -----
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                search(txtSearch.getText());
            }
        });

        // ----- ACTIONS -----
        btnAdd.addActionListener(e -> addItem());
        btnUpdate.addActionListener(e -> updateItem());
        btnDelete.addActionListener(e -> deleteItem());
        btnClear.addActionListener(e -> clearFields());

        // LOAD SAVED DATA
        loadData();
        refreshTable();

        setVisible(true);
    }

    // BUTTON STYLING
    void styleButton(JButton btn) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setBackground(new Color(33, 150, 243));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // SEARCH
    void search(String query) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
    }

    // ADD
    void addItem() {
        try {
            int id = Integer.parseInt(txtId.getText());
            String name = txtName.getText();
            int qty = Integer.parseInt(txtQty.getText());
            double price = Double.parseDouble(txtPrice.getText());
            String category = cbCategory.getSelectedItem().toString();

            items.add(new Item(id, name, qty, price, category));
            refreshTable();
            saveData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Item Added Successfully");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid Input!");
        }
    }

    // UPDATE
    void updateItem() {
        try {
            int id = Integer.parseInt(txtId.getText());
            for (Item item : items) {
                if (item.id == id) {
                    item.name = txtName.getText();
                    item.qty = Integer.parseInt(txtQty.getText());
                    item.price = Double.parseDouble(txtPrice.getText());
                    item.category = cbCategory.getSelectedItem().toString();

                    refreshTable();
                    saveData();
                    JOptionPane.showMessageDialog(this, "Item Updated Successfully");
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Item Not Found!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid Input!");
        }
    }

    // DELETE
    void deleteItem() {
        try {
            int id = Integer.parseInt(txtId.getText());

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this item?",
                    "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            items.removeIf(i -> i.id == id);
            refreshTable();
            saveData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Item Deleted");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid Input!");
        }
    }

    // REFRESH TABLE
    void refreshTable() {
        model.setRowCount(0);
        for (Item item : items) {
            model.addRow(new Object[]{item.id, item.name, item.qty, item.price, item.category});
        }
    }

    // CLEAR FIELDS
    void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtQty.setText("");
        txtPrice.setText("");
        cbCategory.setSelectedIndex(0);
        table.clearSelection();
    }

    // -------------------------------
    //         FILE SAVE / LOAD
    // -------------------------------

    void saveData() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(saveFile))) {
            for (Item i : items) {
                pw.println(i.id + "," + i.name + "," + i.qty + "," + i.price + "," + i.category);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving data!");
        }
    }

    void loadData() {
        if (!saveFile.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(saveFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                int id = Integer.parseInt(p[0]);
                String name = p[1];
                int qty = Integer.parseInt(p[2]);
                double price = Double.parseDouble(p[3]);
                String category = p[4];

                items.add(new Item(id, name, qty, price, category));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading saved data!");
        }
    }

    public static void main(String[] args) {
        new InventorySystem();
    }
}
