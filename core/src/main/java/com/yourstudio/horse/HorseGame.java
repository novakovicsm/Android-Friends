package com.yourstudio.horse;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.yourstudio.horse.screens.MainMenuScreen;

public class HorseGame extends Game {
    private AssetManager assets;

    @Override
    public void create() {
        assets = new AssetManager();
        assets.load("sfx/click.wav", Sound.class);
        assets.load("sfx/powerup.wav", Sound.class);
        assets.load("sfx/win.wav", Sound.class);
        assets.load("sfx/menu_music.wav", Music.class);
        assets.load("sfx/race_music.wav", Music.class);
        assets.finishLoading();
        setScreen(new MainMenuScreen(this));
    }

    public AssetManager getAssets() {
        return assets;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (assets != null) {
            assets.dispose();
        }
    }
}
