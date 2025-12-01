package com.library.controller;

import com.library.model.Book;
import com.library.model.Fine;
import com.library.model.Loan;
import com.library.model.Student;
import com.library.service.LibraryRepository;
import com.library.service.RepositoryType;
import com.library.util.DataSyncManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Logic layer that mediates between the Swing presentation tier and the
 * repository/DAO implementations. All validation and business decisions live
 * here so both Oracle and MongoDB variants behave identically.
 */
public class LibraryController {

    private static final BigDecimal DAILY_FINE_RATE = BigDecimal.valueOf(0.50);
    private static final int DEFAULT_LOAN_PERIOD_DAYS = 14;

    private final LibraryRepository repository;

    public LibraryController(LibraryRepository repository) {
        this.repository = repository;
    }

    // -----------------------------------------------------------------
    //  Connection / metadata
    // -----------------------------------------------------------------

    public RepositoryType getRepositoryType() {
        return repository.getRepositoryType();
    }

    public boolean testConnection() throws Exception {
        return repository.testConnection();
    }

    // -----------------------------------------------------------------
    //  Student operations
    // -----------------------------------------------------------------

    public List<Student> getAllStudents() throws Exception {
        return repository.getAllStudents();
    }

    public List<Student> searchStudents(String term) throws Exception {
        String query = normalise(term);
        if (query.isEmpty()) {
            return getAllStudents();
        }
        List<Student> matches = new ArrayList<>();
        for (Student student : repository.getAllStudents()) {
            if (studentMatches(student, query)) {
                matches.add(student);
            }
        }
        return matches;
    }

    public void addStudent(Student student) throws Exception {
        validateStudent(student);
        repository.addStudent(student);
        DataSyncManager.getInstance().notifyStudentDataChanged();
    }

    public void updateStudent(Student student) throws Exception {
        validateStudent(student);
        repository.updateStudent(student);
        DataSyncManager.getInstance().notifyStudentDataChanged();
    }

    public void deleteStudent(int studentId) throws Exception {
        repository.deleteStudent(studentId);
        DataSyncManager.getInstance().notifyStudentDataChanged();
    }

    private void validateStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        if (isBlank(student.getName())) {
            throw new IllegalArgumentException("Student name is required");
        }
        if (isBlank(student.getEmail())) {
            throw new IllegalArgumentException("Student email is required");
        }
    }

    private boolean studentMatches(Student student, String query) {
        if (student == null) {
            return false;
        }
        if (String.valueOf(student.getStudentId()).contains(query)) {
            return true;
        }
        if (contains(student.getName(), query)) {
            return true;
        }
        if (contains(student.getEmail(), query)) {
            return true;
        }
        return contains(student.getPhone(), query);
    }

    // -----------------------------------------------------------------
    //  Book operations
    // -----------------------------------------------------------------

    public List<Book> getAllBooks() throws Exception {
        return repository.getAllBooks();
    }

    public List<Book> getAvailableBooks() throws Exception {
        return repository.getAvailableBooks();
    }

    public List<Book> searchBooks(String term, boolean onlyAvailable) throws Exception {
        String query = normalise(term);
        List<Book> source = onlyAvailable ? repository.getAvailableBooks() : repository.getAllBooks();
        if (query.isEmpty()) {
            return source;
        }
        List<Book> matches = new ArrayList<>();
        for (Book book : source) {
            if (bookMatches(book, query)) {
                matches.add(book);
            }
        }
        return matches;
    }

    public Book addBook(Book book) throws Exception {
        validateBook(book);
        repository.addBook(book);
        DataSyncManager.getInstance().notifyBookDataChanged();
        return book;
    }

    public void updateBook(Book book) throws Exception {
        validateBook(book);
        repository.updateBook(book);
        DataSyncManager.getInstance().notifyBookDataChanged();
    }

    public void deleteBook(int bookId) throws Exception {
        repository.deleteBook(bookId);
        DataSyncManager.getInstance().notifyBookDataChanged();
    }

    private void validateBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if (isBlank(book.getTitle())) {
            throw new IllegalArgumentException("Book title is required");
        }
        if (isBlank(book.getAuthor())) {
            throw new IllegalArgumentException("Book author is required");
        }
        if (book.getAvailableCopies() < 0 || book.getTotalCopies() < 0) {
            throw new IllegalArgumentException("Copy counts must be non-negative");
        }
        if (book.getAvailableCopies() > book.getTotalCopies()) {
            throw new IllegalArgumentException("Available copies must not exceed total copies");
        }
    }

    private boolean bookMatches(Book book, String query) {
        if (book == null) {
            return false;
        }
        if (contains(book.getTitle(), query)) {
            return true;
        }
        if (contains(book.getAuthor(), query)) {
            return true;
        }
        if (contains(book.getIsbn(), query)) {
            return true;
        }
        return contains(book.getCategory(), query);
    }

    // -----------------------------------------------------------------
    //  Loan operations
    // -----------------------------------------------------------------

    public List<Loan> getAllActiveLoans() throws Exception {
        return repository.getAllActiveLoans();
    }

    public List<Loan> getActiveLoans() throws Exception {
        return repository.getActiveLoans();
    }

    public List<Loan> getOverdueLoans() throws Exception {
        return repository.getOverdueLoans();
    }

    public List<Loan> getLoansByStudent(int studentId) throws Exception {
        return repository.getLoansByStudent(studentId);
    }

    public Loan borrowBook(int studentId, int bookId) throws Exception {
        Book book = repository.getBookById(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No copies available for this book");
        }
        if (repository.hasActiveLoan(studentId, bookId)) {
            throw new IllegalStateException("Student already has this book borrowed");
        }

        Loan loan = new Loan();
        loan.setStudentId(studentId);
        loan.setBookId(bookId);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(DEFAULT_LOAN_PERIOD_DAYS));
        loan.setStatus("ACTIVE");
        repository.createLoan(loan);

        repository.updateBookAvailability(bookId, book.getAvailableCopies() - 1);
        DataSyncManager.getInstance().notifyLoanDataChanged();
        DataSyncManager.getInstance().notifyBookDataChanged();
        return loan;
    }

    public ReturnResult returnLoan(Loan loan) throws Exception {
        if (loan == null) {
            throw new IllegalArgumentException("Loan cannot be null");
        }
        
        // Validate that the loan is not already returned
        if ("RETURNED".equals(loan.getStatus())) {
            throw new IllegalStateException("This book has already been returned and cannot be returned again");
        }
        
        // Get fresh loan data from database to ensure current status
        Loan currentLoan = repository.getLoanById(loan.getLoanId());
        if (currentLoan == null) {
            throw new IllegalArgumentException("Loan not found in database");
        }
        
        if ("RETURNED".equals(currentLoan.getStatus())) {
            throw new IllegalStateException("This book has already been returned and cannot be returned again");
        }
        
        repository.returnLoan(loan.getLoanId());

        Book book = repository.getBookById(loan.getBookId());
        if (book != null) {
            repository.updateBookAvailability(book.getBookId(), book.getAvailableCopies() + 1);
        }

        ReturnResult result = new ReturnResult();
        result.setReturnDate(LocalDate.now());

        long overdueDays = calculateOverdueDays(loan.getDueDate(), result.getReturnDate());
        if (overdueDays > 0) {
            BigDecimal fineAmount = DAILY_FINE_RATE.multiply(BigDecimal.valueOf(overdueDays));
            Fine fine = new Fine();
            fine.setStudentId(loan.getStudentId());
            fine.setLoanId(loan.getLoanId());
            fine.setFineAmount(fineAmount);
            fine.setFineDate(result.getReturnDate());
            fine.setPaymentStatus("UNPAID");
            repository.createFine(fine);

            result.setFineCreated(true);
            result.setFineAmount(fineAmount);
            result.setDaysOverdue(overdueDays);
            DataSyncManager.getInstance().notifyFineDataChanged();
        }

        DataSyncManager.getInstance().notifyLoanDataChanged();
        DataSyncManager.getInstance().notifyBookDataChanged();
        return result;
    }

    public Loan renewLoan(Loan loan, LocalDate newDueDate) throws Exception {
        if (loan == null) {
            throw new IllegalArgumentException("Loan cannot be null");
        }
        if (newDueDate == null) {
            throw new IllegalArgumentException("New due date is required");
        }
        LocalDate currentDue = loan.getDueDate();
        if (currentDue != null && !newDueDate.isAfter(currentDue)) {
            throw new IllegalArgumentException("New due date must be after the current due date");
        }
        if (!repository.renewLoan(loan.getLoanId(), newDueDate)) {
            throw new IllegalStateException("Loan renewal failed");
        }
        loan.setDueDate(newDueDate);
        loan.setRenewalCount(loan.getRenewalCount() + 1);
        DataSyncManager.getInstance().notifyLoanDataChanged();
        return loan;
    }

    public List<Loan> getLoanHistory(int studentId, Integer month, Integer year) throws Exception {
        List<Loan> history = repository.getLoanHistoryByStudent(studentId);
        if ((month == null || month == 0) && (year == null || year == 0)) {
            return history;
        }
        List<Loan> filtered = new ArrayList<>();
        for (Loan loan : history) {
            LocalDate loanDate = loan.getLoanDate();
            if (loanDate == null) {
                continue;
            }
            if (month != null && month != 0 && loanDate.getMonthValue() != month) {
                continue;
            }
            if (year != null && year != 0 && loanDate.getYear() != year) {
                continue;
            }
            filtered.add(loan);
        }
        return filtered;
    }

    // -----------------------------------------------------------------
    //  Fine operations
    // -----------------------------------------------------------------

    public List<Fine> getUnpaidFinesByStudent(int studentId) throws Exception {
        return repository.getUnpaidFinesByStudent(studentId);
    }

    public List<Fine> getFineHistory(int studentId) throws Exception {
        return repository.getAllFineHistory(studentId);
    }

    /**
     * Fine history filtered by fine_date month/year. If month/year are null or zero,
     * returns all fines (delegates to repository and filters in controller).
     */
    public List<Fine> getFineHistory(int studentId, Integer month, Integer year) throws Exception {
        return repository.getFineHistoryByStudentMonthYear(studentId, month, year);
    }

    public List<Fine> getAllFines() throws Exception {
        return repository.getAllFines();
    }

    /**
     * Get fines that were paid by a student in a specific month/year (by payment_date).
     * If month/year are null or zero, returns all paid fines.
     */
    public List<Fine> getPaidFinesByStudentInMonth(int studentId, Integer month, Integer year) throws Exception {
        return repository.getPaidFinesByStudentInMonth(studentId, month, year);
    }

    /**
     * Sum of fines paid by a student in a specific month/year (by payment_date).
     */
    public java.math.BigDecimal getMonthlyFinesPaidTotal(int studentId, Integer month, Integer year) throws Exception {
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        for (Fine f : getPaidFinesByStudentInMonth(studentId, month, year)) {
            if (f.getFineAmount() != null) {
                total = total.add(f.getFineAmount());
            }
        }
        return total;
    }

    public Fine payFine(int fineId) throws Exception {
        if (!repository.payFine(fineId)) {
            throw new IllegalStateException("Could not update fine status");
        }
        DataSyncManager.getInstance().notifyFineDataChanged();
        return findFineById(fineId);
    }

    public BigDecimal payAllFines(int studentId) throws Exception {
        List<Fine> unpaid = repository.getUnpaidFinesByStudent(studentId);
        BigDecimal total = BigDecimal.ZERO;
        for (Fine fine : unpaid) {
            if (repository.payFine(fine.getFineId())) {
                total = total.add(fine.getFineAmount());
            }
        }
        if (!unpaid.isEmpty()) {
            DataSyncManager.getInstance().notifyFineDataChanged();
        }
        return total;
    }

    private Fine findFineById(int fineId) throws Exception {
        for (Fine fine : repository.getAllFines()) {
            if (fine.getFineId() == fineId) {
                return fine;
            }
        }
        return null;
    }

    // -----------------------------------------------------------------
    //  Helper utilities
    // -----------------------------------------------------------------

    private String normalise(String term) {
        return term == null ? "" : term.trim().toLowerCase(Locale.ROOT);
    }

    private boolean contains(String value, String query) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(query);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private long calculateOverdueDays(LocalDate dueDate, LocalDate referenceDate) {
        if (dueDate == null || referenceDate == null) {
            return 0;
        }
        if (!referenceDate.isAfter(dueDate)) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(dueDate, referenceDate);
    }


    // -----------------------------------------------------------------
    //  DTOs
    // -----------------------------------------------------------------

    public static class ReturnResult {
        private boolean fineCreated;
        private BigDecimal fineAmount = BigDecimal.ZERO;
        private long daysOverdue;
        private LocalDate returnDate;

        public boolean isFineCreated() {
            return fineCreated;
        }

        public void setFineCreated(boolean fineCreated) {
            this.fineCreated = fineCreated;
        }

        public BigDecimal getFineAmount() {
            return fineAmount;
        }

        public void setFineAmount(BigDecimal fineAmount) {
            this.fineAmount = fineAmount;
        }

        public long getDaysOverdue() {
            return daysOverdue;
        }

        public void setDaysOverdue(long daysOverdue) {
            this.daysOverdue = daysOverdue;
        }

        public LocalDate getReturnDate() {
            return returnDate;
        }

        public void setReturnDate(LocalDate returnDate) {
            this.returnDate = returnDate;
        }
    }
}
