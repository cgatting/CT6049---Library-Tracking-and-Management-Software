@echo off
setlocal
set SCRIPT_DIR=%~dp0
powershell -NoProfile -ExecutionPolicy Bypass -File "%SCRIPT_DIR%update-app.ps1" %*
if %ERRORLEVEL% NEQ 0 (
  echo Update failed. See update.log in the executables folder.
  exit /b %ERRORLEVEL%
)
echo Update completed.
endlocal