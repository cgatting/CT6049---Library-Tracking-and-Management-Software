package com.library.integration;

import com.library.controller.LibraryController;
import com.library.controller.LibraryController.ReturnResult;
import com.library.model.Book;
import com.library.model.Fine;
import com.library.model.Loan;
import com.library.model.Student;
import com.library.service.LibraryContext;
import com.library.service.RepositoryType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test that exercises the MongoDB-backed workflow end-to-end,
 * mirroring the manual JShell smoke script.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MongoRepositoryIntegrationTest {

    private LibraryController controller;
    private final List<Integer> createdStudentIds = new CopyOnWriteArrayList<>();
    private final List<Integer> createdBookIds = new CopyOnWriteArrayList<>();

    @BeforeAll
    void setUp() throws Exception {
        LibraryContext.initialize(RepositoryType.MONGODB);
        controller = new LibraryController(LibraryContext.getRepository());
    }

    @AfterEach
    void cleanUpEntities() {
        for (Integer bookId : new ArrayList<>(createdBookIds)) {
            try {
                controller.deleteBook(bookId);
            } catch (Exception ignored) {
                // best-effort cleanup
            } finally {
                createdBookIds.remove(bookId);
            }
        }
        for (Integer studentId : new ArrayList<>(createdStudentIds)) {
            try {
                controller.deleteStudent(studentId);
            } catch (Exception ignored) {
                // best-effort cleanup
            } finally {
                createdStudentIds.remove(studentId);
            }
        }
    }

    @AfterAll
    void tearDown() {
        LibraryContext.shutdown();
    }

    @Test
    void endToEndMongoWorkflow() throws Exception {
        String tag = "QA" + System.currentTimeMillis();

        Student student = new Student();
        student.setName("QA Student " + tag);
        student.setEmail(tag + "@example.com");
        student.setPhone("555-0101");
        student.setAddress("1 Testing Way");
        student.setRegistrationDate(LocalDate.now());

        controller.addStudent(student);
        createdStudentIds.add(student.getStudentId());
        assertTrue(student.getStudentId() > 0, "Student ID should be assigned");

        student.setPhone("555-9999");
        controller.updateStudent(student);
        List<Student> studentMatches = controller.searchStudents(tag);
        assertEquals(1, studentMatches.size(), "Updated student should be searchable by tag");
        assertEquals("555-9999", studentMatches.get(0).getPhone(), "Phone number should reflect update");

        Book book = new Book();
        book.setTitle("Automation Adventures " + tag);
        book.setAuthor("Quality Author");
        book.setIsbn("ISBN-" + tag);
        book.setPublisher("QA Press");
        book.setCategory("Testing");
        book.setPublicationYear(2024);
        book.setLocation("Shelf QA");
        book.setTotalCopies(4);
        book.setAvailableCopies(4);

        controller.addBook(book);
        createdBookIds.add(book.getBookId());
        assertTrue(book.getBookId() > 0, "Book ID should be assigned");

        book.setAvailableCopies(3);
        controller.updateBook(book);
        List<Book> bookMatches = controller.searchBooks(tag, false);
        assertFalse(bookMatches.isEmpty(), "Book lookup should find the updated title");
        assertEquals(3, bookMatches.get(0).getAvailableCopies(), "Book availability should reflect update");

        Loan loan = controller.borrowBook(student.getStudentId(), book.getBookId());
        assertNotNull(loan, "Borrow should return loan details");
        assertTrue(loan.getLoanId() > 0, "Loan ID should be assigned");
        assertNotNull(loan.getDueDate(), "Loan should have a due date");

        LocalDate renewedDueDate = loan.getDueDate().plusDays(7);
        Loan renewed = controller.renewLoan(loan, renewedDueDate);
        assertEquals(renewedDueDate, renewed.getDueDate(), "Renewed loan should reflect new due date");
        assertEquals(1, renewed.getRenewalCount(), "Renewal count should increment");

        // Force an overdue scenario for fine creation
        loan.setDueDate(LocalDate.now().minusDays(3));
        ReturnResult returnResult = controller.returnLoan(loan);
        assertTrue(returnResult.isFineCreated(), "Returning overdue loan should create fine");
        assertNotNull(returnResult.getFineAmount(), "Fine amount should be populated");
        assertTrue(returnResult.getFineAmount().doubleValue() > 0, "Fine amount should be positive");

        List<Fine> unpaidFines = controller.getUnpaidFinesByStudent(student.getStudentId());
        assertFalse(unpaidFines.isEmpty(), "Fine should be recorded as unpaid");

        Fine fine = unpaidFines.get(0);
        controller.payFine(fine.getFineId());
        List<Fine> outstandingAfterPayment = controller.getUnpaidFinesByStudent(student.getStudentId());
        assertTrue(outstandingAfterPayment.isEmpty(), "Fine should be cleared after payment");
    }
}
