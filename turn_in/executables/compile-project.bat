@echo off
setlocal

REM Submission-friendly compile helper.
REM This package is meant for running, not building the full project.

set "ROOT=%~dp0.."
pushd "%ROOT%" >nul

echo [info] This submission contains prebuilt executables and a runnable JAR.
echo [info] Building the full project is supported only from the full source repo.
echo.
echo [hint] To run the app from this submission, use:
echo   executables\java_mongo.bat   ^|   executables\java_oracle.bat
echo   or double-click the corresponding .exe in executables\
echo.
echo [hint] To build from source, open the full repository (not turn_in)
echo        and run: mvn clean package

popd >nul
endlocal

pause
