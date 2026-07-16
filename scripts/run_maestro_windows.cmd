@echo off
setlocal

set "ROOT_DIR=%~dp0.."
set "APK_PATH=%ROOT_DIR%\android\build\outputs\apk\debug\android-debug.apk"
set "FLOW_PATH=%ROOT_DIR%\.maestro\mvp_smoke.yaml"
if not defined ANDROID_SDK_ROOT set "ANDROID_SDK_ROOT=%USERPROFILE%\AppData\Local\Android\Sdk"
set "ADB_BIN=%ANDROID_SDK_ROOT%\platform-tools\adb.exe"
if not exist "%ADB_BIN%" set "ADB_BIN=%LOCALAPPDATA%\Android\Sdk\platform-tools\adb.exe"
if not defined MAESTRO_BIN set "MAESTRO_BIN=%USERPROFILE%\.maestro\bin\maestro.bat"

if not exist "%MAESTRO_BIN%" (
  echo Maestro CLI not found: %MAESTRO_BIN%
  echo Set MAESTRO_BIN to the installed maestro.bat path.
  exit /b 127
)

if not exist "%ADB_BIN%" (
  echo Android adb not found: %ADB_BIN%
  exit /b 127
)

if not defined JAVA_HOME (
  for /f "delims=" %%J in ('where java 2^>nul') do if not defined JAVA_EXE set "JAVA_EXE=%%J"
) else (
  set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
)

if not "%SKIP_BUILD%"=="1" call "%ROOT_DIR%\gradlew.bat" --no-daemon :android:assembleDebug || exit /b %ERRORLEVEL%
"%ADB_BIN%" wait-for-device || exit /b %ERRORLEVEL%
"%ADB_BIN%" install --no-incremental -r "%APK_PATH%" || exit /b %ERRORLEVEL%

set "MAESTRO_CLI_NO_ANALYTICS=1"
set "MAESTRO_CLI_ANALYSIS_NOTIFICATION_DISABLED=true"
call "%MAESTRO_BIN%" test "%FLOW_PATH%"
exit /b %ERRORLEVEL%
