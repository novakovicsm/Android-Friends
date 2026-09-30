#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
APK_PATH="$ROOT_DIR/android/build/outputs/apk/debug/android-debug.apk"
FLOW_PATH="${MAESTRO_FLOW:-$ROOT_DIR/.maestro/mvp_smoke.yaml}"

if ! command -v maestro >/dev/null 2>&1; then
  echo "Maestro CLI is not installed. Install it from https://docs.maestro.dev/maestro-cli/install" >&2
  exit 127
fi

ADB_BIN="${ADB_BIN:-}"
if [[ -z "$ADB_BIN" ]]; then
  if command -v adb >/dev/null 2>&1; then
    ADB_BIN="$(command -v adb)"
  elif [[ -x "/mnt/c/Users/Admin/AppData/Local/Android/Sdk/platform-tools/adb.exe" ]]; then
    ADB_BIN="/mnt/c/Users/Admin/AppData/Local/Android/Sdk/platform-tools/adb.exe"
  else
    echo "adb was not found. Set ADB_BIN to the Android platform-tools adb executable." >&2
    exit 127
  fi
fi

ADB_APK_PATH="$APK_PATH"
if [[ "$ADB_BIN" == *.exe ]] && command -v wslpath >/dev/null 2>&1; then
  ADB_APK_PATH="$(wslpath -w "$APK_PATH")"
fi

export ANDROID_HOME="${ANDROID_HOME:-/mnt/c/Users/Admin/Documents/Codex/.tools/android-sdk}"
export MAESTRO_CLI_NO_ANALYTICS="${MAESTRO_CLI_NO_ANALYTICS:-1}"
export MAESTRO_CLI_ANALYSIS_NOTIFICATION_DISABLED="${MAESTRO_CLI_ANALYSIS_NOTIFICATION_DISABLED:-true}"

if [[ "${SKIP_BUILD:-0}" != "1" ]]; then
  export JAVA_HOME="${JAVA_HOME:-/mnt/c/Users/Admin/Documents/Codex/.tools/jdk-17.0.19+10}"
  "$ROOT_DIR/gradlew" --no-daemon :android:assembleDebug
fi

"$ADB_BIN" wait-for-device
"$ADB_BIN" install --no-incremental -r "$ADB_APK_PATH"

echo "Running Maestro flow: $FLOW_PATH"
maestro test "$FLOW_PATH"
