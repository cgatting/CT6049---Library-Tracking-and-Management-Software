-- Oracle Database User Setup Script
-- Run this as SYSTEM to create the LIBRARY_USER schema owner

-- Uncomment to recreate the user from scratch
-- DROP USER library_user CASCADE;

CREATE USER library_user IDENTIFIED BY "cgatting1";

GRANT CONNECT TO library_user;
GRANT RESOURCE TO library_user;
GRANT CREATE SESSION TO library_user;
GRANT UNLIMITED TABLESPACE TO library_user;
GRANT CREATE TABLE TO library_user;
GRANT CREATE SEQUENCE TO library_user;
GRANT CREATE VIEW TO library_user;
GRANT CREATE PROCEDURE TO library_user;

SELECT username, account_status, created
FROM   dba_users
WHERE  username = 'LIBRARY_USER';

COMMIT;

-- Usage
-- sqlplus system/<SYSTEM_PASSWORD>@localhost:1521/xe
-- @database/oracle/scripts/setup_oracle_user.sql
