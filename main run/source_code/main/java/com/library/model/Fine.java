package com.library.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Fine model class representing a fine for overdue books
 */
public class Fine {
    private int fineId;
    private int studentId;
    private int loanId;
    private BigDecimal fineAmount;
    private LocalDate fineDate;
    private LocalDate paymentDate;
    private String paymentStatus; // UNPAID, PAID
    
    // Additional fields for display purposes
    private String studentName;
    private String bookTitle;
    private LocalDate dueDate;
    private LocalDate actualReturnDate; // New field for actual return date
    
    public Fine() {}
    
    public Fine(int fineId, int studentId, int loanId, BigDecimal fineAmount, 
                LocalDate fineDate, LocalDate paymentDate, String paymentStatus) {
        this.fineId = fineId;
        this.studentId = studentId;
        this.loanId = loanId;
        this.fineAmount = fineAmount;
        this.fineDate = fineDate;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
    }
    
    // Getters and Setters
    public int getFineId() { return fineId; }
    public void setFineId(int fineId) { this.fineId = fineId; }
    
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    
    public int getLoanId() { return loanId; }
    public void setLoanId(int loanId) { this.loanId = loanId; }
    
    public BigDecimal getFineAmount() { return fineAmount; }
    public void setFineAmount(BigDecimal fineAmount) { this.fineAmount = fineAmount; }
    
    public LocalDate getFineDate() { return fineDate; }
    public void setFineDate(LocalDate fineDate) { this.fineDate = fineDate; }
    
    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
    
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    
    public LocalDate getActualReturnDate() { return actualReturnDate; }
    public void setActualReturnDate(LocalDate actualReturnDate) { this.actualReturnDate = actualReturnDate; }
    
    /**
     * Get fine amount (alias for getFineAmount for compatibility)
     */
    public BigDecimal getAmount() {
        return getFineAmount();
    }
    
    /**
     * Calculate days overdue based on due date and fine date
     */
    public long getDaysOverdue() {
        if (dueDate != null && fineDate != null) {
            return ChronoUnit.DAYS.between(dueDate, fineDate);
        }
        return 0;
    }
    
    /**
     * Calculate actual days overdue based on due date and actual return date
     * If not returned yet, calculate from current date
     */
    public long getActualDaysOverdue() {
        if (dueDate == null) {
            return 0;
        }
        
        LocalDate comparisonDate = actualReturnDate != null ? actualReturnDate : LocalDate.now();
        long days = ChronoUnit.DAYS.between(dueDate, comparisonDate);
        return Math.max(0, days); // Don't return negative values
    }
    
    /**
     * Get the return status description
     */
    public String getReturnStatus() {
        if (actualReturnDate != null) {
            if (actualReturnDate.isAfter(dueDate)) {
                return "Returned Late";
            } else {
                return "Returned On Time";
            }
        } else {
            if (LocalDate.now().isAfter(dueDate)) {
                return "Overdue - Not Returned";
            } else {
                return "Active - Not Due";
            }
        }
    }
    
    /**
     * Get formatted return time information
     */
    public String getReturnTimeInfo() {
        StringBuilder info = new StringBuilder();
        
        if (dueDate != null) {
            info.append("Due: ").append(dueDate);
        }
        
        if (actualReturnDate != null) {
            info.append(" | Returned: ").append(actualReturnDate);
            long daysDiff = ChronoUnit.DAYS.between(dueDate, actualReturnDate);
            if (daysDiff > 0) {
                info.append(" (").append(daysDiff).append(" days late)");
            } else if (daysDiff == 0) {
                info.append(" (on time)");
            } else {
                info.append(" (").append(Math.abs(daysDiff)).append(" days early)");
            }
        } else {
            info.append(" | Not Returned");
            if (dueDate != null && LocalDate.now().isAfter(dueDate)) {
                long overdueDays = ChronoUnit.DAYS.between(dueDate, LocalDate.now());
                info.append(" (").append(overdueDays).append(" days overdue)");
            }
        }
        
        return info.toString();
    }
    
    /**
     * Check if the fine is paid
     */
    public boolean isPaid() {
        return "PAID".equals(paymentStatus);
    }
    
    /**
     * Check if the fine is unpaid
     */
    public boolean isUnpaid() {
        return "UNPAID".equals(paymentStatus);
    }
    
    /**
     * Calculate fine amount based on days overdue
     * Standard rate: $1.00 per day overdue
     */
    public static BigDecimal calculateFineAmount(long daysOverdue) {
        if (daysOverdue <= 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(daysOverdue).multiply(BigDecimal.valueOf(1.00));
    }
    
    @Override
    public String toString() {
        return String.format("Fine{id=%d, student=%s, amount=$%.2f, status=%s, date=%s, returnInfo=%s}", 
                           fineId, studentName, fineAmount, paymentStatus, fineDate, getReturnTimeInfo());
    }
}