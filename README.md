# Versenylovak libGDX projekt (VS Code)

Ez a projekt egy libGDX + VS Code alapú, Android tabletre optimalizált 2D játék kezdő skeletonja. A mappák és modulok a Gradle multi-module felépítést követik, így később könnyen bővíthető és karbantartható.

## Könyvtárstruktúra (jelenlegi)

```
Versenylovak/
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

- `core/`: Platformfüggetlen játéklogika, minden platform ezt használja.
- `desktop/`: Fejlesztői/teszt indító, gyors iterációhoz.
- `android/`: Android platformra forduló launcher.
- `assets/`: Minden platform által használt assetek.
- `docs/`: Dokumentáció, design, workflow.

## Build & Run

A projekt Gradle-t használ. Fordításhoz és futtatáshoz:

### Minden modul buildelése

```
./gradlew build
```

### Desktop verzió futtatása

```
./gradlew desktop:run
```

### Tisztítás és újra build

```
./gradlew clean build
```

## Javadoc generálása

Java dokumentáció generálásához:

```
./gradlew javadoc
```

Az eredmény a `core/build/docs/javadoc/`, `desktop/build/docs/javadoc/` stb. mappákban lesz.

## Dokumentáció

- Lásd a `docs/` mappát (design, asset workflow, todos).
- Lásd ezt a README-t a build és használat részleteihez.
- Headless Android UI smoke teszt: `./scripts/run_maestro.sh`.
- Windows emulátor esetén: `scripts\\run_maestro_windows.cmd`.
- A Maestro beállítása és a LibGDX canvas korlátozásai: `docs/ui-automation.md`.
- Headless Android UI smoke teszt: `./scripts/run_maestro.sh`.
- A Maestro beállítása és a LibGDX canvas korlátozásai: `docs/ui-automation.md`.

## License

Add meg a licencet itt.