package com.yourstudio.horse.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

public class CharacterSelectScreen extends ScreenAdapter {
    private final HorseGame game;
    private Stage stage;
    private Texture background;
    private BitmapFont font;
    private Texture buttonUp;
    private Texture buttonDown;
    private Texture[] horsePreviews;
    private Texture[] riderPreviews;
    private Texture[] petPreviews;
    private Image horsePreviewImage;
    private Image riderPreviewImage;
    private Image petPreviewImage;

    private final String[] horses = {"Villám", "Pihe", "Csillag", "Futó"};
    private final String[] riders = {"Lili", "Noel", "Mira", "Áron"};
    private final String[] pets = {"Kutya", "Cica", "Nyuszi", "Papagáj"};

    private int horseIndex;
    private int riderIndex;
    private int petIndex;

    private Label horseValue;
    private Label riderValue;
    private Label petValue;

    public CharacterSelectScreen(HorseGame game) {
        this(game, null, null, null);
    }

    public CharacterSelectScreen(HorseGame game, String horseName, String riderName, String petName) {
        this.game = game;
        this.horseIndex = findIndex(horses, horseName);
        this.riderIndex = findIndex(riders, riderName);
        this.petIndex = findIndex(pets, petName);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        font = new BitmapFont();
        buttonUp = createColorTexture(new Color(0.29f, 0.6f, 0.85f, 1f));
        buttonDown = createColorTexture(new Color(0.2f, 0.48f, 0.7f, 1f));
        background = createColorTexture(new Color(0.08f, 0.2f, 0.12f, 1f));
        horsePreviews = createHorsePreviews();
        riderPreviews = createRiderPreviews();
        petPreviews = createPetPreviews();

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

        horsePreviewImage = new Image(toDrawable(horsePreviews[horseIndex]));
        riderPreviewImage = new Image(toDrawable(riderPreviews[riderIndex]));
        petPreviewImage = new Image(toDrawable(petPreviews[petIndex]));

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
        addSelectorRow(layout, "Lovas", riderValue, buttonStyle, () -> updateRider(-1), () -> updateRider(1));
        addSelectorRow(layout, "Kis kedvenc", petValue, buttonStyle, () -> updatePet(-1), () -> updatePet(1));

        layout.row().padTop(30f);
        TextButton backButton = new TextButton("Vissza", buttonStyle);
        TextButton startButton = new TextButton("Verseny indítása", buttonStyle);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TrackSelectScreen(game, horses[horseIndex], riders[riderIndex], pets[petIndex]));
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
        disposeTextureArray(horsePreviews);
        disposeTextureArray(riderPreviews);
        disposeTextureArray(petPreviews);
    }

    private void addSelectorRow(Table layout, String label, Label valueLabel, TextButton.TextButtonStyle buttonStyle,
                                Runnable previousAction, Runnable nextAction) {
        Label rowLabel = new Label(label, new Label.LabelStyle(font, Color.WHITE));
        TextButton prevButton = new TextButton("<", buttonStyle);
        TextButton nextButton = new TextButton(">", buttonStyle);

        prevButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                previousAction.run();
            }
        });
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nextAction.run();
            }
        });

        layout.add(rowLabel).left().padBottom(18f);
        layout.add(prevButton).width(70f).height(60f).padBottom(18f);
        layout.add(valueLabel).width(220f).padBottom(18f).padLeft(10f).padRight(10f);
        layout.add(nextButton).width(70f).height(60f).padBottom(18f);
        layout.row();
    }

    private void updateHorse(int delta) {
        horseIndex = wrapIndex(horseIndex + delta, horses.length);
        horseValue.setText(horses[horseIndex]);
        horsePreviewImage.setDrawable(toDrawable(horsePreviews[horseIndex]));
    }

    private void updateRider(int delta) {
        riderIndex = wrapIndex(riderIndex + delta, riders.length);
        riderValue.setText(riders[riderIndex]);
        riderPreviewImage.setDrawable(toDrawable(riderPreviews[riderIndex]));
    }

    private void updatePet(int delta) {
        petIndex = wrapIndex(petIndex + delta, pets.length);
        petValue.setText(pets[petIndex]);
        petPreviewImage.setDrawable(toDrawable(petPreviews[petIndex]));
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

    private Texture[] createHorsePreviews() {
        Color[] bodies = {
            new Color(0.65f, 0.44f, 0.3f, 1f),
            new Color(0.78f, 0.72f, 0.6f, 1f),
            new Color(0.35f, 0.35f, 0.42f, 1f),
            new Color(0.9f, 0.5f, 0.25f, 1f)
        };
        Color[] manes = {
            new Color(0.25f, 0.16f, 0.1f, 1f),
            new Color(0.5f, 0.35f, 0.2f, 1f),
            new Color(0.2f, 0.2f, 0.25f, 1f),
            new Color(0.55f, 0.2f, 0.08f, 1f)
        };
        Texture[] previews = new Texture[bodies.length];
        for (int i = 0; i < bodies.length; i++) {
            previews[i] = createHorsePreview(bodies[i], manes[i]);
        }
        return previews;
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
