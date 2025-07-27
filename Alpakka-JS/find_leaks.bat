@echo off
REM Change the directory to the location of this script
cd /d "%~dp0"

REM Loop through each file in the apks directory with .apk extension
for %%f in (apks\*.apk) do (
    REM Run the command for each .apk file
    npx alpakka classic ./scripts/DetectAndCorrectLeaks.js -p "%%f"
)

REM Pause to keep the command prompt open
pause