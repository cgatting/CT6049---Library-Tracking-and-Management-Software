package com.library.dao;

import com.library.model.Loan;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Loan Data Access Object for Oracle Database
 */
public class LoanDAO {
    
    private final OracleConnectionManager connectionManager;
    
    public LoanDAO() {
        this.connectionManager = OracleConnectionManager.getInstance();
    }
    
    /**
     * Create a new loan
     */
    public int createLoan(Loan loan) throws SQLException {
        String sql = "INSERT INTO Loans (loan_id, student_id, book_id, loan_date, due_date, status) " +
                    "VALUES (seq_loan_id.NEXTVAL, ?, ?, ?, ?, ?)";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"loan_id"})) {
            
            stmt.setInt(1, loan.getStudentId());
            stmt.setInt(2, loan.getBookId());
            stmt.setDate(3, Date.valueOf(loan.getLoanDate()));
            stmt.setDate(4, Date.valueOf(loan.getDueDate()));
            stmt.setString(5, loan.getStatus());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        }
        
        throw new SQLException("Failed to create loan");
    }
    
    /**
     * Get all active loans
     */
    public List<Loan> getActiveLoans() throws SQLException {
        String sql = "SELECT l.loan_id, l.student_id, l.book_id, l.loan_date, l.due_date, " +
                    "l.return_date, l.status, s.name as student_name, b.title as book_title, " +
                    "b.author as book_author " +
                    "FROM Loans l " +
                    "JOIN Students s ON l.student_id = s.student_id " +
                    "JOIN Books b ON l.book_id = b.book_id " +
                    "WHERE l.status = 'ACTIVE' " +
                    "ORDER BY l.due_date ASC";
        
        return executeQuery(sql);
    }
    
    /**
     * Get all overdue loans
     */
    public List<Loan> getOverdueLoans() throws SQLException {
        String sql = "SELECT l.loan_id, l.student_id, l.book_id, l.loan_date, l.due_date, " +
                    "l.return_date, l.status, s.name as student_name, b.title as book_title, " +
                    "b.author as book_author " +
                    "FROM Loans l " +
                    "JOIN Students s ON l.student_id = s.student_id " +
                    "JOIN Books b ON l.book_id = b.book_id " +
                    "WHERE l.status = 'ACTIVE' AND l.due_date < SYSDATE " +
                    "ORDER BY l.due_date ASC";
        
        return executeQuery(sql);
    }
    
    /**
     * Get loans by student ID
     */
    public List<Loan> getLoansByStudentId(int studentId) throws SQLException {
        String sql = "SELECT l.loan_id, l.student_id, l.book_id, l.loan_date, l.due_date, " +
                    "l.return_date, l.status, s.name as student_name, b.title as book_title, " +
                    "b.author as book_author " +
                    "FROM Loans l " +
                    "JOIN Students s ON l.student_id = s.student_id " +
                    "JOIN Books b ON l.book_id = b.book_id " +
                    "WHERE l.student_id = ? " +
                    "ORDER BY l.loan_date DESC";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return mapResultSetToLoans(rs);
            }
        }
    }
    
    /**
     * Get loan history for a student in a specific month/year
     */
    public List<Loan> getLoanHistory(int studentId, int month, int year) throws SQLException {
        String sql = "SELECT l.loan_id, l.student_id, l.book_id, l.loan_date, l.due_date, " +
                    "l.return_date, l.status, s.name as student_name, b.title as book_title, " +
                    "b.author as book_author " +
                    "FROM Loans l " +
                    "JOIN Students s ON l.student_id = s.student_id " +
                    "JOIN Books b ON l.book_id = b.book_id " +
                    "WHERE l.student_id = ? " +
                    "AND EXTRACT(MONTH FROM l.loan_date) = ? " +
                    "AND EXTRACT(YEAR FROM l.loan_date) = ? " +
                    "ORDER BY l.loan_date DESC";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            stmt.setInt(2, month);
            stmt.setInt(3, year);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return mapResultSetToLoans(rs);
            }
        }
    }
    
    /**
     * Return a book (update loan status and return date)
     */
    public boolean returnBook(int loanId) throws SQLException {
        // First check if the loan is already returned
        String checkSql = "SELECT status FROM Loans WHERE loan_id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            
            checkStmt.setInt(1, loanId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    String currentStatus = rs.getString("status");
                    if ("RETURNED".equals(currentStatus)) {
                        throw new SQLException("Cannot return book: Loan is already marked as RETURNED");
                    }
                } else {
                    throw new SQLException("Loan not found with ID: " + loanId);
                }
            }
        }
        
        // Proceed with the return if validation passes
        String sql = "UPDATE Loans SET status = 'RETURNED', return_date = SYSDATE WHERE loan_id = ? AND status != 'RETURNED'";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, loanId);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No active loan found to return or loan already returned");
            }
            return rowsAffected > 0;
        }
    }
    
    /**
     * Update loan status to OVERDUE
     */
    public int updateOverdueLoans() throws SQLException {
        String sql = "UPDATE Loans SET status = 'OVERDUE' " +
                    "WHERE status = 'ACTIVE' AND due_date < SYSDATE";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            return stmt.executeUpdate();
        }
    }
    
    /**
     * Get loan by ID
     */
    public Loan getLoanById(int loanId) throws SQLException {
        String sql = "SELECT l.loan_id, l.student_id, l.book_id, l.loan_date, l.due_date, " +
                    "l.return_date, l.status, s.name as student_name, b.title as book_title, " +
                    "b.author as book_author " +
                    "FROM Loans l " +
                    "JOIN Students s ON l.student_id = s.student_id " +
                    "JOIN Books b ON l.book_id = b.book_id " +
                    "WHERE l.loan_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, loanId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<Loan> loans = mapResultSetToLoans(rs);
                return loans.isEmpty() ? null : loans.get(0);
            }
        }
    }
    
    /**
     * Check if student has active loan for a specific book
     */
    public boolean hasActiveLoan(int studentId, int bookId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Loans WHERE student_id = ? AND book_id = ? AND status = 'ACTIVE'";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            stmt.setInt(2, bookId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Helper method to execute query and return loans
     */
    private List<Loan> executeQuery(String sql) throws SQLException {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            return mapResultSetToLoans(rs);
        }
    }
    
    /**
     * Get all active loans
     */
    public List<Loan> getAllActiveLoans() throws SQLException {
        String sql = "SELECT l.loan_id, l.student_id, l.book_id, l.loan_date, l.due_date, " +
                    "l.return_date, l.status, s.name as student_name, b.title as book_title, " +
                    "b.author as book_author " +
                    "FROM Loans l " +
                    "JOIN Students s ON l.student_id = s.student_id " +
                    "JOIN Books b ON l.book_id = b.book_id " +
                    "WHERE l.status IN ('ACTIVE', 'OVERDUE') " +
                    "ORDER BY l.loan_date DESC";
        
        return executeQuery(sql);
    }
    
    /**
     * Get loan history by student ID (all loans for a student)
     */
    public List<Loan> getLoanHistoryByStudentId(int studentId) throws SQLException {
        String sql = "SELECT l.loan_id, l.student_id, l.book_id, l.loan_date, l.due_date, " +
                    "l.return_date, l.status, l.renewal_count, s.name as student_name, " +
                    "b.title as book_title, b.author as book_author, " +
                    "COALESCE(f.fine_amount, 0) as fine_amount " +
                    "FROM Loans l " +
                    "JOIN Students s ON l.student_id = s.student_id " +
                    "JOIN Books b ON l.book_id = b.book_id " +
                    "LEFT JOIN Fines f ON l.loan_id = f.loan_id " +
                    "WHERE l.student_id = ? " +
                    "ORDER BY l.loan_date DESC";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return mapResultSetToLoans(rs);
            }
        }
    }
    
    /**
     * Renew a loan by extending the due date
     */
    public boolean renewLoan(int loanId, LocalDate newDueDate) throws SQLException {
        String sql = "UPDATE Loans SET due_date = ?, renewal_count = renewal_count + 1 WHERE loan_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(newDueDate));
            stmt.setInt(2, loanId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    /**
     * Helper method to map ResultSet to Loan objects
     */
    private List<Loan> mapResultSetToLoans(ResultSet rs) throws SQLException {
        List<Loan> loans = new ArrayList<>();
        
        while (rs.next()) {
            Loan loan = new Loan();
            loan.setLoanId(rs.getInt("loan_id"));
            loan.setStudentId(rs.getInt("student_id"));
            loan.setBookId(rs.getInt("book_id"));
            
            Date loanDate = rs.getDate("loan_date");
            if (loanDate != null) {
                loan.setLoanDate(loanDate.toLocalDate());
            }
            
            Date dueDate = rs.getDate("due_date");
            if (dueDate != null) {
                loan.setDueDate(dueDate.toLocalDate());
            }
            
            Date returnDate = rs.getDate("return_date");
            if (returnDate != null) {
                loan.setReturnDate(returnDate.toLocalDate());
            }
            
            loan.setStatus(rs.getString("status"));
            loan.setStudentName(rs.getString("student_name"));
            loan.setBookTitle(rs.getString("book_title"));
            loan.setBookAuthor(rs.getString("book_author"));
            
            // Set renewal count if available
            try {
                loan.setRenewalCount(rs.getInt("renewal_count"));
            } catch (SQLException e) {
                loan.setRenewalCount(0); // Default to 0 if column doesn't exist
            }
            
            // Set fine amount if available
            try {
                java.math.BigDecimal fineAmount = rs.getBigDecimal("fine_amount");
                loan.setFineAmount(fineAmount);
            } catch (SQLException e) {
                loan.setFineAmount(java.math.BigDecimal.ZERO); // Default to 0 if column doesn't exist
            }
            
            loans.add(loan);
        }
        
        return loans;
    }
}