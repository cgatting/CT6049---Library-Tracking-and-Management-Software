package com.library.gui;

import com.library.controller.LibraryController;
import com.library.model.Book;
import com.library.model.Fine;
import com.library.model.Loan;
import com.library.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Reporting panel showing student loan/fine history and high level system stats.
 */
public class ReportsPanel extends JPanel {

    private final LibraryController controller;

    private JComboBox<Student> studentComboBox;
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> yearComboBox;
    private JTextArea studentReportArea;
    private JTable statsTable;

    public ReportsPanel(LibraryController controller) {
        this.controller = controller;
        initializeComponents();
        setupLayout();
        loadStudents();
        refreshStatistics();
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

        monthComboBox = new JComboBox<>(new String[] {
            "All Months", "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        });
        yearComboBox = new JComboBox<>();
        int currentYear = LocalDate.now().getYear();
        yearComboBox.addItem(0);
        for (int year = currentYear - 5; year <= currentYear + 1; year++) {
            yearComboBox.addItem(year);
        }
        yearComboBox.setSelectedItem(currentYear);

        studentReportArea = new JTextArea(18, 70);
        studentReportArea.setEditable(false);
        studentReportArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        statsTable = new JTable(new DefaultTableModel(new Object[] {"Metric", "Value", "Description"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Student Reports", createStudentReportPanel());
        tabs.addTab("System Overview", createSystemPanel());
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel createStudentReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel filterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        filterPanel.add(new JLabel("Student:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        filterPanel.add(studentComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        filterPanel.add(new JLabel("Month:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        filterPanel.add(monthComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        filterPanel.add(new JLabel("Year:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        filterPanel.add(yearComboBox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton loanHistoryButton = new JButton("Loan History");
        loanHistoryButton.addActionListener(e -> generateLoanHistory());
        JButton fineHistoryButton = new JButton("Fine History");
        fineHistoryButton.addActionListener(e -> generateFineHistory());
        JButton exportButton = new JButton("Export to CSV");
        exportButton.addActionListener(e -> exportStudentReport());
        buttonPanel.add(loanHistoryButton);
        buttonPanel.add(fineHistoryButton);
        buttonPanel.add(exportButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(filterPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(studentReportArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSystemPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton refreshButton = new JButton("Refresh Overview");
        refreshButton.addActionListener(e -> refreshStatistics());

        panel.add(new JScrollPane(statsTable), BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);
        return panel;
    }

    private void loadStudents() {
        try {
            studentComboBox.removeAllItems();
            List<Student> students = controller.getAllStudents();
            for (Student student : students) {
                studentComboBox.addItem(student);
            }
        } catch (Exception e) {
            showError("Error loading students", e);
        }
    }

    private void generateLoanHistory() {
        Student student = (Student) studentComboBox.getSelectedItem();
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Select a student first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Integer month = monthComboBox.getSelectedIndex();
        if (month != null && month == 0) {
            month = null;
        }
        Integer year = (Integer) yearComboBox.getSelectedItem();
        if (year != null && year == 0) {
            year = null;
        }
        try {
            List<Loan> loans = controller.getLoanHistory(student.getStudentId(), month, year);
            StringBuilder report = new StringBuilder();
            report.append(String.format("Loan History for %s (ID %d)\n", student.getName(), student.getStudentId()));
            report.append(String.format("Period: %s %s\n\n", monthComboBox.getSelectedItem(), year == null ? "All Years" : year));
            if (loans.isEmpty()) {
                report.append("No loans recorded for the selected period.\n");
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
                report.append(String.format("%-12s %-30s %-10s %-12s%n", "Loan Date", "Book", "Status", "Due Date"));
                report.append(repeat('-', 70)).append('\n');
                int overdue = 0;
                for (Loan loan : loans) {
                    LocalDate due = loan.getDueDate();
                    String status = loan.getStatus();
                    if ("OVERDUE".equalsIgnoreCase(status)) {
                        overdue++;
                    }
                    report.append(String.format("%-12s %-30s %-10s %-12s%n",
                        loan.getLoanDate() != null ? formatter.format(loan.getLoanDate()) : "",
                        truncate(loan.getBookTitle(), 30),
                        status != null ? status : "",
                        due != null ? formatter.format(due) : ""));
                }
                report.append("\nTotal loans: ").append(loans.size()).append(", Overdue: ").append(overdue).append('\n');
            }
            studentReportArea.setText(report.toString());
        } catch (Exception e) {
            showError("Error generating loan history", e);
        }
    }

    private void generateFineHistory() {
        Student student = (Student) studentComboBox.getSelectedItem();
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Select a student first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            List<Fine> fines = controller.getFineHistory(student.getStudentId());
            StringBuilder report = new StringBuilder();
            report.append(String.format("Fine History for %s (ID %d)\n", student.getName(), student.getStudentId()));
            report.append(String.format("Period: %s %s\n\n", monthComboBox.getSelectedItem(),
                yearComboBox.getSelectedItem() == null || (Integer) yearComboBox.getSelectedItem() == 0 ? "All Years" : yearComboBox.getSelectedItem()));
            if (fines.isEmpty()) {
                report.append("No fines recorded for this student.\n");
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
                BigDecimal totalPaid = BigDecimal.ZERO;
                BigDecimal totalUnpaid = BigDecimal.ZERO;
                report.append(String.format("%-12s %-30s %-10s %-10s%n", "Fine Date", "Book", "Status", "Amount"));
                report.append(repeat('-', 70)).append('\n');
                for (Fine fine : fines) {
                    String status = fine.getPaymentStatus();
                    if ("PAID".equalsIgnoreCase(status)) {
                        totalPaid = totalPaid.add(fine.getFineAmount());
                    } else {
                        totalUnpaid = totalUnpaid.add(fine.getFineAmount());
                    }
                    report.append(String.format("%-12s %-30s %-10s $%-10.2f%n",
                        fine.getFineDate() != null ? formatter.format(fine.getFineDate()) : "",
                        truncate(fine.getBookTitle(), 30),
                        status,
                        fine.getFineAmount()));
                }
                report.append("\nTotal paid: $" + totalPaid + ", Outstanding: $" + totalUnpaid + "\n");
            }
            studentReportArea.setText(report.toString());
        } catch (Exception e) {
            showError("Error generating fine history", e);
        }
    }

    private void exportStudentReport() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Export Student Report");
        chooser.setSelectedFile(new java.io.File("student_report.csv"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (java.io.FileWriter writer = new java.io.FileWriter(chooser.getSelectedFile())) {
                writer.write(studentReportArea.getText());
                JOptionPane.showMessageDialog(this, "Report exported successfully.", "Export", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                showError("Error exporting report", e);
            }
        }
    }

    private void refreshStatistics() {
        DefaultTableModel model = (DefaultTableModel) statsTable.getModel();
        model.setRowCount(0);
        try {
            List<Student> students = controller.getAllStudents();
            List<Book> books = controller.getAllBooks();
            List<Loan> activeLoans = controller.getActiveLoans();
            List<Loan> overdueLoans = controller.getOverdueLoans();

            model.addRow(new Object[]{"Registered Students", students.size(), "Total number of students"});
            model.addRow(new Object[]{"Books in Catalogue", books.size(), "Total number of books"});
            long availableBooks = books.stream().filter(book -> book.getAvailableCopies() > 0).count();
            model.addRow(new Object[]{"Books Available", availableBooks, "Inventory currently on shelves"});
            model.addRow(new Object[]{"Active Loans", activeLoans.size(), "Books currently on loan"});
            model.addRow(new Object[]{"Overdue Loans", overdueLoans.size(), "Loans past their due date"});
        } catch (Exception e) {
            showError("Error refreshing statistics", e);
        }
    }

    private void showError(String title, Exception e) {
        JOptionPane.showMessageDialog(this,
            title + ": " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    private String truncate(String value, int length) {
        if (value == null) {
            return "";
        }
        return value.length() <= length ? value : value.substring(0, length - 3) + "...";
    }

    private String repeat(char c, int count) {
        StringBuilder builder = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            builder.append(c);
        }
        return builder.toString();
    }
}