package com.yourstudio.horse.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.MathUtils;
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
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.yourstudio.horse.HorseGame;
import com.yourstudio.horse.model.MvpGameConfig;
import com.yourstudio.horse.model.MvpProgress;
import com.yourstudio.horse.model.MvpProgressStore;
import com.yourstudio.horse.ui.PixelArtFactory;
import com.yourstudio.horse.ui.ScreenNavigator;

public class CharacterSelectScreen extends ScreenAdapter {
    private static final boolean FORCE_PROCEDURAL_HORSE = true;
    private static final String PREFS_NAME = "versenylovak_prefs";
    private static final String PREF_HORSE = "horse";
    private static final String PREF_RIDER = "rider";
    private static final String PREF_PET = "pet";
    private static final String PREF_HORSE_COLOR = "horseColor";
    private static final String PREF_MANE_COLOR = "maneColor";
    private static final String PREF_SADDLE_COLOR = "saddleColor";
    private static final String PREF_OUTFIT_COLOR = "outfitColor";
    private final HorseGame game;
    private Stage stage;
    private Texture background;
    private BitmapFont titleFont;
    private BitmapFont bodyFont;
    private Sound clickSound;
    private Texture[] riderPreviews;
    private Texture[] petPreviews;
    private Texture[] horseSheets;
    private TextureRegion[] horsePreviewRegions;
    private Texture[] horseColorSwatches;
    private Texture[] maneColorSwatches;
    private Texture[] saddleColorSwatches;
    private Texture[] outfitColorSwatches;
    private Image horsePreviewImage;
    private Image riderPreviewImage;
    private Image petPreviewImage;
    private Image horseColorSwatchImage;
    private Image maneColorSwatchImage;
    private Image saddleColorSwatchImage;
    private Image outfitColorSwatchImage;
    private Texture horsePreviewCustom;
    private Texture riderPreviewCustom;
    private Color[] horseColorValues;
    private Color[] maneColorValues;
    private Color[] saddleColorValues;
    private Color[] outfitColorValues;
    private Color[] riderHairColors;
    private Table layout;

    private final String[] horses = horseNamesFromConfig();
    private final String[] riders = MvpGameConfig.RIDER_NAMES;
    private final String[] pets = {"Kutya"};
    private final String[] horseColors = {"Meleg barna", "Arany", "Hamvas", "S\u00F6t\u00E9t"};
    private final String[] maneColors = {"Fekete", "Csokol\u00E1d\u00E9", "Sz\u00FCrke", "Sz\u0151ke"};
    private final String[] saddleColors = {"V\u00F6r\u00F6s", "K\u00E9k", "Z\u00F6ld", "Fekete"};
    private final String[] outfitColors = {"Piros", "K\u00E9k", "Z\u00F6ld", "Lila"};
    private final MvpGameConfig.Difficulty[] difficulties = MvpGameConfig.Difficulty.values();
    private final String[] difficultyLabels = {"K\u00F6nny\u0171", "K\u00F6zepes", "Neh\u00E9z"};

    private int horseIndex;
    private int riderIndex;
    private int petIndex;
    private int horseColorIndex;
    private int maneColorIndex;
    private int saddleColorIndex;
    private int outfitColorIndex;
    private int difficultyIndex;

    private Label horseValue;
    private Label riderValue;
    private Label petValue;
    private Label horseColorValue;
    private Label maneColorValue;
    private Label saddleColorValue;
    private Label outfitColorValue;
    private Label difficultyValue;
    private Label horseDescriptionValue;
    private Label horseStatsValue;
    private Label riderBonusValue;
    private Label petInfoValue;

    public CharacterSelectScreen(HorseGame game) {
        this(game, null, null, null, null, null, null, null);
    }

    public CharacterSelectScreen(HorseGame game, String horseName, String riderName, String petName) {
        this(game, horseName, riderName, petName, null, null, null, null);
    }

    public CharacterSelectScreen(HorseGame game, String horseName, String riderName, String petName,
                                 String horseColor, String maneColor, String saddleColor) {
        this(game, horseName, riderName, petName, horseColor, maneColor, saddleColor, null);
    }

    public CharacterSelectScreen(HorseGame game, String horseName, String riderName, String petName,
                                 String horseColor, String maneColor, String saddleColor, String outfitColor) {
        this(game, horseName, riderName, petName, horseColor, maneColor, saddleColor, outfitColor, null);
    }

    public CharacterSelectScreen(HorseGame game, String horseName, String riderName, String petName,
                                 String horseColor, String maneColor, String saddleColor, String outfitColor,
                                 MvpGameConfig.Difficulty difficulty) {
        this.game = game;
        Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        MvpProgress progress = new MvpProgressStore(Gdx.app.getPreferences(MvpProgressStore.PREFS_NAME)).load();
        String resolvedHorse = horseName != null ? horseName : prefs.getString(PREF_HORSE, progress.selectedHorse);
        String resolvedRider = riderName != null ? riderName : prefs.getString(PREF_RIDER, progress.selectedRiderName);
        String resolvedPet = petName != null ? petName : prefs.getString(PREF_PET, progress.selectedPet);
        String resolvedHorseColor = horseColor != null ? horseColor : prefs.getString(PREF_HORSE_COLOR, null);
        String resolvedManeColor = maneColor != null ? maneColor : prefs.getString(PREF_MANE_COLOR, null);
        String resolvedSaddleColor = saddleColor != null ? saddleColor : prefs.getString(PREF_SADDLE_COLOR, null);
        String resolvedOutfitColor = outfitColor != null ? outfitColor : prefs.getString(PREF_OUTFIT_COLOR, null);
        MvpGameConfig.Difficulty resolvedDifficulty = difficulty != null ? difficulty : progress.selectedDifficulty;

        this.horseIndex = findIndex(horses, resolvedHorse);
        this.riderIndex = findIndex(riders, resolvedRider);
        this.petIndex = findIndex(pets, resolvedPet);
        this.horseColorIndex = findIndex(horseColors, resolvedHorseColor);
        this.maneColorIndex = findIndex(maneColors, resolvedManeColor);
        this.saddleColorIndex = findIndex(saddleColors, resolvedSaddleColor);
        this.outfitColorIndex = findIndex(outfitColors, resolvedOutfitColor);
        this.difficultyIndex = findDifficultyIndex(resolvedDifficulty);
    }

    @Override
    public void show() {
        stage = new Stage(new ExtendViewport(1280f, 720f));
        titleFont = createUIFont(54, 3.1f);
        bodyFont = createUIFont(28, 1.45f);
        Skin skin = game.getSkin();
        background = loadUiTexture("ui/bg_select.png");
        clickSound = game.getAssets().get("sfx/click.wav", Sound.class);
        horseColorValues = new Color[] {
            new Color(0.64f, 0.38f, 0.2f, 1f),
            new Color(0.85f, 0.72f, 0.42f, 1f),
            new Color(0.7f, 0.72f, 0.78f, 1f),
            new Color(0.25f, 0.2f, 0.15f, 1f)
        };
        maneColorValues = new Color[] {
            new Color(0.08f, 0.08f, 0.08f, 1f),
            new Color(0.28f, 0.16f, 0.08f, 1f),
            new Color(0.5f, 0.5f, 0.55f, 1f),
            new Color(0.85f, 0.78f, 0.5f, 1f)
        };
        saddleColorValues = new Color[] {
            new Color(0.65f, 0.2f, 0.2f, 1f),
            new Color(0.2f, 0.35f, 0.7f, 1f),
            new Color(0.2f, 0.55f, 0.3f, 1f),
            new Color(0.12f, 0.12f, 0.12f, 1f)
        };
        outfitColorValues = new Color[] {
            new Color(0.75f, 0.2f, 0.2f, 1f),
            new Color(0.2f, 0.4f, 0.8f, 1f),
            new Color(0.2f, 0.6f, 0.35f, 1f),
            new Color(0.55f, 0.3f, 0.75f, 1f)
        };
        riderHairColors = createRiderHairColors(riders.length);
        loadHorsePreviews();
        riderPreviews = createRiderPreviews();
        petPreviews = createPetPreviews();
        horseColorSwatches = createSwatches(horseColorValues);
        maneColorSwatches = createSwatches(maneColorValues);
        saddleColorSwatches = createSwatches(saddleColorValues);
        outfitColorSwatches = createSwatches(outfitColorValues);

        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        Label.LabelStyle labelStyle = new Label.LabelStyle(bodyFont, Color.WHITE);
        TextButton.TextButtonStyle buttonStyle = skin.get("primary", TextButton.TextButtonStyle.class);

        Label title = new Label("Karakter v\u00E1laszt\u00E1s", titleStyle);
        horseValue = new Label(horses[horseIndex], labelStyle);
        riderValue = new Label(riders[riderIndex], labelStyle);
        petValue = new Label(pets[petIndex], labelStyle);
        horseColorValue = new Label(horseColors[horseColorIndex], labelStyle);
        maneColorValue = new Label(maneColors[maneColorIndex], labelStyle);
        saddleColorValue = new Label(saddleColors[saddleColorIndex], labelStyle);
        outfitColorValue = new Label(outfitColors[outfitColorIndex], labelStyle);
        difficultyValue = new Label(difficultyLabels[difficultyIndex], labelStyle);

        horsePreviewImage = new Image(horsePreviewRegions[horseIndex]);
        riderPreviewImage = new Image(toDrawable(riderPreviews[riderIndex]));
        petPreviewImage = new Image(toDrawable(petPreviews[petIndex]));
        horseColorSwatchImage = new Image(toDrawable(horseColorSwatches[horseColorIndex]));
        maneColorSwatchImage = new Image(toDrawable(maneColorSwatches[maneColorIndex]));
        saddleColorSwatchImage = new Image(toDrawable(saddleColorSwatches[saddleColorIndex]));
        outfitColorSwatchImage = new Image(toDrawable(outfitColorSwatches[outfitColorIndex]));
        refreshHorsePreview();
        refreshRiderPreview();

        layout = new Table();
        layout.pad(24f);

        layout.add(title).colspan(5).padBottom(30f);
        layout.row();

        Table previewRow = new Table();
        previewRow.add(horsePreviewImage).width(150f).height(110f).pad(6f);
        previewRow.add(riderPreviewImage).width(150f).height(110f).pad(6f);
        previewRow.add(petPreviewImage).width(150f).height(110f).pad(6f);
        layout.add(previewRow).colspan(5).padBottom(24f);
        layout.row();

        addSelectorRow(layout, "L\u00F3", horseValue, buttonStyle, () -> updateHorse(-1), () -> updateHorse(1));
        addSelectorRow(layout, "L\u00F3sz\u00EDn", horseColorValue, horseColorSwatchImage, buttonStyle, () -> updateHorseColor(-1), () -> updateHorseColor(1));
        addSelectorRow(layout, "S\u00F6r\u00E9ny", maneColorValue, maneColorSwatchImage, buttonStyle, () -> updateManeColor(-1), () -> updateManeColor(1));
        addSelectorRow(layout, "Nyereg", saddleColorValue, saddleColorSwatchImage, buttonStyle, () -> updateSaddleColor(-1), () -> updateSaddleColor(1));
        addSelectorRow(layout, "Lovas", riderValue, buttonStyle, () -> updateRider(-1), () -> updateRider(1));
        TextButton randomRiderButton = new TextButton("V\u00E9letlen n\u00E9v", buttonStyle);
        randomRiderButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playClick();
                randomizeRiderName();
            }
        });
        layout.add(randomRiderButton).colspan(5).width(260f).height(60f).padBottom(18f);
        layout.row();
        addSelectorRow(layout, "Ruh\u00E1zat", outfitColorValue, outfitColorSwatchImage, buttonStyle, () -> updateOutfitColor(-1), () -> updateOutfitColor(1));
        addSelectorRow(layout, "Kis kedvenc", petValue, buttonStyle, () -> updatePet(-1), () -> updatePet(1));
        addSelectorRow(layout, "Neh\u00E9zs\u00E9g", difficultyValue, buttonStyle, () -> updateDifficulty(-1), () -> updateDifficulty(1));

        horseDescriptionValue = createInfoLabel(labelStyle, horseDescriptionText());
        horseStatsValue = createInfoLabel(labelStyle, horseStatsText());
        riderBonusValue = createInfoLabel(labelStyle, riderBonusText());
        petInfoValue = createInfoLabel(labelStyle, "Kutya: kezd\u0151 kedvenc, b\u00F3nusz n\u00E9lk\u00FCl.");
        Table infoPanel = new Table();
        infoPanel.add(horseDescriptionValue).width(420f).left().padRight(24f);
        infoPanel.add(horseStatsValue).width(280f).left();
        infoPanel.row();
        infoPanel.add(riderBonusValue).width(420f).left().padTop(12f).padRight(24f);
        infoPanel.add(petInfoValue).width(280f).left().padTop(12f);
        layout.add(infoPanel).colspan(5).padTop(4f).padBottom(8f);
        layout.row();

        layout.row().padTop(30f);
        TextButton backButton = new TextButton("Vissza", buttonStyle);
        TextButton startButton = new TextButton("Verseny ind\u00EDt\u00E1sa", buttonStyle);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playClick();
                ScreenNavigator.toMainMenu(game);
            }
        });
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playClick();
                ScreenNavigator.Selection selection = new ScreenNavigator.Selection(
                    horses[horseIndex],
                    riders[riderIndex],
                    pets[petIndex],
                    horseColors[horseColorIndex],
                    maneColors[maneColorIndex],
                    saddleColors[saddleColorIndex],
                    outfitColors[outfitColorIndex],
                    difficulties[difficultyIndex]
                );
                ScreenNavigator.toDefaultRace(game, selection);
            }
        });

        layout.add(backButton).width(220f).height(80f).padRight(20f);
        layout.add(startButton).width(320f).height(80f).colspan(2);

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
        if (bodyFont != null) {
            bodyFont.dispose();
        }
        if (background != null) {
            background.dispose();
        }
        if (horsePreviewCustom != null) {
            horsePreviewCustom.dispose();
        }
        if (riderPreviewCustom != null) {
            riderPreviewCustom.dispose();
        }
        disposeTextureArray(horseSheets);
        disposeTextureArray(riderPreviews);
        disposeTextureArray(petPreviews);
        disposeTextureArray(horseColorSwatches);
        disposeTextureArray(maneColorSwatches);
        disposeTextureArray(saddleColorSwatches);
        disposeTextureArray(outfitColorSwatches);
    }

    private void addSelectorRow(Table layout, String label, Label valueLabel, TextButton.TextButtonStyle buttonStyle,
                                Runnable previousAction, Runnable nextAction) {
        Label rowLabel = new Label(label, new Label.LabelStyle(bodyFont, Color.WHITE));
        TextButton prevButton = new TextButton("<", buttonStyle);
        TextButton nextButton = new TextButton(">", buttonStyle);

        prevButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playClick();
                previousAction.run();
            }
        });
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playClick();
                nextAction.run();
            }
        });

        layout.add(rowLabel).left().padBottom(18f);
        layout.add(prevButton).width(70f).height(60f).padBottom(18f);
        layout.add(valueLabel).width(220f).padBottom(18f).padLeft(10f).padRight(10f);
        layout.add(nextButton).width(70f).height(60f).padBottom(18f);
        layout.row();
    }

    private void addSelectorRow(Table layout, String label, Label valueLabel, Image previewImage,
                                TextButton.TextButtonStyle buttonStyle, Runnable previousAction, Runnable nextAction) {
        Label rowLabel = new Label(label, new Label.LabelStyle(bodyFont, Color.WHITE));
        TextButton prevButton = new TextButton("<", buttonStyle);
        TextButton nextButton = new TextButton(">", buttonStyle);

        prevButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playClick();
                previousAction.run();
            }
        });
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playClick();
                nextAction.run();
            }
        });

        layout.add(rowLabel).left().padBottom(18f);
        layout.add(prevButton).width(70f).height(60f).padBottom(18f);
        layout.add(previewImage).width(36f).height(24f).padBottom(18f).padLeft(6f).padRight(6f);
        layout.add(valueLabel).width(200f).padBottom(18f).padLeft(6f).padRight(6f);
        layout.add(nextButton).width(70f).height(60f).padBottom(18f);
        layout.row();
    }

    private void updateHorse(int delta) {
        horseIndex = wrapIndex(horseIndex + delta, horses.length);
        horseValue.setText(horses[horseIndex]);
        horseDescriptionValue.setText(horseDescriptionText());
        horseStatsValue.setText(horseStatsText());
        refreshHorsePreview();
        saveSelectionPrefs();
    }

    private void updateHorseColor(int delta) {
        horseColorIndex = wrapIndex(horseColorIndex + delta, horseColors.length);
        horseColorValue.setText(horseColors[horseColorIndex]);
        horseColorSwatchImage.setDrawable(toDrawable(horseColorSwatches[horseColorIndex]));
        refreshHorsePreview();
        saveSelectionPrefs();
    }

    private void updateManeColor(int delta) {
        maneColorIndex = wrapIndex(maneColorIndex + delta, maneColors.length);
        maneColorValue.setText(maneColors[maneColorIndex]);
        maneColorSwatchImage.setDrawable(toDrawable(maneColorSwatches[maneColorIndex]));
        refreshHorsePreview();
        saveSelectionPrefs();
    }

    private void updateSaddleColor(int delta) {
        saddleColorIndex = wrapIndex(saddleColorIndex + delta, saddleColors.length);
        saddleColorValue.setText(saddleColors[saddleColorIndex]);
        saddleColorSwatchImage.setDrawable(toDrawable(saddleColorSwatches[saddleColorIndex]));
        refreshHorsePreview();
        saveSelectionPrefs();
    }

    private void updateOutfitColor(int delta) {
        outfitColorIndex = wrapIndex(outfitColorIndex + delta, outfitColors.length);
        outfitColorValue.setText(outfitColors[outfitColorIndex]);
        outfitColorSwatchImage.setDrawable(toDrawable(outfitColorSwatches[outfitColorIndex]));
        refreshRiderPreview();
        saveSelectionPrefs();
    }

    private void updateRider(int delta) {
        riderIndex = wrapIndex(riderIndex + delta, riders.length);
        riderValue.setText(riders[riderIndex]);
        riderBonusValue.setText(riderBonusText());
        refreshRiderPreview();
        saveSelectionPrefs();
    }

    private void randomizeRiderName() {
        if (riders.length <= 1) {
            return;
        }
        int nextIndex = riderIndex;
        while (nextIndex == riderIndex) {
            nextIndex = MathUtils.random(riders.length - 1);
        }
        riderIndex = nextIndex;
        riderValue.setText(riders[riderIndex]);
        riderBonusValue.setText(riderBonusText());
        refreshRiderPreview();
        saveSelectionPrefs();
    }

    private void updatePet(int delta) {
        petIndex = wrapIndex(petIndex + delta, pets.length);
        petValue.setText(pets[petIndex]);
        petPreviewImage.setDrawable(toDrawable(petPreviews[petIndex]));
        saveSelectionPrefs();
    }

    private void updateDifficulty(int delta) {
        difficultyIndex = wrapIndex(difficultyIndex + delta, difficulties.length);
        difficultyValue.setText(difficultyLabels[difficultyIndex]);
        saveSelectionPrefs();
    }

    private void saveSelectionPrefs() {
        Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        prefs.putString(PREF_HORSE, horses[horseIndex]);
        prefs.putString(PREF_RIDER, riders[riderIndex]);
        prefs.putString(PREF_PET, pets[petIndex]);
        prefs.putString(PREF_HORSE_COLOR, horseColors[horseColorIndex]);
        prefs.putString(PREF_MANE_COLOR, maneColors[maneColorIndex]);
        prefs.putString(PREF_SADDLE_COLOR, saddleColors[saddleColorIndex]);
        prefs.putString(PREF_OUTFIT_COLOR, outfitColors[outfitColorIndex]);
        prefs.flush();

        MvpProgressStore progressStore = new MvpProgressStore(Gdx.app.getPreferences(MvpProgressStore.PREFS_NAME));
        MvpProgress progress = progressStore.load();
        progress.selectedHorse = horses[horseIndex];
        progress.selectedRiderName = riders[riderIndex];
        progress.selectedPet = pets[petIndex];
        progress.selectedRiderColor = outfitColors[outfitColorIndex];
        progress.selectedDifficulty = difficulties[difficultyIndex];
        progressStore.save(progress);
    }

    private void playClick() {
        MvpProgress progress = new MvpProgressStore(Gdx.app.getPreferences(MvpProgressStore.PREFS_NAME)).load();
        if (!progress.muted && clickSound != null) {
            clickSound.play(0.6f);
        }
    }

    private int wrapIndex(int value, int size) {
        int result = value % size;
        return result < 0 ? result + size : result;
    }

    private int findIndex(String[] values, String target) {
        if (target == null) {
            return 0;
        }
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(target)) {
                return i;
            }
        }
        return 0;
    }

    private static String[] horseNamesFromConfig() {
        String[] names = new String[MvpGameConfig.HORSES.length];
        for (int i = 0; i < MvpGameConfig.HORSES.length; i++) {
            names[i] = MvpGameConfig.HORSES[i].name;
        }
        return names;
    }

    private int findDifficultyIndex(MvpGameConfig.Difficulty difficulty) {
        for (int i = 0; i < difficulties.length; i++) {
            if (difficulties[i] == difficulty) {
                return i;
            }
        }
        return 0;
    }

    private Label createInfoLabel(Label.LabelStyle labelStyle, String text) {
        Label label = new Label(text, labelStyle);
        label.setWrap(true);
        return label;
    }

    private String horseDescriptionText() {
        MvpGameConfig.HorseProfile horse = MvpGameConfig.HORSES[horseIndex];
        return horse.name + ": " + horse.description;
    }

    private String horseStatsText() {
        MvpGameConfig.HorseProfile horse = MvpGameConfig.HORSES[horseIndex];
        return "L\u00F3 statok\n"
            + "Gyorsas\u00E1g: " + statBar(horse.speed) + "\n"
            + "Fordul\u00E1s: " + statBar(horse.turning) + "\n"
            + "Gyorsul\u00E1s: " + statBar(horse.acceleration) + "\n"
            + "Boost: " + statBar(horse.boost);
    }

    private String riderBonusText() {
        MvpGameConfig.RiderBonus bonus = MvpGameConfig.riderBonusForIndex(riderIndex);
        if (bonus.type == MvpGameConfig.RiderBonusType.ACCELERATION) {
            return "Lovas b\u00F3nusz: +1% gyorsul\u00E1s.";
        }
        return "Lovas b\u00F3nusz: +1% boost t\u00F6lt\u00E9s.";
    }

    private String statBar(int value) {
        StringBuilder builder = new StringBuilder(5);
        for (int i = 1; i <= 5; i++) {
            builder.append(i <= value ? '#' : '.');
        }
        return builder.toString();
    }

    private Texture createColorTexture(Color color) {
        return PixelArtFactory.createSolidTexture(color);
    }

    private void loadHorsePreviews() {
        String[] variants = {"chestnut", "bay", "gray", "palomino"};
        horseSheets = new Texture[variants.length];
        horsePreviewRegions = new TextureRegion[variants.length];
        if (FORCE_PROCEDURAL_HORSE) {
            Color[] bodies = {
                new Color(0.65f, 0.44f, 0.3f, 1f),
                new Color(0.48f, 0.3f, 0.2f, 1f),
                new Color(0.72f, 0.72f, 0.78f, 1f),
                new Color(0.85f, 0.72f, 0.42f, 1f)
            };
            Color[] manes = {
                new Color(0.25f, 0.16f, 0.1f, 1f),
                new Color(0.2f, 0.12f, 0.08f, 1f),
                new Color(0.5f, 0.5f, 0.55f, 1f),
                new Color(0.55f, 0.4f, 0.2f, 1f)
            };
            for (int i = 0; i < variants.length; i++) {
                Texture fallback = createHorsePreview(bodies[i], manes[i], new Color(0.35f, 0.2f, 0.12f, 1f));
                horseSheets[i] = fallback;
                horsePreviewRegions[i] = new TextureRegion(fallback);
            }
            return;
        }
        for (int i = 0; i < variants.length; i++) {
            try {
                Texture sheet = new Texture("sprites/horse_idle_" + variants[i] + ".png");
                horseSheets[i] = sheet;
                TextureRegion[][] split = TextureRegion.split(sheet, 128, 128);
                horsePreviewRegions[i] = split[0][0];
            } catch (RuntimeException exception) {
                Texture fallback = createHorsePreview(new Color(0.65f, 0.44f, 0.3f, 1f), new Color(0.25f, 0.16f, 0.1f, 1f), new Color(0.35f, 0.2f, 0.1f, 1f));
                horseSheets[i] = fallback;
                horsePreviewRegions[i] = new TextureRegion(fallback);
            }
        }
    }

    private Texture[] createRiderPreviews() {
        Color[] outfits = {
            new Color(0.35f, 0.6f, 0.85f, 1f),
            new Color(0.6f, 0.45f, 0.8f, 1f),
            new Color(0.2f, 0.7f, 0.45f, 1f),
            new Color(0.85f, 0.4f, 0.4f, 1f)
        };
        Color[] hair = {
            new Color(0.2f, 0.15f, 0.1f, 1f),
            new Color(0.4f, 0.25f, 0.1f, 1f),
            new Color(0.1f, 0.08f, 0.05f, 1f),
            new Color(0.7f, 0.55f, 0.3f, 1f)
        };
        Texture[] previews = new Texture[riders.length];
        for (int i = 0; i < riders.length; i++) {
            previews[i] = createRiderPreview(outfits[i % outfits.length], hair[i % hair.length]);
        }
        return previews;
    }

    private Color[] createRiderHairColors(int count) {
        Color[] palette = {
            new Color(0.2f, 0.15f, 0.1f, 1f),
            new Color(0.4f, 0.25f, 0.1f, 1f),
            new Color(0.1f, 0.08f, 0.05f, 1f),
            new Color(0.7f, 0.55f, 0.3f, 1f)
        };
        Color[] colors = new Color[count];
        for (int i = 0; i < count; i++) {
            colors[i] = palette[i % palette.length];
        }
        return colors;
    }

    private Texture[] createPetPreviews() {
        // Colors: Kutya, Cica, Nyuszi, Papagáj, Kapibara, Lajhár
        Color[] petColors = {
            new Color(0.85f, 0.65f, 0.4f, 1f),   // Kutya
            new Color(0.6f, 0.6f, 0.65f, 1f),    // Cica
            new Color(0.95f, 0.9f, 0.75f, 1f),   // Nyuszi
            new Color(0.2f, 0.75f, 0.45f, 1f),   // Papagáj
            new Color(0.7f, 0.5f, 0.3f, 1f),     // Kapibara
            new Color(0.6f, 0.7f, 0.5f, 1f)      // Lajhár
        };
        Texture[] previews = new Texture[petColors.length];
        for (int i = 0; i < petColors.length; i++) {
            previews[i] = createPetPreview(petColors[i]);
        }
        return previews;
    }

    private Texture[] createSwatches(Color... colors) {
        Texture[] swatches = new Texture[colors.length];
        for (int i = 0; i < colors.length; i++) {
            swatches[i] = createColorTexture(colors[i]);
        }
        return swatches;
    }

    private Texture createHorsePreview(Color body, Color mane, Color saddle) {
        Pixmap pixmap = createPreviewPanel();
        Color bodyShade = darken(body, 0.12f);
        Color hoof = darken(body, 0.25f);
        Color maneDark = darken(mane, 0.12f);

        pixmap.setColor(0f, 0f, 0f, 0.2f);
        pixmap.fillRectangle(30, 36, 90, 10);
        pixmap.fillCircle(30, 41, 6);
        pixmap.fillCircle(120, 41, 6);

        pixmap.setColor(body);
        pixmap.fillRectangle(32, 52, 72, 26);
        pixmap.fillRectangle(86, 60, 28, 18);
        pixmap.fillCircle(118, 70, 14);

        pixmap.setColor(bodyShade);
        pixmap.fillRectangle(36, 50, 60, 6);
        pixmap.fillRectangle(86, 58, 24, 4);

        pixmap.setColor(bodyShade);
        pixmap.fillRectangle(38, 36, 10, 20);
        pixmap.fillRectangle(60, 36, 10, 20);
        pixmap.fillRectangle(82, 36, 10, 20);
        pixmap.fillRectangle(100, 36, 8, 18);
        pixmap.setColor(hoof);
        pixmap.fillRectangle(38, 34, 10, 4);
        pixmap.fillRectangle(60, 34, 10, 4);
        pixmap.fillRectangle(82, 34, 10, 4);
        pixmap.fillRectangle(100, 34, 8, 4);

        pixmap.setColor(mane);
        pixmap.fillRectangle(88, 78, 28, 10);
        pixmap.fillRectangle(44, 78, 24, 8);
        pixmap.setColor(maneDark);
        pixmap.fillRectangle(26, 60, 8, 22);

        pixmap.setColor(Color.WHITE);
        pixmap.fillRectangle(124, 70, 3, 3);
        if (saddle != null) {
            pixmap.setColor(saddle);
            pixmap.fillRectangle(66, 60, 22, 12);
        }
        return finalizePreviewTexture(pixmap);
    }

    private Color darken(Color color, float amount) {
        return new Color(
            Math.max(0f, color.r - amount),
            Math.max(0f, color.g - amount),
            Math.max(0f, color.b - amount),
            color.a
        );
    }

    private void refreshHorsePreview() {
        if (horsePreviewImage == null) {
            return;
        }
        if (horsePreviewCustom != null) {
            horsePreviewCustom.dispose();
        }
        Color body = horseColorValues[horseColorIndex];
        Color mane = maneColorValues[maneColorIndex];
        Color saddle = saddleColorValues[saddleColorIndex];
        horsePreviewCustom = createHorsePreview(body, mane, saddle);
        horsePreviewImage.setDrawable(toDrawable(horsePreviewCustom));
    }

    private void refreshRiderPreview() {
        if (riderPreviewImage == null) {
            return;
        }
        if (riderPreviewCustom != null) {
            riderPreviewCustom.dispose();
        }
        Color outfit = outfitColorValues[outfitColorIndex];
        Color hair = riderHairColors[riderIndex % riderHairColors.length];
        riderPreviewCustom = createRiderPreview(outfit, hair);
        riderPreviewImage.setDrawable(toDrawable(riderPreviewCustom));
    }

    private Texture createRiderPreview(Color outfit, Color hair) {
        Pixmap pixmap = createPreviewPanel();
        pixmap.setColor(outfit);
        pixmap.fillRectangle(60, 44, 40, 48);
        pixmap.fillRectangle(50, 56, 14, 24);
        pixmap.fillRectangle(96, 56, 14, 24);
        pixmap.setColor(new Color(0.9f, 0.75f, 0.6f, 1f));
        pixmap.fillCircle(80, 98, 12);
        pixmap.setColor(hair);
        pixmap.fillRectangle(68, 104, 24, 6);
        return finalizePreviewTexture(pixmap);
    }

    private Texture createPetPreview(Color fur) {
        Pixmap pixmap = createPreviewPanel();
        pixmap.setColor(fur);
        pixmap.fillCircle(80, 64, 20);
        pixmap.fillCircle(60, 72, 10);
        pixmap.fillCircle(100, 72, 10);
        pixmap.setColor(new Color(0.2f, 0.2f, 0.2f, 1f));
        pixmap.fillCircle(72, 68, 3);
        pixmap.fillCircle(88, 68, 3);
        pixmap.fillRectangle(78, 56, 4, 6);
        return finalizePreviewTexture(pixmap);
    }

    private Pixmap createPreviewPanel() {
        int width = 160;
        int height = 120;
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.94f, 0.91f, 0.85f, 1f);
        pixmap.fill();
        pixmap.setColor(0.35f, 0.26f, 0.18f, 1f);
        pixmap.drawRectangle(0, 0, width, height);
        pixmap.drawRectangle(1, 1, width - 2, height - 2);
        return pixmap;
    }

    private Texture finalizePreviewTexture(Pixmap pixmap) {
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

    private Drawable toDrawable(Texture texture) {
        return new TextureRegionDrawable(texture);
    }

    private Texture loadUiTexture(String path) {
        Texture texture = new Texture(path);
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        return texture;
    }
}
