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

public class RaceScreen extends ScreenAdapter {
    private final HorseGame game;
    private final String horseName;
    private final String riderName;
    private final String petName;
    private final String trackName;

    private Stage stage;
    private Texture background;
    private BitmapFont font;
    private Texture buttonUp;
    private Texture buttonDown;
    private Texture hudPanel;
    private Label speedLabel;
    private Label lapLabel;
    private Label powerupLabel;
    private float elapsedTime;
    private int currentLap = 1;

    public RaceScreen(HorseGame game, String horseName, String riderName, String petName) {
        this.game = game;
        this.horseName = horseName;
        this.riderName = riderName;
        this.petName = petName;
        this.trackName = "Ismeretlen";
    }
    
    public RaceScreen(HorseGame game, String horseName, String riderName, String petName, String trackName) {
        this.game = game;
        this.horseName = horseName;
        this.riderName = riderName;
        this.petName = petName;
        this.trackName = trackName;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        font = new BitmapFont();
        buttonUp = createColorTexture(new Color(0.29f, 0.6f, 0.85f, 1f));
        buttonDown = createColorTexture(new Color(0.2f, 0.48f, 0.7f, 1f));
        background = createColorTexture(new Color(0.2f, 0.12f, 0.08f, 1f));
        hudPanel = createPanelTexture(new Color(0.12f, 0.12f, 0.16f, 0.85f), new Color(0.35f, 0.35f, 0.45f, 1f), 260, 130);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = toDrawable(buttonUp);
        buttonStyle.down = toDrawable(buttonDown);
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;

        Label title = new Label("Verseny (placeholder)", labelStyle);
        Label selection = new Label("Ló: " + horseName + " | Lovas: " + riderName + " | Kedvenc: " + petName, labelStyle);
        Label trackLabel = new Label("Pálya: " + trackName, labelStyle);
        TextButton backButton = new TextButton("Vissza", buttonStyle);

        speedLabel = new Label("Sebesség: 0 km/h", labelStyle);
        lapLabel = new Label("Kör: 1/3", labelStyle);
        powerupLabel = new Label("Power-up: --", labelStyle);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new CharacterSelectScreen(game, horseName, riderName, petName));
            }
        });

        Table hudTable = new Table();
        hudTable.setFillParent(true);
        hudTable.top().left().pad(16f);
        Table hudContent = new Table();
        hudContent.setBackground(toDrawable(hudPanel));
        hudContent.pad(12f);
        hudContent.add(speedLabel).left().row();
        hudContent.add(lapLabel).left().padTop(6f).row();
        hudContent.add(powerupLabel).left().padTop(6f);
        hudTable.add(hudContent);

        Table layout = new Table();
        layout.setFillParent(true);
        layout.pad(24f);
        layout.add(title).padBottom(20f);
        layout.row();
        layout.add(selection).padBottom(30f);
        layout.row();
        layout.add(trackLabel).padBottom(30f);
        layout.row();
        layout.add(backButton).width(220f).height(80f);

        stage.addActor(layout);
        stage.addActor(hudTable);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        elapsedTime += delta;
        float speed = 22f + 12f * (float) Math.abs(Math.sin(elapsedTime * 0.8f));
        int lap = 1 + ((int) (elapsedTime / 18f) % 3);
        if (lap != currentLap) {
            currentLap = lap;
        }
        String[] powerups = {"Boost", "Pajzs", "Villám", "---"};
        String powerup = powerups[(int) (elapsedTime / 5f) % powerups.length];
        speedLabel.setText("Sebesség: " + (int) speed + " km/h");
        lapLabel.setText("Kör: " + currentLap + "/3");
        powerupLabel.setText("Power-up: " + powerup);
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
        if (hudPanel != null) {
            hudPanel.dispose();
        }
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

    private Texture createPanelTexture(Color fillColor, Color borderColor, int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(fillColor);
        pixmap.fill();
        pixmap.setColor(borderColor);
        for (int i = 0; i < 3; i++) {
            pixmap.drawRectangle(i, i, width - (i * 2), height - (i * 2));
        }
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }
}
