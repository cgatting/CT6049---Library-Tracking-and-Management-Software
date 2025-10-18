#requires -Version 5.1
Add-Type -AssemblyName System.Windows.Forms
Add-Type -AssemblyName System.Drawing

# Absolute paths to the existing batch files
$OracleBat = 'c:\Users\cgatt\OneDrive\Pictures\Desktop\CT6049 - Java DB\turn_in\executables\java_oracle.bat'
$MongoBat  = 'c:\Users\cgatt\OneDrive\Pictures\Desktop\CT6049 - Java DB\turn_in\executables\java_mongo.bat'
$ExecDir   = [System.IO.Path]::GetDirectoryName($OracleBat)

function Show-Error([string]$message) {
    [System.Windows.Forms.MessageBox]::Show($message, 'Error', [System.Windows.Forms.MessageBoxButtons]::OK, [System.Windows.Forms.MessageBoxIcon]::Error) | Out-Null
}

function Launch-Batch([string]$batchPath, [string]$displayName) {
    if (-not (Test-Path -LiteralPath $batchPath)) {
        Show-Error "Cannot find $displayName launcher at: `n$batchPath"
        return
    }

    try {
        $global:statusLabel.Text = "Launching $displayName..."
        $global:statusLabel.ForeColor = [System.Drawing.Color]::DarkSlateBlue
        # Start the batch directly; UseShellExecute lets Windows handle .bat via cmd
        $psi = New-Object System.Diagnostics.ProcessStartInfo
        $psi.FileName = $batchPath
        $psi.WorkingDirectory = $ExecDir
        $psi.UseShellExecute = $true
        $psi.WindowStyle = [System.Diagnostics.ProcessWindowStyle]::Normal
        [System.Diagnostics.Process]::Start($psi) | Out-Null

        $global:statusLabel.Text = "$displayName launched. Check the console window."
        $global:statusLabel.ForeColor = [System.Drawing.Color]::ForestGreen
    }
    catch {
        Show-Error "Failed to launch $displayName. `n`n$($_.Exception.Message)"
        $global:statusLabel.Text = "Launch failed for $displayName"
        $global:statusLabel.ForeColor = [System.Drawing.Color]::Firebrick
    }
}

# Build the form
$form = New-Object System.Windows.Forms.Form
$form.Text = 'Library Backend Launcher'
$form.StartPosition = 'CenterScreen'
$form.FormBorderStyle = 'FixedDialog'
$form.MaximizeBox = $false
$form.MinimizeBox = $true
$form.ClientSize = New-Object System.Drawing.Size(460, 230)
$form.TopMost = $false

# Title label
$title = New-Object System.Windows.Forms.Label
$title.Text = 'Choose backend to launch'
$title.Font = New-Object System.Drawing.Font('Segoe UI', 12, [System.Drawing.FontStyle]::Bold)
$title.AutoSize = $true
$title.Location = New-Object System.Drawing.Point(20, 20)
$form.Controls.Add($title)

# Oracle button
$btnOracle = New-Object System.Windows.Forms.Button
$btnOracle.Text = 'Launch Oracle'
$btnOracle.Font = New-Object System.Drawing.Font('Segoe UI', 10)
$btnOracle.Size = New-Object System.Drawing.Size(180, 50)
$btnOracle.Location = New-Object System.Drawing.Point(20, 70)
$btnOracle.Add_Click({ Launch-Batch -batchPath $OracleBat -displayName 'Oracle' })
$form.Controls.Add($btnOracle)

# Mongo button
$btnMongo = New-Object System.Windows.Forms.Button
$btnMongo.Text = 'Launch MongoDB'
$btnMongo.Font = New-Object System.Drawing.Font('Segoe UI', 10)
$btnMongo.Size = New-Object System.Drawing.Size(180, 50)
$btnMongo.Location = New-Object System.Drawing.Point(220, 70)
$btnMongo.Add_Click({ Launch-Batch -batchPath $MongoBat -displayName 'MongoDB' })
$form.Controls.Add($btnMongo)

# Status label
$global:statusLabel = New-Object System.Windows.Forms.Label
$global:statusLabel.Text = 'Idle'
$global:statusLabel.Font = New-Object System.Drawing.Font('Segoe UI', 9)
$global:statusLabel.AutoSize = $true
$global:statusLabel.Location = New-Object System.Drawing.Point(20, 140)
$global:statusLabel.ForeColor = [System.Drawing.Color]::Gray
$form.Controls.Add($global:statusLabel)

# Close button
$btnClose = New-Object System.Windows.Forms.Button
$btnClose.Text = 'Close'
$btnClose.Font = New-Object System.Drawing.Font('Segoe UI', 9)
$btnClose.Size = New-Object System.Drawing.Size(100, 32)
$btnClose.Location = New-Object System.Drawing.Point(20, 175)
$btnClose.Add_Click({ $form.Close() })
$form.Controls.Add($btnClose)

# Keyboard shortcuts
$form.KeyPreview = $true
$form.Add_KeyDown({
    if ($_.KeyCode -eq 'Escape') { $form.Close() }
})

# Show the form
[void]$form.ShowDialog()