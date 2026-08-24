# Android-Friends MVP playtest checklist

Last updated: 2026-07-15

## Build

- Branch: `codex/mvp-phase0-implementation`
- Debug APK: `android/build/outputs/apk/debug/android-debug.apk`
- Last local checks:
  - `.\gradlew core:test`
  - `.\gradlew assembleDebug`

## First-run flow

- Main menu opens without crash.
- Mute toggle changes menu audio state.
- First practice/tutorial opens and can be completed.
- Tutorial text mentions movement, jump, obstacle slowdown, +20% boost pickup, 4 opponents, podium, rewards, and new race.
- Start game opens character selection.

## Selection flow

- Rider selection is shown before horse selection.
- Preset rider names can be changed.
- Custom rider name accepts up to 15 characters.
- Random name button changes the rider name.
- Rider bonus text is visible.
- Horse stats and child-friendly description are visible.
- Pet starts as `Kutya`.
- Difficulty can be changed.
- Starting the race goes directly to `forest.tmx`.

## Race flow

- Forest race loads.
- Player horse is visible and controllable with joystick.
- Boost button uses charge only when available.
- Jump button has visible cooldown and avoids obstacle slowdown when timed.
- Boost power-up pickup adds 20% boost charge.
- Four obstacle visuals can appear: fallen log, fence, river, puddle.
- Four NPC racers are visible and do not use boost.
- HUD text wraps instead of overflowing.
- Finish shows placement, horseshoes, XP, record feedback if applicable, and podium.
- New race button starts another forest race.
- Race timer and gameplay spawning stop after finish.

## Progression and shop

- Race result updates horseshoes, player XP, pet XP, player level, pet level, and record time.
- Main menu progress summary updates after race.
- Shop labels wrap and remain readable.
- Skin purchase/select works when affordable.
- Pet unlock works when affordable.
- Upgrade purchase works and affects race stats.
- No reset progress control is visible.

## Known residual risks

- Character selection is visually ordered rider -> horse, but still implemented as one screen rather than a strict two-screen flow.
- NPC racing is deterministic and visual, not full collision/AI.
- Procedural placeholders remain for several in-race visuals.
- Deprecated Gradle warnings remain, but `assembleDebug` succeeds.
