-- Check table ownership and load sample data
ALTER SESSION SET CONTAINER = XEPDB1;

-- Check which user owns the tables
SELECT owner, table_name FROM all_tables 
WHERE table_name IN ('STUDENTS', 'BOOKS', 'LOANS', 'FINES') 
ORDER BY owner, table_name;

-- Grant access to library_user if tables are owned by SYS
GRANT SELECT, INSERT, UPDATE, DELETE ON SYS.Students TO library_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON SYS.Books TO library_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON SYS.Loans TO library_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON SYS.Fines TO library_user;

-- Grant sequence access
GRANT SELECT ON SYS.seq_student_id TO library_user;
GRANT SELECT ON SYS.seq_book_id TO library_user;
GRANT SELECT ON SYS.seq_loan_id TO library_user;
GRANT SELECT ON SYS.seq_fine_id TO library_user;

-- Show current data
SELECT COUNT(*) as student_count FROM Students;
SELECT COUNT(*) as book_count FROM Books;
SELECT COUNT(*) as loan_count FROM Loans;
SELECT COUNT(*) as fine_count FROM Fines;

exit;