package com.library.model;

import java.time.LocalDate;

/**
 * Loan model class representing a book loan transaction
 */
public class Loan {
    private int loanId;
    private int studentId;
    private int bookId;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status; // ACTIVE, RETURNED, OVERDUE
    private int renewalCount; // Number of times loan has been renewed
    private java.math.BigDecimal fineAmount; // Associated fine amount
    
    // Additional fields for display purposes
    private String studentName;
    private String bookTitle;
    private String bookAuthor;
    
    public Loan() {}
    
    public Loan(int loanId, int studentId, int bookId, LocalDate loanDate, 
                LocalDate dueDate, LocalDate returnDate, String status) {
        this.loanId = loanId;
        this.studentId = studentId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }
    
    // Getters and Setters
    public int getLoanId() { return loanId; }
    public void setLoanId(int loanId) { this.loanId = loanId; }
    
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    
    public LocalDate getLoanDate() { return loanDate; }
    public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }
    
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    
    public String getBookAuthor() { return bookAuthor; }
    public void setBookAuthor(String bookAuthor) { this.bookAuthor = bookAuthor; }
    
    public int getRenewalCount() { return renewalCount; }
    public void setRenewalCount(int renewalCount) { this.renewalCount = renewalCount; }
    
    public java.math.BigDecimal getFineAmount() { return fineAmount; }
    public void setFineAmount(java.math.BigDecimal fineAmount) { this.fineAmount = fineAmount; }
    
    /**
     * Check if the loan is overdue
     */
    public boolean isOverdue() {
        if (status.equals("RETURNED")) {
            return false;
        }
        return LocalDate.now().isAfter(dueDate);
    }
    
    /**
     * Calculate days until due (negative if overdue)
     */
    public long getDaysUntilDue() {
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
    }
    
    /**
     * Calculate days overdue (0 if not overdue)
     */
    public long getDaysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }
    
    @Override
    public String toString() {
        return String.format("Loan{id=%d, student=%s, book=%s, status=%s, due=%s}", 
                           loanId, studentName, bookTitle, status, dueDate);
    }
}