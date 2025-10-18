@echo off
setlocal
set "SCRIPT_DIR=%~dp0"

REM GUI launcher for choosing Oracle or MongoDB backend
powershell -NoProfile -ExecutionPolicy Bypass -File "%SCRIPT_DIR%launcher.ps1"

endlocal