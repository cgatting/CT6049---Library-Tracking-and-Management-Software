@echo off
setlocal enabledelayedexpansion

REM Self-contained launcher for the submission package.
REM Runs the shaded JAR using dependencies located under turn_in\dependencies.

set "ROOT=%~dp0.."
pushd "%ROOT%" >nul

REM Infer backend from script filename first, then argument; default to mongo
set "SCRIPT_NAME=%~n0"
set "BACKEND="
echo %SCRIPT_NAME% | findstr /I "oracle" >nul && set "BACKEND=oracle"
if not defined BACKEND echo %SCRIPT_NAME% | findstr /I "mongo" >nul && set "BACKEND=mongo"
if not defined BACKEND if not "%~1"=="" set "BACKEND=%~1"
if not defined BACKEND set "BACKEND=mongo"

set "APP_ARGS="
if /I "%BACKEND%"=="oracle" set "APP_ARGS=--backend=oracle"
if /I "%BACKEND%"=="mongo" set "APP_ARGS=--backend=mongo"

REM Validate Java availability
where java >nul 2>&1
if errorlevel 1 (
    echo [error] Java Runtime not found. Please install JRE/JDK 8 or newer.
    goto :fallback
)

REM Build classpath: submission executables JAR + all dependencies
set "JAR_PATH=executables\library-database-system-1.0.0.jar"
set "DEPS_PATH=dependencies\*"
if not exist "%JAR_PATH%" (
    echo [error] Missing JAR: %CD%\%JAR_PATH%
    echo [hint] Ensure you are running from the 'turn_in' package.
    goto :fallback
)
if not exist "dependencies" (
    echo [error] Missing 'dependencies' folder next to executables.
    goto :fallback
)

echo [info] Starting application with backend: %BACKEND%
set "JAVA_PROPS="
if /I "%BACKEND%"=="oracle" set "JAVA_PROPS=-Dlibrary.backend=oracle"
if /I "%BACKEND%"=="mongo" set "JAVA_PROPS=-Dlibrary.backend=mongo"

set "JAVA_CMD=java %JAVA_PROPS% -cp \"%JAR_PATH%;%DEPS_PATH%\" com.library.LibraryApp %APP_ARGS%"
echo [debug] Java command: %JAVA_CMD%
%JAVA_CMD%
if errorlevel 1 (
    echo [warn] Java class launch failed; attempting EXE fallback...
    goto :fallback
)

goto :end

:fallback
REM Try Launch4j executables if available
if /I "%BACKEND%"=="oracle" (
    if exist "executables\library_oracle.exe" (
        echo [info] Launching fallback: executables\library_oracle.exe
        start "Library Oracle" /B "executables\library_oracle.exe"
        goto :end
    )
)
if /I "%BACKEND%"=="mongo" (
    if exist "executables\library_mongo.exe" (
        echo [info] Launching fallback: executables\library_mongo.exe
        start "Library Mongo" /B "executables\library_mongo.exe"
        goto :end
    )
)

echo [error] Unable to start application. Neither Java class nor EXE fallback succeeded.
echo [hint] Ensure JAR contains com.library.LibraryApp or use the EXE launchers directly.

:end
popd >nul
endlocal

pause
