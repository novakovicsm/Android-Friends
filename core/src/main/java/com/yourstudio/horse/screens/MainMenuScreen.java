package com.yourstudio.horse.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.yourstudio.horse.HorseGame;
import com.yourstudio.horse.ui.UiFactory;
import com.yourstudio.horse.ui.ScreenNavigator;

public class MainMenuScreen extends ScreenAdapter {
    private final HorseGame game;
    private Stage stage;
    private BitmapFont titleFont;
    private BitmapFont buttonFont;
    private Texture buttonUp;
    private Texture buttonDown;
    private Texture buttonOver;
    private Texture background;
    private Texture logoPanel;
    private Sound clickSound;
    private Music menuMusic;

    public MainMenuScreen(HorseGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        titleFont = new BitmapFont();
        titleFont.getData().setScale(2.4f);
        buttonFont = new BitmapFont();
        buttonFont.getData().setScale(1.2f);

        buttonUp = createPanelTexture(new Color(0.29f, 0.6f, 0.85f, 1f), new Color(0.1f, 0.2f, 0.3f, 1f), 280, 96);
        buttonDown = createPanelTexture(new Color(0.22f, 0.5f, 0.76f, 1f), new Color(0.08f, 0.16f, 0.24f, 1f), 280, 96);
        buttonOver = createPanelTexture(new Color(0.38f, 0.7f, 0.95f, 1f), new Color(0.12f, 0.24f, 0.36f, 1f), 280, 96);
        background = createPixelBackground(320, 180);
        logoPanel = createPanelTexture(new Color(0.95f, 0.89f, 0.65f, 1f), new Color(0.4f, 0.25f, 0.12f, 1f), 520, 140);
        clickSound = game.getAssets().get("sfx/click.wav", Sound.class);
        menuMusic = game.getAssets().get("sfx/menu_music.wav", Music.class);
        menuMusic.setLooping(true);
        menuMusic.setVolume(0.5f);
        menuMusic.play();

        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, new Color(0.28f, 0.16f, 0.08f, 1f));
        Label.LabelStyle subtitleStyle = new Label.LabelStyle(buttonFont, new Color(0.28f, 0.16f, 0.08f, 1f));
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = toDrawable(buttonUp);
        buttonStyle.down = toDrawable(buttonDown);
        buttonStyle.over = toDrawable(buttonOver);
        buttonStyle.font = buttonFont;
        buttonStyle.fontColor = Color.WHITE;

        Label title = UiFactory.label("Versenylovak", titleStyle);
        Label subtitle = UiFactory.label("Pixel lovas kalandok", subtitleStyle);
        TextButton startButton = UiFactory.button("Indítás", buttonStyle, () -> {
            if (clickSound != null) {
                clickSound.play(0.6f);
            }
            ScreenNavigator.toCharacterSelect(game, null);
        });

        Table layout = new Table();
        layout.setFillParent(true);
        layout.pad(40f);
        Table logoTable = new Table();
        logoTable.setBackground(toDrawable(logoPanel));
        logoTable.pad(18f, 28f, 18f, 28f);
        logoTable.add(title).padBottom(6f);
        logoTable.row();
        logoTable.add(subtitle);

        layout.add(logoTable).padBottom(50f);
        layout.row();
        layout.add(startButton).width(280f).height(96f);

        stage.addActor(layout);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        stage.getBatch().end();
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
        if (titleFont != null) {
            titleFont.dispose();
        }
        if (buttonFont != null) {
            buttonFont.dispose();
        }
        if (buttonUp != null) {
            buttonUp.dispose();
        }
        if (buttonDown != null) {
            buttonDown.dispose();
        }
        if (buttonOver != null) {
            buttonOver.dispose();
        }
        if (background != null) {
            background.dispose();
        }
        if (logoPanel != null) {
            logoPanel.dispose();
        }
        if (menuMusic != null) {
            menuMusic.stop();
        }
    }

    private Texture createPixelBackground(int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        Color skyTop = new Color(0.38f, 0.7f, 0.9f, 1f);
        Color skyBottom = new Color(0.66f, 0.86f, 0.96f, 1f);
        for (int y = 0; y < height; y++) {
            float t = y / (float) (height - 1);
            pixmap.setColor(
                skyBottom.r + (skyTop.r - skyBottom.r) * t,
                skyBottom.g + (skyTop.g - skyBottom.g) * t,
                skyBottom.b + (skyTop.b - skyBottom.b) * t,
                1f
            );
            pixmap.drawLine(0, y, width, y);
        }

        pixmap.setColor(0.2f, 0.55f, 0.25f, 1f);
        pixmap.fillRectangle(0, 0, width, height / 3);
        pixmap.setColor(0.15f, 0.42f, 0.2f, 1f);
        for (int x = 0; x < width; x += 16) {
            pixmap.fillRectangle(x, height / 3 - 10, 12, 10);
        }

        pixmap.setColor(0.12f, 0.33f, 0.18f, 1f);
        pixmap.fillCircle(width / 4, height / 3, 28);
        pixmap.fillCircle(width / 2, height / 3 + 6, 36);
        pixmap.fillCircle(width * 3 / 4, height / 3, 30);

        pixmap.setColor(1f, 1f, 1f, 1f);
        for (int x = 12; x < width; x += 40) {
            pixmap.fillRectangle(x, height - 30, 6, 4);
            pixmap.fillRectangle(x + 8, height - 34, 10, 4);
        }

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private Texture createPanelTexture(Color fillColor, Color borderColor, int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(fillColor);
        pixmap.fill();
        pixmap.setColor(borderColor);
        for (int i = 0; i < 4; i++) {
            pixmap.drawRectangle(i, i, width - (i * 2), height - (i * 2));
        }
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private Drawable toDrawable(Texture texture) {
        return new TextureRegionDrawable(texture);
    }
}
