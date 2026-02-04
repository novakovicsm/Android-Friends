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

    private Stage stage;
    private Texture background;
    private BitmapFont font;
    private Texture buttonUp;
    private Texture buttonDown;

    public RaceScreen(HorseGame game, String horseName, String riderName, String petName) {
        this.game = game;
        this.horseName = horseName;
        this.riderName = riderName;
        this.petName = petName;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        font = new BitmapFont();
        buttonUp = createColorTexture(new Color(0.29f, 0.6f, 0.85f, 1f));
        buttonDown = createColorTexture(new Color(0.2f, 0.48f, 0.7f, 1f));
        background = createColorTexture(new Color(0.2f, 0.12f, 0.08f, 1f));

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = toDrawable(buttonUp);
        buttonStyle.down = toDrawable(buttonDown);
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;

        Label title = new Label("Verseny (placeholder)", labelStyle);
        Label selection = new Label("Ló: " + horseName + " | Lovas: " + riderName + " | Kedvenc: " + petName, labelStyle);
        TextButton backButton = new TextButton("Vissza", buttonStyle);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new CharacterSelectScreen(game));
            }
        });

        Table layout = new Table();
        layout.setFillParent(true);
        layout.pad(24f);
        layout.add(title).padBottom(20f);
        layout.row();
        layout.add(selection).padBottom(30f);
        layout.row();
        layout.add(backButton).width(220f).height(80f);

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
