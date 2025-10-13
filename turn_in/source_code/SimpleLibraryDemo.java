import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import com.formdev.flatlaf.FlatDarkLaf;

/**
 * Simple Library Database Demo Application
 * This is a simplified version that demonstrates the core functionality
 * without requiring compilation or external dependencies.
 */
public class SimpleLibraryDemo extends JFrame {
    
    // Simple data structures to simulate database
    private List<Student> students;
    private List<Book> books;
    private List<Loan> loans;
    private List<Fine> fines;
    
    // GUI Components
    private JTabbedPane tabbedPane;
    private JComboBox<Student> studentCombo;
    private JComboBox<Book> bookCombo;
    private JTextArea statusArea;
    private JTextArea reportArea;
    
    public SimpleLibraryDemo() {
        initializeData();
        initializeGUI();
    }
    
    private void initializeData() {
        // Initialize sample data
        students = new ArrayList<>();
        students.add(new Student(1, "John Smith", "john@email.com"));
        students.add(new Student(2, "Jane Doe", "jane@email.com"));
        students.add(new Student(3, "Bob Johnson", "bob@email.com"));
        
        books = new ArrayList<>();
        books.add(new Book(1, "Java Programming", "Oracle Press", 5));
        books.add(new Book(2, "Database Systems", "Pearson", 3));
        books.add(new Book(3, "Software Engineering", "McGraw Hill", 2));
        
        loans = new ArrayList<>();
        fines = new ArrayList<>();
    }
    
    private void initializeGUI() {
        setTitle("Modern Library Database System - Enhanced Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Set modern colors and fonts
        Font modernFont = new Font("Segoe UI", Font.PLAIN, 14);
        UIManager.put("defaultFont", modernFont);
        
        // Create tabbed pane with modern styling
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Add tabs with icons
        tabbedPane.addTab("Borrow/Return", createBorrowReturnPanel());
        tabbedPane.addTab("Pay Fines", createFinePaymentPanel());
        tabbedPane.addTab("Reports", createReportsPanel());
        
        add(tabbedPane);
        
        // Modern status bar
        statusArea = new JTextArea(3, 50);
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusArea.setBackground(new Color(45, 45, 45));
        statusArea.setForeground(new Color(220, 220, 220));
        statusArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(80, 80, 80)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        statusArea.setText("Library Database System - Modern Edition Ready");
        
        add(statusArea, BorderLayout.SOUTH);
    }
    
    private JPanel createBorrowReturnPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(250, 250, 250));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Modern title
        JLabel titleLabel = new JLabel("Book Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(33, 150, 243));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Student selection with modern styling
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel studentLabel = new JLabel("Select Student:");
        studentLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(studentLabel, gbc);
        
        studentCombo = new JComboBox<>();
        studentCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentCombo.setPreferredSize(new Dimension(200, 35));
        for (Student student : students) {
            studentCombo.addItem(student);
        }
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(studentCombo, gbc);
        
        // Book selection with modern styling
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel bookLabel = new JLabel("Select Book:");
        bookLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(bookLabel, gbc);
        
        bookCombo = new JComboBox<>();
        bookCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bookCombo.setPreferredSize(new Dimension(200, 35));
        for (Book book : books) {
            bookCombo.addItem(book);
        }
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(bookCombo, gbc);
        
        // Modern buttons
        JButton borrowButton = createModernButton("Borrow Book", new Color(76, 175, 80));
        borrowButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                borrowBook();
            }
        });
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(borrowButton, gbc);
        
        JButton returnButton = createModernButton("Return Book", new Color(255, 152, 0));
        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnBook();
            }
        });
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(returnButton, gbc);
        
        // Current loans display with modern styling
        JTextArea loansArea = new JTextArea(12, 50);
        loansArea.setEditable(false);
        loansArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        loansArea.setBackground(new Color(248, 249, 250));
        loansArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        updateLoansDisplay(loansArea);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        JScrollPane scrollPane = new JScrollPane(loansArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            "Current Loans", 
            0, 0, 
            new Font("Segoe UI", Font.BOLD, 14), 
            new Color(100, 100, 100)
        ));
        panel.add(scrollPane, gbc);
        
        return panel;
    }
    
    private JPanel createFinePaymentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(250, 250, 250));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Modern title
        JLabel titleLabel = new JLabel("Fine Payment Center");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(244, 67, 54));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Student selection with modern styling
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel studentLabel = new JLabel("Select Student:");
        studentLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(studentLabel, gbc);
        
        JComboBox<Student> fineStudentCombo = new JComboBox<>();
        fineStudentCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fineStudentCombo.setPreferredSize(new Dimension(200, 35));
        for (Student student : students) {
            fineStudentCombo.addItem(student);
        }
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(fineStudentCombo, gbc);
        
        // Fine amount with modern styling
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel amountLabel = new JLabel("Fine Amount:");
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(amountLabel, gbc);
        
        JTextField fineAmountField = new JTextField(10);
        fineAmountField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fineAmountField.setPreferredSize(new Dimension(200, 35));
        fineAmountField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(fineAmountField, gbc);
        
        // Modern pay button
        JButton payButton = createModernButton("Pay Fine", new Color(76, 175, 80));
        payButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Student student = (Student) fineStudentCombo.getSelectedItem();
                    double amount = Double.parseDouble(fineAmountField.getText());
                    payFine(student, amount);
                    fineAmountField.setText("");
                } catch (NumberFormatException ex) {
                    statusArea.setText("Error: Please enter a valid amount");
                }
            }
        });
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(payButton, gbc);
        
        // Fines display with modern styling
        JTextArea finesArea = new JTextArea(15, 50);
        finesArea.setEditable(false);
        finesArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        finesArea.setBackground(new Color(248, 249, 250));
        finesArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        updateFinesDisplay(finesArea);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        JScrollPane scrollPane = new JScrollPane(finesArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            "Outstanding Fines", 
            0, 0, 
            new Font("Segoe UI", Font.BOLD, 14), 
            new Color(100, 100, 100)
        ));
        panel.add(scrollPane, gbc);
        
        return panel;
    }
    
    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(250, 250, 250));
        
        // Modern title and controls
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(250, 250, 250));
        
        JLabel titleLabel = new JLabel("Library Reports Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(63, 81, 181));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Modern control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        controlPanel.setBackground(new Color(250, 250, 250));
        
        JButton loanReportButton = createModernButton("Generate Loan Report", new Color(33, 150, 243));
        loanReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateLoanReport();
            }
        });
        controlPanel.add(loanReportButton);
        
        JButton fineReportButton = createModernButton("Generate Fine Report", new Color(255, 152, 0));
        fineReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateFineReport();
            }
        });
        controlPanel.add(fineReportButton);
        
        headerPanel.add(controlPanel, BorderLayout.CENTER);
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Modern report display
        reportArea = new JTextArea(20, 60);
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        reportArea.setBackground(new Color(248, 249, 250));
        reportArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            "Report Output", 
            0, 0, 
            new Font("Segoe UI", Font.BOLD, 14), 
            new Color(100, 100, 100)
        ));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void borrowBook() {
        Student student = (Student) studentCombo.getSelectedItem();
        Book book = (Book) bookCombo.getSelectedItem();
        
        if (book.availableCopies > 0) {
            book.availableCopies--;
            loans.add(new Loan(loans.size() + 1, student.id, book.id, 
                              student.name, book.title, "Active"));
            statusArea.setText("Book '" + book.title + "' borrowed by " + student.name);
            
            // Update displays
            updateLoansDisplay(null);
        } else {
            statusArea.setText("Error: No copies of '" + book.title + "' available");
        }
    }
    
    private void returnBook() {
        Student student = (Student) studentCombo.getSelectedItem();
        Book book = (Book) bookCombo.getSelectedItem();
        
        // Find active loan
        for (Loan loan : loans) {
            if (loan.studentId == student.id && loan.bookId == book.id && 
                loan.status.equals("Active")) {
                loan.status = "Returned";
                book.availableCopies++;
                
                // Add a fine (simulate overdue)
                fines.add(new Fine(fines.size() + 1, student.id, student.name, 5.0, "Unpaid"));
                
                statusArea.setText("Book '" + book.title + "' returned by " + student.name + 
                                 ". Fine of $5.00 added for late return.");
                updateLoansDisplay(null);
                return;
            }
        }
        
        statusArea.setText("Error: No active loan found for this student and book");
    }
    
    private void payFine(Student student, double amount) {
        // Find unpaid fine
        for (Fine fine : fines) {
            if (fine.studentId == student.id && fine.status.equals("Unpaid")) {
                if (amount >= fine.amount) {
                    fine.status = "Paid";
                    statusArea.setText("Fine of $" + fine.amount + " paid by " + student.name);
                    updateFinesDisplay(null);
                    return;
                } else {
                    statusArea.setText("Error: Payment amount is less than fine amount");
                    return;
                }
            }
        }
        
        statusArea.setText("No unpaid fines found for " + student.name);
    }
    
    private void updateLoansDisplay(JTextArea area) {
        if (area == null) return;
        
        StringBuilder sb = new StringBuilder();
        sb.append("Current Loans:\n");
        sb.append("ID\tStudent\t\tBook\t\tStatus\n");
        sb.append("----------------------------------------\n");
        
        for (Loan loan : loans) {
            sb.append(loan.id).append("\t")
              .append(loan.studentName).append("\t\t")
              .append(loan.bookTitle).append("\t\t")
              .append(loan.status).append("\n");
        }
        
        area.setText(sb.toString());
    }
    
    private void updateFinesDisplay(JTextArea area) {
        if (area == null) return;
        
        StringBuilder sb = new StringBuilder();
        sb.append("Student Fines:\n");
        sb.append("ID\tStudent\t\tAmount\tStatus\n");
        sb.append("--------------------------------\n");
        
        for (Fine fine : fines) {
            sb.append(fine.id).append("\t")
              .append(fine.studentName).append("\t\t$")
              .append(fine.amount).append("\t")
              .append(fine.status).append("\n");
        }
        
        area.setText(sb.toString());
    }
    
    private void generateLoanReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("LOAN HISTORY REPORT\n");
        sb.append("Generated: ").append(new java.util.Date()).append("\n\n");
        
        sb.append("Total Loans: ").append(loans.size()).append("\n");
        sb.append("Active Loans: ").append(countActiveLoans()).append("\n");
        sb.append("Returned Books: ").append(loans.size() - countActiveLoans()).append("\n\n");
        
        sb.append("Detailed Loan History:\n");
        sb.append("ID\tStudent\t\tBook\t\tStatus\n");
        sb.append("----------------------------------------\n");
        
        for (Loan loan : loans) {
            sb.append(loan.id).append("\t")
              .append(loan.studentName).append("\t\t")
              .append(loan.bookTitle).append("\t\t")
              .append(loan.status).append("\n");
        }
        
        reportArea.setText(sb.toString());
    }
    
    private void generateFineReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("FINE PAYMENT REPORT\n");
        sb.append("Generated: ").append(new java.util.Date()).append("\n\n");
        
        double totalFines = 0;
        double paidFines = 0;
        
        for (Fine fine : fines) {
            totalFines += fine.amount;
            if (fine.status.equals("Paid")) {
                paidFines += fine.amount;
            }
        }
        
        sb.append("Total Fines Issued: $").append(totalFines).append("\n");
        sb.append("Total Fines Paid: $").append(paidFines).append("\n");
        sb.append("Outstanding Fines: $").append(totalFines - paidFines).append("\n\n");
        
        sb.append("Detailed Fine History:\n");
        sb.append("ID\tStudent\t\tAmount\tStatus\n");
        sb.append("--------------------------------\n");
        
        for (Fine fine : fines) {
            sb.append(fine.id).append("\t")
              .append(fine.studentName).append("\t\t$")
              .append(fine.amount).append("\t")
              .append(fine.status).append("\n");
        }
        
        reportArea.setText(sb.toString());
    }
    
    // Helper method to create modern styled buttons
    private JButton createModernButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(160, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
    }
    
    private int countActiveLoans() {
        int count = 0;
        for (Loan loan : loans) {
            if (loan.status.equals("Active")) {
                count++;
            }
        }
        return count;
    }
    
    // Simple data classes
    static class Student {
        int id;
        String name;
        String email;
        
        Student(int id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }
        
        public String toString() {
            return name + " (" + email + ")";
        }
    }
    
    static class Book {
        int id;
        String title;
        String author;
        int availableCopies;
        
        Book(int id, String title, String author, int availableCopies) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.availableCopies = availableCopies;
        }
        
        public String toString() {
            return title + " by " + author + " (" + availableCopies + " available)";
        }
    }
    
    static class Loan {
        int id;
        int studentId;
        int bookId;
        String studentName;
        String bookTitle;
        String status;
        
        Loan(int id, int studentId, int bookId, String studentName, String bookTitle, String status) {
            this.id = id;
            this.studentId = studentId;
            this.bookId = bookId;
            this.studentName = studentName;
            this.bookTitle = bookTitle;
            this.status = status;
        }
    }
    
    static class Fine {
        int id;
        int studentId;
        String studentName;
        double amount;
        String status;
        
        Fine(int id, int studentId, String studentName, double amount, String status) {
            this.id = id;
            this.studentId = studentId;
            this.studentName = studentName;
            this.amount = amount;
            this.status = status;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Set FlatLaf Dark theme for modern look
                    FlatDarkLaf.setup();
                } catch (Exception e) {
                    // Use default look and feel
                }
                
                new SimpleLibraryDemo().setVisible(true);
            }
        });
    }
}