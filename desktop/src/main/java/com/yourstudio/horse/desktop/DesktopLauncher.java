package com.yourstudio.horse.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.yourstudio.horse.HorseGame;

public class DesktopLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Android Friends");
        config.setWindowedMode(1280, 720);
        new Lwjgl3Application(new HorseGame(), config);
    }
}
