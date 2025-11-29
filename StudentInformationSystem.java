import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

class Student implements Serializable {
    int id;
    String name;
    String course;
    int age;

    public Student(int id, String name, String course, int age) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.age = age;
    }
}

public class StudentInformationSystem extends JFrame {

    private JTextField txtName, txtCourse, txtAge, txtId;
    private DefaultTableModel model;
    private JTable table;

    ArrayList<Student> students = new ArrayList<>();
    int autoID = 1;

    public StudentInformationSystem() {

        setTitle("Student Information System");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        loadDataFromFile();  // LOAD SAVED DATA

        // ---------- Top Panel ----------
        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        form.add(new JLabel("Student ID:"));
        txtId = new JTextField();
        txtId.setEnabled(false);
        form.add(txtId);

        form.add(new JLabel("Student Name:"));
        txtName = new JTextField();
        form.add(txtName);

        form.add(new JLabel("Course:"));
        txtCourse = new JTextField();
        form.add(txtCourse);

        form.add(new JLabel("Age:"));
        txtAge = new JTextField();
        form.add(txtAge);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        form.add(btnPanel);
        add(form, BorderLayout.WEST);

        // ---------- Table ----------
        model = new DefaultTableModel(new Object[]{"ID", "Name", "Course", "Age"}, 0);
        table = new JTable(model);

        loadTableData(); // LOAD INTO TABLE

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ---------- BUTTON ACTIONS ----------

        btnAdd.addActionListener(e -> {
            if (txtName.getText().isEmpty() ||
                txtCourse.getText().isEmpty() ||
                txtAge.getText().isEmpty()) {

                showMessage("Please fill all fields!");
                return;
            }

            try {
                int age = Integer.parseInt(txtAge.getText().trim());
                Student s = new Student(autoID, txtName.getText(), txtCourse.getText(), age);
                students.add(s);

                model.addRow(new Object[]{autoID, s.name, s.course, s.age});
                autoID++;

                clearFields();
                saveDataToFile();  // SAVE AFTER ADD
                showMessage("Student Added Successfully!");

            } catch (Exception ex) {
                showMessage("Invalid age!");
            }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = table.getSelectedRow();
                txtId.setText(model.getValueAt(i, 0).toString());
                txtName.setText(model.getValueAt(i, 1).toString());
                txtCourse.setText(model.getValueAt(i, 2).toString());
                txtAge.setText(model.getValueAt(i, 3).toString());
            }
        });

        btnUpdate.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected == -1) {
                showMessage("Select a student to update!");
                return;
            }

            try {
                int id = Integer.parseInt(txtId.getText());
                String name = txtName.getText();
                String course = txtCourse.getText();
                int age = Integer.parseInt(txtAge.getText());

                model.setValueAt(name, selected, 1);
                model.setValueAt(course, selected, 2);
                model.setValueAt(age, selected, 3);

                for (Student s : students) {
                    if (s.id == id) {
                        s.name = name;
                        s.course = course;
                        s.age = age;
                    }
                }

                saveDataToFile(); // SAVE AFTER UPDATE
                clearFields();
                showMessage("Updated Successfully!");

            } catch (Exception ex) {
                showMessage("Invalid input!");
            }
        });

        btnDelete.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected == -1) {
                showMessage("Select a student to delete!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(null,
                    "Are you sure?", "Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {

                int id = Integer.parseInt(model.getValueAt(selected, 0).toString());
                students.removeIf(s -> s.id == id);
                model.removeRow(selected);

                saveDataToFile(); // SAVE AFTER DELETE
                clearFields();
                showMessage("Deleted Successfully!");
            }
        });

        btnClear.addActionListener(e -> clearFields());
    }

    // ------------------------------------------------------------
    //           SAVE & LOAD DATA USING FILE
    // ------------------------------------------------------------

    void saveDataToFile() {
        try {
            FileOutputStream fos = new FileOutputStream("students.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(students);
            oos.writeInt(autoID);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadDataFromFile() {
        try {
            File file = new File("students.dat");
            if (!file.exists()) return;

            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            students = (ArrayList<Student>) ois.readObject();
            autoID = ois.readInt();
            ois.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadTableData() {
        for (Student s : students) {
            model.addRow(new Object[]{s.id, s.name, s.course, s.age});
        }
    }

    void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtCourse.setText("");
        txtAge.setText("");
    }

    void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        new StudentInformationSystem().setVisible(true);
    }
}
