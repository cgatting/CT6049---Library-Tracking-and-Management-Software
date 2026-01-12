package com.library.test;

import com.library.model.Book;
import com.library.model.Fine;
import com.library.model.Loan;
import com.library.model.Student;
import com.library.service.LibraryRepository;
import com.library.service.RepositoryType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * In-memory implementation of LibraryRepository for testing.
 */
public class FakeLibraryRepository implements LibraryRepository {

    private final Map<Integer, Student> students = new HashMap<>();
    private final Map<Integer, Book> books = new HashMap<>();
    private final Map<Integer, Loan> loans = new HashMap<>();
    private final Map<Integer, Fine> fines = new HashMap<>();

    private final AtomicInteger studentIdSeq = new AtomicInteger(1);
    private final AtomicInteger bookIdSeq = new AtomicInteger(1);
    private final AtomicInteger loanIdSeq = new AtomicInteger(1);
    private final AtomicInteger fineIdSeq = new AtomicInteger(1);

    @Override
    public boolean testConnection() {
        return true;
    }

    @Override
    public void close() {
        // no-op
    }

    @Override
    public RepositoryType getRepositoryType() {
        return RepositoryType.ORACLE; // Pretend to be Oracle
    }

    // Students
    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }

    @Override
    public Student getStudentById(int studentId) {
        return students.get(studentId);
    }

    @Override
    public void addStudent(Student student) {
        int id = studentIdSeq.getAndIncrement();
        student.setStudentId(id);
        students.put(id, student);
    }

    @Override
    public void updateStudent(Student student) {
        students.put(student.getStudentId(), student);
    }

    @Override
    public void deleteStudent(int studentId) {
        students.remove(studentId);
    }

    // Books
    @Override
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    @Override
    public List<Book> getAvailableBooks() {
        return books.values().stream()
                .filter(b -> b.getAvailableCopies() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public Book getBookById(int bookId) {
        return books.get(bookId);
    }

    @Override
    public void addBook(Book book) {
        int id = bookIdSeq.getAndIncrement();
        book.setBookId(id);
        books.put(id, book);
    }

    @Override
    public void updateBook(Book book) {
        books.put(book.getBookId(), book);
    }

    @Override
    public void deleteBook(int bookId) {
        books.remove(bookId);
    }

    @Override
    public void updateBookAvailability(int bookId, int availableCopies) {
        Book book = books.get(bookId);
        if (book != null) {
            book.setAvailableCopies(availableCopies);
        }
    }

    // Loans
    @Override
    public int createLoan(Loan loan) {
        int id = loanIdSeq.getAndIncrement();
        loan.setLoanId(id);
        loans.put(id, loan);
        return id;
    }

    @Override
    public List<Loan> getActiveLoans() {
        return loans.values().stream()
                .filter(l -> "ACTIVE".equals(l.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Loan> getAllActiveLoans() {
        return loans.values().stream()
                .filter(l -> !"RETURNED".equals(l.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Loan> getOverdueLoans() {
        LocalDate today = LocalDate.now();
        return loans.values().stream()
                .filter(l -> "ACTIVE".equals(l.getStatus()) && l.getDueDate().isBefore(today))
                .collect(Collectors.toList());
    }

    @Override
    public List<Loan> getLoansByStudent(int studentId) {
        return loans.values().stream()
                .filter(l -> l.getStudentId() == studentId)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Loan> getLoanHistoryByStudent(int studentId) {
        return getLoansByStudent(studentId);
    }

    @Override
    public Loan getLoanById(int loanId) {
        return loans.get(loanId);
    }

    @Override
    public boolean hasActiveLoan(int studentId, int bookId) {
        return loans.values().stream()
                .anyMatch(l -> l.getStudentId() == studentId && l.getBookId() == bookId && "ACTIVE".equals(l.getStatus()));
    }

    @Override
    public boolean returnLoan(int loanId) {
        Loan loan = loans.get(loanId);
        if (loan != null && !"RETURNED".equals(loan.getStatus())) {
            loan.setStatus("RETURNED");
            loan.setReturnDate(LocalDate.now());
            return true;
        }
        return false;
    }

    @Override
    public boolean renewLoan(int loanId, LocalDate newDueDate) {
        Loan loan = loans.get(loanId);
        if (loan != null) {
            loan.setDueDate(newDueDate);
            // In a real DB, this happens in SQL. In-memory, if we modify the object here,
            // and the Controller also modifies the object (to sync UI), we get double increment
            // because they share the reference. 
            // We rely on the Controller's update for the in-memory object state in this test harness.
            return true;
        }
        return false;
    }

    // Fines
    @Override
    public int createFine(Fine fine) {
        int id = fineIdSeq.getAndIncrement();
        fine.setFineId(id);
        fines.put(id, fine);
        return id;
    }

    @Override
    public List<Fine> getUnpaidFinesByStudent(int studentId) {
        return fines.values().stream()
                .filter(f -> f.getStudentId() == studentId && "UNPAID".equals(f.getPaymentStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Fine> getAllFineHistory(int studentId) {
        return fines.values().stream()
                .filter(f -> f.getStudentId() == studentId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Fine> getAllFines() {
        return new ArrayList<>(fines.values());
    }

    @Override
    public boolean payFine(int fineId) {
        Fine fine = fines.get(fineId);
        if (fine != null && "UNPAID".equals(fine.getPaymentStatus())) {
            fine.setPaymentStatus("PAID");
            fine.setPaymentDate(LocalDate.now());
            return true;
        }
        return false;
    }
}
