package com.yourstudio.horse;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.yourstudio.horse.screens.MainMenuScreen;

public class HorseGame extends Game {
    private AssetManager assets;
    private Skin skin;

    @Override
    public void create() {
        assets = new AssetManager();
        assets.load("sfx/click.wav", Sound.class);
        assets.load("sfx/powerup.wav", Sound.class);
        assets.load("sfx/win.wav", Sound.class);
        assets.load("sfx/menu_music.wav", Music.class);
        assets.load("sfx/race_music.wav", Music.class);
        assets.finishLoading();

        // Load UI skin programmatically with generated texture assets
        skin = new Skin();

        BitmapFont uiFont = new BitmapFont();
        BitmapFont titleFont = new BitmapFont();
        skin.add("ui-font", uiFont);
        skin.add("title-font", titleFont);
        skin.add("default-font", uiFont);

        skin.add("button-primary-up", new TextureRegion(new Texture(Gdx.files.internal("ui/button-primary-up.png"))));
        skin.add("button-primary-down", new TextureRegion(new Texture(Gdx.files.internal("ui/button-primary-down.png"))));
        skin.add("button-secondary-up", new TextureRegion(new Texture(Gdx.files.internal("ui/button-secondary-up.png"))));
        skin.add("button-secondary-down", new TextureRegion(new Texture(Gdx.files.internal("ui/button-secondary-down.png"))));
        skin.add("button-up", new TextureRegion(new Texture(Gdx.files.internal("ui/button-up.png"))));
        skin.add("button-down", new TextureRegion(new Texture(Gdx.files.internal("ui/button-down.png"))));
        skin.add("panel", new TextureRegion(new Texture(Gdx.files.internal("ui/panel.png"))));
        skin.add("panel-dark", new TextureRegion(new Texture(Gdx.files.internal("ui/panel-dark.png"))));
        skin.add("window-bg", new TextureRegion(new Texture(Gdx.files.internal("ui/window-bg.png"))));
        skin.add("progress-bg", new TextureRegion(new Texture(Gdx.files.internal("ui/progress-bg.png"))));
        skin.add("progress-knob", new TextureRegion(new Texture(Gdx.files.internal("ui/progress-knob.png"))));
        skin.add("slider-bg", new TextureRegion(new Texture(Gdx.files.internal("ui/slider-bg.png"))));
        skin.add("slider-knob", new TextureRegion(new Texture(Gdx.files.internal("ui/slider-knob.png"))));
        skin.add("checkbox-off", new TextureRegion(new Texture(Gdx.files.internal("ui/checkbox-off.png"))));
        skin.add("checkbox-on", new TextureRegion(new Texture(Gdx.files.internal("ui/checkbox-on.png"))));
        skin.add("icon-play", new TextureRegion(new Texture(Gdx.files.internal("ui/icon-play.png"))));
        skin.add("icon-pause", new TextureRegion(new Texture(Gdx.files.internal("ui/icon-pause.png"))));
        skin.add("icon-settings", new TextureRegion(new Texture(Gdx.files.internal("ui/icon-settings.png"))));
        skin.add("icon-home", new TextureRegion(new Texture(Gdx.files.internal("ui/icon-home.png"))));
        skin.add("icon-replay", new TextureRegion(new Texture(Gdx.files.internal("ui/icon-replay.png"))));
        skin.add("icon-sound-on", new TextureRegion(new Texture(Gdx.files.internal("ui/icon-sound-on.png"))));
        skin.add("icon-sound-off", new TextureRegion(new Texture(Gdx.files.internal("ui/icon-sound-off.png"))));
        skin.add("icon-vibration-on", new TextureRegion(new Texture(Gdx.files.internal("ui/icon-vibration-on.png"))));
        skin.add("icon-vibration-off", new TextureRegion(new Texture(Gdx.files.internal("ui/icon-vibration-off.png"))));
        skin.add("icon-heart", new TextureRegion(new Texture(Gdx.files.internal("ui/icon-heart.png"))));
        skin.add("icon-coin", new TextureRegion(new Texture(Gdx.files.internal("ui/icon-coin.png"))));
        skin.add("icon-star", new TextureRegion(new Texture(Gdx.files.internal("ui/icon-star.png"))));
        skin.add("icon-powerup-speed", new TextureRegion(new Texture(Gdx.files.internal("ui/icon-powerup-speed.png"))));
        skin.add("icon-powerup-shield", new TextureRegion(new Texture(Gdx.files.internal("ui/icon-powerup-shield.png"))));
        skin.add("icon-powerup-lightning", new TextureRegion(new Texture(Gdx.files.internal("ui/icon-powerup-lightning.png"))));
        skin.add("icon-powerup-stamina", new TextureRegion(new Texture(Gdx.files.internal("ui/icon-powerup-stamina.png"))));

        Label.LabelStyle defaultLabel = new Label.LabelStyle(uiFont, Color.BLACK);
        Label.LabelStyle titleLabel = new Label.LabelStyle(titleFont, Color.BLACK);
        Label.LabelStyle secondaryLabel = new Label.LabelStyle(uiFont, Color.DARK_GRAY);
        skin.add("default", defaultLabel);
        skin.add("title", titleLabel);
        skin.add("secondary", secondaryLabel);

        TextButton.TextButtonStyle defaultButton = new TextButton.TextButtonStyle(
            skin.getDrawable("button-up"),
            skin.getDrawable("button-down"),
            null,
            uiFont);
        defaultButton.fontColor = Color.BLACK;
        defaultButton.downFontColor = Color.DARK_GRAY;
        skin.add("default", defaultButton);

        TextButton.TextButtonStyle primaryButton = new TextButton.TextButtonStyle(
            skin.getDrawable("button-primary-up"),
            skin.getDrawable("button-primary-down"),
            null,
            uiFont);
        primaryButton.fontColor = Color.WHITE;
        primaryButton.downFontColor = Color.LIGHT_GRAY;
        skin.add("primary", primaryButton);

        TextButton.TextButtonStyle secondaryButton = new TextButton.TextButtonStyle(
            skin.getDrawable("button-secondary-up"),
            skin.getDrawable("button-secondary-down"),
            null,
            uiFont);
        secondaryButton.fontColor = Color.WHITE;
        secondaryButton.downFontColor = Color.LIGHT_GRAY;
        skin.add("secondary", secondaryButton);

        CheckBox.CheckBoxStyle checkboxStyle = new CheckBox.CheckBoxStyle(
            skin.getDrawable("checkbox-off"),
            skin.getDrawable("checkbox-on"),
            uiFont,
            Color.BLACK);
        skin.add("checkbox", checkboxStyle);

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle(
            skin.getDrawable("slider-bg"),
            skin.getDrawable("slider-knob"));
        skin.add("slider", sliderStyle);

        ProgressBar.ProgressBarStyle progressStyle = new ProgressBar.ProgressBarStyle(
            skin.getDrawable("progress-bg"),
            skin.getDrawable("progress-knob"));
        skin.add("progress", progressStyle);

        setScreen(new MainMenuScreen(this));
    }

    public AssetManager getAssets() {
        return assets;
    }

    public Skin getSkin() {
        return skin;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (assets != null) {
            assets.dispose();
        }
        if (skin != null) {
            skin.dispose();
        }
    }
}
