package com.yourstudio.horse.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.yourstudio.horse.HorseGame;

public class DesktopLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Android Friends");
        config.setWindowedMode(1280, 720);
        // Allow running PreviewImageGenerator for preview purposes
        try {
            Class<?> previewClass = Class.forName("com.yourstudio.horse.PreviewImageGenerator");
            if (System.getProperty("preview") != null) {
                new Lwjgl3Application((com.badlogic.gdx.ApplicationListener) previewClass.getDeclaredConstructor().newInstance(), config);
                return;
            }
        } catch (Exception ignored) {}
        new Lwjgl3Application(new HorseGame(), config);
    }
}
