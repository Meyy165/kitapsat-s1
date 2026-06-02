$projectDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$pidFile = Join-Path $projectDir "payara.pid"

if (-not (Test-Path $pidFile)) {
    Write-Host "Kayitli Payara process'i yok."
    exit 0
}

$payaraPid = Get-Content $pidFile -ErrorAction SilentlyContinue
if (-not $payaraPid) {
    Remove-Item $pidFile -ErrorAction SilentlyContinue
    Write-Host "PID dosyasi bostu."
    exit 0
}

$process = Get-Process -Id $payaraPid -ErrorAction SilentlyContinue
if ($process) {
    Stop-Process -Id $payaraPid -Force
    Write-Host "Payara durduruldu. PID: $payaraPid"
} else {
    Write-Host "Process zaten calismiyor. PID: $payaraPid"
}

Remove-Item $pidFile -ErrorAction SilentlyContinue
