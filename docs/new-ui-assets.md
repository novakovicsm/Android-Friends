# Új UI Asset-ek Lista

Ez a dokumentum az új UI textúrák és asset-ek listáját tartalmazza, amelyek szükségesek a modernizált skin megvalósításához.

## Szükséges Textúrák (skin.atlas)

### Gombok (Button Textures)
- **button-primary-up**: Kerekített kék gomb normál állapot (64x32 pixel)
- **button-primary-down**: Kerekített sötétkék gomb lenyomott állapot
- **button-secondary-up**: Kerekített zöld gomb normál állapot
- **button-secondary-down**: Kerekített sötétzöld gomb lenyomott állapot
- **button-up**: Általános szürke gomb (hátrafelé kompatibilitás)
- **button-down**: Általános sötétszürke gomb

### Panelek és Háttér
- **panel**: Kerekített sarok panel háttér (64x64 pixel, 9-patch)
- **panel-dark**: Sötét mód panel háttér
- **window-bg**: Ablak háttér átlátszó sarkokkal

### Progress Bar és Slider
- **progress-bg**: Progress bar háttér (200x20 pixel)
- **progress-knob**: Progress bar előrehaladás (szín: zöld)
- **slider-bg**: Slider sáv (200x10 pixel)
- **slider-knob**: Slider gomb (20x20 pixel, kerek)

### Checkbox és Kapcsolók
- **checkbox-off**: Üres checkbox (20x20 pixel)
- **checkbox-on**: Kitöltött checkbox (20x20 pixel, pipával)

### Ikonok (Icons)
- **icon-play**: Játszás ikon (32x32 pixel)
- **icon-pause**: Szünet ikon
- **icon-settings**: Beállítások ikon
- **icon-home**: Főmenü ikon
- **icon-replay**: Újrajátszás ikon
- **icon-sound-on**: Hang bekapcsolva
- **icon-sound-off**: Hang kikapcsolva
- **icon-vibration-on**: Rezgés bekapcsolva
- **icon-vibration-off**: Rezgés kikapcsolva

### UI Elem Ikonok
- **icon-heart**: Élet ikon (16x16 pixel)
- **icon-coin**: Coin ikon
- **icon-star**: Csillag/bónusz ikon
- **icon-powerup-speed**: Gyorsítás power-up ikon
- **icon-powerup-shield**: Pajzs power-up ikon
- **icon-powerup-lightning**: Villám power-up ikon
- **icon-powerup-stamina**: Kitartás power-up ikon

## Textúra Specifikációk

### Méretek és Formátum
- **Felbontás**: 32x32 vagy többszörösei (64x64, 128x128)
- **Formátum**: RGBA8888 (átlátszóság miatt)
- **Szűrő**: Nearest (pixel art konzisztencia)
- **9-Patch**: Panelekhez és nyújtáshoz

### Stílus Irányelvek
- **Kerekített sarkok**: 4-8 pixel sugár
- **Árnyékok**: Finom drop shadow effektek
- **Színek**: Színpaletta alapján (lásd color-palette.md)
- **Kontraszt**: Legalább 4.5:1 arány

## Asset Létrehozás Lépései

1. **Grafikai Szoftver**: GIMP vagy Photoshop használata
2. **Template**: Létrehozni egy 256x256 pixel atlas template-et
3. **Rajzolás**: Minden textúra megrajzolása a színpaletta szerint
4. **Optimalizálás**: PNG export, alpha channel megtartásával
5. **Atlas Generálás**: TexturePacker vagy hasonló eszköz használata

## Példa Layout (skin.atlas)

```
ui-new.png
size: 256,256
format: RGBA8888
filter: Nearest,Nearest
repeat: none

button-primary-up
  rotate: false
  xy: 0, 0
  size: 64, 32
  orig: 64, 32
  offset: 0, 0
  index: -1

button-primary-down
  rotate: false
  xy: 64, 0
  size: 64, 32
  orig: 64, 32
  offset: 0, 0
  index: -1

panel
  rotate: false
  xy: 0, 32
  size: 64, 64
  orig: 64, 64
  offset: 0, 0
  index: -1

... (további textúrák)
```

## Tesztelés és Validáció

- **LibGDX Skin Editor**: Használat a skin teszteléséhez
- **Képernyőképek**: Különböző eszközökön tesztelés
- **Kontraszt Ellenőrzés**: Színpaletta compliance
- **Performance**: Textúra méret és betöltési idő ellenőrzése

## Következő Lépések

- Asset-ek létrehozása grafikus designer-rel
- Skin frissítés új textúrákkal
- UI kód módosítása új stílusok használatára
- Tesztelés és finomhangolás

Ez a lista biztosítja az összes szükséges vizuális elemet a modern UI megvalósításához.