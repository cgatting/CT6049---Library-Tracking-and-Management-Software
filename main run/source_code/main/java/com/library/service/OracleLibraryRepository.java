package com.library.service;

import com.library.dao.BookDAO;
import com.library.dao.FineDAO;
import com.library.dao.LoanDAO;
import com.library.dao.OracleConnectionManager;
import com.library.dao.StudentDAO;
import com.library.model.Book;
import com.library.model.Fine;
import com.library.model.Loan;
import com.library.model.Student;

import java.time.LocalDate;
import java.util.List;

/**
 * Oracle-backed implementation of {@link LibraryRepository} that delegates to the
 * existing DAO classes.
 */
class OracleLibraryRepository implements LibraryRepository {

    private final StudentDAO studentDAO;
    private final BookDAO bookDAO;
    private final LoanDAO loanDAO;
    private final FineDAO fineDAO;

    OracleLibraryRepository() {
        this.studentDAO = new StudentDAO();
        this.bookDAO = new BookDAO();
        this.loanDAO = new LoanDAO();
        this.fineDAO = new FineDAO();
    }

    // ---- Lifecycle --------------------------------------------------
    @Override
    public boolean testConnection() throws Exception {
        return OracleConnectionManager.getInstance().testConnection();
    }

    @Override
    public void close() {
        OracleConnectionManager.getInstance().closeConnection();
    }

    @Override
    public RepositoryType getRepositoryType() {
        return RepositoryType.ORACLE;
    }

    // ---- Students ---------------------------------------------------
    @Override
    public List<Student> getAllStudents() throws Exception {
        return studentDAO.getAllStudents();
    }

    @Override
    public Student getStudentById(int studentId) throws Exception {
        return studentDAO.getStudentById(studentId);
    }

    @Override
    public void addStudent(Student student) throws Exception {
        studentDAO.addStudent(student);
    }

    @Override
    public void updateStudent(Student student) throws Exception {
        studentDAO.updateStudent(student);
    }

    @Override
    public void deleteStudent(int studentId) throws Exception {
        studentDAO.deleteStudent(studentId);
    }

    // ---- Books ------------------------------------------------------
    @Override
    public List<Book> getAllBooks() throws Exception {
        return bookDAO.getAllBooks();
    }

    @Override
    public List<Book> getAvailableBooks() throws Exception {
        return bookDAO.getAvailableBooks();
    }

    @Override
    public Book getBookById(int bookId) throws Exception {
        return bookDAO.getBookById(bookId);
    }

    @Override
    public void addBook(Book book) throws Exception {
        bookDAO.addBook(book);
    }

    @Override
    public void updateBook(Book book) throws Exception {
        bookDAO.updateBook(book);
    }

    @Override
    public void deleteBook(int bookId) throws Exception {
        bookDAO.deleteBook(bookId);
    }

    @Override
    public void updateBookAvailability(int bookId, int availableCopies) throws Exception {
        bookDAO.updateAvailableCopies(bookId, availableCopies);
    }

    // ---- Loans ------------------------------------------------------
    @Override
    public int createLoan(Loan loan) throws Exception {
        return loanDAO.createLoan(loan);
    }

    @Override
    public List<Loan> getActiveLoans() throws Exception {
        return loanDAO.getActiveLoans();
    }

    @Override
    public List<Loan> getAllActiveLoans() throws Exception {
        return loanDAO.getAllActiveLoans();
    }

    @Override
    public List<Loan> getOverdueLoans() throws Exception {
        return loanDAO.getOverdueLoans();
    }

    @Override
    public List<Loan> getLoansByStudent(int studentId) throws Exception {
        return loanDAO.getLoansByStudentId(studentId);
    }

    @Override
    public List<Loan> getLoanHistoryByStudent(int studentId) throws Exception {
        return loanDAO.getLoanHistoryByStudentId(studentId);
    }

    @Override
    public Loan getLoanById(int loanId) throws Exception {
        return loanDAO.getLoanById(loanId);
    }

    @Override
    public boolean hasActiveLoan(int studentId, int bookId) throws Exception {
        return loanDAO.hasActiveLoan(studentId, bookId);
    }

    @Override
    public boolean returnLoan(int loanId) throws Exception {
        return loanDAO.returnBook(loanId);
    }

    @Override
    public boolean renewLoan(int loanId, LocalDate newDueDate) throws Exception {
        return loanDAO.renewLoan(loanId, newDueDate);
    }

    // ---- Fines ------------------------------------------------------
    @Override
    public int createFine(Fine fine) throws Exception {
        return fineDAO.createFine(fine);
    }

    @Override
    public List<Fine> getUnpaidFinesByStudent(int studentId) throws Exception {
        return fineDAO.getUnpaidFinesByStudentId(studentId);
    }

    @Override
    public List<Fine> getAllFineHistory(int studentId) throws Exception {
        return fineDAO.getAllFineHistory(studentId);
    }

    @Override
    public List<Fine> getAllFines() throws Exception {
        return fineDAO.getAllFines();
    }

    @Override
    public boolean payFine(int fineId) throws Exception {
        return fineDAO.payFine(fineId);
    }

    @Override
    public List<Fine> getFineHistoryByStudentMonthYear(int studentId, Integer month, Integer year) throws Exception {
        if (month != null && month != 0 && year != null && year != 0) {
            return fineDAO.getFineHistory(studentId, month, year);
        }
        // For other combinations, fall back to unfiltered + controller filtering
        return fineDAO.getAllFineHistory(studentId);
    }

    @Override
    public List<Fine> getPaidFinesByStudentInMonth(int studentId, Integer month, Integer year) throws Exception {
        if (month != null && month != 0 && year != null && year != 0) {
            return fineDAO.getPaidFinesByStudentInMonth(studentId, month, year);
        }
        // Fallback: get all and filter in controller
        return fineDAO.getAllFineHistory(studentId);
    }
}
