$ErrorActionPreference = "Stop"

$projectDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$javaExe = "C:\Program Files\Java\jdk-21.0.10\bin\java.exe"
$payaraJar = "$env:USERPROFILE\.m2\repository\fish\payara\extras\payara-micro\6.2024.10\payara-micro-6.2024.10.jar"
$postgresJar = "$env:USERPROFILE\.m2\repository\org\postgresql\postgresql\42.7.4\postgresql-42.7.4.jar"
$pidFile = Join-Path $projectDir "payara.pid"
$logFile = Join-Path $projectDir "payara.log"
$stdoutFile = Join-Path $projectDir "payara.out.log"
$stderrFile = Join-Path $projectDir "payara.err.log"

Set-Location $projectDir

if (Test-Path $pidFile) {
    $existingPid = Get-Content $pidFile -ErrorAction SilentlyContinue
    if ($existingPid) {
        $existingProcess = Get-Process -Id $existingPid -ErrorAction SilentlyContinue
        if ($existingProcess) {
            Write-Host "Payara zaten calisiyor. PID: $existingPid"
            Write-Host "Uygulama: http://localhost:9093/kitap-satis/login.xhtml"
            exit 0
        }
    }
    Remove-Item $pidFile -ErrorAction SilentlyContinue
}

mvn package

$process = Start-Process `
    -FilePath $javaExe `
    -ArgumentList @(
        "-jar", $payaraJar,
        "--port", "9093",
        "--logtofile", $logFile,
        "--addlibs", $postgresJar,
        "--postbootcommandfile", "payara-postboot.asadmin",
        "--deploy", "target\kitap-satis-1.0-SNAPSHOT.war",
        "--contextroot", "kitap-satis"
    ) `
    -WorkingDirectory $projectDir `
    -WindowStyle Hidden `
    -RedirectStandardOutput $stdoutFile `
    -RedirectStandardError $stderrFile `
    -PassThru

$process.Id | Set-Content $pidFile

Write-Host "Payara baslatildi. PID: $($process.Id)"
Write-Host "Uygulama: http://localhost:9093/kitap-satis/login.xhtml"
Write-Host "Durdurmak icin: .\\stop-project.ps1"
