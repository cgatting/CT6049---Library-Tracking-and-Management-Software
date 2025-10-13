package com.library.gui;

import com.library.controller.LibraryController;
import com.library.controller.LibraryController.ReturnResult;
import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.Student;
import com.library.util.DataSyncListener;
import com.library.util.DataSyncManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel for borrowing and returning books (presentation layer).
 */
public class BorrowReturnPanel extends JPanel implements DataSyncListener {

    private final LibraryController controller;

    private JComboBox<Student> studentComboBox;
    private JComboBox<Book> bookComboBox;
    private JTextField studentSearchField;
    private JTextField bookSearchField;
    private JButton borrowButton;
    private JButton returnButton;
    private JButton refreshButton;
    private JButton viewAllLoansButton;
    private JButton searchStudentButton;
    private JButton viewLoanHistoryButton;
    private JButton renewLoanButton;
    private JTextArea statusArea;
    private JTable activeLoansTable;
    private DefaultTableModel activeLoansTableModel;

    private List<Loan> displayedLoans = new ArrayList<>();

    public BorrowReturnPanel(LibraryController controller) {
        this.controller = controller;
        DataSyncManager.getInstance().addListener(this);

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadData();
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

        bookComboBox = new JComboBox<>();
        bookComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Book) {
                    Book book = (Book) value;
                    setText(book.getTitle() + " by " + book.getAuthor() + " (Available: " + book.getAvailableCopies() + ")");
                }
                return this;
            }
        });

        studentSearchField = new JTextField(20);
        bookSearchField = new JTextField(20);

        borrowButton = new JButton("Borrow Book");
        returnButton = new JButton("Return Book");
        refreshButton = new JButton("Refresh Data");
        viewAllLoansButton = new JButton("View All Books on Loan");
        searchStudentButton = new JButton("Search Student");
        viewLoanHistoryButton = new JButton("View Loan History");
        renewLoanButton = new JButton("Renew Loan");

        statusArea = new JTextArea(8, 40);
        statusArea.setEditable(false);
        statusArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        statusArea.setText("Welcome to the Enhanced Library System!\nSelect a student and book to borrow or return.\n");

        String[] columnNames = {"Student", "Book Title", "Author", "Loan Date", "Due Date", "Status", "Days Left"};
        activeLoansTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        activeLoansTable = new JTable(activeLoansTableModel);
        activeLoansTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        activeLoansTable.getTableHeader().setReorderingAllowed(false);
        activeLoansTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(createSearchPanel(), BorderLayout.NORTH);
        topPanel.add(createSelectionPanel(), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(borrowButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(renewLoanButton);
        buttonPanel.add(viewLoanHistoryButton);
        buttonPanel.add(viewAllLoansButton);
        buttonPanel.add(refreshButton);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Active Loans"));
        tablePanel.add(new JScrollPane(activeLoansTable), BorderLayout.CENTER);
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createTitledBorder("Status"));
        statusPanel.add(new JScrollPane(statusArea), BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Quick Search"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(new JLabel("Search Student:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        searchPanel.add(studentSearchField, gbc);
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        searchPanel.add(searchStudentButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        searchPanel.add(new JLabel("Search Book:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        searchPanel.add(bookSearchField, gbc);

        return searchPanel;
    }

    private JPanel createSelectionPanel() {
        JPanel selectionPanel = new JPanel(new GridBagLayout());
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Selection"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        selectionPanel.add(new JLabel("Student:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        selectionPanel.add(studentComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        selectionPanel.add(new JLabel("Book:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        selectionPanel.add(bookComboBox, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        selectionPanel.add(new JLabel(""), gbc); // placeholder for alignment

        return selectionPanel;
    }

    private void setupEventHandlers() {
        borrowButton.addActionListener(e -> borrowBook());
        returnButton.addActionListener(e -> returnBook());
        refreshButton.addActionListener(e -> loadData());
        viewAllLoansButton.addActionListener(e -> viewAllBooksOnLoan());
        searchStudentButton.addActionListener(e -> searchStudents());
        viewLoanHistoryButton.addActionListener(e -> viewLoanHistory());
        renewLoanButton.addActionListener(e -> renewLoan());

        studentSearchField.addActionListener(e -> searchStudents());
        bookSearchField.addActionListener(e -> searchBooks());

        activeLoansTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
            }
        });
    }

    private void loadData() {
        try {
            loadStudents();
            loadBooks();
            loadActiveLoansTable();
            statusArea.append("Data refreshed successfully.\n\n");
        } catch (Exception e) {
            statusArea.append("Error loading data: " + e.getMessage() + "\n");
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadStudents() throws Exception {
        List<Student> students = controller.getAllStudents();
        studentComboBox.removeAllItems();
        for (Student student : students) {
            studentComboBox.addItem(student);
        }
    }

    private void loadBooks() throws Exception {
        List<Book> books = controller.getAvailableBooks();
        bookComboBox.removeAllItems();
        for (Book book : books) {
            bookComboBox.addItem(book);
        }
    }

    private void loadActiveLoansTable() throws Exception {
        displayedLoans = controller.getAllActiveLoans();
        activeLoansTableModel.setRowCount(0);

        for (Loan loan : displayedLoans) {
            String status = loan.getStatus();
            long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), loan.getDueDate());
            String daysLeftStr = daysLeft >= 0 ? daysLeft + " days" : Math.abs(daysLeft) + " days overdue";
            Object[] row = {
                loan.getStudentName(),
                loan.getBookTitle(),
                loan.getBookAuthor(),
                loan.getLoanDate() != null ? loan.getLoanDate().toString() : "",
                loan.getDueDate() != null ? loan.getDueDate().toString() : "",
                status,
                daysLeftStr
            };
            activeLoansTableModel.addRow(row);
        }

        activeLoansTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String currentStatus = (String) table.getValueAt(row, 5);
                    if (currentStatus != null && currentStatus.startsWith("OVERDUE")) {
                        c.setBackground(new Color(255, 230, 230));
                    } else {
                        String left = (String) table.getValueAt(row, 6);
                        if (left != null && (left.contains("1 days") || left.contains("2 days"))) {
                            c.setBackground(new Color(255, 255, 200));
                        } else {
                            c.setBackground(Color.WHITE);
                        }
                    }
                }
                return c;
            }
        });
        updateButtonStates();
    }

    private void borrowBook() {
        Student student = (Student) studentComboBox.getSelectedItem();
        Book book = (Book) bookComboBox.getSelectedItem();
        if (student == null || book == null) {
            JOptionPane.showMessageDialog(this, "Please select both a student and a book.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Loan loan = controller.borrowBook(student.getStudentId(), book.getBookId());
            statusArea.append(String.format("BORROWED: %s borrowed \"%s\" (Due: %s)%n",
                student.getName(), book.getTitle(), loan.getDueDate()));
            loadData();
            JOptionPane.showMessageDialog(this,
                "Book borrowed successfully!\nDue date: " + loan.getDueDate(),
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            statusArea.append("Error borrowing book: " + e.getMessage() + "\n");
            JOptionPane.showMessageDialog(this,
                "Error borrowing book: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void returnBook() {
        Student student = (Student) studentComboBox.getSelectedItem();
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Please select a student.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            List<Loan> studentLoans = controller.getLoansByStudent(student.getStudentId());
            if (studentLoans.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No active loans found for this student.", "No Active Loans", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Loan selectedLoan = (Loan) JOptionPane.showInputDialog(
                this,
                "Select the book to return:",
                "Return Book",
                JOptionPane.QUESTION_MESSAGE,
                null,
                studentLoans.toArray(),
                studentLoans.get(0));
            if (selectedLoan == null) {
                return;
            }
            ReturnResult result = controller.returnLoan(selectedLoan);
            statusArea.append(String.format("RETURNED: %s returned \"%s\"%n",
                student.getName(), selectedLoan.getBookTitle()));
            if (result.isFineCreated()) {
                statusArea.append(String.format("FINE CREATED: $%.2f for %d days overdue%n",
                    result.getFineAmount(), result.getDaysOverdue()));
                JOptionPane.showMessageDialog(this,
                    String.format("Book returned with a fine of $%.2f (%d days overdue).", result.getFineAmount(), result.getDaysOverdue()),
                    "Fine Created",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Book returned successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            loadData();
        } catch (Exception e) {
            statusArea.append("Error returning book: " + e.getMessage() + "\n");
            JOptionPane.showMessageDialog(this,
                "Error returning book: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewAllBooksOnLoan() {
        try {
            List<Loan> loans = controller.getAllActiveLoans();
            if (loans.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No books are currently on loan.", "Active Loans", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            StringBuilder builder = new StringBuilder();
            builder.append("Books Currently on Loan:\n\n");
            for (Loan loan : loans) {
                builder.append(String.format("%s -> %s (Due %s)%n",
                    loan.getStudentName(),
                    loan.getBookTitle(),
                    loan.getDueDate()));
            }
            JTextArea area = new JTextArea(builder.toString(), 20, 60);
            area.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Current Loans", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading active loans: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchStudents() {
        String term = studentSearchField.getText();
        try {
            List<Student> matches = controller.searchStudents(term);
            studentComboBox.removeAllItems();
            for (Student student : matches) {
                studentComboBox.addItem(student);
            }
            statusArea.append(String.format("Search completed for students: %s (%d matches)\n",
                term, matches.size()));
        } catch (Exception e) {
            statusArea.append("Error searching students: " + e.getMessage() + "\n");
        }
    }

    private void searchBooks() {
        String term = bookSearchField.getText();
        try {
            List<Book> matches = controller.searchBooks(term, true);
            bookComboBox.removeAllItems();
            for (Book book : matches) {
                bookComboBox.addItem(book);
            }
            statusArea.append(String.format("Search completed for books: %s (%d matches)\n",
                term, matches.size()));
        } catch (Exception e) {
            statusArea.append("Error searching books: " + e.getMessage() + "\n");
        }
    }

    private void viewLoanHistory() {
        Student student = (Student) studentComboBox.getSelectedItem();
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Please select a student.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String monthLabel = (String) JOptionPane.showInputDialog(this,
            "Select month (leave blank for all):",
            "Month",
            JOptionPane.QUESTION_MESSAGE,
            null,
            new String[]{"All", "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"},
            "All");
        Integer month = parseMonth(monthLabel);
        Integer year = null;
        String yearInput = JOptionPane.showInputDialog(this, "Enter year (blank for all):", "", JOptionPane.QUESTION_MESSAGE);
        if (yearInput != null && !yearInput.trim().isEmpty()) {
            try {
                year = Integer.parseInt(yearInput.trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid year entry.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        try {
            List<Loan> history = controller.getLoanHistory(student.getStudentId(), month, year);
            if (history.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No loan history found for the selected period.", "Loan History", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            showLoanHistoryDialog(student, history);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error retrieving loan history: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showLoanHistoryDialog(Student student, List<Loan> history) {
        String[] columnNames = {"Book", "Author", "Loan Date", "Due Date", "Return Date", "Status", "Fine"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (Loan loan : history) {
            Object[] row = {
                loan.getBookTitle(),
                loan.getBookAuthor(),
                valueOrBlank(loan.getLoanDate()),
                valueOrBlank(loan.getDueDate()),
                valueOrBlank(loan.getReturnDate()),
                loan.getStatus(),
                loan.getFineAmount() != null ? "$" + loan.getFineAmount() : ""
            };
            model.addRow(row);
        }
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getTableHeader().setReorderingAllowed(false);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            "Loan History - " + student.getName(), true);
        dialog.setLayout(new BorderLayout());
        dialog.add(new JScrollPane(table), BorderLayout.CENTER);
        JButton close = new JButton("Close");
        close.addActionListener(e -> dialog.dispose());
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(close);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setSize(900, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void renewLoan() {
        int row = activeLoansTable.getSelectedRow();
        if (row == -1 || row >= displayedLoans.size()) {
            JOptionPane.showMessageDialog(this, "Please select a loan from the table.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Loan loan = displayedLoans.get(row);
        if (loan.getStatus() != null && loan.getStatus().startsWith("OVERDUE")) {
            JOptionPane.showMessageDialog(this, "Cannot renew overdue loans. Please return the book first.", "Cannot Renew", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            LocalDate newDueDate = loan.getDueDate() != null ? loan.getDueDate().plusDays(14) : LocalDate.now().plusDays(14);
            controller.renewLoan(loan, newDueDate);
            statusArea.append(String.format("RENEWED: %s renewed \"%s\" (New due date: %s)%n",
                loan.getStudentName(), loan.getBookTitle(), newDueDate));
            loadActiveLoansTable();
            JOptionPane.showMessageDialog(this,
                "Loan renewed successfully!\nNew due date: " + newDueDate,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            statusArea.append("Error renewing loan: " + e.getMessage() + "\n");
            JOptionPane.showMessageDialog(this, "Error renewing loan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateButtonStates() {
        boolean hasSelection = activeLoansTable.getSelectedRow() != -1;
        renewLoanButton.setEnabled(hasSelection);
    }

    private String valueOrBlank(LocalDate date) {
        return date == null ? "" : date.toString();
    }

    private Integer parseMonth(String label) {
        if (label == null || "All".equalsIgnoreCase(label)) {
            return null;
        }
        String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
        for (int i = 0; i < months.length; i++) {
            if (months[i].equalsIgnoreCase(label)) {
                return i + 1;
            }
        }
        return null;
    }

    // -----------------------------------------------------------------
    // DataSyncListener callbacks
    // -----------------------------------------------------------------

    @Override
    public void onStudentDataChanged() {
        SwingUtilities.invokeLater(this::loadStudentsSafely);
    }

    @Override
    public void onBookDataChanged() {
        SwingUtilities.invokeLater(this::loadBooksSafely);
    }

    @Override
    public void onLoanDataChanged() {
        SwingUtilities.invokeLater(this::loadLoansSafely);
    }

    @Override
    public void onFineDataChanged() {
        // No direct UI update required here.
    }

    @Override
    public void onAllDataChanged() {
        SwingUtilities.invokeLater(this::loadData);
    }

    private void loadStudentsSafely() {
        try {
            loadStudents();
        } catch (Exception e) {
            statusArea.append("Error refreshing students: " + e.getMessage() + "\n");
        }
    }

    private void loadBooksSafely() {
        try {
            loadBooks();
        } catch (Exception e) {
            statusArea.append("Error refreshing books: " + e.getMessage() + "\n");
        }
    }

    private void loadLoansSafely() {
        try {
            loadActiveLoansTable();
        } catch (Exception e) {
            statusArea.append("Error refreshing loans: " + e.getMessage() + "\n");
        }
    }
}