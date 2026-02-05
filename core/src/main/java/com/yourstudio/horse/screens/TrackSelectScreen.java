package com.yourstudio.horse.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
import com.yourstudio.horse.ui.PixelArtFactory;
import com.yourstudio.horse.ui.UiFactory;
import com.yourstudio.horse.ui.ScreenNavigator;

public class TrackSelectScreen extends ScreenAdapter {
    private final HorseGame game;
    private final String horseName;
    private final String riderName;
    private final String petName;
    private final String horseColor;
    private final String maneColor;
    private final String saddleColor;
    private final String outfitColor;

    private Stage stage;
    private BitmapFont titleFont;
    private BitmapFont labelFont;
    private Texture background;
    private Texture buttonUp;
    private Texture buttonDown;
    private Texture buttonOver;
    private Sound clickSound;
    private Texture[] trackCards;
    private Texture[] trackCardsSelected;
    private Image[] cardImages;
    private Label trackNameLabel;
    private Label trackDescLabel;
    private int trackIndex;

    private final String[] trackNames = {"Erd\u0151", "Tengerpart", "Hegyek", "\u00C9jszakai v\u00E1ros"};
    private final String[] trackDescriptions = {
        "S\u0171r\u0171 f\u00E1k, puha \u00F6sv\u00E9nyek \u00E9s napf\u00E9nyes tiszt\u00E1sok.",
        "Homokos partok, hull\u00E1mok hangja, szeles sprint.",
        "Meredek emelked\u0151k \u00E9s h\u0171v\u00F6s hegyi leveg\u0151.",
        "Neonf\u00E9nyek, sz\u0171k utc\u00E1k \u00E9s gyors kanyarok."
    };

    public TrackSelectScreen(HorseGame game, String horseName, String riderName, String petName) {
        this(game, horseName, riderName, petName, null, null, null, null);
    }

    public TrackSelectScreen(HorseGame game, String horseName, String riderName, String petName,
                             String horseColor, String maneColor, String saddleColor, String outfitColor) {
        this.game = game;
        this.horseName = horseName;
        this.riderName = riderName;
        this.petName = petName;
        this.horseColor = horseColor;
        this.maneColor = maneColor;
        this.saddleColor = saddleColor;
        this.outfitColor = outfitColor;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        titleFont = createUIFont(54, 3.1f);
        labelFont = createUIFont(28, 1.45f);

        background = createPixelBackground(360, 200);
        buttonUp = createPanelTexture(new Color(0.29f, 0.6f, 0.85f, 1f), new Color(0.1f, 0.2f, 0.3f, 1f), 280, 96);
        buttonDown = createPanelTexture(new Color(0.22f, 0.5f, 0.76f, 1f), new Color(0.08f, 0.16f, 0.24f, 1f), 280, 96);
        buttonOver = createPanelTexture(new Color(0.38f, 0.7f, 0.95f, 1f), new Color(0.12f, 0.24f, 0.36f, 1f), 280, 96);
        clickSound = game.getAssets().get("sfx/click.wav", Sound.class);

        createTrackCards();

        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        Label.LabelStyle labelStyle = new Label.LabelStyle(labelFont, Color.WHITE);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = toDrawable(buttonUp);
        buttonStyle.down = toDrawable(buttonDown);
        buttonStyle.over = toDrawable(buttonOver);
        buttonStyle.font = labelFont;
        buttonStyle.fontColor = Color.WHITE;

        Label title = UiFactory.label("P\u00E1lyav\u00E1laszt\u00E1s", titleStyle);
        trackNameLabel = UiFactory.label(trackNames[trackIndex], titleStyle);
        trackDescLabel = UiFactory.label(trackDescriptions[trackIndex], labelStyle);
        trackDescLabel.setAlignment(Align.center);
        trackDescLabel.setWrap(true);

        Table cardsTable = new Table();
        cardImages = new Image[trackNames.length];
        for (int i = 0; i < trackNames.length; i++) {
            Image image = new Image(toDrawable(trackCards[i]));
            final int index = i;
            image.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playClick();
                    trackIndex = index;
                    updateSelection();
                }
            });
            cardImages[i] = image;
            cardsTable.add(image).width(200f).height(140f).pad(12f);
        }
        updateSelection();

        TextButton backButton = UiFactory.button("Vissza", buttonStyle, () -> {
            playClick();
            ScreenNavigator.Selection selection = new ScreenNavigator.Selection(
                horseName, riderName, petName, horseColor, maneColor, saddleColor, outfitColor
            );
            ScreenNavigator.toCharacterSelect(game, selection);
        });
        TextButton startButton = UiFactory.button("Verseny ind\u00EDt\u00E1sa", buttonStyle, () -> {
            playClick();
            ScreenNavigator.Selection selection = new ScreenNavigator.Selection(
                horseName, riderName, petName, horseColor, maneColor, saddleColor, outfitColor
            );
            ScreenNavigator.toRace(game, selection, trackNames[trackIndex]);
        });

        Table layout = new Table();
        layout.setFillParent(true);
        layout.pad(24f);
        layout.add(title).padBottom(18f);
        layout.row();
        layout.add(cardsTable).padBottom(24f);
        layout.row();
        layout.add(trackNameLabel).padBottom(10f);
        layout.row();
        layout.add(trackDescLabel).width(620f).padBottom(26f);
        layout.row();

        Table buttonRow = new Table();
        buttonRow.add(backButton).width(240f).height(92f).padRight(18f);
        buttonRow.add(startButton).width(300f).height(92f);
        layout.add(buttonRow);

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
        if (labelFont != null) {
            labelFont.dispose();
        }
        if (background != null) {
            background.dispose();
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
        disposeTextureArray(trackCards);
        disposeTextureArray(trackCardsSelected);
    }

    private void playClick() {
        if (clickSound != null) {
            clickSound.play(0.6f);
        }
    }

    private void updateSelection() {
        for (int i = 0; i < cardImages.length; i++) {
            Texture cardTexture = (i == trackIndex) ? trackCardsSelected[i] : trackCards[i];
            cardImages[i].setDrawable(toDrawable(cardTexture));
        }
        trackNameLabel.setText(trackNames[trackIndex]);
        trackDescLabel.setText(trackDescriptions[trackIndex]);
    }

    private void createTrackCards() {
        trackCards = new Texture[trackNames.length];
        trackCardsSelected = new Texture[trackNames.length];

        Color[] skies = {
            new Color(0.35f, 0.65f, 0.35f, 1f),
            new Color(0.35f, 0.7f, 0.9f, 1f),
            new Color(0.6f, 0.65f, 0.75f, 1f),
            new Color(0.2f, 0.2f, 0.35f, 1f)
        };
        Color[] grounds = {
            new Color(0.18f, 0.4f, 0.18f, 1f),
            new Color(0.95f, 0.84f, 0.55f, 1f),
            new Color(0.4f, 0.35f, 0.3f, 1f),
            new Color(0.2f, 0.2f, 0.25f, 1f)
        };
        Color[] accents = {
            new Color(0.1f, 0.25f, 0.1f, 1f),
            new Color(0.1f, 0.4f, 0.6f, 1f),
            new Color(0.25f, 0.2f, 0.2f, 1f),
            new Color(0.6f, 0.3f, 0.85f, 1f)
        };

        for (int i = 0; i < trackNames.length; i++) {
            trackCards[i] = createTrackCard(skies[i], grounds[i], accents[i], false);
            trackCardsSelected[i] = createTrackCard(skies[i], grounds[i], accents[i], true);
        }
    }

    private Texture createTrackCard(Color sky, Color ground, Color accent, boolean selected) {
        int width = 170;
        int height = 120;
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(sky);
        pixmap.fillRectangle(0, height / 2, width, height / 2);
        pixmap.setColor(ground);
        pixmap.fillRectangle(0, 0, width, height / 2);

        pixmap.setColor(accent);
        pixmap.fillRectangle(10, height / 2 + 8, 24, 8);
        pixmap.fillRectangle(44, height / 2 + 12, 18, 6);
        pixmap.fillRectangle(70, height / 2 + 6, 30, 10);

        pixmap.fillRectangle(20, height / 2 - 18, 24, 10);
        pixmap.fillRectangle(90, height / 2 - 22, 30, 12);

        Color border = selected ? new Color(0.95f, 0.86f, 0.4f, 1f) : new Color(0.2f, 0.2f, 0.25f, 1f);
        pixmap.setColor(border);
        for (int i = 0; i < 3; i++) {
            pixmap.drawRectangle(i, i, width - (i * 2), height - (i * 2));
        }

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private Texture createPixelBackground(int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        Color top = new Color(0.12f, 0.16f, 0.25f, 1f);
        Color bottom = new Color(0.22f, 0.3f, 0.42f, 1f);
        for (int y = 0; y < height; y++) {
            float t = y / (float) (height - 1);
            pixmap.setColor(
                bottom.r + (top.r - bottom.r) * t,
                bottom.g + (top.g - bottom.g) * t,
                bottom.b + (top.b - bottom.b) * t,
                1f
            );
            pixmap.drawLine(0, y, width, y);
        }
        pixmap.setColor(0.1f, 0.2f, 0.12f, 1f);
        pixmap.fillRectangle(0, 0, width, height / 3);
        pixmap.setColor(0.08f, 0.18f, 0.1f, 1f);
        for (int x = 0; x < width; x += 18) {
            pixmap.fillRectangle(x, height / 3 - 8, 12, 8);
        }
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private Texture createPanelTexture(Color fillColor, Color borderColor, int width, int height) {
        Color shade = new Color(Math.max(0f, fillColor.r - 0.08f), Math.max(0f, fillColor.g - 0.08f), Math.max(0f, fillColor.b - 0.08f), 0.18f);
        Color borderLight = new Color(0.92f, 0.92f, 0.96f, 1f);
        return PixelArtFactory.createPixelPanel(width, height, fillColor, shade, borderLight, borderColor);
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

    private void disposeTextureArray(Texture[] textures) {
        if (textures == null) {
            return;
        }
        for (Texture texture : textures) {
            if (texture != null) {
                texture.dispose();
            }
        }
    }
}
