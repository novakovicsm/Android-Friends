package com.yourstudio.horse.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;
import com.yourstudio.horse.HorseGame;

public class MainMenuScreen extends ScreenAdapter {
    private final HorseGame game;
    private SpriteBatch batch;
    private BitmapFont font;

    public MainMenuScreen(HorseGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.13f, 0.13f, 0.2f, 1f);
        batch.begin();
        font.draw(batch, "Android Friends", 48, 96);
        font.draw(batch, "Main menu placeholder", 48, 64);
        batch.end();
    }

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }
        if (font != null) {
            font.dispose();
        }
    }
}
