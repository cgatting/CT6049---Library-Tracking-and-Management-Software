-- Library Database System - Oracle Schema
-- Simple version for CT6049 Assessment

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

-- Create Sequences for Primary Keys
CREATE SEQUENCE seq_student_id START WITH 1001 INCREMENT BY 1;
CREATE SEQUENCE seq_book_id START WITH 2001 INCREMENT BY 1;
CREATE SEQUENCE seq_loan_id START WITH 3001 INCREMENT BY 1;
CREATE SEQUENCE seq_fine_id START WITH 4001 INCREMENT BY 1;

-- Insert Sample Students
INSERT INTO Students (student_id, name, email, phone) VALUES 
(seq_student_id.NEXTVAL, 'John Doe', 'john.doe@email.com', '+1234567890');
INSERT INTO Students (student_id, name, email, phone) VALUES 
(seq_student_id.NEXTVAL, 'Jane Smith', 'jane.smith@email.com', '+1234567891');
INSERT INTO Students (student_id, name, email, phone) VALUES 
(seq_student_id.NEXTVAL, 'Bob Johnson', 'bob.johnson@email.com', '+1234567892');

-- Insert Sample Books
INSERT INTO Books (book_id, title, author, isbn, category, publication_year, available_copies, total_copies) VALUES 
(seq_book_id.NEXTVAL, 'Database Systems Concepts', 'Abraham Silberschatz', '978-0073523323', 'Computer Science', 2019, 3, 5);
INSERT INTO Books (book_id, title, author, isbn, category, publication_year, available_copies, total_copies) VALUES 
(seq_book_id.NEXTVAL, 'Java: The Complete Reference', 'Herbert Schildt', '978-1260440232', 'Programming', 2020, 2, 3);
INSERT INTO Books (book_id, title, author, isbn, category, publication_year, available_copies, total_copies) VALUES 
(seq_book_id.NEXTVAL, 'Clean Code', 'Robert C. Martin', '978-0132350884', 'Software Engineering', 2008, 1, 2);

-- Commit the changes
COMMIT;

-- Display inserted data
SELECT 'Students:' as table_name FROM dual;
SELECT * FROM Students;

SELECT 'Books:' as table_name FROM dual;
SELECT * FROM Books;

PROMPT Schema created successfully with sample data!