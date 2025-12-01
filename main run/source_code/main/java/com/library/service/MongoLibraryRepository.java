package com.library.service;

import com.library.config.DatabaseConfig;
import com.library.model.Book;
import com.library.model.Fine;
import com.library.model.Loan;
import com.library.model.Student;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Decimal128;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * MongoDB-backed implementation of {@link LibraryRepository}.
 */
class MongoLibraryRepository implements LibraryRepository {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    private final MongoClient client;
    private final MongoDatabase database;
    private final MongoCollection<Document> students;
    private final MongoCollection<Document> books;
    private final MongoCollection<Document> loans;
    private final MongoCollection<Document> fines;
    private final MongoCollection<Document> counters;

    MongoLibraryRepository() {
        try {
            ConnectionString connectionString = new ConnectionString(DatabaseConfig.MONGODB_CONNECTION_STRING);
            MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToConnectionPoolSettings(builder -> 
                    builder.maxConnectionIdleTime(30, java.util.concurrent.TimeUnit.SECONDS)
                           .maxWaitTime(10, java.util.concurrent.TimeUnit.SECONDS))
                .applyToSocketSettings(builder -> 
                    builder.connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                           .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS))
                .build();

            this.client = MongoClients.create(settings);
            this.database = client.getDatabase(DatabaseConfig.MONGODB_DATABASE_NAME);
            this.students = database.getCollection("students");
            this.books = database.getCollection("books");
            this.loans = database.getCollection("loans");
            this.fines = database.getCollection("fines");
            this.counters = database.getCollection("counters");
            
            // Test connection immediately to fail fast if MongoDB is not available
            testConnection();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize MongoDB connection: " + e.getMessage(), e);
        }
    }

    // ---- Lifecycle --------------------------------------------------
    @Override
    public boolean testConnection() {
        try {
            database.runCommand(new Document("ping", 1));
            return true;
        } catch (Exception e) {
            throw new RuntimeException("MongoDB connection test failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        try {
            if (client != null) {
                client.close();
            }
        } catch (Exception e) {
            System.err.println("Warning: Error closing MongoDB connection: " + e.getMessage());
        }
    }

    @Override
    public RepositoryType getRepositoryType() {
        return RepositoryType.MONGODB;
    }

    // ---- Students ---------------------------------------------------
    @Override
    public List<Student> getAllStudents() {
        List<Student> results = new ArrayList<>();
        try (MongoCursor<Document> cursor = students.find()
                .sort(Sorts.ascending("name"))
                .iterator()) {
            while (cursor.hasNext()) {
                results.add(mapStudent(cursor.next()));
            }
        }
        return results;
    }

    @Override
    public Student getStudentById(int studentId) {
        Document doc = students.find(Filters.eq("student_id", studentId)).first();
        return doc != null ? mapStudent(doc) : null;
    }

    @Override
    public void addStudent(Student student) {
        int id = student.getStudentId() != 0 ? student.getStudentId() : getNextSequence("student_id");
        Document doc = new Document("student_id", id)
            .append("name", student.getName())
            .append("email", student.getEmail())
            .append("phone", student.getPhone())
            .append("address", student.getAddress())
            .append("registration_date", formatDate(defaultDate(student.getRegistrationDate())));
        students.insertOne(doc);
        student.setStudentId(id);
    }

    @Override
    public void updateStudent(Student student) {
        Document updates = new Document("name", student.getName())
            .append("email", student.getEmail())
            .append("phone", student.getPhone())
            .append("address", student.getAddress())
            .append("registration_date", formatDate(defaultDate(student.getRegistrationDate())));
        students.updateOne(Filters.eq("student_id", student.getStudentId()), new Document("$set", updates));
    }

    @Override
    public void deleteStudent(int studentId) {
        students.deleteOne(Filters.eq("student_id", studentId));
    }

    // ---- Books ------------------------------------------------------
    @Override
    public List<Book> getAllBooks() {
        List<Book> results = new ArrayList<>();
        try (MongoCursor<Document> cursor = books.find()
                .sort(Sorts.ascending("title"))
                .iterator()) {
            while (cursor.hasNext()) {
                results.add(mapBook(cursor.next()));
            }
        }
        return results;
    }

    @Override
    public List<Book> getAvailableBooks() {
        List<Book> results = new ArrayList<>();
        try (MongoCursor<Document> cursor = books.find(Filters.gt("available_copies", 0))
                .sort(Sorts.ascending("title"))
                .iterator()) {
            while (cursor.hasNext()) {
                results.add(mapBook(cursor.next()));
            }
        }
        return results;
    }

    @Override
    public Book getBookById(int bookId) {
        Document doc = books.find(Filters.eq("book_id", bookId)).first();
        return doc != null ? mapBook(doc) : null;
    }

    @Override
    public void addBook(Book book) {
        int id = book.getBookId() != 0 ? book.getBookId() : getNextSequence("book_id");
        Document doc = new Document("book_id", id)
            .append("title", book.getTitle())
            .append("author", book.getAuthor())
            .append("isbn", book.getIsbn())
            .append("category", book.getCategory())
            .append("publication_year", book.getPublicationYear())
            .append("available_copies", book.getAvailableCopies())
            .append("total_copies", book.getTotalCopies());
        books.insertOne(doc);
        book.setBookId(id);
    }

    @Override
    public void updateBook(Book book) {
        Document updates = new Document("title", book.getTitle())
            .append("author", book.getAuthor())
            .append("isbn", book.getIsbn())
            .append("category", book.getCategory())
            .append("publication_year", book.getPublicationYear())
            .append("available_copies", book.getAvailableCopies())
            .append("total_copies", book.getTotalCopies());
        books.updateOne(Filters.eq("book_id", book.getBookId()), new Document("$set", updates));
    }

    @Override
    public void deleteBook(int bookId) {
        books.deleteOne(Filters.eq("book_id", bookId));
    }

    @Override
    public void updateBookAvailability(int bookId, int availableCopies) {
        books.updateOne(Filters.eq("book_id", bookId), Updates.set("available_copies", availableCopies));
    }

    // ---- Loans ------------------------------------------------------
    @Override
    public int createLoan(Loan loan) {
        int id = loan.getLoanId() != 0 ? loan.getLoanId() : getNextSequence("loan_id");
        Document doc = new Document("loan_id", id)
            .append("student_id", loan.getStudentId())
            .append("book_id", loan.getBookId())
            .append("loan_date", formatDate(defaultDate(loan.getLoanDate())))
            .append("due_date", formatDate(defaultDate(loan.getDueDate())))
            .append("return_date", formatDate(loan.getReturnDate()))
            .append("status", Objects.toString(loan.getStatus(), "ACTIVE"))
            .append("renewal_count", loan.getRenewalCount());
        loans.insertOne(doc);
        books.updateOne(Filters.eq("book_id", loan.getBookId()), Updates.inc("available_copies", -1));
        loan.setLoanId(id);
        return id;
    }

    @Override
    public List<Loan> getActiveLoans() {
        return findLoans(Filters.eq("status", "ACTIVE"));
    }

    @Override
    public List<Loan> getAllActiveLoans() {
        return findLoans(Filters.ne("status", "RETURNED"));
    }

    @Override
    public List<Loan> getOverdueLoans() {
        LocalDate today = LocalDate.now();
        return findLoans(Filters.and(
            Filters.eq("status", "ACTIVE"),
            Filters.lt("due_date", formatDate(today))));
    }

    @Override
    public List<Loan> getLoansByStudent(int studentId) {
        return findLoans(Filters.eq("student_id", studentId));
    }

    @Override
    public List<Loan> getLoanHistoryByStudent(int studentId) {
        return findLoans(Filters.eq("student_id", studentId));
    }

    @Override
    public boolean hasActiveLoan(int studentId, int bookId) {
        Document doc = loans.find(Filters.and(
            Filters.eq("student_id", studentId),
            Filters.eq("book_id", bookId),
            Filters.eq("status", "ACTIVE")))
            .first();
        return doc != null;
    }

    @Override
    public Loan getLoanById(int loanId) {
        Document doc = loans.find(Filters.eq("loan_id", loanId)).first();
        return doc != null ? mapLoan(doc) : null;
    }

    @Override
    public boolean returnLoan(int loanId) {
        // First check if the loan is already returned
        Document existingLoan = loans.find(Filters.eq("loan_id", loanId)).first();
        if (existingLoan == null) {
            throw new IllegalArgumentException("Loan not found with ID: " + loanId);
        }
        
        String currentStatus = existingLoan.getString("status");
        if ("RETURNED".equals(currentStatus)) {
            throw new IllegalStateException("Cannot return book: Loan is already marked as RETURNED");
        }
        
        // Proceed with the return if validation passes
        LocalDate now = LocalDate.now();
        Document update = new Document("status", "RETURNED")
            .append("return_date", formatDate(now));
        
        // Only update if the current status is not RETURNED
        Document filter = new Document("loan_id", loanId)
            .append("status", new Document("$ne", "RETURNED"));
            
        boolean updated = loans.updateOne(filter, new Document("$set", update)).getModifiedCount() > 0;
        
        if (!updated) {
            throw new IllegalStateException("No active loan found to return or loan already returned");
        }
        
        if (updated) {
            Loan loan = getLoanById(loanId);
            if (loan != null) {
                books.updateOne(Filters.eq("book_id", loan.getBookId()), Updates.inc("available_copies", 1));
            }
        }
        return updated;
    }

    @Override
    public boolean renewLoan(int loanId, LocalDate newDueDate) {
        return loans.updateOne(
            Filters.eq("loan_id", loanId),
            new Document("$set", new Document("due_date", formatDate(newDueDate)))
                .append("$inc", new Document("renewal_count", 1)))
            .getModifiedCount() > 0;
    }

    // ---- Fines ------------------------------------------------------
    @Override
    public int createFine(Fine fine) {
        int id = fine.getFineId() != 0 ? fine.getFineId() : getNextSequence("fine_id");
        Document doc = new Document("fine_id", id)
            .append("student_id", fine.getStudentId())
            .append("loan_id", fine.getLoanId())
            .append("fine_amount", fine.getFineAmount() != null ? decimal128(fine.getFineAmount()) : decimal128(BigDecimal.ZERO))
            .append("fine_date", formatDate(defaultDate(fine.getFineDate())))
            .append("payment_date", formatDate(fine.getPaymentDate()))
            .append("payment_status", Objects.toString(fine.getPaymentStatus(), "UNPAID"));
        fines.insertOne(doc);
        fine.setFineId(id);
        return id;
    }

    @Override
    public List<Fine> getUnpaidFinesByStudent(int studentId) {
        return findFines(Filters.and(
            Filters.eq("student_id", studentId),
            Filters.eq("payment_status", "UNPAID")));
    }

    @Override
    public List<Fine> getAllFineHistory(int studentId) {
        return findFines(Filters.eq("student_id", studentId));
    }

    @Override
    public List<Fine> getAllFines() {
        return findFines(new Document());
    }

    @Override
    public boolean payFine(int fineId) {
        Document update = new Document("payment_status", "PAID")
            .append("payment_date", formatDate(LocalDate.now()));
        return fines.updateOne(Filters.eq("fine_id", fineId), new Document("$set", update)).getModifiedCount() > 0;
    }

    @Override
    public List<Fine> getFineHistoryByStudentMonthYear(int studentId, Integer month, Integer year) {
        boolean hasMonth = month != null && month != 0;
        boolean hasYear = year != null && year != 0;
        if (!hasMonth || !hasYear) {
            return getAllFineHistory(studentId);
        }
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        Bson filter = Filters.and(
            Filters.eq("student_id", studentId),
            Filters.gte("fine_date", formatDate(start)),
            Filters.lte("fine_date", formatDate(end))
        );
        List<Fine> results = new ArrayList<>();
        try (MongoCursor<Document> cursor = fines.find(filter).sort(Sorts.descending("fine_date")).iterator()) {
            while (cursor.hasNext()) {
                results.add(mapFine(cursor.next()));
            }
        }
        return results;
    }

    @Override
    public List<Fine> getPaidFinesByStudentInMonth(int studentId, Integer month, Integer year) {
        boolean hasMonth = month != null && month != 0;
        boolean hasYear = year != null && year != 0;
        if (!hasMonth || !hasYear) {
            // Fall back to default interface implementation with checked-exception handling
            try {
                return LibraryRepository.super.getPaidFinesByStudentInMonth(studentId, month, year);
            } catch (Exception e) {
                throw new RuntimeException("Failed to get paid fines by month/year: " + e.getMessage(), e);
            }
        }
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        Bson filter = Filters.and(
            Filters.eq("student_id", studentId),
            Filters.eq("payment_status", "PAID"),
            Filters.gte("payment_date", formatDate(start)),
            Filters.lte("payment_date", formatDate(end))
        );
        List<Fine> results = new ArrayList<>();
        try (MongoCursor<Document> cursor = fines.find(filter).sort(Sorts.descending("payment_date")).iterator()) {
            while (cursor.hasNext()) {
                results.add(mapFine(cursor.next()));
            }
        }
        return results;
    }

    // ---- Helpers ----------------------------------------------------
    private List<Loan> findLoans(Bson filter) {
        List<Loan> results = new ArrayList<>();
        try (MongoCursor<Document> cursor = loans.find(filter)
                .sort(Sorts.descending("loan_date"))
                .iterator()) {
            while (cursor.hasNext()) {
                results.add(mapLoan(cursor.next()));
            }
        }
        return results;
    }

    private List<Fine> findFines(Bson filter) {
        List<Fine> results = new ArrayList<>();
        try (MongoCursor<Document> cursor = fines.find(filter)
                .sort(Sorts.descending("fine_date"))
                .iterator()) {
            while (cursor.hasNext()) {
                results.add(mapFine(cursor.next()));
            }
        }
        return results;
    }

    private Student mapStudent(Document doc) {
        Student student = new Student();
        student.setStudentId(readInt(doc, "student_id"));
        student.setName(doc.getString("name"));
        student.setEmail(doc.getString("email"));
        student.setPhone(doc.getString("phone"));
        student.setAddress(doc.getString("address"));
        student.setRegistrationDate(readDate(doc, "registration_date"));
        return student;
    }

    private Book mapBook(Document doc) {
        Book book = new Book();
        book.setBookId(readInt(doc, "book_id"));
        book.setTitle(doc.getString("title"));
        book.setAuthor(doc.getString("author"));
        book.setIsbn(doc.getString("isbn"));
        book.setCategory(doc.getString("category"));
        book.setPublicationYear(readInt(doc, "publication_year"));
        book.setAvailableCopies(readInt(doc, "available_copies"));
        book.setTotalCopies(readInt(doc, "total_copies"));
        return book;
    }

    private Loan mapLoan(Document doc) {
        Loan loan = new Loan();
        loan.setLoanId(readInt(doc, "loan_id"));
        loan.setStudentId(readInt(doc, "student_id"));
        loan.setBookId(readInt(doc, "book_id"));
        loan.setLoanDate(readDate(doc, "loan_date"));
        loan.setDueDate(readDate(doc, "due_date"));
        loan.setReturnDate(readDate(doc, "return_date"));
        loan.setStatus(doc.getString("status") != null ? doc.getString("status") : "ACTIVE");
        loan.setRenewalCount(readInt(doc, "renewal_count"));

        Student student = getStudentById(loan.getStudentId());
        if (student != null) {
            loan.setStudentName(student.getName());
        }
        Book book = getBookById(loan.getBookId());
        if (book != null) {
            loan.setBookTitle(book.getTitle());
            loan.setBookAuthor(book.getAuthor());
        }

        if (!"RETURNED".equals(loan.getStatus()) && loan.getDueDate() != null && loan.getDueDate().isBefore(LocalDate.now())) {
            loan.setStatus("OVERDUE");
        }
        return loan;
    }

    private Fine mapFine(Document doc) {
        Fine fine = new Fine();
        fine.setFineId(readInt(doc, "fine_id"));
        fine.setStudentId(readInt(doc, "student_id"));
        fine.setLoanId(readInt(doc, "loan_id"));
        fine.setFineAmount(readBigDecimal(doc, "fine_amount"));
        fine.setFineDate(readDate(doc, "fine_date"));
        fine.setPaymentDate(readDate(doc, "payment_date"));
        fine.setPaymentStatus(doc.getString("payment_status"));

        Student student = getStudentById(fine.getStudentId());
        if (student != null) {
            fine.setStudentName(student.getName());
        }
        Loan loan = getLoanById(fine.getLoanId());
        if (loan != null) {
            fine.setBookTitle(loan.getBookTitle());
            fine.setDueDate(loan.getDueDate());
            fine.setActualReturnDate(loan.getReturnDate());
        }
        return fine;
    }

    private int getNextSequence(String key) {
        Document result = counters.findOneAndUpdate(
            new Document("_id", key),
            new Document("$inc", new Document("value", 1)),
            new FindOneAndUpdateOptions()
                .upsert(true)
                .returnDocument(ReturnDocument.AFTER));
        return result.getInteger("value", 1);
    }

    private LocalDate readDate(Document doc, String field) {
        Object value = doc.get(field);
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return LocalDate.parse((String) value, DATE_FORMAT);
        }
        return null;
    }

    private String formatDate(LocalDate date) {
        return date != null ? DATE_FORMAT.format(date) : null;
    }

    private LocalDate defaultDate(LocalDate date) {
        return date != null ? date : LocalDate.now();
    }

    private int readInt(Document doc, String key) {
        Object value = doc.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return 0;
    }

    private BigDecimal readBigDecimal(Document doc, String key) {
        Object value = doc.get(key);
        if (value instanceof Decimal128) {
            return ((Decimal128) value).bigDecimalValue();
        } else if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        return BigDecimal.ZERO;
    }

    private Decimal128 decimal128(BigDecimal value) {
        return value != null ? new Decimal128(value) : null;
    }
}
