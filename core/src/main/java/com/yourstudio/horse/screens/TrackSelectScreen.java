package com.yourstudio.horse.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.yourstudio.horse.HorseGame;
import com.yourstudio.horse.model.MvpProgress;
import com.yourstudio.horse.model.MvpProgressStore;
import com.yourstudio.horse.ui.ScreenNavigator;
import com.yourstudio.horse.ui.UiFactory;

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
    private Sound clickSound;
    private Texture[] trackCards;
    private Texture[] trackCardsSelected;
    private Image[] cardImages;
    private Label trackNameLabel;
    private Label trackDescLabel;
    private int trackIndex;
    private Table layout;

    private final String[] trackNames = {"Erd\u0151", "Tengerpart", "Hegyek", "\u00C9jszakai v\u00E1ros"};
    private final String[] trackDescriptions = {
        "S\u0171r\u0171 f\u00E1k, puha \u00F6sv\u00E9nyek \u00E9s napf\u00E9nyes tiszt\u00E1sok.",
        "Homokos partok, hull\u00E1mok hangja, szeles sprint.",
        "Meredek emelked\u0151k \u00E9s h\u0171v\u00F6s hegyi leveg\u0151.",
        "Neonf\u00E9nyek, sz\u0171k utc\u00E1k \u00E9s gyors kanyarok."
    };
    // Map track index to .tmx file
    private final String[] trackFiles = {"forest.tmx", "tengerpart.tmx", "hegyek.tmx", "ejszakai_varos.tmx"};

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
        stage = new Stage(new ExtendViewport(1280f, 720f));
        float worldWidth = stage.getViewport().getWorldWidth();
        float worldHeight = stage.getViewport().getWorldHeight();
        float scale = Math.min(0.9f, Math.max(0.75f, Math.min(worldWidth / 1280f, worldHeight / 720f)));
        int titleSize = Math.max(32, Math.round(54f * scale));
        int labelSize = Math.max(18, Math.round(28f * scale));
        titleFont = createUIFont(titleSize, 3.1f * scale);
        labelFont = createUIFont(labelSize, 1.45f * scale);
        Skin skin = game.getSkin();

        background = loadUiTexture("ui/bg_select.png");
        clickSound = game.getAssets().get("sfx/click.wav", Sound.class);

        loadTrackCards();

        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        Label.LabelStyle labelStyle = new Label.LabelStyle(labelFont, Color.WHITE);
        TextButton.TextButtonStyle buttonStyle = skin.get("primary", TextButton.TextButtonStyle.class);

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
            cardsTable.add(image).width(200f * scale).height(140f * scale).pad(12f * scale);
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
            // Pass the .tmx filename according to selected icon
            ScreenNavigator.toRace(game, selection, trackFiles[trackIndex]);
        });

        layout = new Table();
        layout.pad(24f * scale);
        layout.add(title).padBottom(18f * scale);
        layout.row();
        layout.add(cardsTable).padBottom(24f * scale);
        layout.row();
        layout.add(trackNameLabel).padBottom(10f * scale);
        layout.row();
        layout.add(trackDescLabel).width(620f * scale).padBottom(26f * scale);
        layout.row();

        Table buttonRow = new Table();
        buttonRow.add(backButton).width(240f * scale).height(92f * scale).padRight(18f * scale);
        buttonRow.add(startButton).width(300f * scale).height(92f * scale);
        layout.add(buttonRow);

        layout.pack();
        applyLayoutScale();
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
            applyLayoutScale();
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
        disposeTextureArray(trackCards);
        disposeTextureArray(trackCardsSelected);
    }

    private void playClick() {
        MvpProgress progress = new MvpProgressStore(Gdx.app.getPreferences(MvpProgressStore.PREFS_NAME)).load();
        if (!progress.muted && clickSound != null) {
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

    private void loadTrackCards() {
        trackCards = new Texture[trackNames.length];
        trackCardsSelected = new Texture[trackNames.length];

        trackCards[0] = loadUiTexture("ui/card_forest.png");
        trackCardsSelected[0] = loadUiTexture("ui/card_forest_selected.png");
        trackCards[1] = loadUiTexture("ui/card_beach.png");
        trackCardsSelected[1] = loadUiTexture("ui/card_beach_selected.png");
        trackCards[2] = loadUiTexture("ui/card_mountain.png");
        trackCardsSelected[2] = loadUiTexture("ui/card_mountain_selected.png");
        trackCards[3] = loadUiTexture("ui/card_city.png");
        trackCardsSelected[3] = loadUiTexture("ui/card_city_selected.png");
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

    private void applyLayoutScale() {
        if (layout == null || stage == null) {
            return;
        }
        float worldWidth = stage.getViewport().getWorldWidth();
        float worldHeight = stage.getViewport().getWorldHeight();
        float maxWidth = worldWidth * 0.96f;
        float maxHeight = worldHeight * 0.9f;
        float fitScale = Math.min(maxWidth / layout.getWidth(), maxHeight / layout.getHeight());
        layout.setTransform(true);
        layout.setScale(fitScale < 1f ? fitScale : 1f);
        float scaledWidth = layout.getWidth() * layout.getScaleX();
        float scaledHeight = layout.getHeight() * layout.getScaleY();
        layout.setPosition((worldWidth - scaledWidth) * 0.5f, (worldHeight - scaledHeight) * 0.5f);
    }
}
