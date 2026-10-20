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
- [x] Izometrikus 2.5D pályarenderelés: gyémántcsempés talaj, mélységi sorrend és izometrikus objektumprojekció.
- [x] Nagyobb, kanyargó 2.5D pálya: képernyőt kitöltő táj, kétoldali kerítés, külső táj és kerítésérintési lassítás.
- [x] Négy pályához tartozó Tiled map fájl és pályaválasztó képernyő rendelkezésre áll.
- [x] Az MVP közvetlenül az erdei pályával indul; a többpályás flow a specifikáció szerint nem része az MVP-nek.
- [x] Alap lótestreszabás (szín, sörény, nyereg és mintaválasztás) elérhető; a további kozmetika későbbi bővítés.
- [x] Kis kedvenc választás és alap MVP-bónuszok összekötése.
- [x] Tematikus power-upok és felvételük: boost-töltet, Gyorsító és Pajzs.
- [x] A karakterválasztó és a verseny HUD lovas/pet előnézetei generált pixel-art PNG-ket használnak; a procedurális rajzolás csak hiányzó asset fallback.

## Milestone 2 – Teendők

### Bugfixes
- [x] Rider, pet, and color specification: végső kiválasztási és megjelenítési regressziótesztek.

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
- [x] Tiled map fájlok a tengerparti, hegyi és éjszakai városi pályához
- [ ] A három további pálya vizuális és játékmeneti ellenőrzése; csak külön scope-döntés után kerüljenek az MVP flow-ba
- [ ] Pálya-specifikus díszletek/tileset elemek
- [ ] Tematikus power-upok pályánként + ikonok (legalább 2-2)
- [ ] Új SFX: boost, pickup variánsok, célba érés
- [x] Pixel-art erdei pályadekorációk frissítése (tölgy, fenyő, bokor, irányjelző, kő)

### Customization depth (horse/rider/pet cosmetics + bonuses)
- [ ] Ló testreszabás további bővítése: minták / foltok / lábvédők (alap mintaválasztás már van)
- [ ] Lovas ruházat további bővítése: sisak / kabát variánsok (alap színek és outfit stílusok már vannak)
- [ ] Kedvenc bónuszok részletes balansz + UI leírás
- [ ] Pet kozmetika: színek + kis kiegészítők
- [ ] Mentett loadout előnézet a főmenüben

## MVP release gate

- [x] Adatmodell, mentés, jutalmak és shop-vásárlások automatizált egységtesztjei rendelkezésre állnak.
- [ ] Teljes első indítási, karakterválasztási, verseny-, jutalom- és shop-flow végigjátszása Android emulátoron vagy eszközön (APK telepítés és indulás ellenőrizve; az eszköz az interaktív ellenőrzés előtt lecsatlakozott).
- [ ] A playtest checklist minden pontjának ellenőrzése; build-siker önmagában nem helyettesíti az eszköztesztet.
