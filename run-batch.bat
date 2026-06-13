@echo off
REM run-batch.bat
REM Usage: run-batch.bat [YYYY-MM-DD] [BATCH_KEY] [HOST]
SETLOCAL
set DATE=%1
set BATCH_KEY=%2
set HOST=%3
if "%DATE%"=="" (
  for /f "tokens=1-3 delims=/.-" %%a in ('powershell -NoProfile -Command "Get-Date -Format yyyy-MM-dd"') do set DATE=%%a-%%b-%%c
)
if "%HOST%"=="" set HOST=http://localhost:4289/auto/prod/api/v1
if "%BATCH_KEY%"=="" (
  echo No batch key provided, will call without batch key header.
) else (
  echo Using batch key: %BATCH_KEY%
)

echo Calling batch run for date %DATE% at %HOST%/batch/run
if "%BATCH_KEY%"=="" (
  curl -s -X POST "%HOST%/batch/run?date=%DATE%" -w "\nHTTP_CODE:%{http_code}\n" -o response.json
) else (
  curl -s -X POST "%HOST%/batch/run?date=%DATE%" -H "X-BATCH-KEY: %BATCH_KEY%" -w "\nHTTP_CODE:%{http_code}\n" -o response.json
)

type response.json
echo.
endlocal
