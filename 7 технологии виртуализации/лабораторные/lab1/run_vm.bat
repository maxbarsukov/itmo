echo 1 - Windows
echo 2 - Ubuntu
echo 0 - Exit
set /p choice=Choose number of VM: 

if "%choice%"=="1" (
    echo Launch WS_BMA_win...
    VBoxManage startvm "WS_BMA_win" --type gui
    goto end
)

if "%choice%"=="2" (
    echo Launch WS_BMA_ubuntu...
    VBoxManage startvm "WS_BMA_ubuntu" --type gui
    goto end
)

if "%choice%"=="0" (
    goto end
)

:end
pause
