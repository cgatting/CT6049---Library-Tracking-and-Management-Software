package com.library.test;

import com.library.controller.LibraryController;
import com.library.model.Book;
import com.library.model.Fine;
import com.library.model.Loan;
import com.library.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive Unit Test Suite for LibraryController.
 * This tests effectively every "button" logic in the application.
 */
class LibraryControllerFullTest {

    private LibraryController controller;
    private FakeLibraryRepository repository;

    @BeforeEach
    void setUp() {
        repository = new FakeLibraryRepository();
        controller = new LibraryController(repository);
    }

    // -------------------------------------------------------------------------
    // Student Management Tests (UserManagementPanel)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should add and retrieve a student")
    void testAddStudent() throws Exception {
        Student s = new Student();
        s.setName("John Doe");
        s.setEmail("john@example.com");
        s.setPhone("1234567890");
        s.setAddress("123 Main St");
        
        controller.addStudent(s);
        
        List<Student> students = controller.getAllStudents();
        assertEquals(1, students.size());
        assertEquals("John Doe", students.get(0).getName());
        assertNotNull(students.get(0).getStudentId());
    }

    @Test
    @DisplayName("Should validate student input")
    void testAddInvalidStudent() {
        Student s = new Student();
        // Missing name and email
        
        assertThrows(IllegalArgumentException.class, () -> controller.addStudent(s));
    }

    @Test
    @DisplayName("Should update student details")
    void testUpdateStudent() throws Exception {
        Student s = new Student();
        s.setName("Old Name");
        s.setEmail("old@example.com");
        controller.addStudent(s);
        
        s.setName("New Name");
        controller.updateStudent(s);
        
        Student updated = repository.getStudentById(s.getStudentId());
        assertEquals("New Name", updated.getName());
    }

    @Test
    @DisplayName("Should delete student")
    void testDeleteStudent() throws Exception {
        Student s = new Student();
        s.setName("To Delete");
        s.setEmail("del@example.com");
        controller.addStudent(s);
        
        controller.deleteStudent(s.getStudentId());
        
        assertTrue(controller.getAllStudents().isEmpty());
    }

    @Test
    @DisplayName("Should search students")
    void testSearchStudents() throws Exception {
        Student s1 = new Student(); s1.setName("Alice"); s1.setEmail("alice@test.com");
        Student s2 = new Student(); s2.setName("Bob"); s2.setEmail("bob@test.com");
        controller.addStudent(s1);
        controller.addStudent(s2);
        
        List<Student> results = controller.searchStudents("Alice");
        assertEquals(1, results.size());
        assertEquals("Alice", results.get(0).getName());
    }

    // -------------------------------------------------------------------------
    // Book Management Tests (BookManagementPanel)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should add and retrieve book")
    void testAddBook() throws Exception {
        Book b = new Book();
        b.setTitle("Java Programming");
        b.setAuthor("Gosling");
        b.setTotalCopies(5);
        b.setAvailableCopies(5);
        
        controller.addBook(b);
        
        List<Book> books = controller.getAllBooks();
        assertEquals(1, books.size());
        assertEquals("Java Programming", books.get(0).getTitle());
    }

    @Test
    @DisplayName("Should not add book with invalid copies")
    void testAddBookInvalidCopies() {
        Book b = new Book();
        b.setTitle("Bad Book");
        b.setAuthor("Author");
        b.setTotalCopies(5);
        b.setAvailableCopies(10); // Impossible
        
        assertThrows(IllegalArgumentException.class, () -> controller.addBook(b));
    }

    @Test
    @DisplayName("Should update book")
    void testUpdateBook() throws Exception {
        Book b = new Book();
        b.setTitle("Original Title");
        b.setAuthor("Author");
        b.setTotalCopies(1);
        b.setAvailableCopies(1);
        controller.addBook(b);
        
        b.setTitle("Updated Title");
        controller.updateBook(b);
        
        Book updated = repository.getBookById(b.getBookId());
        assertEquals("Updated Title", updated.getTitle());
    }

    @Test
    @DisplayName("Should delete book")
    void testDeleteBook() throws Exception {
        Book b = new Book();
        b.setTitle("To Delete");
        b.setAuthor("A");
        b.setTotalCopies(1);
        b.setAvailableCopies(1);
        controller.addBook(b);
        
        controller.deleteBook(b.getBookId());
        
        assertTrue(controller.getAllBooks().isEmpty());
    }

    @Test
    @DisplayName("Should search books available only")
    void testSearchAvailableBooks() throws Exception {
        Book b1 = new Book(); b1.setTitle("Available Book"); b1.setAuthor("A"); b1.setTotalCopies(1); b1.setAvailableCopies(1);
        Book b2 = new Book(); b2.setTitle("Unavailable Book"); b2.setAuthor("B"); b2.setTotalCopies(1); b2.setAvailableCopies(0);
        controller.addBook(b1);
        controller.addBook(b2);
        
        List<Book> all = controller.searchBooks("Book", false);
        assertEquals(2, all.size());
        
        List<Book> available = controller.searchBooks("Book", true);
        assertEquals(1, available.size());
        assertEquals("Available Book", available.get(0).getTitle());
    }

    // -------------------------------------------------------------------------
    // Borrow/Return Tests (BorrowReturnPanel)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should borrow book successfully")
    void testBorrowBook() throws Exception {
        Student s = new Student(); s.setName("Student"); s.setEmail("s@s.com"); controller.addStudent(s);
        Book b = new Book(); b.setTitle("Book"); b.setAuthor("Auth"); b.setTotalCopies(5); b.setAvailableCopies(5); controller.addBook(b);
        
        Loan loan = controller.borrowBook(s.getStudentId(), b.getBookId());
        
        assertNotNull(loan);
        assertEquals("ACTIVE", loan.getStatus());
        assertEquals(s.getStudentId(), loan.getStudentId());
        assertEquals(b.getBookId(), loan.getBookId());
        
        // Check availability decreased
        Book storedBook = repository.getBookById(b.getBookId());
        assertEquals(4, storedBook.getAvailableCopies());
    }

    @Test
    @DisplayName("Should prevent borrowing if no copies available")
    void testBorrowNoCopies() throws Exception {
        Student s = new Student(); s.setName("Student"); s.setEmail("s@s.com"); controller.addStudent(s);
        Book b = new Book(); b.setTitle("Book"); b.setAuthor("Auth"); b.setTotalCopies(1); b.setAvailableCopies(0); controller.addBook(b);
        
        assertThrows(IllegalStateException.class, () -> controller.borrowBook(s.getStudentId(), b.getBookId()));
    }

    @Test
    @DisplayName("Should prevent double borrowing same book")
    void testDoubleBorrow() throws Exception {
        Student s = new Student(); s.setName("Student"); s.setEmail("s@s.com"); controller.addStudent(s);
        Book b = new Book(); b.setTitle("Book"); b.setAuthor("Auth"); b.setTotalCopies(5); b.setAvailableCopies(5); controller.addBook(b);
        
        controller.borrowBook(s.getStudentId(), b.getBookId());
        
        assertThrows(IllegalStateException.class, () -> controller.borrowBook(s.getStudentId(), b.getBookId()));
    }

    @Test
    @DisplayName("Should return book without fine if on time")
    void testReturnBookOnTime() throws Exception {
        Student s = new Student(); s.setName("S"); s.setEmail("e"); controller.addStudent(s);
        Book b = new Book(); b.setTitle("B"); b.setAuthor("A"); b.setTotalCopies(1); b.setAvailableCopies(0); controller.addBook(b);
        
        // Bypass controller to force a past loan that isn't overdue
        Loan loan = new Loan();
        loan.setStudentId(s.getStudentId());
        loan.setBookId(b.getBookId());
        loan.setLoanDate(LocalDate.now().minusDays(5));
        loan.setDueDate(LocalDate.now().plusDays(5));
        loan.setStatus("ACTIVE");
        int loanId = repository.createLoan(loan);
        loan.setLoanId(loanId);
        
        LibraryController.ReturnResult result = controller.returnLoan(loan);
        
        assertFalse(result.isFineCreated());
        assertEquals("RETURNED", repository.getLoanById(loanId).getStatus());
        assertEquals(1, repository.getBookById(b.getBookId()).getAvailableCopies());
    }

    @Test
    @DisplayName("Should create fine when returning overdue book")
    void testReturnBookOverdue() throws Exception {
        Student s = new Student(); s.setName("S"); s.setEmail("e"); controller.addStudent(s);
        Book b = new Book(); b.setTitle("B"); b.setAuthor("A"); b.setTotalCopies(1); b.setAvailableCopies(0); controller.addBook(b);
        
        Loan loan = new Loan();
        loan.setStudentId(s.getStudentId());
        loan.setBookId(b.getBookId());
        loan.setLoanDate(LocalDate.now().minusDays(20));
        loan.setDueDate(LocalDate.now().minusDays(5)); // 5 days overdue
        loan.setStatus("ACTIVE");
        int loanId = repository.createLoan(loan);
        loan.setLoanId(loanId);
        
        LibraryController.ReturnResult result = controller.returnLoan(loan);
        
        assertTrue(result.isFineCreated());
        // 5 days * $0.50 = $2.50
        assertEquals(0, BigDecimal.valueOf(2.50).compareTo(result.getFineAmount()));
        assertEquals(5, result.getDaysOverdue());
    }

    @Test
    @DisplayName("Should renew loan")
    void testRenewLoan() throws Exception {
        Student s = new Student(); s.setName("S"); s.setEmail("e"); controller.addStudent(s);
        Book b = new Book(); b.setTitle("B"); b.setAuthor("A"); b.setTotalCopies(1); b.setAvailableCopies(1); controller.addBook(b);
        
        Loan loan = controller.borrowBook(s.getStudentId(), b.getBookId());
        LocalDate originalDue = loan.getDueDate();
        LocalDate newDue = originalDue.plusDays(14);
        
        controller.renewLoan(loan, newDue);
        
        Loan updated = repository.getLoanById(loan.getLoanId());
        assertEquals(newDue, updated.getDueDate());
        assertEquals(1, updated.getRenewalCount());
    }

    // -------------------------------------------------------------------------
    // Fine Management Tests (FinePaymentPanel)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should pay single fine")
    void testPayFine() throws Exception {
        Student s = new Student(); s.setName("S"); s.setEmail("e"); controller.addStudent(s);
        Fine f = new Fine();
        f.setStudentId(s.getStudentId());
        f.setFineAmount(BigDecimal.valueOf(10.00));
        f.setPaymentStatus("UNPAID");
        int fineId = repository.createFine(f);
        
        controller.payFine(fineId);
        
        // Re-fetch fine directly to check status as controller doesn't return object on pay
        // Actually fineDAO doesn't have getFineById exposed on interface easily, 
        // but our fake repo does have a map.
        // We can use getUnpaidFines to verify it's gone.
        assertTrue(controller.getUnpaidFinesByStudent(s.getStudentId()).isEmpty());
        
        // Verify via Paid history
        List<Fine> paid = controller.getPaidFinesByStudentInMonth(s.getStudentId(), null, null);
        assertEquals(1, paid.size());
        assertEquals("PAID", paid.get(0).getPaymentStatus());
    }

    @Test
    @DisplayName("Should pay all fines for student")
    void testPayAllFines() throws Exception {
        Student s = new Student(); s.setName("S"); s.setEmail("e"); controller.addStudent(s);
        Fine f1 = new Fine(); f1.setStudentId(s.getStudentId()); f1.setFineAmount(BigDecimal.TEN); f1.setPaymentStatus("UNPAID");
        Fine f2 = new Fine(); f2.setStudentId(s.getStudentId()); f2.setFineAmount(BigDecimal.TEN); f2.setPaymentStatus("UNPAID");
        repository.createFine(f1);
        repository.createFine(f2);
        
        BigDecimal totalPaid = controller.payAllFines(s.getStudentId());
        
        assertEquals(0, BigDecimal.valueOf(20).compareTo(totalPaid));
        assertTrue(controller.getUnpaidFinesByStudent(s.getStudentId()).isEmpty());
    }

    // -------------------------------------------------------------------------
    // Report Tests (ReportsPanel)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should filter loan history by date")
    void testLoanHistoryFiltering() throws Exception {
        Student s = new Student(); s.setName("S"); s.setEmail("e"); controller.addStudent(s);
        Book b = new Book(); b.setTitle("B"); b.setAuthor("A"); b.setTotalCopies(1); b.setAvailableCopies(1); controller.addBook(b);
        
        Loan l1 = new Loan(); 
        l1.setStudentId(s.getStudentId()); 
        l1.setLoanDate(LocalDate.of(2023, 1, 15)); 
        l1.setStatus("RETURNED");
        repository.createLoan(l1);
        
        Loan l2 = new Loan(); 
        l2.setStudentId(s.getStudentId()); 
        l2.setLoanDate(LocalDate.of(2023, 2, 15)); 
        l2.setStatus("RETURNED");
        repository.createLoan(l2);
        
        List<Loan> janLoans = controller.getLoanHistory(s.getStudentId(), 1, 2023);
        assertEquals(1, janLoans.size());
        assertEquals(LocalDate.of(2023, 1, 15), janLoans.get(0).getLoanDate());
        
        List<Loan> allLoans = controller.getLoanHistory(s.getStudentId(), null, null);
        assertEquals(2, allLoans.size());
    }
}
