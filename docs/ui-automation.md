# Headless UI automation

The project uses [Maestro CLI](https://docs.maestro.dev/maestro-cli) for black-box Android UI smoke tests. Maestro drives the installed APK through the emulator and records screenshots at the main lifecycle checkpoints.

## Install Maestro

Follow the official installation instructions:

```text
https://docs.maestro.dev/maestro-cli/install
```

Verify the installation:

```bash
maestro --version
```

## Run the MVP flow

Start the `HorseMVP_API34` emulator first, then run:

```bash
./scripts/run_maestro.sh
```

The script builds and installs the debug APK, waits for ADB, and runs `.maestro/mvp_smoke.yaml`. To reuse an existing APK:

```bash
SKIP_BUILD=1 ./scripts/run_maestro.sh
```

For a specific ADB executable or flow:

```bash
ADB_BIN=/path/to/adb MAESTRO_FLOW=.maestro/mvp_smoke.yaml ./scripts/run_maestro.sh
```

On Windows, where the emulator and ADB run natively, use:

```bat
scripts\run_maestro_windows.cmd
```

Set `MAESTRO_BIN` if Maestro is installed outside `%USERPROFILE%\.maestro\bin\maestro.bat`.

## Current UI testing limitation

The game UI is rendered by LibGDX `Stage` objects inside a single Android `SurfaceView`, so the Android accessibility hierarchy does not expose the individual game buttons. The smoke flow therefore uses a percentage-based coordinate tap and screenshot checkpoints. Once semantic accessibility identifiers are added to the game UI, replace coordinate taps with stable `id` or text selectors.
