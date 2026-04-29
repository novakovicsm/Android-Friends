package com.yourstudio.horse.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.yourstudio.horse.HorseGame;
import com.yourstudio.horse.ui.ScreenNavigator;

public class MainMenuScreen extends ScreenAdapter {
    private final HorseGame game;
    private Stage stage;
    private Sound clickSound;
    private Music menuMusic;

    public MainMenuScreen(HorseGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Skin skin = game.getSkin();

        // Get UI styles from the programmed skin
        Label.LabelStyle titleStyle = skin.get("title", Label.LabelStyle.class);
        Label.LabelStyle bodyStyle = skin.get("default", Label.LabelStyle.class);
        TextButton.TextButtonStyle buttonStyle = skin.get("primary", TextButton.TextButtonStyle.class);

        // Create UI elements
        Label title = new Label("Android Friends", titleStyle);
        Label subtitle = new Label("Lovas Kaland", skin.get("secondary", Label.LabelStyle.class));
        Label description = new Label("Válaszd ki a lovadat és indulj versenyre barátaiddal!", bodyStyle);
        description.setWrap(true);
        description.setAlignment(Align.center);

        TextButton startButton = new TextButton("Játék Indítása", buttonStyle);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (clickSound != null) {
                    clickSound.play(0.6f);
                }
                ScreenNavigator.toCharacterSelect(game, null);
            }
        });

        TextButton settingsButton = new TextButton("Beállítások", skin.get("secondary", TextButton.TextButtonStyle.class));
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Navigate to settings screen
            }
        });

        // Layout with Table
        Table layout = new Table();
        layout.setFillParent(true);
        layout.pad(32f);

        // Logo section
        Table logoTable = new Table();
        logoTable.add(title).padBottom(8f).row();
        logoTable.add(subtitle);

        // Menu section
        Table menuTable = new Table();
        menuTable.add(description).width(400f).padBottom(24f).row();
        menuTable.add(startButton).width(280f).height(80f).padBottom(16f).row();
        menuTable.add(settingsButton).width(280f).height(60f);

        // Main layout
        layout.add(logoTable).padBottom(40f).row();
        layout.add(menuTable);

        stage.addActor(layout);
        Gdx.input.setInputProcessor(stage);

        // Load sounds
        clickSound = game.getAssets().get("sfx/click.wav", Sound.class);
        menuMusic = game.getAssets().get("sfx/menu_music.wav", Music.class);
        if (menuMusic != null) {
            menuMusic.setLooping(true);
            menuMusic.setVolume(0.5f);
            menuMusic.play();
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.9f, 0.95f, 1f, 1f); // Light background
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
        if (menuMusic != null) {
            menuMusic.stop();
        }
    }
}
