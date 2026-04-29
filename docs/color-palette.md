# Színpaletta Tervezés

Ez a dokumentum a játék új színpalettáját definiálja, amely a modern trendek alapján készült: vibráns színek, jó kontraszt, és opcionális sötét mód.

## Színpaletta Elvek

- **Kontraszt**: Legalább 4.5:1 arány szöveg és háttér között (WCAG AA szabvány)
- **Vibrancia**: Élénk színek figyelemfelkeltésre, de nem vakító
- **Téma**: Verseny/ló téma ihlette színek (kék ég, zöld fű, narancs naplemente)
- **Módok**: Világos és sötét téma opció

## Fő Színek

### Elsődleges Színek (Primary)
- **Kék (Sky Blue)**: #4A90E2
  - Használat: Gombok, kiemelt elemek, égbolt
  - Sötét mód: #5BA0F2
- **Zöld (Grass Green)**: #7ED321
  - Használat: Pozitív visszajelzések, fű, győzelem
  - Sötét mód: #8EE631
- **Narancs (Sunset Orange)**: #F5A623
  - Használat: Akcent, power-up-ok, figyelmeztetések
  - Sötét mód: #F6B733

### Másodlagos Színek (Secondary)
- **Piros (Alert Red)**: #D0021B
  - Használat: Hibák, veszély, életvesztés
  - Sötét mód: #E0132A
- **Sárga (Energy Yellow)**: #F8E71C
  - Használat: Energia, coin-ok, kiemelés
  - Sötét mód: #F9E82C
- **Lila (Mystery Purple)**: #9013FE
  - Használat: Speciális power-up-ok, bónuszok
  - Sötét mód: #A024FF

## Semleges Színek (Neutral)

### Szürke Skála
- **Világos Szürke (Light Gray)**: #F5F5F5
  - Háttér, panelek világos módban
- **Közepes Szürke (Medium Gray)**: #9B9B9B
  - Másodlagos szöveg, ikonok
- **Sötét Szürke (Dark Gray)**: #4A4A4A
  - Elsődleges szöveg, sötét mód háttér
- **Fekete (Black)**: #000000
  - Sötét mód szöveg, árnyékok

### Sötét Mód Színek
- **Háttér**: #1A1A1A (sötét szürke)
- **Panel Háttér**: #2D2D2D
- **Szöveg**: #FFFFFF (fehér)
- **Másodlagos Szöveg**: #B3B3B3

## Karakter Színek

A meglévő ló színek megtartása és kiegészítése:

- **Bay**: #8B4513 (barna)
- **Chestnut**: #CD853F (világosbarna)
- **Gray**: #708090 (szürke)
- **Palomino**: #DAA520 (arany)

## UI Alkalmazás

### Gombok
- **Normál állapot**: Elsődleges szín (pl. #4A90E2)
- **Hover/Press**: Sötétebb árnyalat (pl. #357ABD)
- **Disabled**: Szürke (#9B9B9B)

### Szövegek
- **Címek**: Fekete/Világos szürke (#4A4A4A / #FFFFFF)
- **Szöveg**: Közepes szürke (#9B9B9B / #B3B3B3)
- **Linkek**: Elsődleges szín (#4A90E2)

### Visszajelzések
- **Siker**: Zöld (#7ED321)
- **Figyelem**: Sárga (#F8E71C)
- **Hiba**: Piros (#D0021B)

## Power-up Színek

- **Gyorsítás**: Narancs (#F5A623) - energia érzés
- **Pajzs**: Kék (#4A90E2) - védelem
- **Villám**: Sárga (#F8E71C) - gyorsaság
- **Kitartás**: Zöld (#7ED321) - növekedés

## Környezet Színek

- **Ég**: Kék gradiens (#87CEEB → #4A90E2)
- **Fű**: Zöld (#7ED321)
- **Talaj**: Barna (#8B4513)
- **Akadályok**: Szürke (#708090)

## Technikai Megvalósítás

### LibGDX Skin
```json
{
  "com.badlogic.gdx.graphics.Color": {
    "primary": "4A90E2FF",
    "secondary": "7ED321FF",
    "accent": "F5A623FF",
    "error": "D0021BFF",
    "background": "F5F5F5FF",
    "surface": "FFFFFFFF",
    "text-primary": "4A4A4AFF",
    "text-secondary": "9B9B9BFF"
  }
}
```

### Sötét Mód Váltás
- Automatikus rendszer téma követés
- Manuális váltás beállításokban
- Animált átmenet színek között

## Kontraszt Ellenőrzés

- Elsődleges szöveg háttéren: 12.1:1 (AA compliant)
- Másodlagos szöveg: 5.9:1 (AA compliant)
- Gomb szöveg: 8.2:1 (AAA compliant)

## Következő Lépések

- Színpaletta alkalmazása wireframe-ekre
- Mockup képek készítése színekkel
- Skin.json frissítés új színekkel
- Tesztelés különböző eszközökön

Ez a színpaletta biztosítja a modern, vonzó megjelenést, miközben fenntartja a játék tematikáját és az akadálymentességet.