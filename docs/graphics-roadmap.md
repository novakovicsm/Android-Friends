# Grafikai Továbbfejlesztési Roadmap

## Bevezetés

Ez a roadmap célja a játék grafikai megjelenésének modernizálása, hogy javítsa a játékélményt. A fejlesztés fázisokra bontva történik, figyelembe véve a LibGDX keretrendszer képességeit és az Android platform követelményeit. Minden fázis tartalmaz konkrét feladatokat, prioritásokat és időbecsléseket.

## Fázis 1: Elemzés és Tervezés (1-2 hét)

### Célok:
- Az aktuális grafikai állapot felmérése
- Felhasználói visszajelzések gyűjtése
- Modern design trendek kutatása játékok területén

### Feladatok:
1. **Grafikai audit**: Az összes jelenlegi asset (sprite-ok, UI elemek, térképek) áttekintése
2. **Felhasználói visszajelzés**: Játékosoktól kérdezni a kinézettel kapcsolatos véleményeket
3. **Trendkutatás**: Modern mobiljátékok (pl. Among Us, Genshin Impact) vizuális elemzése
4. **Színpaletta tervezés**: Kontrasztos, játékhangulathoz illő színséma kidolgozása
5. **Wireframe-ok készítése**: Új UI layout-ok vázlatai

### Prioritás: Magas
### Időbecslés: 1-2 hét

## Fázis 2: UI Modernizálás (2-3 hét)

### Célok:
- Letisztult, modern felhasználói felület
- Jobb olvashatóság és használhatóság
- Reszponzív design különböző képernyőméretekhez

### Feladatok:
1. **Skin frissítés**: Scene2D skin.json és skin.atlas modernizálása
   - Kerekített gombok, modern ikonok
   - Árnyékok és áttűnések hozzáadása
2. **Betűtípusok**: Egységes, jól olvasható fontok kiválasztása és implementálása
3. **Menürendszer áttervezése**: Főmenü, beállítások, játék vége képernyők modernizálása
4. **Animált átmenetek**: Gombnyomások, menüváltások animálása
5. **Sötét mód támogatása**: Opcionális sötét téma implementálása

### Prioritás: Magas
### Időbecslés: 2-3 hét

## Fázis 3: Játék Grafika Fejlesztés (3-4 hét)

### Célok:
- Dinamikusabb játékvilág
- Jobb vizuális visszajelzések
- Immersive élmény növelése

### Feladatok:
1. **Karakter animációk**: Sprite sheet-ek frissítése, új animációs állapotok (futás, ugrás, stb.)
2. **Háttér effektek**: Parallax scrolling, dinamikus háttér elemek
3. **Power-up vizuális effektek**: Gyűjtéskor, aktiváláskor animált visszajelzések
4. **Térkép frissítések**: TMX fájlok modernizálása, új tile-ok hozzáadása
5. **Particle effektek**: LibGDX particle editor használata új effektekhez

### Prioritás: Közepes-Magas
### Időbecslés: 3-4 hét

## Fázis 4: Animációk és Effektek (2-3 hét)

### Célok:
- Folyamatos animációs élmény
- Hangulatos vizuális effektek
- Performance optimalizálás

### Feladatok:
1. **UI animációk**: Tween animációk hozzáadása (pl. Universal Tween Engine)
2. **Játékállapot visszajelzések**: Pontgyűjtés, életvesztés animált megjelenítése
3. **Hanghatások integrálása**: Vizuális effektek szinkronizálása hangokkal
4. **Betöltőképernyő**: Animált progress bar és loading screen
5. **Performance monitoring**: Frame rate és memória használat optimalizálása

### Prioritás: Közepes
### Időbecslés: 2-3 hét

## Fázis 5: Tesztelés és Optimalizálás (1-2 hét)

### Célok:
- Keresztplatform kompatibilitás biztosítása
- Performance problémák megoldása
- Felhasználói visszajelzés alapján finomhangolás

### Feladatok:
1. **Keresztplatform tesztelés**: Android különböző képernyőméreteken, desktop-on
2. **Performance benchmark**: GPU/CPU használat mérése különböző eszközökön
3. **Felhasználói tesztelés**: Béta verzió kiadása visszajelzés gyűjtésére
4. **Bug fixek**: Grafikai glitch-ek és animációs problémák javítása
5. **Dokumentáció frissítés**: Asset workflow és design guide-ok aktualizálása

### Prioritás: Magas
### Időbecslés: 1-2 hét

## Mérföldkövek és Kritériumok

### Sikerkritériumok:
- Legalább 20% javulás a felhasználói visszajelzésekben a kinézettel kapcsolatban
- Stabil 60 FPS minden támogatott eszközön
- Reszponzív UI minden képernyőméreten
- Modern, professzionális megjelenés

### Mérföldkövek:
- Fázis 1 vége: Design specifikáció kész
- Fázis 2 vége: Új UI implementálva
- Fázis 3 vége: Játék grafika frissítve
- Fázis 4 vége: Animációk és effektek hozzáadva
- Fázis 5 vége: Tesztelt és optimalizált végleges verzió

## Erőforrások és Függőségek

### Szükséges eszközök:
- GIMP vagy Photoshop grafikai tervezéshez
- TexturePacker vagy hasonló sprite sheet eszköz
- LibGDX Particle Editor
- Android Studio UI preview

### Csapat követelmények:
- Grafikus designer (külső vagy belső)
- Programozó tapasztalat LibGDX-szel
- QA tesztelő különböző eszközökön

## Kockázatok és Megoldások

### Potenciális kockázatok:
- Performance problémák új animációk miatt
- Asset méret növekedés (APK méret)
- Keresztplatform kompatibilitási problémák

### Megoldások:
- Korai performance tesztelés minden fázisban
- Asset optimalizálás (kompresszió, atlas-ok használata)
- Fokozatos implementáció és tesztelés

## Következtetés

Ez a roadmap biztosítja a strukturált, fázisokra bontott fejlesztést, amely minimalizálja a kockázatokat és maximalizálja a játékélmény javulását. Minden fázis végén mérföldkő értékelés történik a folytatás eldöntésére.