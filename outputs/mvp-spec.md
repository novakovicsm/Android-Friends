# Lovas versenyjáték MVP specifikáció

Utolsó frissítés: 2026-07-06

## MVP cél

Az MVP célja egy gyerekbarát, tableten jól játszható lovas versenyjáték első teljes játszható verziója. A játék azonnal az erdei pályával indul, egyszerű irányítással, 3D/pseudo-3D látványpolish-sal, alap progresszióval, shop-pal, hanggal és mentéssel.

## Fő flow

1. Lovas választás és személyre szabás.
2. Ló választás.
3. Erdei verseny indítása.
4. Verseny teljesítése a játékos és 4 NPC részvételével.
5. Helyezési, XP és aranypatkó jutalmak kiosztása.
6. Progress mentése.
7. Shop / istálló fejlesztések használata.

MVP-ben nincs pályaválasztó képernyő. A játék rögtön az erdei pályával indul.

## Lovasok

- A lovas nevét és fő színét a játékos választja.
- A választás sorrendje: lovas -> ló.
- A lovas bónusz látható legyen UI-ban.
- Két apró lovas bónusz van:
  - +1% gyorsulás
  - +1% boost töltés
- Legyen saját név beírás, maximum 15 karakterrel.
- Legyen "véletlen név" gomb.
- Legyen 10 előre megadott gyerekbarát név:
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

## Lovak

MVP-ben 4 választható ló van. A lóválasztóban legyen stat sáv és rövid, gyerekbarát leírás.

- Villám: magas végsebesség, gyengébb fordulás.
- Pihe: jobb irányíthatóság, kisebb végsebesség.
- Csillag: jobb boost töltés, átlagos sebesség.
- Futó: jobb gyorsulás, a többi stat átlagos.

## Verseny

- Egy versenyben 5 résztvevő van: a játékos és 4 NPC.
- A top 3 kap dobogós / kiemelt visszajelzést.
- Az NPC nevek seedelt magyar névlistából jöhetnek.
- Az NPC-k MVP-ben nem használnak boostot és nem vesznek fel boost power-upot.

## Helyezési jutalom

Alap aranypatkó jutalom:

- 1. hely: +10 aranypatkó
- 2. hely: +7 aranypatkó
- 3. hely: +5 aranypatkó
- 4. hely: +3 aranypatkó
- 5. hely: +1 aranypatkó

Nehézségi aranypatkó szorzók:

- Könnyű: 0,3x
- Közepes: 0,6x
- Nehéz: 1,0x

## Nehézség és XP

Három nehézségi szint legyen:

- Könnyű: 100%-os alap XP = 10 XP
- Közepes: 100%-os alap XP = 20 XP
- Nehéz: 100%-os alap XP = 30 XP

Helyezési XP szorzók:

- 1. hely: 100%
- 2. hely: 80%
- 3. hely: 60%
- 4. hely: 40%
- 5. hely: 20%

Extra XP:

- Rekorddöntés: +20%
- Lejátszott verseny: +5%

Játékosszintek:

- MVP-ben 4 játékosszint legyen.
- 1. szint: 10 XP
- 2. szint: 15 XP
- 3. szint: 23 XP
- 4. szint: 35 XP

## Pet rendszer

- A játékos ingyen kap egy kezdő petet.
- A kezdő pet kutya.
- A kutya alapból unlockolt.
- A kezdő kutya MVP-ben nem ad bónuszt, csak kozmetikai társ.
- Legyen pet XP és pet szint MVP-ben.
- A pet azokból a futamokból gyűjt XP-t, amelyeken részt vesz.
- A pet maximum 10 szintet érhet el.
- Szintenként fixen 100 pet XP kell.
- A pet szint csak fejlődési visszajelzés, nem ad unlockot.

## Irányítás

- Legyen külön ugrás gomb.
- A boost és az ugrás két külön kontroll.
- Az ugrás fő célja az akadályok miatti lassítás elkerülése.
- A tutorial külön tanítsa meg az ugrás gombot.

## Power-up és boost

- MVP-ben a power-up felvételkor boost töltetet ad.
- Egy power-up 20% boost töltetet ad.
- Nincs külön power-up slot.
- Nincs külön power-up gomb.
- A boost-power-up egyszerű, gyerekbarát és azonnal érthető.

## Erdei pálya és akadályok

- Az MVP-ben az erdei pálya az egyetlen aktív pálya.
- Legyen 3D / pseudo-3D polish: mélységérzet, rétegezés, árnyékok, kameraérzet, 3D-snek ható lovak, akadályok és tereptárgyak.
- A pályaszerkezet maradhat Tiled/adatalapú.
- Nem cél teljesen saját, bonyolult 3D engine.

MVP akadálytípusok:

- kidőlt fa
- kerítés
- folyó
- pocsolya

Akadálylogika:

- Az akadályok lassítanak.
- Az akadályok nem vonnak pontot.
- Minden akadály ugyanúgy lassít MVP-ben.
- Pontlevonás későbbi hard mód funkció lehet.

## Shop és upgrade-ek

Árlista:

- Alap szín skin: 5 aranypatkó
- Szebb skin 1: 10 aranypatkó
- Szebb skin 2: 15 aranypatkó
- Szebb skin 3: 20 aranypatkó
- Pet unlock: 20 aranypatkó
- Upgrade 1-3: 10 aranypatkó
- Upgrade 4-6: 15 aranypatkó
- Upgrade 7-10: 20 aranypatkó

Upgrade elosztás:

- 2 gyorsaság upgrade
- 2 fordulás upgrade
- 2 ugrás upgrade
- 2 boost upgrade
- 2 lassításcsökkentés upgrade

## Audio

- MVP-ben legyen hang.
- MVP-ben legyen háttérzene.
- MVP-ben elég egy mute kapcsoló.

Minimum audio tartalom:

- erdei pálya háttérzene
- gombnyomás SFX
- aranypatkó felvétel SFX
- boost SFX
- ugrás SFX
- akadály lassítás / ütközés SFX
- célba érés SFX
- vásárlás SFX

## Mentés és profil

- MVP-ben egy profil legyen a tableten.
- Nincs profilválasztó.
- Minden progress egyetlen mentésbe kerül.
- MVP-ben ne legyen reset progress.

Mentendő adatok:

- aranypatkó
- XP
- játékosszint
- pet XP
- pet szint
- unlockolt skinek
- unlockolt petek
- kiválasztott ló
- kiválasztott lovas
- kiválasztott pet
- rekordidő
- tutorial kész állapot
- audio beállítások

## MVP-n kívül / későbbre hagyva

- 4 pályás pályaválasztó.
- Több profil.
- Reset progress.
- Pontlevonás akadályoknál.
- NPC boost használat.
- Bonyolultabb power-up rendszer.
- Teljes saját 3D engine.
- Pet szinthez kötött unlockok.

## Még pontosítható apróságok

- Maradjon-e végleg a 10 javasolt lovasnév.
- Legyen-e összegző megerősítő képernyő a lovas -> ló választás után.
- Akadályfigyelmeztetés UI ikon, pályán látható jel, vagy mindkettő legyen.
- Legyen-e külön vizuális ikon/effekt boost power-up felvételkor.
- A háttérzene csak verseny közben szóljon-e, vagy menüben/istállóban is.
