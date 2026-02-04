# Asset pipeline: Aseprite → TexturePacker

Ez a dokumentum leírja a javasolt workflow-t a pixel art assetek exportálására és atlas készítésre.

## 1. Aseprite export

**Cél:** egységes PNG-k, átlátszó háttérrel, fix pixelrácson.

- Canvas méret: egységes (pl. 64x64, 96x96 vagy 128x128) sprite típusonként.
- Pixel ratio: 1:1 (nincs scaling exportkor).
- Átlátszó háttér (RGBA).
- Export naming:
  - `horse_idle_01.png`, `horse_run_01.png`, `rider_idle_01.png`
  - UI elemek: `ui_button_primary.png`, `ui_panel.png`

**Aseprite export beállítások:**
- File → Export Sprite Sheet
- **Layout:** Rows
- **Merge Duplicates:** Off
- **Trim:** Off (sprite méret fixen marad)
- **Extrude:** 2 px (ha később atlas-t használsz, segít a bleeding ellen)
- **Output:** PNG

## 2. Mappastruktúra

Ajánlott struktúra (már létrehozva):

- assets/sprites/  (Aseprite PNG-k)
- assets/ui/       (UI PNG-k)
- assets/maps/     (Tiled TMX + tileset)
- assets/sfx/      (hangok)
- assets/atlas/    (TexturePacker output)
- assets/fonts/    (bitmap fontok)
- assets/data/     (JSON konfigurációk)

## 3. TexturePacker atlas

**Cél:** egységes atlas a libGDX-hez.

Ajánlott beállítások:
- **Algorithm:** MaxRects
- **Max Size:** 2048x2048
- **Padding:** 2 px
- **Extrude:** 2 px
- **Duplicate Padding:** On
- **Filter:** Nearest
- **Format:** RGBA8888
- **Output:** `assets/atlas/game.atlas` + `assets/atlas/game.png`

**Input:** `assets/sprites/` és `assets/ui/`

## 4. libGDX használat (példa)

- `TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("atlas/game.atlas"));`
- `TextureRegion horseIdle = atlas.findRegion("horse_idle_01");`

## 5. Frissítési lépések

1. Aseprite-ben export PNG-ket az `assets/sprites/` mappába.
2. Futtasd a TexturePacker exportot az `assets/atlas/` mappába.
3. Ellenőrizd a `game.atlas` és `game.png` fájlokat.
4. Indítsd újra a játékot (hot reload nincs).

## 6. Ajánlott naming convention

- Prefix a típus szerint: `horse_`, `rider_`, `pet_`, `ui_`, `fx_`
- Animáció frame: `_01`, `_02`, `_03`
- Variáns: `_red`, `_blue`, `_night`

---

Ha szeretnél, hozzáadok egy TexturePacker preset fájlt is a repohoz.