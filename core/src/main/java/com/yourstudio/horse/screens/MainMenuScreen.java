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
import com.yourstudio.horse.model.MvpProgress;
import com.yourstudio.horse.model.MvpProgressStore;
import com.yourstudio.horse.ui.ScreenNavigator;

public class MainMenuScreen extends ScreenAdapter {
    private final HorseGame game;
    private Stage stage;
    private Sound clickSound;
    private Music menuMusic;
    private MvpProgressStore progressStore;
    private MvpProgress progress;

    public MainMenuScreen(HorseGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Skin skin = game.getSkin();
        progressStore = new MvpProgressStore(Gdx.app.getPreferences(MvpProgressStore.PREFS_NAME));
        progress = progressStore.load();

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
        Label progressLabel = new Label(progressSummaryText(), bodyStyle);
        progressLabel.setWrap(true);
        progressLabel.setAlignment(Align.center);

        TextButton startButton = new TextButton("Játék Indítása", buttonStyle);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!progress.muted && clickSound != null) {
                    clickSound.play(0.6f);
                }
                ScreenNavigator.toCharacterSelect(game, null);
            }
        });

        TextButton shopButton = new TextButton("Ist\u00E1ll\u00F3", buttonStyle);
        shopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!progress.muted && clickSound != null) {
                    clickSound.play(0.6f);
                }
                ScreenNavigator.toShop(game);
            }
        });

        TextButton muteButton = new TextButton(muteButtonText(), skin.get("secondary", TextButton.TextButtonStyle.class));
        muteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                progress.muted = !progress.muted;
                progressStore.save(progress);
                muteButton.setText(muteButtonText());
                applyMenuMusicState();
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
        menuTable.add(progressLabel).width(400f).padBottom(20f).row();
        menuTable.add(startButton).width(280f).height(80f).padBottom(16f).row();
        menuTable.add(shopButton).width(280f).height(70f).padBottom(16f).row();
        menuTable.add(muteButton).width(280f).height(60f);

        // Main layout
        layout.add(logoTable).padBottom(40f).row();
        layout.add(menuTable);

        stage.addActor(layout);
        Gdx.input.setInputProcessor(stage);

        // Load sounds
        clickSound = game.getAssets().get("sfx/click.wav", Sound.class);
        menuMusic = game.getAssets().get("sfx/menu_music.wav", Music.class);
        applyMenuMusicState();
    }

    private String muteButtonText() {
        return progress != null && progress.muted ? "Hang: kikapcsolva" : "Hang: bekapcsolva";
    }

    private String progressSummaryText() {
        if (progress == null) {
            return "Aranypatk\u00F3: 0 | Szint: 1 | Kutya: 1";
        }
        return "Aranypatk\u00F3: " + progress.horseshoes
            + " | Szint: " + progress.playerLevel
            + " | Kutya: " + progress.petLevel;
    }

    private void applyMenuMusicState() {
        if (menuMusic == null || progress == null) {
            return;
        }
        menuMusic.setLooping(true);
        menuMusic.setVolume(progress.muted ? 0f : 0.5f);
        if (progress.muted) {
            menuMusic.pause();
        } else {
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
