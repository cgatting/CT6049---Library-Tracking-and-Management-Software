-- Oracle Schema synchronized with MongoDB seed data
-- Run as c##library_user

-- Drop existing tables if they exist
DROP TABLE Fines CASCADE CONSTRAINTS;
DROP TABLE Loans CASCADE CONSTRAINTS;
DROP TABLE Books CASCADE CONSTRAINTS;
DROP TABLE Students CASCADE CONSTRAINTS;

-- Drop sequences if they exist
DROP SEQUENCE seq_student_id;
DROP SEQUENCE seq_book_id;
DROP SEQUENCE seq_loan_id;
DROP SEQUENCE seq_fine_id;

-- Create Students Table
CREATE TABLE Students (
    student_id NUMBER PRIMARY KEY,
    name VARCHAR2(100) NOT NULL,
    email VARCHAR2(100) UNIQUE NOT NULL,
    phone VARCHAR2(20),
    address VARCHAR2(200),
    registration_date DATE DEFAULT SYSDATE
);

-- Create Books Table
CREATE TABLE Books (
    book_id NUMBER PRIMARY KEY,
    title VARCHAR2(200) NOT NULL,
    author VARCHAR2(100) NOT NULL,
    isbn VARCHAR2(20) UNIQUE,
    category VARCHAR2(50),
    publication_year NUMBER(4),
    available_copies NUMBER DEFAULT 0,
    total_copies NUMBER DEFAULT 0
);

-- Create Loans Table
CREATE TABLE Loans (
    loan_id NUMBER PRIMARY KEY,
    student_id NUMBER NOT NULL,
    book_id NUMBER NOT NULL,
    loan_date DATE DEFAULT SYSDATE,
    due_date DATE NOT NULL,
    return_date DATE,
    status VARCHAR2(20) DEFAULT 'ACTIVE',
    renewal_count NUMBER DEFAULT 0,
    CONSTRAINT fk_loan_student FOREIGN KEY (student_id) REFERENCES Students(student_id),
    CONSTRAINT fk_loan_book FOREIGN KEY (book_id) REFERENCES Books(book_id),
    CONSTRAINT chk_loan_status CHECK (status IN ('ACTIVE', 'RETURNED', 'OVERDUE'))
);

-- Create Fines Table
CREATE TABLE Fines (
    fine_id NUMBER PRIMARY KEY,
    student_id NUMBER NOT NULL,
    loan_id NUMBER NOT NULL,
    fine_amount NUMBER(10,2) NOT NULL,
    fine_date DATE DEFAULT SYSDATE,
    payment_date DATE,
    payment_status VARCHAR2(20) DEFAULT 'UNPAID',
    CONSTRAINT fk_fine_student FOREIGN KEY (student_id) REFERENCES Students(student_id),
    CONSTRAINT fk_fine_loan FOREIGN KEY (loan_id) REFERENCES Loans(loan_id),
    CONSTRAINT chk_payment_status CHECK (payment_status IN ('UNPAID', 'PAID'))
);

-- Insert synchronized sample data matching MongoDB exactly
INSERT INTO Students (student_id, name, email, phone, address, registration_date) VALUES
    (1001, 'John Smith', 'john.smith@email.com', '+44 7700 900001', '10 Library Lane', DATE '2025-05-15');
INSERT INTO Students (student_id, name, email, phone, address, registration_date) VALUES
    (1002, 'Jane Doe', 'jane.doe@email.com', '+44 7700 900002', '22 Archive Avenue', DATE '2025-06-10');
INSERT INTO Students (student_id, name, email, phone, address, registration_date) VALUES
    (1003, 'Bob Johnson', 'bob.johnson@email.com', '+44 7700 900003', '33 Research Road', DATE '2025-07-20');

INSERT INTO Books (book_id, title, author, isbn, category, publication_year, available_copies, total_copies) VALUES
    (2001, 'Database Systems', 'Silberschatz, Korth, Sudarshan', '9780078022159', 'Databases', 2020, 2, 5);
INSERT INTO Books (book_id, title, author, isbn, category, publication_year, available_copies, total_copies) VALUES
    (2002, 'Effective Java', 'Joshua Bloch', '9780134685991', 'Programming', 2018, 1, 4);
INSERT INTO Books (book_id, title, author, isbn, category, publication_year, available_copies, total_copies) VALUES
    (2003, 'Clean Code', 'Robert C. Martin', '9780132350884', 'Programming', 2008, 0, 3);

INSERT INTO Loans (loan_id, student_id, book_id, loan_date, due_date, return_date, status, renewal_count) VALUES
    (3001, 1001, 2001, DATE '2025-08-01', DATE '2025-08-15', NULL, 'ACTIVE', 0);
INSERT INTO Loans (loan_id, student_id, book_id, loan_date, due_date, return_date, status, renewal_count) VALUES
    (3002, 1002, 2002, DATE '2025-09-05', DATE '2025-09-19', DATE '2025-09-16', 'RETURNED', 1);
INSERT INTO Loans (loan_id, student_id, book_id, loan_date, due_date, return_date, status, renewal_count) VALUES
    (3003, 1003, 2003, DATE '2025-09-20', DATE '2025-10-04', NULL, 'ACTIVE', 0);

INSERT INTO Fines (fine_id, student_id, loan_id, fine_amount, fine_date, payment_date, payment_status) VALUES
    (4001, 1001, 3001, 12.50, DATE '2025-08-25', NULL, 'UNPAID');
INSERT INTO Fines (fine_id, student_id, loan_id, fine_amount, fine_date, payment_date, payment_status) VALUES
    (4002, 1002, 3002, 5.00, DATE '2025-09-18', DATE '2025-09-20', 'PAID');

-- Create sequences starting after the sample data
CREATE SEQUENCE seq_student_id START WITH 1004 INCREMENT BY 1;
CREATE SEQUENCE seq_book_id START WITH 2004 INCREMENT BY 1;
CREATE SEQUENCE seq_loan_id START WITH 3004 INCREMENT BY 1;
CREATE SEQUENCE seq_fine_id START WITH 4003 INCREMENT BY 1;

COMMIT;

-- Display inserted data for verification
SELECT 'Students:' as table_name FROM dual;
SELECT * FROM Students ORDER BY student_id;

SELECT 'Books:' as table_name FROM dual;
SELECT * FROM Books ORDER BY book_id;

SELECT 'Loans:' as table_name FROM dual;
SELECT * FROM Loans ORDER BY loan_id;

SELECT 'Fines:' as table_name FROM dual;
SELECT * FROM Fines ORDER BY fine_id;

PROMPT Oracle database synchronized with MongoDB data successfully!
exit;