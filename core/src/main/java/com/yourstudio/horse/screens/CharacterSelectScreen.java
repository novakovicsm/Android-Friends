package com.yourstudio.horse.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.yourstudio.horse.HorseGame;
import com.yourstudio.horse.ui.ScreenNavigator;

public class CharacterSelectScreen extends ScreenAdapter {
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
    private BitmapFont font;
    private Texture buttonUp;
    private Texture buttonDown;
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

    private final String[] horses = {"Gesztenye", "Pej", "Szürke", "Palomino"};
    private final String[] riders = {"Lili", "Noel", "Mira", "Áron"};
    private final String[] pets = {"Kutya", "Cica", "Nyuszi", "Papagáj"};
    private final String[] horseColors = {"Meleg barna", "Arany", "Hamvas", "Sötét"};
    private final String[] maneColors = {"Fekete", "Csokoládé", "Szürke", "Szőke"};
    private final String[] saddleColors = {"Vörös", "Kék", "Zöld", "Fekete"};
    private final String[] outfitColors = {"Piros", "Kék", "Zöld", "Lila"};

    private int horseIndex;
    private int riderIndex;
    private int petIndex;
    private int horseColorIndex;
    private int maneColorIndex;
    private int saddleColorIndex;
    private int outfitColorIndex;

    private Label horseValue;
    private Label riderValue;
    private Label petValue;
    private Label horseColorValue;
    private Label maneColorValue;
    private Label saddleColorValue;
    private Label outfitColorValue;

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
        this.game = game;
        Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        String resolvedHorse = horseName != null ? horseName : prefs.getString(PREF_HORSE, null);
        String resolvedRider = riderName != null ? riderName : prefs.getString(PREF_RIDER, null);
        String resolvedPet = petName != null ? petName : prefs.getString(PREF_PET, null);
        String resolvedHorseColor = horseColor != null ? horseColor : prefs.getString(PREF_HORSE_COLOR, null);
        String resolvedManeColor = maneColor != null ? maneColor : prefs.getString(PREF_MANE_COLOR, null);
        String resolvedSaddleColor = saddleColor != null ? saddleColor : prefs.getString(PREF_SADDLE_COLOR, null);
        String resolvedOutfitColor = outfitColor != null ? outfitColor : prefs.getString(PREF_OUTFIT_COLOR, null);

        this.horseIndex = findIndex(horses, resolvedHorse);
        this.riderIndex = findIndex(riders, resolvedRider);
        this.petIndex = findIndex(pets, resolvedPet);
        this.horseColorIndex = findIndex(horseColors, resolvedHorseColor);
        this.maneColorIndex = findIndex(maneColors, resolvedManeColor);
        this.saddleColorIndex = findIndex(saddleColors, resolvedSaddleColor);
        this.outfitColorIndex = findIndex(outfitColors, resolvedOutfitColor);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        font = new BitmapFont();
        buttonUp = createColorTexture(new Color(0.29f, 0.6f, 0.85f, 1f));
        buttonDown = createColorTexture(new Color(0.2f, 0.48f, 0.7f, 1f));
        background = createColorTexture(new Color(0.08f, 0.2f, 0.12f, 1f));
        clickSound = game.getAssets().get("sfx/click.wav", Sound.class);
        loadHorsePreviews();
        riderPreviews = createRiderPreviews();
        petPreviews = createPetPreviews();
        horseColorSwatches = createSwatches(new Color(0.64f, 0.38f, 0.2f, 1f), new Color(0.85f, 0.72f, 0.42f, 1f), new Color(0.7f, 0.72f, 0.78f, 1f), new Color(0.25f, 0.2f, 0.15f, 1f));
        maneColorSwatches = createSwatches(new Color(0.08f, 0.08f, 0.08f, 1f), new Color(0.28f, 0.16f, 0.08f, 1f), new Color(0.5f, 0.5f, 0.55f, 1f), new Color(0.85f, 0.78f, 0.5f, 1f));
        saddleColorSwatches = createSwatches(new Color(0.65f, 0.2f, 0.2f, 1f), new Color(0.2f, 0.35f, 0.7f, 1f), new Color(0.2f, 0.55f, 0.3f, 1f), new Color(0.12f, 0.12f, 0.12f, 1f));
        outfitColorSwatches = createSwatches(new Color(0.75f, 0.2f, 0.2f, 1f), new Color(0.2f, 0.4f, 0.8f, 1f), new Color(0.2f, 0.6f, 0.35f, 1f), new Color(0.55f, 0.3f, 0.75f, 1f));

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = toDrawable(buttonUp);
        buttonStyle.down = toDrawable(buttonDown);
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;

        Label title = new Label("Karakter választás", labelStyle);
        horseValue = new Label(horses[horseIndex], labelStyle);
        riderValue = new Label(riders[riderIndex], labelStyle);
        petValue = new Label(pets[petIndex], labelStyle);
        horseColorValue = new Label(horseColors[horseColorIndex], labelStyle);
        maneColorValue = new Label(maneColors[maneColorIndex], labelStyle);
        saddleColorValue = new Label(saddleColors[saddleColorIndex], labelStyle);
        outfitColorValue = new Label(outfitColors[outfitColorIndex], labelStyle);

        horsePreviewImage = new Image(horsePreviewRegions[horseIndex]);
        riderPreviewImage = new Image(toDrawable(riderPreviews[riderIndex]));
        petPreviewImage = new Image(toDrawable(petPreviews[petIndex]));
        horseColorSwatchImage = new Image(toDrawable(horseColorSwatches[horseColorIndex]));
        maneColorSwatchImage = new Image(toDrawable(maneColorSwatches[maneColorIndex]));
        saddleColorSwatchImage = new Image(toDrawable(saddleColorSwatches[saddleColorIndex]));
        outfitColorSwatchImage = new Image(toDrawable(outfitColorSwatches[outfitColorIndex]));

        Table layout = new Table();
        layout.setFillParent(true);
        layout.pad(24f);

        layout.add(title).colspan(3).padBottom(30f);
        layout.row();

        Table previewRow = new Table();
        previewRow.add(horsePreviewImage).width(150f).height(110f).pad(6f);
        previewRow.add(riderPreviewImage).width(150f).height(110f).pad(6f);
        previewRow.add(petPreviewImage).width(150f).height(110f).pad(6f);
        layout.add(previewRow).colspan(4).padBottom(24f);
        layout.row();

        addSelectorRow(layout, "Ló", horseValue, buttonStyle, () -> updateHorse(-1), () -> updateHorse(1));
        addSelectorRow(layout, "Lószín", horseColorValue, horseColorSwatchImage, buttonStyle, () -> updateHorseColor(-1), () -> updateHorseColor(1));
        addSelectorRow(layout, "Sörény", maneColorValue, maneColorSwatchImage, buttonStyle, () -> updateManeColor(-1), () -> updateManeColor(1));
        addSelectorRow(layout, "Nyereg", saddleColorValue, saddleColorSwatchImage, buttonStyle, () -> updateSaddleColor(-1), () -> updateSaddleColor(1));
        addSelectorRow(layout, "Lovas", riderValue, buttonStyle, () -> updateRider(-1), () -> updateRider(1));
        addSelectorRow(layout, "Ruházat", outfitColorValue, outfitColorSwatchImage, buttonStyle, () -> updateOutfitColor(-1), () -> updateOutfitColor(1));
        addSelectorRow(layout, "Kis kedvenc", petValue, buttonStyle, () -> updatePet(-1), () -> updatePet(1));

        layout.row().padTop(30f);
        TextButton backButton = new TextButton("Vissza", buttonStyle);
        TextButton startButton = new TextButton("Verseny indítása", buttonStyle);

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
                    outfitColors[outfitColorIndex]
                );
                ScreenNavigator.toTrackSelect(game, selection);
            }
        });

        layout.add(backButton).width(220f).height(80f).padRight(20f);
        layout.add(startButton).width(320f).height(80f).colspan(2);

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
        if (font != null) {
            font.dispose();
        }
        if (buttonUp != null) {
            buttonUp.dispose();
        }
        if (buttonDown != null) {
            buttonDown.dispose();
        }
        if (background != null) {
            background.dispose();
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
        Label rowLabel = new Label(label, new Label.LabelStyle(font, Color.WHITE));
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
        Label rowLabel = new Label(label, new Label.LabelStyle(font, Color.WHITE));
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
        horsePreviewImage.setDrawable(new TextureRegionDrawable(horsePreviewRegions[horseIndex]));
        saveSelectionPrefs();
    }

    private void updateHorseColor(int delta) {
        horseColorIndex = wrapIndex(horseColorIndex + delta, horseColors.length);
        horseColorValue.setText(horseColors[horseColorIndex]);
        horseColorSwatchImage.setDrawable(toDrawable(horseColorSwatches[horseColorIndex]));
        saveSelectionPrefs();
    }

    private void updateManeColor(int delta) {
        maneColorIndex = wrapIndex(maneColorIndex + delta, maneColors.length);
        maneColorValue.setText(maneColors[maneColorIndex]);
        maneColorSwatchImage.setDrawable(toDrawable(maneColorSwatches[maneColorIndex]));
        saveSelectionPrefs();
    }

    private void updateSaddleColor(int delta) {
        saddleColorIndex = wrapIndex(saddleColorIndex + delta, saddleColors.length);
        saddleColorValue.setText(saddleColors[saddleColorIndex]);
        saddleColorSwatchImage.setDrawable(toDrawable(saddleColorSwatches[saddleColorIndex]));
        saveSelectionPrefs();
    }

    private void updateOutfitColor(int delta) {
        outfitColorIndex = wrapIndex(outfitColorIndex + delta, outfitColors.length);
        outfitColorValue.setText(outfitColors[outfitColorIndex]);
        outfitColorSwatchImage.setDrawable(toDrawable(outfitColorSwatches[outfitColorIndex]));
        saveSelectionPrefs();
    }

    private void updateRider(int delta) {
        riderIndex = wrapIndex(riderIndex + delta, riders.length);
        riderValue.setText(riders[riderIndex]);
        riderPreviewImage.setDrawable(toDrawable(riderPreviews[riderIndex]));
        saveSelectionPrefs();
    }

    private void updatePet(int delta) {
        petIndex = wrapIndex(petIndex + delta, pets.length);
        petValue.setText(pets[petIndex]);
        petPreviewImage.setDrawable(toDrawable(petPreviews[petIndex]));
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
    }

    private void playClick() {
        if (clickSound != null) {
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

    private Texture createColorTexture(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private void loadHorsePreviews() {
        String[] variants = {"chestnut", "bay", "gray", "palomino"};
        horseSheets = new Texture[variants.length];
        horsePreviewRegions = new TextureRegion[variants.length];
        for (int i = 0; i < variants.length; i++) {
            try {
                Texture sheet = new Texture("sprites/horse_idle_" + variants[i] + ".png");
                horseSheets[i] = sheet;
                TextureRegion[][] split = TextureRegion.split(sheet, 128, 128);
                horsePreviewRegions[i] = split[0][0];
            } catch (RuntimeException exception) {
                Texture fallback = createHorsePreview(new Color(0.65f, 0.44f, 0.3f, 1f), new Color(0.25f, 0.16f, 0.1f, 1f));
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
        Texture[] previews = new Texture[outfits.length];
        for (int i = 0; i < outfits.length; i++) {
            previews[i] = createRiderPreview(outfits[i], hair[i]);
        }
        return previews;
    }

    private Texture[] createPetPreviews() {
        Color[] pets = {
            new Color(0.85f, 0.65f, 0.4f, 1f),
            new Color(0.6f, 0.6f, 0.65f, 1f),
            new Color(0.95f, 0.9f, 0.75f, 1f),
            new Color(0.2f, 0.75f, 0.45f, 1f)
        };
        Texture[] previews = new Texture[pets.length];
        for (int i = 0; i < pets.length; i++) {
            previews[i] = createPetPreview(pets[i]);
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

    private Texture createHorsePreview(Color body, Color mane) {
        Pixmap pixmap = createPreviewPanel();
        pixmap.setColor(body);
        pixmap.fillRectangle(26, 48, 86, 32);
        pixmap.fillRectangle(32, 32, 14, 20);
        pixmap.fillRectangle(62, 32, 14, 20);
        pixmap.fillRectangle(94, 32, 14, 20);
        pixmap.fillCircle(126, 64, 16);
        pixmap.setColor(mane);
        pixmap.fillRectangle(108, 78, 24, 8);
        pixmap.fillRectangle(36, 78, 18, 10);
        return finalizePreviewTexture(pixmap);
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

    private Drawable toDrawable(Texture texture) {
        return new TextureRegionDrawable(texture);
    }
}
