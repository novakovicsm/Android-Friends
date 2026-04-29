from PIL import Image, ImageDraw
import os

ui_dir = os.path.join(os.getcwd(), 'assets', 'ui')
os.makedirs(ui_dir, exist_ok=True)

colors = {
    'primary': (74, 144, 226, 255),
    'primary_dark': (53, 122, 221, 255),
    'secondary': (126, 211, 33, 255),
    'secondary_dark': (91, 160, 0, 255),
    'surface': (255, 255, 255, 255),
    'panel': (245, 245, 245, 255),
    'panel_dark': (45, 45, 45, 255),
    'text': (74, 74, 74, 255),
    'text_light': (255, 255, 255, 255),
    'accent': (245, 166, 35, 255),
    'warning': (248, 231, 28, 255),
    'error': (208, 2, 27, 255),
    'transparent': (0, 0, 0, 0),
}


def save(img, name):
    path = os.path.join(ui_dir, name)
    img.save(path)
    print('saved', name)


def rounded_rect(draw, xy, radius, fill, outline=None, width=1):
    draw.rounded_rectangle(xy, radius=radius, fill=fill, outline=outline, width=width)

# Buttons
buttons = [
    ('button-primary-up.png', colors['primary'], colors['primary_dark']),
    ('button-primary-down.png', colors['primary_dark'], colors['primary_dark']),
    ('button-secondary-up.png', colors['secondary'], colors['secondary_dark']),
    ('button-secondary-down.png', colors['secondary_dark'], colors['secondary_dark']),
    ('button-up.png', (200, 200, 200, 255), (170, 170, 170, 255)),
    ('button-down.png', (170, 170, 170, 255), (140, 140, 140, 255)),
]
for name, fill, outline in buttons:
    img = Image.new('RGBA', (64, 32), colors['transparent'])
    draw = ImageDraw.Draw(img)
    rounded_rect(draw, (1, 1, 62, 30), radius=12, fill=fill, outline=outline, width=2)
    save(img, name)

# Panels
img = Image.new('RGBA', (64, 64), colors['transparent'])
draw = ImageDraw.Draw(img)
rounded_rect(draw, (1, 1, 62, 62), radius=14, fill=colors['panel'], outline=(220, 220, 220, 255), width=2)
save(img, 'panel.png')

img = Image.new('RGBA', (64, 64), colors['transparent'])
draw = ImageDraw.Draw(img)
rounded_rect(draw, (1, 1, 62, 62), radius=14, fill=colors['panel_dark'], outline=(80, 80, 80, 255), width=2)
save(img, 'panel-dark.png')

img = Image.new('RGBA', (128, 96), colors['transparent'])
draw = ImageDraw.Draw(img)
rounded_rect(draw, (1, 1, 126, 94), radius=18, fill=(255, 255, 255, 230), outline=(220, 220, 220, 255), width=2)
save(img, 'window-bg.png')

# Progress and slider
img = Image.new('RGBA', (200, 20), colors['transparent'])
draw = ImageDraw.Draw(img)
rounded_rect(draw, (0, 0, 200, 20), radius=10, fill=(230, 230, 230, 255), outline=None)
save(img, 'progress-bg.png')

img = Image.new('RGBA', (200, 20), colors['transparent'])
draw = ImageDraw.Draw(img)
rounded_rect(draw, (0, 0, 80, 20), radius=10, fill=colors['secondary'], outline=None)
save(img, 'progress-knob.png')

img = Image.new('RGBA', (200, 10), colors['transparent'])
draw = ImageDraw.Draw(img)
rounded_rect(draw, (0, 0, 200, 10), radius=5, fill=(210, 210, 210, 255), outline=None)
save(img, 'slider-bg.png')

img = Image.new('RGBA', (20, 20), colors['transparent'])
draw = ImageDraw.Draw(img)
rounded_rect(draw, (0, 0, 20, 20), radius=10, fill=colors['primary'], outline=(80, 80, 80, 255), width=2)
save(img, 'slider-knob.png')

# Checkbox
img = Image.new('RGBA', (20, 20), colors['transparent'])
draw = ImageDraw.Draw(img)
rounded_rect(draw, (0, 0, 20, 20), radius=4, fill=(255, 255, 255, 255), outline=(160, 160, 160, 255), width=2)
save(img, 'checkbox-off.png')

img = Image.new('RGBA', (20, 20), colors['transparent'])
draw = ImageDraw.Draw(img)
rounded_rect(draw, (0, 0, 20, 20), radius=4, fill=colors['primary'], outline=(53, 122, 221, 255), width=2)
draw.line((5, 11, 9, 16), fill=colors['text_light'], width=3)
draw.line((9, 16, 16, 5), fill=colors['text_light'], width=3)
save(img, 'checkbox-on.png')

# Icons helper
def icon(name, size, draw_func):
    img = Image.new('RGBA', (size, size), colors['transparent'])
    draw = ImageDraw.Draw(img)
    draw_func(draw, size)
    save(img, name)

icon('icon-play.png', 32, lambda d,s: d.polygon([(10, 6), (10, 26), (24, 16)], fill=colors['primary']))
icon('icon-pause.png', 32, lambda d,s: [d.rectangle((10, 6, 14, 26), fill=colors['primary']), d.rectangle((18, 6, 22, 26), fill=colors['primary'])])
icon('icon-settings.png', 32, lambda d,s: [d.ellipse((6, 6, 26, 26), outline=colors['primary'], width=3), d.line((16, 4, 16, 12), fill=colors['primary'], width=3), d.line((16, 20, 16, 28), fill=colors['primary'], width=3), d.line((4, 16, 12, 16), fill=colors['primary'], width=3), d.line((20, 16, 28, 16), fill=colors['primary'], width=3)])
icon('icon-home.png', 32, lambda d,s: d.polygon([(6, 16), (16, 6), (26, 16), (26, 26), (18, 26), (18, 18), (14, 18), (14, 26), (6, 26)], fill=colors['primary']))
icon('icon-replay.png', 32, lambda d,s: [d.arc((6, 6, 26, 26), start=30, end=300, fill=colors['primary'], width=4), d.polygon([(18, 6), (24, 10), (20, 14)], fill=colors['primary'])])
icon('icon-sound-on.png', 32, lambda d,s: [d.polygon([(8, 12), (14, 12), (18, 8), (18, 24), (14, 20), (8, 20)], fill=colors['primary']), d.arc((16, 10, 26, 22), start=-45, end=45, fill=colors['primary'], width=3), d.arc((18, 8, 28, 24), start=-45, end=45, fill=colors['primary'], width=2)])
icon('icon-sound-off.png', 32, lambda d,s: [d.polygon([(8, 12), (14, 12), (18, 8), (18, 24), (14, 20), (8, 20)], fill=colors['primary']), d.line((20, 10, 28, 22), fill=colors['error'], width=4), d.line((28, 10, 20, 22), fill=colors['error'], width=4)])
icon('icon-vibration-on.png', 32, lambda d,s: [d.rectangle((10, 6, 12, 26), fill=colors['primary']), d.rectangle((14, 8, 18, 24), fill=colors['primary']), d.rectangle((20, 6, 22, 26), fill=colors['primary'])])
icon('icon-vibration-off.png', 32, lambda d,s: [d.rectangle((10, 6, 12, 26), fill=colors['primary']), d.rectangle((14, 8, 18, 24), fill=colors['primary']), d.rectangle((20, 6, 22, 26), fill=colors['primary']), d.line((6, 6, 26, 26), fill=colors['error'], width=4)])
icon('icon-heart.png', 16, lambda d,s: d.polygon([(8, 14), (2, 8), (2, 5), (5, 3), (8, 6), (11, 3), (14, 5), (14, 8)], fill=colors['error']))
icon('icon-coin.png', 16, lambda d,s: [d.ellipse((2, 2, 14, 14), fill=colors['warning']), d.ellipse((5, 5, 11, 11), fill=(255, 255, 255, 180))])
icon('icon-star.png', 16, lambda d,s: d.polygon([(8, 2), (10, 6), (14, 6), (11, 9), (12, 14), (8, 11), (4, 14), (5, 9), (2, 6), (6, 6)], fill=colors['accent']))
icon('icon-powerup-speed.png', 16, lambda d,s: d.polygon([(4, 4), (12, 4), (8, 12)], fill=colors['secondary']))
icon('icon-powerup-shield.png', 16, lambda d,s: d.polygon([(8, 0), (14, 4), (14, 10), (8, 14), (2, 10), (2, 4)], fill=colors['secondary_dark']))
icon('icon-powerup-lightning.png', 16, lambda d,s: d.polygon([(6, 0), (10, 8), (8, 8), (12, 16), (6, 10), (8, 10)], fill=colors['accent']))
icon('icon-powerup-stamina.png', 16, lambda d,s: [d.ellipse((4, 1, 12, 9), fill=colors['primary']), d.polygon([(7, 6), (9, 9), (5, 9)], fill=colors['surface'])])
