# MVP implementációs terv

Utolsó frissítés: 2026-07-06

## Cél

Ez a terv a `outputs/mvp-spec.md` alapján bontja fejlesztési fázisokra a lovas versenyjáték MVP-jét. A cél egy teljes, játszható, gyerekbarát tablet MVP, amely az erdei pályával indul, tartalmazza a lovas/ló/pet választást, verseny loopot, jutalmazást, shopot, hangot és mentést.

## Alapelv

- Az MVP specifikáció legyen befagyasztva: új funkció csak külön döntéssel kerüljön be.
- Először stabil, végigjátszható alap készüljön, utána polish.
- Minden fázis végén legyen futtatható állapot.
- A gyerekbarát UX és tablet ergonómia nem utólagos extra, hanem minden UI-fázis része.

## Phase 0: Projekt és build stabilizálás

### Cél

Legyen egy stabilan futtatható projektalap, amelyen biztonságosan lehet iterálni.

### Feladatok

- Projektstruktúra felmérése.
- Build, futtatás és teszt parancsok azonosítása.
- Aktuális hibák, placeholder állapotok és hiányzó asset útvonalak felmérése.
- Alap konfigurációk rendezése.
- Fejlesztői ellenőrző parancsok dokumentálása.

### Kész állapot

- A projekt lokálisan indítható.
- Van ismert parancs buildre/futtatásra.
- A fő képernyő vagy jelenlegi játékállapot hibamentesen betölt.
- A további fázisok előtt nincs blokkoló setup-hiba.

## Phase 1: Adatmodellek és mentés

### Cél

Az MVP összes alapadatának legyen egységes modellje és perzisztens mentése.

### Feladatok

- Lovas modell:
  - név
  - fő szín
  - bónusz típus
  - bónusz érték
- Ló modell:
  - név
  - statok
  - gyerekbarát leírás
- Pet modell:
  - típus
  - unlock állapot
  - XP
  - szint
- Progress modell:
  - aranypatkó
  - játékos XP
  - játékosszint
  - rekordidő
  - tutorial kész állapot
  - audio beállítások
  - kiválasztott lovas/ló/pet
  - unlockolt skinek/petek
- Egyprofilos mentés implementálása.
- Reset progress kihagyása MVP-ből.

### Kész állapot

- A játék képes menteni és visszatölteni az egyprofilos progresszt.
- Alap kezdőállapot létrejön új mentésnél.
- A kezdő kutya alapból unlockolt.
- A mentés tartalmazza az MVP-specben rögzített mezőket.

## Phase 2: Lovas, ló és pet választó flow

### Cél

Legyen végigvihető kezdő választási flow: lovas -> ló -> kezdő pet.

### Feladatok

- Lovasválasztó képernyő:
  - saját név beírás 15 karakterlimittel
  - 10 előre megadott név
  - véletlen név gomb
  - fő szín választás
  - látható +1% bónusz UI
- Lóválasztó képernyő:
  - 4 ló
  - stat sávok
  - rövid leírások
- Pet kijelzés:
  - kezdő kutya látszik
  - pet XP és szint helye megvan
- Választások mentése.

### Kész állapot

- A játékos kiválasztja vagy beírja a lovasnevet.
- A játékos színt és lovas bónuszt választ.
- A játékos kiválasztja a lovat.
- A kiválasztások mentődnek és újraindítás után is megmaradnak.

## Phase 3: Erdei pálya és pseudo-3D render

### Cél

Legyen játszható erdei pálya 3D/pseudo-3D érzettel.

### Feladatok

- Erdei pálya betöltése vagy létrehozása Tiled/adatalapú szerkezettel.
- Pseudo-3D kamera vagy izometrikus mélységérzet kialakítása.
- Rétegzett tereptárgyak és árnyékok.
- 4 akadálytípus elhelyezése:
  - kidőlt fa
  - kerítés
  - folyó
  - pocsolya
- Játékos ló és NPC-k mélységi rendezése.
- Tablet képarányok és érintéses UI helyigény ellenőrzése.

### Kész állapot

- Az erdei pálya betölt.
- A játékos lova látszik és mozog.
- A pálya 3D/pseudo-3D érzetet ad.
- A 4 akadálytípus megjelenik.
- Nincs pályaválasztó képernyő.

## Phase 4: Verseny loop és irányítás

### Cél

Legyen végigjátszható verseny kezdéstől célba érésig.

### Feladatok

- Verseny indítása az erdei pályán.
- Játékos mozgás:
  - gyorsítás
  - fordulás
  - boost
  - külön ugrás gomb
- 4 NPC ellenfél alap mozgással.
- NPC-k seedelt magyar nevekkel.
- NPC-k ne használjanak boostot.
- Célba érés és helyezés számítása.
- Top 3 dobogós visszajelzés.

### Kész állapot

- A verseny elindul, végigjátszható és befejezhető.
- 5 résztvevő helyezése kiszámolódik.
- A top 3 kiemelt visszajelzést kap.
- NPC boost nincs MVP-ben.

## Phase 5: Akadályok, ugrás és power-up

### Cél

Az akadályok és boost power-up működjenek az MVP szabályai szerint.

### Feladatok

- Akadály ütközés detektálása.
- Minden akadály azonos mértékben lassít.
- Akadály nem von pontot.
- Ugrással elkerülhető vagy mérsékelhető legyen a lassítás.
- Boost töltet rendszer.
- Power-up felvétel:
  - nincs slot
  - nincs külön power-up gomb
  - felvételkor +20% boost töltet
- NPC-k ne vegyenek fel boost power-upot.

### Kész állapot

- Az akadályok érezhetően lassítanak.
- Az ugrásnak van játékmechanikai haszna.
- A power-up felvétele 20% boostot ad.
- Nincs pontlevonás akadályból.

## Phase 6: XP, aranypatkó és progresszió

### Cél

A verseny után működjön a jutalmazás, XP, szintlépés és pet fejlődés.

### Feladatok

- Aranypatkó alapjutalom:
  - 1. hely: 10
  - 2. hely: 7
  - 3. hely: 5
  - 4. hely: 3
  - 5. hely: 1
- Nehézségi aranypatkó szorzók:
  - könnyű: 0,3x
  - közepes: 0,6x
  - nehéz: 1,0x
- XP alapértékek:
  - könnyű: 10 XP
  - közepes: 20 XP
  - nehéz: 30 XP
- Helyezési XP szorzók:
  - 1. hely: 100%
  - 2. hely: 80%
  - 3. hely: 60%
  - 4. hely: 40%
  - 5. hely: 20%
- Extra XP:
  - rekorddöntés: +20%
  - lejátszott verseny: +5%
- Játékosszintek:
  - 1. szint: 10 XP
  - 2. szint: 15 XP
  - 3. szint: 23 XP
  - 4. szint: 35 XP
- Pet XP:
  - csak részt vett futamokból
  - 100 XP / pet szint
  - maximum 10 pet szint
  - pet szint csak visszajelzés, nem unlock

### Kész állapot

- Verseny után helyesen számolódik az aranypatkó.
- Verseny után helyesen számolódik a játékos XP.
- Rekorddöntés és lejátszott verseny bónusz működik.
- Játékosszint és pet szint frissül.
- Minden progress mentődik.

## Phase 7: Shop és upgrade-ek

### Cél

Legyen használható shop / istálló, ahol a játékos elköltheti az aranypatkókat.

### Feladatok

- Shop UI kialakítása.
- Skin árak:
  - alap szín skin: 5
  - szebb skin 1: 10
  - szebb skin 2: 15
  - szebb skin 3: 20
- Pet unlock ár: 20.
- Upgrade árak:
  - upgrade 1-3: 10
  - upgrade 4-6: 15
  - upgrade 7-10: 20
- 10 upgrade:
  - 2 gyorsaság
  - 2 fordulás
  - 2 ugrás
  - 2 boost
  - 2 lassításcsökkentés
- Vásárlás mentése.
- Elégtelen aranypatkó állapot kezelése.

### Kész állapot

- A játékos vásárolhat skineket és upgrade-eket.
- Az aranypatkó levonódik.
- A vásárlás mentődik.
- Az upgrade hatása megjelenik a versenyben vagy statban.

## Phase 8: Audio és tutorial

### Cél

Legyen alap hangélmény és rövid, gyerekbarát tutorial.

### Feladatok

- Egy mute kapcsoló.
- Erdei pálya háttérzene.
- SFX-ek:
  - gombnyomás
  - aranypatkó felvétel
  - boost
  - ugrás
  - akadály lassítás / ütközés
  - célba érés
  - vásárlás
- Tutorial lépések:
  - mozgás
  - ugrás
  - boost
  - akadályok
  - power-up
  - jutalmak

### Kész állapot

- Hangok működnek.
- Mute kapcsoló minden hangot némít.
- Tutorial érthetően végigviszi az MVP aktív funkcióit.
- Tutorial kész állapot mentődik.

## Phase 9: Polish, balansz és QA

### Cél

Az MVP legyen stabil, gyerekbarát és első tesztelésre alkalmas.

### Feladatok

- Tablet UI ellenőrzés.
- Gombméretek és érintési célok ellenőrzése.
- Szövegek rövidítése, gyerekbarát fogalmazás.
- Ló stat balansz.
- NPC sebesség balansz.
- XP és aranypatkó jutalom balansz.
- Akadály lassítás mértékének finomítása.
- Pseudo-3D vizuális polish.
- Mentés és visszatöltés tesztelése.
- Hang némítás és audio edge case-ek ellenőrzése.

### Kész állapot

- A játék végigjátszható több egymást követő versennyel.
- Nincs ismert blokkoló hiba.
- A progress nem veszik el.
- A fő MVP flow 5-10 perces tesztmenetben stabil.

## Javasolt mérföldkövek

### M1: Stabil alap

Phase 0-1 kész.

Eredmény: futtatható projekt, adatmodellek, mentés.

### M2: Választásból versenybe

Phase 2-4 kész.

Eredmény: lovas/ló választás után elindul és befejezhető az erdei verseny.

### M3: Teljes játékmechanikai MVP

Phase 5-7 kész.

Eredmény: akadályok, power-up, XP, jutalom, shop és upgrade-ek működnek.

### M4: Tesztelhető MVP

Phase 8-9 kész.

Eredmény: hang, tutorial, polish, balansz és QA után gyerek/parent playtestre alkalmas build.

## Nyitott, de nem blokkoló döntések

- Maradjon-e végleg a javasolt 10 lovasnév.
- Legyen-e összegző megerősítő képernyő a lovas -> ló választás után.
- Akadályfigyelmeztetés UI ikon, pályán látható jel, vagy mindkettő legyen.
- Legyen-e külön vizuális ikon/effekt boost power-up felvételkor.
- A háttérzene csak verseny közben szóljon-e, vagy menüben/istállóban is.

## Első implementációs teendők

1. Projekt build és futtatás ellenőrzése.
2. Jelenlegi kódstruktúra feltérképezése.
3. `GameConfig` / adatkonstansok létrehozása az MVP számértékekhez.
4. Egyprofilos mentési modell kialakítása.
5. Lovas/ló/pet alapadatok felvétele.
