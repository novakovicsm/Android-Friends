
package com.yourstudio.horse;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PreviewImageGenerator extends ApplicationAdapter {
    private Stage stage;
    private Texture horseTexture, riderTexture, petTexture;
    private Image horse, rider, pet;

    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());
        horseTexture = new Texture(Gdx.files.internal("sprites/horse_idle_bay.png"));
        riderTexture = new Texture(Gdx.files.internal("ui/panel_logo.png"));
        petTexture = new Texture(Gdx.files.internal("ui/panel_menu.png"));
        horse = new Image(horseTexture);
        rider = new Image(riderTexture);
        pet = new Image(petTexture);
        horse.setPosition(0, 0);
        rider.setPosition(200, 0);
        pet.setPosition(400, 0);
        stage.addActor(horse);
        stage.addActor(rider);
        stage.addActor(pet);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        horseTexture.dispose();
        riderTexture.dispose();
        petTexture.dispose();
    }

    // Desktop launcher is now in the desktop module.
}
