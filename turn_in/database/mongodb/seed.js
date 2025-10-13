// MongoDB seed script for Library Database System
// Load with: mongo --quiet --eval "load('database/mongodb/seed.js')"

(function() {
    const conn = new Mongo("localhost:27017");
    const db = conn.getDB("library_db");

    print("Resetting library_db (MongoDB backend)...");

    db.students.drop();
    db.books.drop();
    db.loans.drop();
    db.fines.drop();
    db.counters.drop();

    db.students.insertMany([
        {
            student_id: 1001,
            name: "John Smith",
            email: "john.smith@email.com",
            phone: "+44 7700 900001",
            address: "10 Library Lane",
            registration_date: "2025-05-15"
        },
        {
            student_id: 1002,
            name: "Jane Doe",
            email: "jane.doe@email.com",
            phone: "+44 7700 900002",
            address: "22 Archive Avenue",
            registration_date: "2025-06-10"
        },
        {
            student_id: 1003,
            name: "Bob Johnson",
            email: "bob.johnson@email.com",
            phone: "+44 7700 900003",
            address: "33 Research Road",
            registration_date: "2025-07-20"
        }
    ]);

    db.books.insertMany([
        {
            book_id: 2001,
            title: "Database Systems",
            author: "Silberschatz, Korth, Sudarshan",
            isbn: "9780078022159",
            category: "Databases",
            publication_year: 2020,
            available_copies: 2,
            total_copies: 5
        },
        {
            book_id: 2002,
            title: "Effective Java",
            author: "Joshua Bloch",
            isbn: "9780134685991",
            category: "Programming",
            publication_year: 2018,
            available_copies: 1,
            total_copies: 4
        },
        {
            book_id: 2003,
            title: "Clean Code",
            author: "Robert C. Martin",
            isbn: "9780132350884",
            category: "Programming",
            publication_year: 2008,
            available_copies: 0,
            total_copies: 3
        }
    ]);

    db.loans.insertMany([
        {
            loan_id: 3001,
            student_id: 1001,
            book_id: 2001,
            loan_date: "2025-08-01",
            due_date: "2025-08-15",
            return_date: null,
            status: "ACTIVE",
            renewal_count: 0
        },
        {
            loan_id: 3002,
            student_id: 1002,
            book_id: 2002,
            loan_date: "2025-09-05",
            due_date: "2025-09-19",
            return_date: "2025-09-16",
            status: "RETURNED",
            renewal_count: 1
        },
        {
            loan_id: 3003,
            student_id: 1003,
            book_id: 2003,
            loan_date: "2025-09-20",
            due_date: "2025-10-04",
            return_date: null,
            status: "ACTIVE",
            renewal_count: 0
        }
    ]);

    db.fines.insertMany([
        {
            fine_id: 4001,
            student_id: 1001,
            loan_id: 3001,
            fine_amount: NumberDecimal("12.50"),
            fine_date: "2025-08-25",
            payment_date: null,
            payment_status: "UNPAID"
        },
        {
            fine_id: 4002,
            student_id: 1002,
            loan_id: 3002,
            fine_amount: NumberDecimal("5.00"),
            fine_date: "2025-09-18",
            payment_date: "2025-09-20",
            payment_status: "PAID"
        }
    ]);

    db.counters.insertMany([
        { _id: "student_id", value: 1003 },
        { _id: "book_id", value: 2003 },
        { _id: "loan_id", value: 3003 },
        { _id: "fine_id", value: 4002 }
    ]);

    print("MongoDB seed complete.");
})();
