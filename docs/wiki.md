# Versenylovak – Projekt wiki

## Összefoglaló
Az Versenylovak egy gyerekbarát, 2D pixel art stílusú lóverseny játék Android tabletre. A játékban választható lovas, ló, pálya, valamint kis kedvenc. A pályákon tematikus power-upok gyűjthetők.

## Célplatform
- Android tablet (elsődleges)
- Desktop (fejlesztéshez, gyors teszteléshez)

## Tech stack
- **Motor:** libGDX
- **Nyelv:** Java
- **Build:** Gradle multi-module
- **Szerkesztő:** VS Code
- **Pályák:** Tiled (.tmx/.tsx)

## Projekt modulok
- **core**: platformfüggetlen játéklogika (screen-ek, UI, játékobjektumok).
- **android**: Android launcher, manifest, erőforrások.
- **desktop**: Desktop launcher gyors futtatáshoz.

## Fő képernyők (jelenleg)
- **MainMenuScreen**: főmenü, Start gomb.
- **CharacterSelectScreen**: ló/lovas/kedvenc választó (placeholder).
- **RaceScreen**: verseny placeholder (kiválasztott opciók megjelenítése).

## Asset struktúra
```
assets/
├─ maps/          # Tiled pályák (.tmx) és tileset-ek (.tsx)
├─ sprites/       # Sprite sheet-ek
├─ ui/            # UI elemek
├─ sfx/           # Hangok
└─ data/          # JSON konfigurációk
```

## Első pálya
- **forest.tmx**: erdei pálya placeholder
- **forest.tsx**: tileset definíció (4x4, 32x32 tile)
- **forest_tiles.png**: 128x128 tileset kép

## Hogyan futtasd
- Desktop futtatás:
  - `./gradlew desktop:run`
- Android build:
  - `./gradlew android:assembleDebug`

## Következő lépések
- Asset pipeline véglegesítése (Aseprite → TexturePacker)
- Tiled pálya import a RaceScreen-be
- Egyszerű mozgásmodell + kamera követés
- Power-up rendszer alapjai

## Döntések és irányelvek
- **Pixel art** stílus prioritás.
- **Gyerekbarát UI** nagy gombokkal, egyszerű navigációval.
- **Moduláris felépítés** a későbbi bővíthetőség miatt.
