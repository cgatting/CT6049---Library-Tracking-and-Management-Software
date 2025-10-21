# Reset MongoDB database to initial state with demo data
# Usage: powershell -ExecutionPolicy Bypass -File .\database\mongodb\reset_mongo.ps1

param(
    [string]$ScriptPath = "$PSScriptRoot\\reset_mongo.js"
)

Write-Host "[mongo-reset] Starting MongoDB reset via PowerShell wrapper..."

# Detect mongosh or legacy mongo
$mongosh = Get-Command mongosh -ErrorAction SilentlyContinue
$mongo = Get-Command mongo -ErrorAction SilentlyContinue

if ($mongosh) {
    Write-Host "[mongo-reset] Using mongosh: $($mongosh.Source)"
    & $mongosh.Source --quiet --file $ScriptPath
    $exitCode = $LASTEXITCODE
    if ($exitCode -ne 0) {
        Write-Error "[mongo-reset] mongosh execution failed with exit code $exitCode"
        exit $exitCode
    }
} elseif ($mongo) {
    Write-Host "[mongo-reset] Using legacy mongo: $($mongo.Source)"
    & $mongo.Source --quiet --eval "load('$ScriptPath')"
    $exitCode = $LASTEXITCODE
    if ($exitCode -ne 0) {
        Write-Error "[mongo-reset] mongo execution failed with exit code $exitCode"
        exit $exitCode
    }
} else {
    Write-Error "[mongo-reset] Neither 'mongosh' nor 'mongo' was found in PATH. Please install MongoDB Shell."
    exit 1
}

Write-Host "[mongo-reset] MongoDB reset completed successfully."