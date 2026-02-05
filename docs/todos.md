# Fejlesztési TODO lista

Az alábbi lista a hátralévő fő feladatokat tartalmazza. A későbbi feladatokat lépésről lépésre fogjuk megoldani.

## 1. UI és képernyő flow
- [x] Main menu finomhangolás (pixel art háttér, logo, gomb stílusok)
- [x] Karakterválasztó UI: ló/lovas/kedvenc előnézet (placeholder sprite-ok)
- [x] Pályaválasztó képernyő (4 pálya, tematikus kártyák)
- [x] Verseny HUD (sebesség, kör, power-up indikátor)

## 2. Asset pipeline
- [x] Asset mappastruktúra véglegesítése (sprites, ui, maps, sfx)
- [x] Aseprite → TexturePacker workflow dokumentálása
- [x] Tiled map első pálya (erdő) létrehozása

## 3. Játékmenet alapok
- [x] RaceScreen: alap pályarajzolás (tilemap)
- [x] Ló sprite animációk (idle, run)
- [x] Egyszerű mozgásmodell (gyorsulás, max speed)
- [x] Kamera követés

## 4. Power-up rendszer
- [x] Power-up adatmodellek (JSON)
- [x] Power-up spawn logika
- [x] Power-up felvétel és UI jelzés

## 5. Testreszabás és mentés
- [x] Ló testreszabás (szín, sörény, nyereg)
- [x] Lovas ruházat színek
- [x] Kedvenc választás és bónuszok
- [x] Kiválasztott beállítások mentése (local prefs)

## 6. Hang és visszajelzések
- [x] Alap SFX (gomb katt, power-up, győzelem)
- [x] Zene (menü + pálya)
- [x] Egyszerű rezgés / feedback (opcionális)

## 7. Technikai adósság / refaktor
- [x] AssetManager bevezetése
- [x] UI skin létrehozása (skin.json)
- [x] Shared UI komponensek
- [x] Screen navigációs helper

## 8. Release előkészítés
- [x] App ikon és splash
- [x] Alap beállítások / privacy notes
- [x] APK build script + release pipeline

## Specifikációs eltérések / issue-k
- [ ] Hiányzik a pályaválasztás a flow-ból (spec: lovas/ló/pálya választás).
- [ ] A specifikáció szerinti 4 pálya nincs megvalósítva (jelenleg 1 placeholder map).
- [ ] Ló testreszabás még nem készült el (szín, sörény, nyereg).
- [ ] Kis kedvenc választás nincs összekötve bónuszokkal.
- [ ] Tematikus power-upok és felvételük hiányzik a verseny során.
- [ ] Pixel art UI/assetek helyett még csak programozott placeholder színek vannak.
