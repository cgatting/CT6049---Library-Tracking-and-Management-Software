package com.library.gui;

import com.library.controller.LibraryController;
import com.library.model.Fine;
import com.library.model.Student;
import com.library.util.DataSyncListener;
import com.library.util.DataSyncManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Fine management panel that delegates logic to the controller layer.
 */
public class FinePaymentPanel extends JPanel implements DataSyncListener {

    private final LibraryController controller;

    private JComboBox<Student> studentComboBox;
    private JTable finesTable;
    private DefaultTableModel finesModel;
    private JTextArea statusArea;
    private JTextField fineAmountField;
    private JButton paySelectedButton;
    private JButton payAllButton;
    private JButton viewHistoryButton;
    private JButton refreshButton;

    public FinePaymentPanel(LibraryController controller) {
        this.controller = controller;
        DataSyncManager.getInstance().addListener(this);

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadStudents();
        loadFines();
    }

    private void initializeComponents() {
        studentComboBox = new JComboBox<>();
        studentComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Student) {
                    Student student = (Student) value;
                    setText(student.getName() + " (ID: " + student.getStudentId() + ")");
                }
                return this;
            }
        });

        String[] columns = {"Fine ID", "Amount", "Fine Date", "Status", "Loan", "Book"};
        finesModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        finesTable = new JTable(finesModel);
        finesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        finesTable.getTableHeader().setReorderingAllowed(false);

        statusArea = new JTextArea(8, 40);
        statusArea.setEditable(false);
        statusArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        fineAmountField = new JTextField(10);
        paySelectedButton = new JButton("Pay Selected Fine");
        payAllButton = new JButton("Pay All Fines");
        viewHistoryButton = new JButton("View Full History");
        refreshButton = new JButton("Refresh");
    }

    private void setupLayout() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Select Student"));
        selectionPanel.add(new JLabel("Student:"));
        selectionPanel.add(studentComboBox);
        selectionPanel.add(refreshButton);

        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        paymentPanel.setBorder(BorderFactory.createTitledBorder("Payment"));
        paymentPanel.add(new JLabel("Amount (optional override):"));
        paymentPanel.add(fineAmountField);
        paymentPanel.add(paySelectedButton);
        paymentPanel.add(payAllButton);
        paymentPanel.add(viewHistoryButton);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createTitledBorder("Status"));
        statusPanel.add(new JScrollPane(statusArea), BorderLayout.CENTER);

        add(selectionPanel, BorderLayout.NORTH);
        add(new JScrollPane(finesTable), BorderLayout.CENTER);
        add(paymentPanel, BorderLayout.SOUTH);
        add(statusPanel, BorderLayout.EAST);
    }

    private void setupEventHandlers() {
        refreshButton.addActionListener(e -> loadFines());
        studentComboBox.addActionListener(e -> loadFines());
        paySelectedButton.addActionListener(e -> paySelectedFine());
        payAllButton.addActionListener(e -> payAllFines());
        viewHistoryButton.addActionListener(e -> showFineHistory());
    }

    private void loadStudents() {
        try {
            List<Student> students = controller.getAllStudents();
            studentComboBox.removeAllItems();
            for (Student student : students) {
                studentComboBox.addItem(student);
            }
        } catch (Exception e) {
            statusArea.append("Error loading students: " + e.getMessage() + "\n");
        }
    }

    private void loadFines() {
        Student selected = (Student) studentComboBox.getSelectedItem();
        if (selected == null) {
            finesModel.setRowCount(0);
            return;
        }
        try {
            List<Fine> fines = controller.getUnpaidFinesByStudent(selected.getStudentId());
            finesModel.setRowCount(0);
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            for (Fine fine : fines) {
                Object[] row = {
                    fine.getFineId(),
                    String.format("$%.2f", fine.getFineAmount()),
                    fine.getFineDate() != null ? formatter.format(fine.getFineDate()) : "",
                    fine.getPaymentStatus(),
                    fine.getLoanId(),
                    fine.getBookTitle() != null ? fine.getBookTitle() : ""
                };
                finesModel.addRow(row);
            }
            statusArea.append(String.format("Loaded %d unpaid fines for %s.%n",
                fines.size(), selected.getName()));
        } catch (Exception e) {
            statusArea.append("Error loading fines: " + e.getMessage() + "\n");
        }
    }

    private void paySelectedFine() {
        Student student = (Student) studentComboBox.getSelectedItem();
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Select a student first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int row = finesTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a fine to pay.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int fineId = (Integer) finesModel.getValueAt(row, 0);
        try {
            Fine fine = controller.payFine(fineId);
            loadFines();
            statusArea.append(String.format("Paid fine #%d amount $%.2f for %s.%n",
                fineId,
                fine != null ? fine.getFineAmount() : BigDecimal.ZERO,
                student.getName()));
            JOptionPane.showMessageDialog(this, "Fine paid successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            statusArea.append("Error paying fine: " + e.getMessage() + "\n");
            JOptionPane.showMessageDialog(this, "Error paying fine: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void payAllFines() {
        Student student = (Student) studentComboBox.getSelectedItem();
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Select a student first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            BigDecimal total = controller.payAllFines(student.getStudentId());
            loadFines();
            statusArea.append(String.format("Paid all fines for %s (Total $%.2f).%n", student.getName(), total));
            JOptionPane.showMessageDialog(this,
                String.format("All fines paid. Total: $%.2f", total),
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            statusArea.append("Error paying fines: " + e.getMessage() + "\n");
            JOptionPane.showMessageDialog(this, "Error paying fines: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showFineHistory() {
        Student student = (Student) studentComboBox.getSelectedItem();
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Select a student first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            List<Fine> fines = controller.getFineHistory(student.getStudentId());
            if (fines.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No fine history for this student.", "Fine History", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            StringBuilder builder = new StringBuilder();
            builder.append("Fine History for ").append(student.getName()).append("\n\n");
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            for (Fine fine : fines) {
                builder.append(String.format("#%d - $%.2f on %s (%s)%n",
                    fine.getFineId(),
                    fine.getFineAmount(),
                    fine.getFineDate() != null ? formatter.format(fine.getFineDate()) : "",
                    fine.getPaymentStatus()));
            }
            JTextArea area = new JTextArea(builder.toString(), 15, 50);
            area.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Fine History", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            statusArea.append("Error loading fine history: " + e.getMessage() + "\n");
        }
    }

    // -----------------------------------------------------------------
    // DataSyncListener implementation
    // -----------------------------------------------------------------

    public void onStudentDataChanged() {
        SwingUtilities.invokeLater(() -> {
            loadStudents();
            loadFines();
        });
    }

    public void onBookDataChanged() {
        // Not required
    }

    public void onLoanDataChanged() {
        SwingUtilities.invokeLater(this::loadFines);
    }

    public void onFineDataChanged() {
        SwingUtilities.invokeLater(this::loadFines);
    }

    public void onAllDataChanged() {
        SwingUtilities.invokeLater(() -> {
            loadStudents();
            loadFines();
        });
    }
}