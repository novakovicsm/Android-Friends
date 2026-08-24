package com.yourstudio.horse.ui;

import com.yourstudio.horse.HorseGame;
import com.yourstudio.horse.model.MvpGameConfig;
import com.yourstudio.horse.screens.CharacterSelectScreen;
import com.yourstudio.horse.screens.MainMenuScreen;
import com.yourstudio.horse.screens.RaceScreen;
import com.yourstudio.horse.screens.ShopScreen;
import com.yourstudio.horse.screens.TrackSelectScreen;
import com.yourstudio.horse.screens.TutorialScreen;

public final class ScreenNavigator {
    private ScreenNavigator() {
    }

    public static void toMainMenu(HorseGame game) {
        game.setScreen(new MainMenuScreen(game));
    }

    public static void toCharacterSelect(HorseGame game, Selection selection) {
        if (selection == null) {
            game.setScreen(new CharacterSelectScreen(game));
            return;
        }
        game.setScreen(new CharacterSelectScreen(game, selection.horseName, selection.riderName, selection.petName,
            selection.horseColor, selection.maneColor, selection.saddleColor, selection.outfitColor,
            selection.difficulty));
    }

    public static void toShop(HorseGame game) {
        game.setScreen(new ShopScreen(game));
    }

    public static void toTutorial(HorseGame game) {
        game.setScreen(new TutorialScreen(game));
    }

    public static void toTrackSelect(HorseGame game, Selection selection) {
        if (selection == null) {
            game.setScreen(new TrackSelectScreen(game, null, null, null));
            return;
        }
        game.setScreen(new TrackSelectScreen(game, selection.horseName, selection.riderName, selection.petName,
            selection.horseColor, selection.maneColor, selection.saddleColor, selection.outfitColor));
    }

    public static void toDefaultRace(HorseGame game, Selection selection) {
        toRace(game, selection, MvpGameConfig.DEFAULT_TRACK);
    }

    public static void toRace(HorseGame game, Selection selection, String trackName) {
        if (selection == null) {
            game.setScreen(new RaceScreen(game, null, null, null, trackName));
            return;
        }
        game.setScreen(new RaceScreen(game, selection.horseName, selection.riderName, selection.petName, trackName,
            selection.horseColor, selection.maneColor, selection.saddleColor, selection.outfitColor,
            selection.difficulty));
    }

    public static final class Selection {
        public final String horseName;
        public final String riderName;
        public final String petName;
        public final String horseColor;
        public final String maneColor;
        public final String saddleColor;
        public final String outfitColor;
        public final MvpGameConfig.Difficulty difficulty;

        public Selection(String horseName, String riderName, String petName,
                         String horseColor, String maneColor, String saddleColor, String outfitColor) {
            this(horseName, riderName, petName, horseColor, maneColor, saddleColor, outfitColor,
                MvpGameConfig.Difficulty.EASY);
        }

        public Selection(String horseName, String riderName, String petName,
                         String horseColor, String maneColor, String saddleColor, String outfitColor,
                         MvpGameConfig.Difficulty difficulty) {
            this.horseName = horseName;
            this.riderName = riderName;
            this.petName = petName;
            this.horseColor = horseColor;
            this.maneColor = maneColor;
            this.saddleColor = saddleColor;
            this.outfitColor = outfitColor;
            this.difficulty = difficulty != null ? difficulty : MvpGameConfig.Difficulty.EASY;
        }
    }
}
