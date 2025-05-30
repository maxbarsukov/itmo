# Author: https://github.com/Vovanm88

$endTime = (Get-Date).AddHours(1)
while ((Get-Date) -lt $endTime) {
    if (-not (Get-Process -Name "explorer" -ErrorAction SilentlyContinue)) {
        Start-Process explorer.exe
        Write-Host "explorer.exe перезапущен в $(Get-Date)"
    }
    Start-Sleep -Seconds 5
}
