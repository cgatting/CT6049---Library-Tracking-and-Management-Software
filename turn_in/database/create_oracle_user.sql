-- Create Oracle user for library database
CREATE USER c##library_user IDENTIFIED BY cgatting1;
GRANT CONNECT TO c##library_user;
GRANT RESOURCE TO c##library_user;
GRANT UNLIMITED TABLESPACE TO c##library_user;
GRANT CREATE TABLE TO c##library_user;
GRANT CREATE SEQUENCE TO c##library_user;
GRANT CREATE VIEW TO c##library_user;
GRANT CREATE PROCEDURE TO c##library_user;
exit;