# MVP döntések és nyitott kérdések

Utolsó frissítés: 2026-07-06

## Lezárt döntések

### Indítás és pálya

- Az MVP-ben nincs pályaválasztó képernyő.
- A játék rögtön az erdei pályával indul.
- A teljes verzióban később jöhet a 4 pályás pályaválasztó.

### Helyezések és jutalmak

- Egy versenyben 5 tényleges helyezés van: a játékos és 4 NPC.
- A top 3 helyezett kap dobogós / kiemelt visszajelzést.
- Az MVP helyezési jutalomtáblája:
  - 1. hely: +10 aranypatkó
  - 2. hely: +7 aranypatkó
  - 3. hely: +5 aranypatkó
  - 4. hely: +3 aranypatkó
  - 5. hely: +1 aranypatkó

### Power-up és boost

- Az MVP-ben a power-up felvételkor boost töltetet ad.
- Egy power-up 20% boost töltetet adjon.
- Nincs külön power-up slot vagy külön power-up gomb az MVP-ben.
- A boost és a power-up rendszer egyszerű, gyerekbarát és gyorsan érthető marad.

### Látvány és pályarender

- Az MVP-ben is legyen 3D / pseudo-3D polish.
- A pályaszerkezet maradhat Tiled/adatalapú.
- A látvány kapjon mélységérzetet, rétegezést, árnyékokat, kameraérzetet, valamint 3D-snek ható lovakat, akadályokat és tereptárgyakat.
- Nem cél az MVP-ben egy túl bonyolult, teljesen saját 3D engine.

### Lovak statprofiljai

- Villám: magas végsebesség, gyengébb fordulás.
- Pihe: jobb irányíthatóság, kisebb végsebesség.
- Csillag: jobb boost töltés, átlagos sebesség.
- Futó: jobb gyorsulás, a többi stat átlagos.
- A lóválasztóban legyenek stat sávok.
- Minden lóhoz legyen rövid, gyerekbarát leírás.

### Lovasok

- A lovas nevét és fő színét a játékos válassza.
- Legyen gyermekbarát előre megadott névlista.
- Lehessen saját lovasnevet is beírni.
- A lovasok kapjanak apró stat bónuszt.
- A két apró lovas bónusz: +1% gyorsulás és +1% boost töltés.
- A lovasválasztás és a lóválasztás két külön lépés legyen.
- A választási sorrend: lovas -> ló.
- A lovas bónusz látható legyen UI-ban.
- Legyen 10 előre megadott gyermekbarát lovasnév.
- A 10 előre megadott lovasnév:
  - Peti
  - Szandi
  - Bogi
  - Máté
  - Lili
  - Dani
  - Panni
  - Marci
  - Zsófi
  - Levi
- Legyen "véletlen név" gomb.
- A saját lovasnév karakterlimitje 15 karakter legyen.

### NPC-k

- Az NPC nevek jöhetnek seedelt magyar névlistából.
- Az NPC-k ne használjanak boostot MVP-ben.
- Az NPC-k ne vegyenek fel boost power-upot MVP-ben.

### Pet rendszer

- A játékos az MVP-ben ingyen kap egy kezdő petet.
- A kezdő pet kutya.
- A kutya alapból unlockolt.
- A kezdő kutya MVP-ben ne adjon bónuszt, csak kozmetikai társ legyen.
- Legyen pet XP és pet szint már MVP-ben.
- A pet XP-t azokból a futamokból gyűjti a pet, amelyeken részt vesz.
- A pet összesen 10 szintet tudjon elérni MVP-ben.
- A pet szintje MVP-ben csak fejlődési visszajelzés legyen, ne adjon unlockot.
- A pet szintlépéshez fixen 100 pet XP kelljen szintenként.
- A többi pet későbbi vagy shopos unlock lehet, például 20 aranypatkóért.

### Játékos XP és szintek

- A játékos XP forrásai MVP-ben:
  - győzelem
  - rekorddöntés
  - lejátszott versenyek
- A játékos szintlépéshez szükséges XP lépcsőzetesen emelkedjen, tehát magasabb szinteken egyre több XP kelljen.
- MVP-ben 4 játékosszint legyen.
- Az első játékosszinthez 10 XP kelljen.
- A következő játékosszintekhez mindig 50%-kal több XP kelljen, kerekített értékekkel:
  - 1. szint: 10 XP
  - 2. szint: 15 XP
  - 3. szint: 23 XP
  - 4. szint: 35 XP
- Legyen 3 nehézségi szint:
  - könnyű
  - közepes
  - nehéz
- A 100%-os alap XP a nehézségi szinttől függjön:
  - könnyű: 10 XP
  - közepes: 20 XP
  - nehéz: 30 XP
- A legnehezebb szinten kapják a legtöbb XP-t a versenyzők.
- A nehézségi szint az aranypatkó jutalmakat is módosítsa.
- Az aranypatkó jutalmak nehézségi szorzói:
  - könnyű: 0,3x
  - közepes: 0,6x
  - nehéz: 1,0x
- A verseny XP helyezési szorzói:
  - 1. hely: 100%
  - 2. hely: 80%
  - 3. hely: 60%
  - 4. hely: 40%
  - 5. hely: 20%
- Rekorddöntésért +20% XP járjon.
- Lejátszott versenyenként +5% XP járjon.

### Shop és árlista

- Az MVP shop árlista elfogadott:
  - alap szín skin: 5 aranypatkó
  - szebb skin 1: 10 aranypatkó
  - szebb skin 2: 15 aranypatkó
  - szebb skin 3: 20 aranypatkó
  - pet unlock: 20 aranypatkó
  - upgrade 1-3: 10 aranypatkó
  - upgrade 4-6: 15 aranypatkó
  - upgrade 7-10: 20 aranypatkó
- A 10 upgrade kategóriái ezekből épüljenek:
  - gyorsaság
  - fordulás
  - ugrás
  - boost
  - lassítás csökkentése
- A 10 upgrade az 5 kategóriára 2-2 upgrade-es elosztásban menjen:
  - 2 gyorsaság upgrade
  - 2 fordulás upgrade
  - 2 ugrás upgrade
  - 2 boost upgrade
  - 2 lassításcsökkentés upgrade

### Irányítás és ugrás

- Az MVP-ben legyen külön ugrás gomb.
- A boost és az ugrás két külön kontroll.
- Az ugrás fő célja az akadályok miatti lassítás elkerülése.
- A tutorial külön tanítsa meg az ugrás gombot.

### Akadályok

- Az MVP-ben az akadályok lassítanak.
- Az akadályok MVP-ben nem vonnak pontot.
- Az erdei pályán 4 tematikus akadálytípus legyen:
  - kidőlt fa
  - kerítés
  - folyó
  - pocsolya
- MVP-ben minden akadály ugyanúgy lassítson.
- A pontlevonás későbbi nehézségi vagy hard mód funkció lehet.

### Profil és mentés

- Az MVP-ben egy profil legyen a tableten.
- Nincs profilválasztó képernyő.
- Minden progress egyetlen mentésbe kerül.
- MVP-ben ne legyen reset progress.
- A többprofilos mód későbbi bővítés lehet.

### Audio

- Az MVP-ben legyen hang.
- Az MVP-ben legyen háttérzene is.
- MVP-ben elég egy mute kapcsoló.
- Minimum audio tartalom:
  - erdei pálya háttérzene
  - gombnyomás SFX
  - aranypatkó felvétel SFX
  - boost SFX
  - ugrás SFX
  - akadály lassítás / ütközés SFX
  - célba érés SFX
  - vásárlás SFX
  - mute kapcsoló

## Még nyitott kérdések

### 1. Lovasok

1. Maradjon ez a 10 előre megadott gyermekbarát lovasnév, vagy cseréljünk belőlük?

### 2. Karakter- és lóválasztó UI

2. A lovas -> ló választás után legyen összegző megerősítő képernyő?

### 3. NPC-k

6. Legyenek külön fiú/lány NPC nevek?
7. Az NPC nevek versenyenként változzanak seed alapján, vagy pályánként fix ellenfelek legyenek?

### 4. Petek

6. A többi pet már látszódjon szürkítve a shopban?

### 5. Shop és upgrade-ek

11. Egy upgrade megvásárlása azonnal aktív legyen?
12. Upgrade-ek legyenek visszavonhatók? Javaslat: ne, mert profilfejlődésként működnek.
13. Vásárlás után az undo csak az adott istálló/shop képernyő elhagyásáig legyen elérhető?

### 6. XP és szintlépés

22. Szintlépés unlockoljon konkrét dolgokat MVP-ben?

### 7. Nehézségi szintek

27. Közepes fokozaton legyen NPC ütközés?
28. Nehéz fokozaton legyen pontvesztés NPC vagy akadály ütközéskor?
29. Közepes és nehéz fokozaton pontosan hogyan viselkedjen az NPC ütközés?

### 8. Akadálytípusok

32. Akadályfigyelmeztetés legyen UI ikon, pályán látható jel, vagy mindkettő?

### 9. Power-up részletek

36. Legyen-e külön vizuális ikon/effekt, amikor a játékos boost power-upot vesz fel?

### 10. Audio részletek

39. A háttérzene folyamatosan loopoljon verseny közben?
40. Menüben/istállóban is legyen külön háttérzene, vagy csak az erdei pályán?

### 11. Mentés és reset

42. Pontosan mit mentsünk MVP-ben?
   - aranypatkó
   - XP
   - játékosszint
   - pet XP / pet szint
   - unlockolt skinek
   - unlockolt petek
   - kiválasztott ló
   - kiválasztott lovas
   - kiválasztott pet
   - rekordidő
   - tutorial kész állapot
   - audio beállítások
43. Reset progress későbbi verzióban legyen rejtett vagy szülői megerősítéses?

### 12. Következő dokumentációs lépés

45. Külön MVP-specifikáció elkészült: `outputs/mvp-spec.md`.
46. Legyen "spec freeze" pont, ami után az MVP-t már nem bővítjük, csak finomítjuk?
47. A következő részletes tervezési fókusz mi legyen?
   - Phase 0: stabil build/setup
   - Phase 1: adatmodellek
   - Phase 2: erdei pálya render és 3D polish
   - Phase 3: verseny loop

## Rövid döntési lista a következő körre

Ha gyorsan akarunk haladni, elég ezekre válaszolni:

1. Maradjon a javasolt 10 lovasnév?
2. Legyen összegző megerősítő képernyő a lovas -> ló választás után?
3. Akadályfigyelmeztetés legyen UI ikon, pályán látható jel, vagy mindkettő?
4. Legyen most spec freeze, és utána részletes phase bontás az implementációhoz?
