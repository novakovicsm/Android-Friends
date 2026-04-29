# UI Wireframe-ok

Ez a dokumentum az új, modernizált UI wireframe-eket tartalmazza szöveges leírásban. A wireframe-ek a trendkutatás alapján készültek: minimalista design, nagy érintési területek, kerekített elemek, animált átmenetek.

## Általános UI Irányelvek

- **Képernyő arány**: 16:9 (mobil orientáció)
- **Margók**: 16dp minden oldalon
- **Betűtípus**: ArchitectsDaughter (címekhez), ui.ttf (szövegekhez)
- **Színek**: Vibráns paletta (lásd színpaletta dokumentum)
- **Interakciók**: Nagy gombok (min. 48dp), érintésre visszajelzés (árnyék változás)
- **Animációk**: Tween átmenetek képernyők között

## 1. Főmenü Wireframe

```
+-----------------------------+
|        [LOGO]               |
|   Android Friends           |
+-----------------------------+
|                             |
|   [JÁTÉK INDÍTÁSA]          |
|   (nagy, kerekített gomb)   |
|                             |
|   [BEÁLLÍTÁSOK]             |
|   (közepes gomb)            |
|                             |
|   [KILÉPÉS]                 |
|   (kis gomb)                |
+-----------------------------+
```

**Elemek**:
- Logo: Középen, nagy betűkkel
- Gombok: Vertikális elrendezés, egyenlő távolságokkal
- Háttér: Egyszerű gradiens vagy textúra
- Animáció: Gombok hover effekttel

## 2. Játék Képernyő Wireframe

```
+-----------------------------+
| [PAUSE] [ÉLET: ♥♥♥] [PONT: 0] |
+-----------------------------+
|                             |
|        [JÁTÉK TERÜLET]      |
|     (karakter, pálya)       |
|                             |
+-----------------------------+
| [POWER-UP 1] [POWER-UP 2]   |
| [POWER-UP 3] [POWER-UP 4]   |
+-----------------------------+
```

**Elemek**:
- Felső sáv: Pause gomb, élet ikonok, pontszám
- Középső terület: Játék canvas (karakter animációval)
- Alsó sáv: Power-up gombok (nagy érintési terület)
- Vizuális visszajelzések: Particle effektek gyűjtéskor

## 3. Beállítások Képernyő Wireframe

```
+-----------------------------+
|      ← [VISSZA]             |
+-----------------------------+
| HANG: [---○------] 80%     |
| ZENE: [---○------] 60%     |
| REZGÉS: [✓]                |
| SÖTÉT MÓD: [✓]             |
| NYELV: [MAGYAR ▼]          |
+-----------------------------+
|   [MENTÉS]                  |
+-----------------------------+
```

**Elemek**:
- Vissza gomb: Bal felső sarokban
- Slider-ek: Hangerőkhöz
- Checkbox-ok: Bináris opciókhoz
- Dropdown: Nyelv kiválasztáshoz
- Mentés gomb: Alul középen

## 4. Végeredmény Képernyő Wireframe

```
+-----------------------------+
|        GYŐZELEM!            |
|   (nagy szöveg, animált)    |
+-----------------------------+
| PONTSZÁM: 1250              |
| IDŐ: 02:34                  |
| LEGJOBB: 1500               |
+-----------------------------+
|   [ÚJRAJÁTSZÁS]             |
|   (nagy gomb)               |
|                             |
|   [MENÜ]                    |
|   (közepes gomb)            |
+-----------------------------+
```

**Elemek**:
- Cím: Animált szöveg (pl. confetti effekt)
- Statisztikák: Pontszám, idő, rekord
- Gombok: Újrajátszás kiemelve
- Háttér: Győzelem effektekkel

## 5. Betöltőképernyő Wireframe

```
+-----------------------------+
|                             |
|        [LOGO]               |
|                             |
|   [PROGRESS BAR]            |
|   ████████░░░░░░ 70%        |
|                             |
|   Betöltés...               |
+-----------------------------+
```

**Elemek**:
- Logo: Középen
- Progress bar: Animált töltés
- Szöveg: Betöltési állapot
- Háttér: Egyszerű vagy animált

## Technikai Megvalósítás Javaslatok

- **LibGDX Scene2D**: Table layout használata reszponzív elrendezéshez
- **Skin frissítés**: Új atlas textúrák (kerekített gombok, ikonok)
- **Animációk**: Universal Tween Engine tween-ekkel
- **Reszponzivitás**: Viewport és scaling használata különböző képernyőkön

## Következő Lépések

- Színpaletta alkalmazása a wireframe-ekre
- Mockup képek készítése (grafikus szoftverrel)
- Prototípus implementálása teszteléshez

Ezek a wireframe-ek alapot adnak a UI redesign-hoz, biztosítva a modern megjelenést és jobb használhatóságot.