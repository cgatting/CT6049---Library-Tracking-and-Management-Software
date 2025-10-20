package com.library.gui;

import com.library.controller.LibraryController;
import com.library.model.Student;
import com.library.util.DataSyncListener;
import com.library.util.DataSyncManager;

import javax.swing.*;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Student management UI, delegating logic to {@link LibraryController}.
 */
public class UserManagementPanel extends JPanel implements DataSyncListener {

    private final LibraryController controller;

    private JTable studentTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    private JTextField studentIdField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField;
    private JTextField searchField;

    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton searchButton;
    private JButton refreshButton;

    private int selectedStudentId = -1;

    public UserManagementPanel(LibraryController controller) {
        this.controller = controller;
        DataSyncManager.getInstance().addListener(this);

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadStudentData();
    }

    private void initializeComponents() {
        String[] columns = {"ID", "Name", "Email", "Phone", "Address", "Registered"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.getTableHeader().setReorderingAllowed(false);
        sorter = new TableRowSorter<>(tableModel);
        studentTable.setRowSorter(sorter);

        studentIdField = new JTextField(8);
        studentIdField.setEditable(false);
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(15);
        addressField = new JTextField(25);
        searchField = new JTextField(20);

        addButton = new JButton("Add Student");
        updateButton = new JButton("Update Student");
        deleteButton = new JButton("Delete Student");
        clearButton = new JButton("Clear Form");
        searchButton = new JButton("Search");
        refreshButton = new JButton("Refresh");

        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(createSearchPanel(), BorderLayout.NORTH);
        add(new JScrollPane(studentTable), BorderLayout.CENTER);
        add(createFormPanel(), BorderLayout.SOUTH);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Search Students"));
        panel.add(new JLabel("Keyword:"));
        panel.add(searchField);
        panel.add(searchButton);
        panel.add(refreshButton);
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Student Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        panel.add(studentIdField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        panel.add(addressField, gbc);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private void setupEventHandlers() {
        refreshButton.addActionListener(e -> loadStudentData());
        searchButton.addActionListener(e -> applySearchFilter());
        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        clearButton.addActionListener(e -> clearForm());

        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                populateFormFromSelection();
            }
        });
    }

    private void loadStudentData() {
        try {
            List<Student> students = controller.getAllStudents();
            tableModel.setRowCount(0);
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            for (Student student : students) {
                Object[] row = {
                    student.getStudentId(),
                    nonNull(student.getName()),
                    nonNull(student.getEmail()),
                    nonNull(student.getPhone()),
                    nonNull(student.getAddress()),
                    student.getRegistrationDate() != null ? formatter.format(student.getRegistrationDate()) : ""
                };
                tableModel.addRow(row);
            }
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading students: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addStudent() {
        try {
            Student student = buildStudentFromForm();
            controller.addStudent(student);
            loadStudentData();
            JOptionPane.showMessageDialog(this, "Student added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error adding student: " + e.getMessage(),
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStudent() {
        if (selectedStudentId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to update.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Student student = buildStudentFromForm();
            student.setStudentId(selectedStudentId);
            controller.updateStudent(student);
            loadStudentData();
            JOptionPane.showMessageDialog(this, "Student updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error updating student: " + e.getMessage(),
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent() {
        if (selectedStudentId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete this student? This action cannot be undone.",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            controller.deleteStudent(selectedStudentId);
            loadStudentData();
            JOptionPane.showMessageDialog(this, "Student deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error deleting student: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private Student buildStudentFromForm() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (email.isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("Valid email is required");
        }
        Student student = new Student();
        student.setName(name);
        student.setEmail(email);
        student.setPhone(trimToNull(phoneField.getText()));
        student.setAddress(trimToNull(addressField.getText()));
        return student;
    }

    private void populateFormFromSelection() {
        int viewRow = studentTable.getSelectedRow();
        if (viewRow == -1) {
            clearForm();
            return;
        }
        int modelRow = studentTable.convertRowIndexToModel(viewRow);
        selectedStudentId = (Integer) tableModel.getValueAt(modelRow, 0);
        studentIdField.setText(String.valueOf(selectedStudentId));
        nameField.setText((String) tableModel.getValueAt(modelRow, 1));
        emailField.setText((String) tableModel.getValueAt(modelRow, 2));
        phoneField.setText((String) tableModel.getValueAt(modelRow, 3));
        addressField.setText((String) tableModel.getValueAt(modelRow, 4));
        updateButton.setEnabled(true);
        deleteButton.setEnabled(true);
    }

    private void applySearchFilter() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
        }
    }

    private void clearForm() {
        selectedStudentId = -1;
        studentIdField.setText("");
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        studentTable.clearSelection();
    }

    private String nonNull(String value) {
        return value == null ? "" : value;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    // -----------------------------------------------------------------
    // DataSyncListener implementation
    // -----------------------------------------------------------------

    public void onStudentDataChanged() {
        SwingUtilities.invokeLater(this::loadStudentData);
    }

    public void onBookDataChanged() {
        // Not required
    }

    public void onLoanDataChanged() {
        // Not required
    }

    public void onFineDataChanged() {
        // Not required
    }

    public void onAllDataChanged() {
        SwingUtilities.invokeLater(this::loadStudentData);
    }
}