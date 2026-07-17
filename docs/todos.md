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
- [x] Hiányzik a pályaválasztás a flow-ból (spec: lovas/ló/pálya választás).
- [ ] A teljes játék 4 pályája még nincs megvalósítva; MVP-ben csak az erdei pálya aktív.
- [x] Ló testreszabás még nem készült el (szín, sörény, nyereg).
- [x] Kis kedvenc választás és alap MVP-bónuszok összekötése.
- [ ] Tematikus power-upok és felvételük hiányzik a verseny során.
- [x] A karakterválasztó és a verseny HUD lovas/pet előnézetei generált pixel-art PNG-ket használnak; a procedurális rajzolás csak hiányzó asset fallback.

## Milestone 2 – Teendők

### Bugfixes
- [ ] Rider, pet, and color specification: végső kiválasztási és megjelenítési regressziótesztek.

### Playable loop polish (race feel, input, HUD, rewards)
- [x] Input finomhangolás: érzékenység, gyorsítás/lassítás görbék, irányváltás animáció
- [x] Versenyérzet: sebességvisszajelzés (sebességfüggő kamera zoom/dőlés)
- [x] Versenyérzet: sebességfüggő por effekt
- [x] Versenyeffekt: sparkle/boost vizuális effekt
- [x] HUD: aktív power-up időzítő, futamidő, köridő és célvonal-jelzés
- [x] HUD: legjobb idő részletes eredmény-összesítése
- [x] Célba érés flow: győzelmi panel, jutalom összegzés, újraindítás gomb
- [x] Jutalom rendszer: egyszerű valuta/pontok, beváltás preview
- [x] Akadályok a pályán: MVP-ben egységes lassítás, pontlevonás nélkül.


### Content expansion (more tracks, power-ups, assets)
- [ ] 3 új pálya (tengerpart, hegyek, éjszakai város) Tiled map
- [ ] Pálya-specifikus díszletek/tileset elemek
- [ ] Tematikus power-upok pályánként + ikonok (legalább 2-2)
- [ ] Új SFX: boost, pickup variánsok, célba érés
- [ ] Pixel art race háttér és pálya dekorációk frissítése

### Customization depth (horse/rider/pet cosmetics + bonuses)
- [ ] Ló testreszabás bővítése: minták / foltok / lábvédők
- [ ] Lovas ruházat: több szín + sisak / kabát variánsok
- [ ] Kedvenc bónuszok részletes balansz + UI leírás
- [ ] Pet kozmetika: színek + kis kiegészítők
- [ ] Mentett loadout előnézet a főmenüben
