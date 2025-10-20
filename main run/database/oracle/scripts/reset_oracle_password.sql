-- Reset Oracle SYSTEM password
-- Run as SYSDBA to set a known password

-- Set SYSTEM password to 'oracle'
ALTER USER SYSTEM IDENTIFIED BY oracle;

-- Unlock SYSTEM account if locked
ALTER USER SYSTEM ACCOUNT UNLOCK;

-- Verify the change
SELECT 'SYSTEM password reset to oracle successfully!' FROM dual;

-- Show user status
SELECT username, account_status FROM dba_users WHERE username = 'SYSTEM';

EXIT;