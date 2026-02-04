# Fejlesztési TODO lista

Az alábbi lista a hátralévő fő feladatokat tartalmazza. A későbbi feladatokat lépésről lépésre fogjuk megoldani.

## 1. UI és képernyő flow
- [ ] Main menu finomhangolás (pixel art háttér, logo, gomb stílusok)
- [ ] Karakterválasztó UI: ló/lovas/kedvenc előnézet (placeholder sprite-ok)
- [ ] Pályaválasztó képernyő (4 pálya, tematikus kártyák)
- [ ] Verseny HUD (sebesség, kör, power-up indikátor)

## 2. Asset pipeline
- [ ] Asset mappastruktúra véglegesítése (sprites, ui, maps, sfx)
- [ ] Aseprite → TexturePacker workflow dokumentálása
- [ ] Tiled map első pálya (erdő) létrehozása

## 3. Játékmenet alapok
- [ ] RaceScreen: alap pályarajzolás (tilemap)
- [ ] Ló sprite animációk (idle, run)
- [ ] Egyszerű mozgásmodell (gyorsulás, max speed)
- [ ] Kamera követés

## 4. Power-up rendszer
- [ ] Power-up adatmodellek (JSON)
- [ ] Power-up spawn logika
- [ ] Power-up felvétel és UI jelzés

## 5. Testreszabás és mentés
- [ ] Ló testreszabás (szín, sörény, nyereg)
- [ ] Lovas ruházat színek
- [ ] Kedvenc választás és bónuszok
- [ ] Kiválasztott beállítások mentése (local prefs)

## 6. Hang és visszajelzések
- [ ] Alap SFX (gomb katt, power-up, győzelem)
- [ ] Zene (menü + pálya)
- [ ] Egyszerű rezgés / feedback (opcionális)

## 7. Technikai adósság / refaktor
- [ ] AssetManager bevezetése
- [ ] UI skin létrehozása (skin.json)
- [ ] Shared UI komponensek
- [ ] Screen navigációs helper

## 8. Release előkészítés
- [ ] App ikon és splash
- [ ] Alap beállítások / privacy notes
- [ ] APK build script + release pipeline
