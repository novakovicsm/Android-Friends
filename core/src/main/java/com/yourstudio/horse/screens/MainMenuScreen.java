package com.yourstudio.horse.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
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
    private Texture menuPanel;
    private Sound clickSound;
    private Music menuMusic;

    public MainMenuScreen(HorseGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        float worldWidth = stage.getViewport().getWorldWidth();
        float worldHeight = stage.getViewport().getWorldHeight();
        float scale = Math.max(0.8f, Math.min(worldWidth / 1280f, worldHeight / 720f));
        int titleSize = Math.max(32, Math.round(54f * scale));
        int buttonSize = Math.max(18, Math.round(28f * scale));
        titleFont = createUIFont(titleSize, 3.1f * scale);
        buttonFont = createUIFont(buttonSize, 1.45f * scale);

        buttonUp = loadUiTexture("ui/button_primary.png");
        buttonDown = loadUiTexture("ui/button_primary_down.png");
        buttonOver = loadUiTexture("ui/button_primary_over.png");
        background = loadUiTexture("ui/bg_menu.png");
        logoPanel = loadUiTexture("ui/panel_logo.png");
        menuPanel = loadUiTexture("ui/panel_menu.png");
        clickSound = game.getAssets().get("sfx/click.wav", Sound.class);
        menuMusic = game.getAssets().get("sfx/menu_music.wav", Music.class);
        menuMusic.setLooping(true);
        menuMusic.setVolume(0.5f);
        menuMusic.play();

        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, new Color(0.18f, 0.12f, 0.1f, 1f));
        Label.LabelStyle subtitleStyle = new Label.LabelStyle(buttonFont, new Color(0.18f, 0.12f, 0.1f, 1f));
        Label.LabelStyle bodyStyle = new Label.LabelStyle(buttonFont, new Color(0.9f, 0.92f, 0.96f, 1f));
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = toDrawable(buttonUp);
        buttonStyle.down = toDrawable(buttonDown);
        buttonStyle.over = toDrawable(buttonOver);
        buttonStyle.font = buttonFont;
        buttonStyle.fontColor = Color.WHITE;

        Label title = UiFactory.label("Versenylovak", titleStyle);
        Label subtitle = UiFactory.label("Bar\u00E1ts\u00E1gos lovas kaland", subtitleStyle);
        Label description = UiFactory.label("V\u00E1laszd ki a lovadat, a lovast \u00E9s a kedvencet, majd indulhat a verseny.", bodyStyle);
        description.setWrap(true);
        description.setAlignment(Align.center);
        TextButton startButton = UiFactory.button("J\u00E1t\u00E9k ind\u00EDt\u00E1sa", buttonStyle, () -> {
            if (clickSound != null) {
                clickSound.play(0.6f);
            }
            ScreenNavigator.toCharacterSelect(game, null);
        });
        Label hint = UiFactory.label("Koppints az ind\u00EDt\u00E1shoz", bodyStyle);
        hint.setAlignment(Align.center);

        Table layout = new Table();
        layout.setFillParent(true);
        layout.pad(28f * scale);
        Table logoTable = new Table();
        logoTable.setBackground(toDrawable(logoPanel));
        logoTable.pad(18f * scale, 28f * scale, 18f * scale, 28f * scale);
        logoTable.add(title).padBottom(6f);
        logoTable.row();
        logoTable.add(subtitle);

        Table menuTable = new Table();
        menuTable.setBackground(toDrawable(menuPanel));
        menuTable.pad(20f * scale);
        menuTable.add(description).width(500f * scale).padBottom(18f * scale).row();
        menuTable.add(startButton).width(360f * scale).height(112f * scale).padBottom(12f * scale).row();
        menuTable.add(hint);

        layout.add(logoTable).width(560f * scale).height(150f * scale).padBottom(24f * scale);
        layout.row();
        layout.add(menuTable).width(600f * scale).height(320f * scale);

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
        if (menuPanel != null) {
            menuPanel.dispose();
        }
        if (menuMusic != null) {
            menuMusic.stop();
        }
    }

    private BitmapFont createUIFont(int size, float fallbackScale) {
        FileHandle fontFile = Gdx.files.internal("fonts/ArchitectsDaughter.ttf");
        if (fontFile.exists()) {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
            FreeTypeFontParameter parameter = new FreeTypeFontParameter();
            parameter.size = size;
            parameter.minFilter = TextureFilter.Linear;
            parameter.magFilter = TextureFilter.Linear;
            parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "\u00C1\u00C9\u00CD\u00D3\u00D6\u0150\u00DA\u00DC\u0170\u00E1\u00E9\u00ED\u00F3\u00F6\u0151\u00FA\u00FC\u0171";
            BitmapFont font = generator.generateFont(parameter);
            generator.dispose();
            return font;
        }
        BitmapFont font = new BitmapFont();
        font.getData().setScale(fallbackScale);
        return font;
    }

    private Drawable toDrawable(Texture texture) {
        return new TextureRegionDrawable(texture);
    }

    private Texture loadUiTexture(String path) {
        Texture texture = new Texture(path);
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        return texture;
    }
}
