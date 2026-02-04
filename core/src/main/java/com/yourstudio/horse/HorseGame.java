package com.yourstudio.horse;

import com.badlogic.gdx.Game;
import com.yourstudio.horse.screens.MainMenuScreen;

public class HorseGame extends Game {
    @Override
    public void create() {
        setScreen(new MainMenuScreen(this));
    }
}
