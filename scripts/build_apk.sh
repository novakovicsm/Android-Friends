#!/usr/bin/env bash
set -euo pipefail

BUILD_TYPE="${1:-debug}"

case "$BUILD_TYPE" in
  debug|release) ;;
  *)
    echo "Usage: $0 [debug|release]" >&2
    exit 1
    ;;
 esac

if [[ "$BUILD_TYPE" == "release" ]]; then
  if [[ -z "${ANDROID_KEYSTORE_PATH:-}" || -z "${ANDROID_KEYSTORE_PASSWORD:-}" || -z "${ANDROID_KEY_ALIAS:-}" || -z "${ANDROID_KEY_PASSWORD:-}" ]]; then
    echo "Release build requires ANDROID_KEYSTORE_PATH, ANDROID_KEYSTORE_PASSWORD, ANDROID_KEY_ALIAS, ANDROID_KEY_PASSWORD." >&2
    exit 1
  fi
  echo "Building release APK..."
  ./gradlew :android:assembleRelease \
    -Pandroid.injected.signing.store.file="$ANDROID_KEYSTORE_PATH" \
    -Pandroid.injected.signing.store.password="$ANDROID_KEYSTORE_PASSWORD" \
    -Pandroid.injected.signing.key.alias="$ANDROID_KEY_ALIAS" \
    -Pandroid.injected.signing.key.password="$ANDROID_KEY_PASSWORD"
  echo "APK: android/build/outputs/apk/release/android-release.apk"
else
  echo "Building debug APK..."
  ./gradlew :android:assembleDebug
  echo "APK: android/build/outputs/apk/debug/android-debug.apk"
fi
