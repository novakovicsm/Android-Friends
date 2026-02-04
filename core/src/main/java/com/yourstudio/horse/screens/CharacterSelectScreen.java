package com.yourstudio.horse.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
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

public class CharacterSelectScreen extends ScreenAdapter {
    private final HorseGame game;
    private Stage stage;
    private Texture background;
    private BitmapFont font;
    private Texture buttonUp;
    private Texture buttonDown;

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
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        font = new BitmapFont();
        buttonUp = createColorTexture(new Color(0.29f, 0.6f, 0.85f, 1f));
        buttonDown = createColorTexture(new Color(0.2f, 0.48f, 0.7f, 1f));
        background = createColorTexture(new Color(0.08f, 0.2f, 0.12f, 1f));

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

        Table layout = new Table();
        layout.setFillParent(true);
        layout.pad(24f);

        layout.add(title).colspan(3).padBottom(30f);
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
                game.setScreen(new RaceScreen(game, horses[horseIndex], riders[riderIndex], pets[petIndex]));
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
    }

    private void updateRider(int delta) {
        riderIndex = wrapIndex(riderIndex + delta, riders.length);
        riderValue.setText(riders[riderIndex]);
    }

    private void updatePet(int delta) {
        petIndex = wrapIndex(petIndex + delta, pets.length);
        petValue.setText(pets[petIndex]);
    }

    private int wrapIndex(int value, int size) {
        int result = value % size;
        return result < 0 ? result + size : result;
    }

    private Texture createColorTexture(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private Drawable toDrawable(Texture texture) {
        return new TextureRegionDrawable(texture);
    }
}
