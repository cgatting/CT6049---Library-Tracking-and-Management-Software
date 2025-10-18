# PowerShell Update Script for Library Database System
# Performs clean update while preserving user configs/data, with version check and rollback.

param(
    [string]$SourceUrl,
    [switch]$DryRun,
    [switch]$Rollback
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

function Write-Log {
    param([string]$Message)
    $timestamp = (Get-Date).ToString('yyyy-MM-dd HH:mm:ss')
    $line = "[$timestamp] $Message"
    Write-Host $line
    if (-not $Global:LogFile) {
        try { $Global:LogFile = Join-Path (Get-Location) 'update.log' } catch {}
    }
    if ($Global:LogFile) { Add-Content -Path $Global:LogFile -Value $line }
}

function Get-ExecutableDir {
    if ($PSScriptRoot -and $PSScriptRoot -ne '') { return $PSScriptRoot }
    return (Split-Path -Parent $MyInvocation.MyCommand.Path)
}

function Get-JarVersion {
    param([string]$JarPath)
    if (-not (Test-Path $JarPath)) { return $null }
    # Extract version from filename: library-database-system-X.Y.Z.jar
    $name = [System.IO.Path]::GetFileNameWithoutExtension($JarPath)
    $m = [Regex]::Match($name, '^library-database-system-(?<ver>[0-9A-Za-z\.-]+)$')
    if ($m.Success) { return $m.Groups['ver'].Value } else { return $null }
}

function Compare-VersionStrings {
    param([string]$A,[string]$B)
    # Try semantic comparison, fallback to string compare
    try {
        $va = [Version]$A; $vb = [Version]$B
        return $va.CompareTo($vb)
    } catch { return [String]::Compare($A,$B) }
}

function Find-LocalArtifacts {
    param([string]$SourceDir)
    $jar = Get-ChildItem -Path $SourceDir -Filter 'library-database-system-*.jar' -File -ErrorAction SilentlyContinue |
        Sort-Object LastWriteTime -Descending | Select-Object -First 1
    $exeOraclePath = $null
    $exeMongoPath  = $null
    $oracleCandidate = Join-Path $SourceDir 'library_oracle.exe'
    $mongoCandidate  = Join-Path $SourceDir 'library_mongo.exe'
    if (Test-Path $oracleCandidate) { $exeOraclePath = $oracleCandidate }
    if (Test-Path $mongoCandidate)  { $exeMongoPath  = $mongoCandidate }
    return [PSCustomObject]@{ Jar = $jar; ExeOracle = $exeOraclePath; ExeMongo = $exeMongoPath }
}

function Download-ArtifactsFromUrl {
    param([string]$Url,[string]$TempDir)
    $zipPath = Join-Path $TempDir 'update_package.zip'
    Write-Log "Downloading update package from $Url"
    Invoke-WebRequest -Uri $Url -OutFile $zipPath -UseBasicParsing
    $extractDir = Join-Path $TempDir 'package'
    if (Test-Path $extractDir) { Remove-Item -Recurse -Force $extractDir }
    Expand-Archive -Path $zipPath -DestinationPath $extractDir -Force
    $jar = Get-ChildItem -Path $extractDir -Recurse -Filter 'library-database-system-*.jar' -File | Select-Object -First 1
    $exeOracle = Get-ChildItem -Path $extractDir -Recurse -Filter 'library_oracle.exe' -File | Select-Object -First 1
    $exeMongo  = Get-ChildItem -Path $extractDir -Recurse -Filter 'library_mongo.exe' -File | Select-Object -First 1
    $exeOraclePath = if ($exeOracle) { $exeOracle.FullName } else { $null }
    $exeMongoPath  = if ($exeMongo)  { $exeMongo.FullName }  else { $null }
    return [PSCustomObject]@{ Jar = $jar; ExeOracle = $exeOraclePath; ExeMongo = $exeMongoPath }
}

function Backup-Executables {
    param([string]$ExecDir)
    $backupRoot = Join-Path $ExecDir 'backup'
    if (-not (Test-Path $backupRoot)) { New-Item -ItemType Directory -Path $backupRoot | Out-Null }
    $stamp = (Get-Date).ToString('yyyyMMdd_HHmmss')
    $backupDir = Join-Path $backupRoot $stamp
    Write-Log "Creating backup at $backupDir"
    if (-not $DryRun) { Copy-Item -Path $ExecDir\* -Destination $backupDir -Recurse -Force }
    return $backupDir
}

function Restore-Backup {
    param([string]$BackupDir,[string]$ExecDir)
    Write-Log "Restoring backup from $BackupDir to $ExecDir"
    if (-not (Test-Path $BackupDir)) { throw "Backup directory not found: $BackupDir" }
    if (-not $DryRun) {
        # Remove updated files (jar/exe) then restore everything from backup
        Get-ChildItem -Path $ExecDir -File | ForEach-Object { Remove-Item -Force $_.FullName }
        Copy-Item -Path $BackupDir\* -Destination $ExecDir -Recurse -Force
    }
}

function Apply-Update {
    param([string]$ExecDir,[string]$JarSource,[string]$ExeOracleSource,[string]$ExeMongoSource)
    $targetJar = Join-Path $ExecDir 'library-database-system-1.0.0.jar'
    # Preserve the naming convention used in executables folder; replace the file contents
    Write-Log "Applying update: JAR -> $targetJar"
    if (-not $DryRun) { Copy-Item -Force $JarSource $targetJar }
    if ($ExeOracleSource) {
        $targetOracle = Join-Path $ExecDir 'library_oracle.exe'
        Write-Log "Updating Oracle EXE -> $targetOracle"
        if (-not $DryRun) { Copy-Item -Force $ExeOracleSource $targetOracle }
    }
    if ($ExeMongoSource) {
        $targetMongo = Join-Path $ExecDir 'library_mongo.exe'
        Write-Log "Updating Mongo EXE -> $targetMongo"
        if (-not $DryRun) { Copy-Item -Force $ExeMongoSource $targetMongo }
    }
}

function Main {
    $execDir = Get-ExecutableDir
    $Global:LogFile = Join-Path $execDir 'update.log'
    Write-Log "Starting update in $execDir"

    if ($Rollback) {
        $latestBackup = Get-ChildItem -Path (Join-Path $execDir 'backup') -Directory -ErrorAction SilentlyContinue |
            Sort-Object Name -Descending | Select-Object -First 1
        if (-not $latestBackup) { throw 'No backups found to rollback.' }
        Restore-Backup -BackupDir $latestBackup.FullName -ExecDir $execDir
        Write-Log 'Rollback completed.'
        return
    }

    $currentJar = Join-Path $execDir 'library-database-system-1.0.0.jar'
    $currentVersion = Get-JarVersion -JarPath $currentJar
    $cv = if ($null -ne $currentVersion -and $currentVersion -ne '') { $currentVersion } else { 'unknown' }
    Write-Log ("Current JAR: {0} (version: {1})" -f $currentJar, $cv)

    $backupDir = Backup-Executables -ExecDir $execDir

    $artifacts = $null
    $sourceDesc = ''
    if ($SourceUrl) {
        $tempDir = New-Item -ItemType Directory -Path (Join-Path $env:TEMP ('libsys_update_' + (Get-Random)))
        try {
            $artifacts = Download-ArtifactsFromUrl -Url $SourceUrl -TempDir $tempDir.FullName
            $sourceDesc = "downloaded package"
        } finally {
            # leave temp for inspection on DryRun
            if (-not $DryRun) { Remove-Item -Recurse -Force $tempDir }
        }
    }
    if (-not $artifacts) {
        $localTargetDir = Join-Path (Split-Path -Parent $execDir) 'source_code\target'
        Write-Log "Searching local artifacts in $localTargetDir"
        $found = Find-LocalArtifacts -SourceDir $localTargetDir
        if ($found.Jar) { $artifacts = $found; $sourceDesc = "local build artifacts" }
    }
    if (-not $artifacts -or -not $artifacts.Jar) { throw 'No update artifacts found (JAR missing). Provide -SourceUrl or build locally.' }

    $newVersion = Get-JarVersion -JarPath $artifacts.Jar.FullName
    $nv = if ($null -ne $newVersion -and $newVersion -ne '') { $newVersion } else { 'unknown' }
    Write-Log ("New JAR: {0} (version: {1}) from {2}" -f $artifacts.Jar.FullName, $nv, $sourceDesc)

    if ($currentVersion -and $newVersion) {
        $cmp = Compare-VersionStrings -A $newVersion -B $currentVersion
        if ($cmp -le 0) { Write-Log "Warning: New version ($newVersion) is not newer than current ($currentVersion). Proceeding anyway." }
    }

    Apply-Update -ExecDir $execDir -JarSource $artifacts.Jar.FullName -ExeOracleSource $artifacts.ExeOracle -ExeMongoSource $artifacts.ExeMongo

    # Verify that target jar now has expected version
    $postVersion = Get-JarVersion -JarPath (Join-Path $execDir 'library-database-system-1.0.0.jar')
    $pv = if ($null -ne $postVersion -and $postVersion -ne '') { $postVersion } else { 'unknown' }
    Write-Log ("Post-update version: {0}" -f $pv)
    if ($newVersion -and $postVersion -and ($newVersion -ne $postVersion)) {
        Write-Log "Version verification failed; initiating rollback."
        Restore-Backup -BackupDir $backupDir -ExecDir $execDir
        throw "Update failed: version mismatch ($newVersion vs $postVersion)."
    }

    Write-Log 'Update completed successfully.'
}

try { Main } catch {
    Write-Log ("ERROR: " + $_.Exception.Message)
    Write-Log 'See update.log for details.'
    throw
}