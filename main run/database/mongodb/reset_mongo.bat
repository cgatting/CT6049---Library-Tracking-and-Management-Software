@echo off
setlocal
REM MongoDB Database Reset Script
REM Usage: Double-click this file or run from command line: reset_mongo.bat

echo [mongo-reset] Starting MongoDB reset...

REM Get the directory where this batch file is located
set "SCRIPT_DIR=%~dp0"
set "JS_SCRIPT=%SCRIPT_DIR%reset_mongo.js"
set "LOGFILE=%SCRIPT_DIR%reset_mongo.out.log"

REM Check if the JavaScript file exists
if not exist "%JS_SCRIPT%" (
    echo [mongo-reset][ERROR] JavaScript file not found: %JS_SCRIPT%
    pause
    endlocal
    exit /b 1
)

REM Try mongosh in PATH
where mongosh >nul 2>&1
if %errorlevel%==0 (
    set "MONGOSH_EXE=mongosh"
) else (
    REM Try environment variable override
    if defined MONGOSH_PATH if exist "%MONGOSH_PATH%" set "MONGOSH_EXE=%MONGOSH_PATH%"
    REM Try common install locations for mongosh
    if not defined MONGOSH_EXE if exist "%ProgramFiles%\MongoDB\mongosh\bin\mongosh.exe" set "MONGOSH_EXE=%ProgramFiles%\MongoDB\mongosh\bin\mongosh.exe"
    if not defined MONGOSH_EXE if exist "%LOCALAPPDATA%\Programs\mongosh\mongosh.exe" set "MONGOSH_EXE=%LOCALAPPDATA%\Programs\mongosh\mongosh.exe"
    if not defined MONGOSH_EXE if exist "%ProgramFiles(x86)%\MongoDB\mongosh\bin\mongosh.exe" set "MONGOSH_EXE=%ProgramFiles(x86)%\MongoDB\mongosh\bin\mongosh.exe"
)

if defined MONGOSH_EXE (
    echo [mongo-reset] Using mongosh at "%MONGOSH_EXE%"
    "%MONGOSH_EXE%" --quiet --file "%JS_SCRIPT%" > "%LOGFILE%" 2>&1
    powershell -NoProfile -Command "$text = Get-Content -Raw -Path '%LOGFILE%'; if ($text -match 'MongoDB reset completed successfully\.') { exit 0 } else { exit 1 }"
    if %errorlevel% EQU 0 (
        type "%LOGFILE%"
        goto :success
    ) else (
        echo [mongo-reset][ERROR] mongosh run did not report success. Full log:
        type "%LOGFILE%"
        goto :error
    )
)

REM Try legacy mongo in PATH or common server locations
where mongo >nul 2>&1
if %errorlevel%==0 (
    set "MONGO_EXE=mongo"
) else (
    for /D %%v in ("%ProgramFiles%\MongoDB\Server\*") do (
        if exist "%%v\bin\mongo.exe" (
            set "MONGO_EXE=%%v\bin\mongo.exe"
        )
    )
)

if defined MONGO_EXE (
    echo [mongo-reset] Using legacy mongo at "%MONGO_EXE%"
    "%MONGO_EXE%" --quiet --eval "load('%JS_SCRIPT%')" > "%LOGFILE%" 2>&1
    powershell -NoProfile -Command "$text = Get-Content -Raw -Path '%LOGFILE%'; if ($text -match 'MongoDB reset completed successfully\.') { exit 0 } else { exit 1 }"
    if %errorlevel% EQU 0 (
        type "%LOGFILE%"
        goto :success
    ) else (
        echo [mongo-reset][ERROR] mongo run did not report success. Full log:
        type "%LOGFILE%"
        goto :error
    )
)

REM Neither shell found
echo [mongo-reset][ERROR] Neither 'mongosh' nor 'mongo' was found in PATH or common locations.
echo You can set MONGOSH_PATH to the full path, e.g.:
echo   set "MONGOSH_PATH=C:\\Program Files\\MongoDB\\mongosh\\bin\\mongosh.exe"
echo Then run this batch file again.
echo Or add it to PATH: https://www.mongodb.com/try/download/shell
goto :error

:success
echo.
echo Reset completed! The database now contains:
echo - 3 students (IDs: 1001-1003)
echo - 3 books (IDs: 2001-2003)
echo - 3 loans (IDs: 3001-3003)
echo - 2 fines (IDs: 4001-4002)
echo.
pause
endlocal
exit /b 0

:error
echo.
echo Reset failed. Please check the error messages above.
echo Make sure MongoDB is running and accessible at localhost:27017
echo.
pause
endlocal
exit /b 1