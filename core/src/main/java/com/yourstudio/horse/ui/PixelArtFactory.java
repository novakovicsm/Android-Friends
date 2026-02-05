package com.yourstudio.horse.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public final class PixelArtFactory {
    private PixelArtFactory() {
    }

    public static Texture createSolidTexture(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        return texture;
    }

    public static Texture createPixelBackground(int width, int height, Color top, Color bottom,
                                                Color ground, Color groundShade) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        for (int y = 0; y < height; y++) {
            float t = y / (float) Math.max(1, height - 1);
            pixmap.setColor(
                bottom.r + (top.r - bottom.r) * t,
                bottom.g + (top.g - bottom.g) * t,
                bottom.b + (top.b - bottom.b) * t,
                1f
            );
            pixmap.drawLine(0, y, width, y);
        }
        int groundHeight = height / 3;
        pixmap.setColor(ground);
        pixmap.fillRectangle(0, 0, width, groundHeight);
        pixmap.setColor(groundShade);
        for (int x = 0; x < width; x += 14) {
            pixmap.fillRectangle(x, groundHeight - 8, 10, 8);
        }
        addDither(pixmap, new Color(0f, 0f, 0f, 0.08f));
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        return texture;
    }

    public static Texture createPixelPanel(int width, int height, Color fill, Color shade,
                                           Color borderLight, Color borderDark) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(fill);
        pixmap.fill();
        addDither(pixmap, shade);
        drawBevel(pixmap, borderLight, borderDark);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        return texture;
    }

    public static Texture createPixelButton(int width, int height, Color fill, Color shade,
                                            Color borderLight, Color borderDark, boolean pressed) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(fill);
        pixmap.fill();
        addDither(pixmap, shade);
        if (pressed) {
            drawBevel(pixmap, borderDark, borderLight);
        } else {
            drawBevel(pixmap, borderLight, borderDark);
        }
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        return texture;
    }

    private static void addDither(Pixmap pixmap, Color color) {
        pixmap.setColor(color);
        int width = pixmap.getWidth();
        int height = pixmap.getHeight();
        for (int y = 1; y < height; y += 2) {
            int xOffset = (y % 4 == 0) ? 1 : 0;
            for (int x = xOffset; x < width; x += 2) {
                pixmap.drawPixel(x, y);
            }
        }
    }

    private static void drawBevel(Pixmap pixmap, Color light, Color dark) {
        int width = pixmap.getWidth();
        int height = pixmap.getHeight();
        pixmap.setColor(dark);
        pixmap.drawRectangle(0, 0, width, height);
        pixmap.setColor(light);
        pixmap.drawRectangle(1, 1, width - 2, height - 2);
        pixmap.setColor(dark);
        pixmap.drawLine(1, 1, width - 2, 1);
        pixmap.drawLine(1, 1, 1, height - 2);
        pixmap.setColor(light);
        pixmap.drawLine(1, height - 2, width - 2, height - 2);
        pixmap.drawLine(width - 2, 1, width - 2, height - 2);
    }
}
