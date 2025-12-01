@echo off
setlocal

set "ROOT=%~dp0"
set "TARGET=%ROOT%main run\source_code\target"
set "JAR=%TARGET%\library-database-system-1.0.0.jar"
set "EXE_MONGO=%TARGET%\library_mongo.exe"
set "EXE_ORACLE=%TARGET%\library_oracle.exe"

set "arg=%~1"
if /i "%arg%"=="mongo" set "opt=1"
if /i "%arg%"=="oracle" set "opt=2"
if /i "%arg%"=="exit" set "opt=3"

if defined opt goto DOSELECT
echo Select backend:
echo 1^) MongoDB
echo 2^) Oracle
echo 3^) Exit
choice /c 123 /n /m "Enter choice: "
set "opt=%errorlevel%"

:DOSELECT

if "%opt%"=="3" exit /b 0

if "%opt%"=="1" (
  if exist "%EXE_MONGO%" (
    "%EXE_MONGO%"
  ) else (
    java -jar "%JAR%" --backend=mongo
  )
  exit /b %errorlevel%
)
if "%opt%"=="2" (
  if exist "%EXE_ORACLE%" (
    "%EXE_ORACLE%"
  ) else (
    java -jar "%JAR%" --backend=oracle
  )
  exit /b %errorlevel%
)

endlocal
