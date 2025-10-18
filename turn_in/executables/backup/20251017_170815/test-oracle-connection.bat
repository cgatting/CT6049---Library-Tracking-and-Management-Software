@echo off
setlocal

set "ROOT=%~dp0.."
pushd "%ROOT%" >nul

echo [info] Testing Oracle connection for jdbc:oracle:thin:@localhost:1521:xe

echo exit | sqlplus -S library_user/cgatting1@localhost:1521/xe >connection_test.log 2>&1
set "EXIT_CODE=%ERRORLEVEL%"

if "%EXIT_CODE%"=="0" (
    echo [ok] Connected successfully as library_user.
    echo [hint] You can now run schema scripts under database\oracle.
) else (
    echo [fail] Connection attempt failed (exit code %EXIT_CODE%).
    echo ----- sqlplus output -----
    type connection_test.log
    echo -------------------------
    echo [checklist]
    echo   1. OracleServiceXE and OracleXETNSListener services are running
    echo   2. Username/password match database/oracle/scripts/setup_oracle_user.sql
    echo   3. Listener is reachable on localhost:1521 (firewall, VPN, etc.)
)

del connection_test.log 2>nul

popd >nul
endlocal

pause
