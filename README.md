# Android-Friends libGDX projekt (VS Code)

Ez a projekt egy libGDX + VS Code alapú, Android tabletre optimalizált 2D játék kezdő skeletonja. A mappák és modulok a Gradle multi-module felépítést követik, így később könnyen bővíthető és karbantartható.

## Könyvtárstruktúra (jelenlegi)

```
Android-Friends/
├─ android/                      # Android-specifikus indítás és erőforrások
│  ├─ src/main/AndroidManifest.xml
│  ├─ src/main/java/...
│  └─ src/main/res/               # app ikonok, stílusok, stb.
├─ core/                         # Játéklogika (platformfüggetlen)
│  ├─ src/main/java/
│  │  ├─ com/yourstudio/horse/    # fő csomag
│  │  │  ├─ game/                 # játékmenet logika
│  │  │  ├─ screens/              # Screen-ek (menu, select, race, results)
│  │  │  ├─ ecs/                  # komponensek és rendszerek (ECS)
│  │  │  ├─ ui/                   # UI/HUD
│  │  │  ├─ assets/               # asset definíciók / loader-ek
│  │  │  └─ data/                 # JSON modell/konfiguráció
│  └─ src/main/resources/         # core res (ha szükséges)
├─ desktop/                      # Desktop indító (fejlesztéshez, debug)
│  ├─ src/main/java/...
│  └─ src/main/resources/...
├─ assets/                       # Megosztott játék assetek
│  ├─ atlas/                      # TexturePacker atlasok
│  ├─ fonts/                      # bitmap fontok
│  ├─ maps/                       # Tiled pályák
│  ├─ sfx/                        # hangok
│  ├─ sprites/                    # sprite sheet-ek
│  ├─ ui/                         # UI elemek
│  └─ data/                       # JSON konfigurációk (lovak, pályák, power-up)
├─ docs/                          # dokumentáció, design jegyzetek
│  └─ game-design.md
├─ build.gradle                   # root build
├─ settings.gradle                # modulok regisztrációja
└─ README.md
```

## Modulok szerepe

- **core**: A teljes játéklogika itt van (modellek, játékmenet, UI, ECS).
- **android**: Android launcher, manifest, és platform-specifikus integrációk.
- **desktop**: Gyors fejlesztés/tesztelés asztali környezetben.
- **assets**: Közös assetek (pixeles grafika, Tiled map, hangok).

## VS Code ajánlott beállítások

- Java + Gradle kiterjesztés
- `settings.gradle`-ben a modulok felvétele: `core`, `android`, `desktop`
- `./gradlew desktop:run` parancs a gyors lokális futtatáshoz (ha van Gradle wrapper)
- Ha nincs wrapper, használható a helyi `gradle` telepítés is: `gradle desktop:run`

## Következő lépések

1. Alap Game + MainMenu Screen már létrehozva a `core` modulban.
2. Következő Screen-ek létrehozása: CharacterSelect, Race, Results.
3. Asset pipeline kialakítása (Aseprite → TexturePacker → atlas).
4. Android launcherhez ikonok és splash később hozzáadható.
