package com.yourstudio.horse;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PreviewImageGenerator {
    public static void main(String[] args) {
        // This is a mockup for review purposes only.
        // In a real libGDX app, you would use an ApplicationListener.
        Stage stage = new Stage(new ScreenViewport());
        Image horse = new Image(new Texture(Gdx.files.internal("sprites/horse_idle_bay.png")));
        Image rider = new Image(new Texture(Gdx.files.internal("ui/panel_logo.png")));
        Image pet = new Image(new Texture(Gdx.files.internal("ui/panel_menu.png")));
        horse.setPosition(0, 0);
        rider.setPosition(200, 0);
        pet.setPosition(400, 0);
        stage.addActor(horse);
        stage.addActor(rider);
        stage.addActor(pet);
        // Render logic would go here
    }
}
