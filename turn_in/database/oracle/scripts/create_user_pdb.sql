-- Switch to the pluggable database
ALTER SESSION SET CONTAINER = XEPDB1;

-- Create library_user in the PDB
CREATE USER library_user IDENTIFIED BY "cgatting1";

-- Grant necessary privileges
GRANT CONNECT TO library_user;
GRANT RESOURCE TO library_user;
GRANT CREATE SESSION TO library_user;
GRANT UNLIMITED TABLESPACE TO library_user;
GRANT CREATE TABLE TO library_user;
GRANT CREATE SEQUENCE TO library_user;
GRANT CREATE VIEW TO library_user;
GRANT CREATE PROCEDURE TO library_user;

-- Verify user creation
SELECT 'User library_user created successfully in PDB!' FROM dual;

-- Show user status
SELECT username, account_status FROM dba_users WHERE username = 'LIBRARY_USER';

EXIT;
