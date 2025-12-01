package com.library.dao;

import com.library.model.Fine;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Fine Data Access Object for Oracle Database
 */
public class FineDAO {
    
    private final OracleConnectionManager connectionManager;
    
    public FineDAO() {
        this.connectionManager = OracleConnectionManager.getInstance();
    }
    
    /**
     * Create a new fine
     */
    public int createFine(Fine fine) throws SQLException {
        String sql = "INSERT INTO Fines (fine_id, student_id, loan_id, fine_amount, fine_date, payment_status) " +
                    "VALUES (seq_fine_id.NEXTVAL, ?, ?, ?, ?, ?)";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"fine_id"})) {
            
            stmt.setInt(1, fine.getStudentId());
            stmt.setInt(2, fine.getLoanId());
            stmt.setBigDecimal(3, fine.getFineAmount());
            stmt.setDate(4, Date.valueOf(fine.getFineDate()));
            stmt.setString(5, fine.getPaymentStatus());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        }
        
        throw new SQLException("Failed to create fine");
    }
    
    /**
     * Get all unpaid fines
     */
    public List<Fine> getUnpaidFines() throws SQLException {
        String sql = "SELECT f.fine_id, f.student_id, f.loan_id, f.fine_amount, f.fine_date, " +
                    "f.payment_date, f.payment_status, s.name as student_name, b.title as book_title, " +
                    "l.due_date, l.return_date " +
                    "FROM Fines f " +
                    "JOIN Students s ON f.student_id = s.student_id " +
                    "JOIN Loans l ON f.loan_id = l.loan_id " +
                    "JOIN Books b ON l.book_id = b.book_id " +
                    "WHERE f.payment_status = 'UNPAID' " +
                    "ORDER BY f.fine_date ASC";
        
        return executeQuery(sql);
    }
    
    /**
     * Get unpaid fines by student ID
     */
    public List<Fine> getUnpaidFinesByStudentId(int studentId) throws SQLException {
        String sql = "SELECT f.fine_id, f.student_id, f.loan_id, f.fine_amount, f.fine_date, " +
                    "f.payment_date, f.payment_status, s.name as student_name, b.title as book_title, " +
                    "l.due_date, l.return_date " +
                    "FROM Fines f " +
                    "JOIN Students s ON f.student_id = s.student_id " +
                    "JOIN Loans l ON f.loan_id = l.loan_id " +
                    "JOIN Books b ON l.book_id = b.book_id " +
                    "WHERE f.student_id = ? AND f.payment_status = 'UNPAID' " +
                    "ORDER BY f.fine_date ASC";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return mapResultSetToFines(rs);
            }
        }
    }
    
    /**
     * Get fine history for a student in a specific month/year
     */
    public List<Fine> getFineHistory(int studentId, int month, int year) throws SQLException {
        String sql = "SELECT f.fine_id, f.student_id, f.loan_id, f.fine_amount, f.fine_date, " +
                    "f.payment_date, f.payment_status, s.name as student_name, b.title as book_title, " +
                    "l.due_date, l.return_date " +
                    "FROM Fines f " +
                    "JOIN Students s ON f.student_id = s.student_id " +
                    "JOIN Loans l ON f.loan_id = l.loan_id " +
                    "JOIN Books b ON l.book_id = b.book_id " +
                    "WHERE f.student_id = ? " +
                    "AND EXTRACT(MONTH FROM f.fine_date) = ? " +
                    "AND EXTRACT(YEAR FROM f.fine_date) = ? " +
                    "ORDER BY f.fine_date DESC";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            stmt.setInt(2, month);
            stmt.setInt(3, year);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return mapResultSetToFines(rs);
            }
        }
    }

    /**
     * Get PAID fines by payment month/year for a student
     */
    public List<Fine> getPaidFinesByStudentInMonth(int studentId, int month, int year) throws SQLException {
        String sql = "SELECT f.fine_id, f.student_id, f.loan_id, f.fine_amount, f.fine_date, " +
                    "f.payment_date, f.payment_status, s.name as student_name, b.title as book_title, " +
                    "l.due_date, l.return_date " +
                    "FROM Fines f " +
                    "JOIN Students s ON f.student_id = s.student_id " +
                    "JOIN Loans l ON f.loan_id = l.loan_id " +
                    "JOIN Books b ON l.book_id = b.book_id " +
                    "WHERE f.student_id = ? AND f.payment_status = 'PAID' " +
                    "AND EXTRACT(MONTH FROM f.payment_date) = ? " +
                    "AND EXTRACT(YEAR FROM f.payment_date) = ? " +
                    "ORDER BY f.payment_date DESC";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, month);
            stmt.setInt(3, year);

            try (ResultSet rs = stmt.executeQuery()) {
                return mapResultSetToFines(rs);
            }
        }
    }
    
    /**
     * Get all fine history for a student
     */
    public List<Fine> getAllFineHistory(int studentId) throws SQLException {
        String sql = "SELECT f.fine_id, f.student_id, f.loan_id, f.fine_amount, f.fine_date, " +
                    "f.payment_date, f.payment_status, s.name as student_name, b.title as book_title, " +
                    "l.due_date, l.return_date " +
                    "FROM Fines f " +
                    "JOIN Students s ON f.student_id = s.student_id " +
                    "JOIN Loans l ON f.loan_id = l.loan_id " +
                    "JOIN Books b ON l.book_id = b.book_id " +
                    "WHERE f.student_id = ? " +
                    "ORDER BY f.fine_date DESC";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return mapResultSetToFines(rs);
            }
        }
    }
    
    /**
     * Pay a fine (update payment status and payment date)
     */
    public boolean payFine(int fineId) throws SQLException {
        String sql = "UPDATE Fines SET payment_status = 'PAID', payment_date = SYSDATE WHERE fine_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, fineId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    /**
     * Pay multiple fines for a student up to a specified amount
     */
    public BigDecimal payFines(int studentId, BigDecimal paymentAmount) throws SQLException {
        List<Fine> unpaidFines = getUnpaidFinesByStudentId(studentId);
        BigDecimal remainingAmount = paymentAmount;
        BigDecimal totalPaid = BigDecimal.ZERO;
        
        try (Connection conn = connectionManager.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                for (Fine fine : unpaidFines) {
                    if (remainingAmount.compareTo(fine.getFineAmount()) >= 0) {
                        // Pay the entire fine
                        if (payFine(fine.getFineId())) {
                            remainingAmount = remainingAmount.subtract(fine.getFineAmount());
                            totalPaid = totalPaid.add(fine.getFineAmount());
                        }
                    } else {
                        // Partial payment not supported in this implementation
                        break;
                    }
                    
                    if (remainingAmount.compareTo(BigDecimal.ZERO) == 0) {
                        break;
                    }
                }
                
                conn.commit();
                return totalPaid;
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
    
    /**
     * Get total unpaid fines for a student
     */
    public BigDecimal getTotalUnpaidFines(int studentId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(fine_amount), 0) as total " +
                    "FROM Fines WHERE student_id = ? AND payment_status = 'UNPAID'";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total");
                }
            }
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * Get fine by ID
     */
    public Fine getFineById(int fineId) throws SQLException {
        String sql = "SELECT f.fine_id, f.student_id, f.loan_id, f.fine_amount, f.fine_date, " +
                    "f.payment_date, f.payment_status, s.name as student_name, b.title as book_title, " +
                    "l.due_date, l.return_date " +
                    "FROM Fines f " +
                    "JOIN Students s ON f.student_id = s.student_id " +
                    "JOIN Loans l ON f.loan_id = l.loan_id " +
                    "JOIN Books b ON l.book_id = b.book_id " +
                    "WHERE f.fine_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, fineId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<Fine> fines = mapResultSetToFines(rs);
                return fines.isEmpty() ? null : fines.get(0);
            }
        }
    }
    
    /**
     * Get all fines (both paid and unpaid)
     */
    public List<Fine> getAllFines() throws SQLException {
        String sql = "SELECT f.fine_id, f.student_id, f.loan_id, f.fine_amount, f.fine_date, " +
                    "f.payment_date, f.payment_status, s.name as student_name, b.title as book_title, " +
                    "l.due_date, l.return_date " +
                    "FROM Fines f " +
                    "JOIN Students s ON f.student_id = s.student_id " +
                    "JOIN Loans l ON f.loan_id = l.loan_id " +
                    "JOIN Books b ON l.book_id = b.book_id " +
                    "ORDER BY f.fine_date DESC";
        
        return executeQuery(sql);
    }
    
    /**
     * Create fines for overdue loans
     */
    public int createFinesForOverdueLoans() throws SQLException {
        String sql = "INSERT INTO Fines (fine_id, student_id, loan_id, fine_amount, fine_date, payment_status) " +
                    "SELECT seq_fine_id.NEXTVAL, l.student_id, l.loan_id, " +
                    "(SYSDATE - l.due_date) * 1.0, SYSDATE, 'UNPAID' " +
                    "FROM Loans l " +
                    "WHERE l.status IN ('ACTIVE', 'OVERDUE') " +
                    "AND l.due_date < SYSDATE " +
                    "AND NOT EXISTS (SELECT 1 FROM Fines f WHERE f.loan_id = l.loan_id)";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            return stmt.executeUpdate();
        }
    }
    
    /**
     * Helper method to execute query and return fines
     */
    private List<Fine> executeQuery(String sql) throws SQLException {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            return mapResultSetToFines(rs);
        }
    }
    
    /**
     * Helper method to map ResultSet to Fine objects
     */
    private List<Fine> mapResultSetToFines(ResultSet rs) throws SQLException {
        List<Fine> fines = new ArrayList<>();
        
        while (rs.next()) {
            Fine fine = new Fine();
            fine.setFineId(rs.getInt("fine_id"));
            fine.setStudentId(rs.getInt("student_id"));
            fine.setLoanId(rs.getInt("loan_id"));
            fine.setFineAmount(rs.getBigDecimal("fine_amount"));
            
            Date fineDate = rs.getDate("fine_date");
            if (fineDate != null) {
                fine.setFineDate(fineDate.toLocalDate());
            }
            
            Date paymentDate = rs.getDate("payment_date");
            if (paymentDate != null) {
                fine.setPaymentDate(paymentDate.toLocalDate());
            }
            
            fine.setPaymentStatus(rs.getString("payment_status"));
            fine.setStudentName(rs.getString("student_name"));
            fine.setBookTitle(rs.getString("book_title"));
            
            Date dueDate = rs.getDate("due_date");
            if (dueDate != null) {
                fine.setDueDate(dueDate.toLocalDate());
            }
            
            // Set actual return date from loans table
            Date returnDate = rs.getDate("return_date");
            if (returnDate != null) {
                fine.setActualReturnDate(returnDate.toLocalDate());
            }
            
            fines.add(fine);
        }
        
        return fines;
    }
}
