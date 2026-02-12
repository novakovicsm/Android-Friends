package com.yourstudio.horse.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.yourstudio.horse.PreviewImageGenerator;

public class PreviewImageGeneratorLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Preview Image Generator");
        config.setWindowedMode(800, 480);
        new Lwjgl3Application(new PreviewImageGenerator(), config);
    }
}
