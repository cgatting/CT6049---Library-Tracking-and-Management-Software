package com.library.service;

import com.library.model.Book;
import com.library.model.Fine;
import com.library.model.Loan;
import com.library.model.Student;

import java.time.LocalDate;
import java.util.List;

/**
 * Abstraction over the persistence layer so that Oracle and MongoDB backends
 * can share the same Swing user interface.
 */
public interface LibraryRepository {

    // ---- Lifecycle -----------------------------------------------------
    default boolean testConnection() throws Exception {
        return true;
    }

    default void close() {
        // no-op by default
    }

    default RepositoryType getRepositoryType() {
        return RepositoryType.ORACLE;
    }

    // ---- Students ------------------------------------------------------
    List<Student> getAllStudents() throws Exception;

    Student getStudentById(int studentId) throws Exception;

    void addStudent(Student student) throws Exception;

    void updateStudent(Student student) throws Exception;

    void deleteStudent(int studentId) throws Exception;

    // ---- Books ---------------------------------------------------------
    List<Book> getAllBooks() throws Exception;

    List<Book> getAvailableBooks() throws Exception;

    Book getBookById(int bookId) throws Exception;

    void addBook(Book book) throws Exception;

    void updateBook(Book book) throws Exception;

    void deleteBook(int bookId) throws Exception;

    void updateBookAvailability(int bookId, int availableCopies) throws Exception;

    // ---- Loans ---------------------------------------------------------
    int createLoan(Loan loan) throws Exception;

    List<Loan> getActiveLoans() throws Exception;

    List<Loan> getAllActiveLoans() throws Exception;

    List<Loan> getOverdueLoans() throws Exception;

    List<Loan> getLoansByStudent(int studentId) throws Exception;

    List<Loan> getLoanHistoryByStudent(int studentId) throws Exception;

    Loan getLoanById(int loanId) throws Exception;

    boolean hasActiveLoan(int studentId, int bookId) throws Exception;

    boolean returnLoan(int loanId) throws Exception;

    boolean renewLoan(int loanId, LocalDate newDueDate) throws Exception;

    // ---- Fines ---------------------------------------------------------
    int createFine(Fine fine) throws Exception;

    List<Fine> getUnpaidFinesByStudent(int studentId) throws Exception;

    List<Fine> getAllFineHistory(int studentId) throws Exception;

    List<Fine> getAllFines() throws Exception;

    boolean payFine(int fineId) throws Exception;
}
