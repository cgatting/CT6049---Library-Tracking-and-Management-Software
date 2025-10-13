-- Sample data for the Library Database schema
-- Run after schema.sql while connected as LIBRARY_USER

SET DEFINE OFF;

DELETE FROM Fines;
DELETE FROM Loans;
DELETE FROM Books;
DELETE FROM Students;

INSERT INTO Students (student_id, name, email, phone, address, registration_date) VALUES
    (1001, 'John Smith', 'john.smith@email.com', '+44 7700 900001', '10 Library Lane', SYSDATE - 120);
INSERT INTO Students (student_id, name, email, phone, address, registration_date) VALUES
    (1002, 'Jane Doe', 'jane.doe@email.com', '+44 7700 900002', '22 Archive Avenue', SYSDATE - 95);
INSERT INTO Students (student_id, name, email, phone, address, registration_date) VALUES
    (1003, 'Bob Johnson', 'bob.johnson@email.com', '+44 7700 900003', '33 Research Road', SYSDATE - 62);

INSERT INTO Books (book_id, title, author, isbn, category, publication_year, available_copies, total_copies) VALUES
    (2001, 'Database Systems', 'Silberschatz, Korth, Sudarshan', '9780078022159', 'Databases', 2020, 2, 5);
INSERT INTO Books (book_id, title, author, isbn, category, publication_year, available_copies, total_copies) VALUES
    (2002, 'Effective Java', 'Joshua Bloch', '9780134685991', 'Programming', 2018, 1, 4);
INSERT INTO Books (book_id, title, author, isbn, category, publication_year, available_copies, total_copies) VALUES
    (2003, 'Clean Code', 'Robert C. Martin', '9780132350884', 'Programming', 2008, 0, 3);

INSERT INTO Loans (loan_id, student_id, book_id, loan_date, due_date, return_date, status) VALUES
    (3001, 1001, 2001, SYSDATE - 30, SYSDATE - 15, NULL, 'OVERDUE');
INSERT INTO Loans (loan_id, student_id, book_id, loan_date, due_date, return_date, status) VALUES
    (3002, 1002, 2002, SYSDATE - 20, SYSDATE - 5, SYSDATE - 3, 'RETURNED');
INSERT INTO Loans (loan_id, student_id, book_id, loan_date, due_date, return_date, status) VALUES
    (3003, 1003, 2003, SYSDATE - 10, SYSDATE + 10, NULL, 'ACTIVE');

INSERT INTO Fines (fine_id, student_id, loan_id, fine_amount, fine_date, payment_date, payment_status) VALUES
    (4001, 1001, 3001, 12.50, SYSDATE - 10, NULL, 'UNPAID');
INSERT INTO Fines (fine_id, student_id, loan_id, fine_amount, fine_date, payment_date, payment_status) VALUES
    (4002, 1002, 3002, 5.00, SYSDATE - 5, SYSDATE - 2, 'PAID');

COMMIT;

-- Reset sequences so that future inserts continue after the sample IDs
ALTER SEQUENCE seq_student_id RESTART START WITH 1004;
ALTER SEQUENCE seq_book_id RESTART START WITH 2004;
ALTER SEQUENCE seq_loan_id RESTART START WITH 3004;
ALTER SEQUENCE seq_fine_id RESTART START WITH 4003;
