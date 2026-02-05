package com.yourstudio.horse.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
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
        titleFont = createUIFont(54, 3.1f);
        buttonFont = createUIFont(28, 1.45f);

        buttonUp = createPanelTexture(new Color(0.18f, 0.48f, 0.82f, 1f), new Color(0.06f, 0.18f, 0.32f, 1f), 360, 112);
        buttonDown = createPanelTexture(new Color(0.14f, 0.4f, 0.72f, 1f), new Color(0.05f, 0.16f, 0.28f, 1f), 360, 112);
        buttonOver = createPanelTexture(new Color(0.26f, 0.6f, 0.92f, 1f), new Color(0.08f, 0.22f, 0.38f, 1f), 360, 112);
        background = createPixelBackground(360, 200);
        logoPanel = createPanelTexture(new Color(0.96f, 0.93f, 0.88f, 1f), new Color(0.26f, 0.2f, 0.18f, 1f), 560, 150);
        menuPanel = createPanelTexture(new Color(0.13f, 0.17f, 0.24f, 0.88f), new Color(0.55f, 0.55f, 0.62f, 1f), 600, 320);
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
        layout.pad(28f);
        Table logoTable = new Table();
        logoTable.setBackground(toDrawable(logoPanel));
        logoTable.pad(18f, 28f, 18f, 28f);
        logoTable.add(title).padBottom(6f);
        logoTable.row();
        logoTable.add(subtitle);

        Table menuTable = new Table();
        menuTable.setBackground(toDrawable(menuPanel));
        menuTable.pad(20f);
        menuTable.add(description).width(500f).padBottom(18f).row();
        menuTable.add(startButton).width(360f).height(112f).padBottom(12f).row();
        menuTable.add(hint);

        layout.add(logoTable).padBottom(24f);
        layout.row();
        layout.add(menuTable);

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

    private Texture createPixelBackground(int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        Color skyTop = new Color(0.18f, 0.34f, 0.6f, 1f);
        Color skyBottom = new Color(0.56f, 0.78f, 0.94f, 1f);
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

        pixmap.setColor(0.12f, 0.4f, 0.22f, 1f);
        pixmap.fillRectangle(0, 0, width, height / 3);
        pixmap.setColor(0.1f, 0.34f, 0.18f, 1f);
        for (int x = 0; x < width; x += 14) {
            pixmap.fillRectangle(x, height / 3 - 9, 10, 9);
        }

        pixmap.setColor(0.1f, 0.28f, 0.16f, 1f);
        pixmap.fillCircle(width / 4, height / 3, 26);
        pixmap.fillCircle(width / 2, height / 3 + 8, 34);
        pixmap.fillCircle(width * 3 / 4, height / 3 + 2, 28);

        pixmap.setColor(1f, 1f, 1f, 0.9f);
        for (int x = 18; x < width; x += 48) {
            pixmap.fillRectangle(x, height - 26, 8, 4);
            pixmap.fillRectangle(x + 10, height - 30, 14, 4);
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
}
