package com.yourstudio.horse.model;

import com.badlogic.gdx.Preferences;

public final class MvpProgressStore {
    public static final String PREFS_NAME = "versenylovak_mvp_progress";

    private static final String KEY_HORSESHOES = "horseshoes";
    private static final String KEY_PLAYER_XP = "playerXp";
    private static final String KEY_PLAYER_LEVEL = "playerLevel";
    private static final String KEY_PET_XP = "petXp";
    private static final String KEY_PET_LEVEL = "petLevel";
    private static final String KEY_SELECTED_HORSE = "selectedHorse";
    private static final String KEY_SELECTED_RIDER_NAME = "selectedRiderName";
    private static final String KEY_SELECTED_PET = "selectedPet";
    private static final String KEY_SELECTED_RIDER_COLOR = "selectedRiderColor";
    private static final String KEY_SELECTED_DIFFICULTY = "selectedDifficulty";
    private static final String KEY_RECORD_TIME = "recordTime";
    private static final String KEY_TUTORIAL_COMPLETE = "tutorialComplete";
    private static final String KEY_MUTED = "muted";

    private final Preferences preferences;

    public MvpProgressStore(Preferences preferences) {
        this.preferences = preferences;
    }

    public MvpProgress load() {
        MvpProgress defaults = MvpProgress.newGame();
        MvpProgress progress = new MvpProgress();
        progress.horseshoes = preferences.getInteger(KEY_HORSESHOES, defaults.horseshoes);
        progress.playerXp = preferences.getInteger(KEY_PLAYER_XP, defaults.playerXp);
        progress.playerLevel = preferences.getInteger(KEY_PLAYER_LEVEL, defaults.playerLevel);
        progress.petXp = preferences.getInteger(KEY_PET_XP, defaults.petXp);
        progress.petLevel = preferences.getInteger(KEY_PET_LEVEL, defaults.petLevel);
        progress.selectedHorse = preferences.getString(KEY_SELECTED_HORSE, defaults.selectedHorse);
        progress.selectedRiderName = preferences.getString(KEY_SELECTED_RIDER_NAME, defaults.selectedRiderName);
        progress.selectedPet = preferences.getString(KEY_SELECTED_PET, defaults.selectedPet);
        progress.selectedRiderColor = preferences.getString(KEY_SELECTED_RIDER_COLOR, defaults.selectedRiderColor);
        progress.selectedDifficulty = difficultyFromName(
            preferences.getString(KEY_SELECTED_DIFFICULTY, defaults.selectedDifficulty.name()),
            defaults.selectedDifficulty
        );
        progress.recordTime = preferences.getString(KEY_RECORD_TIME, defaults.recordTime);
        progress.tutorialComplete = preferences.getBoolean(KEY_TUTORIAL_COMPLETE, defaults.tutorialComplete);
        progress.muted = preferences.getBoolean(KEY_MUTED, defaults.muted);
        return progress;
    }

    public void save(MvpProgress progress) {
        preferences.putInteger(KEY_HORSESHOES, progress.horseshoes);
        preferences.putInteger(KEY_PLAYER_XP, progress.playerXp);
        preferences.putInteger(KEY_PLAYER_LEVEL, progress.playerLevel);
        preferences.putInteger(KEY_PET_XP, progress.petXp);
        preferences.putInteger(KEY_PET_LEVEL, progress.petLevel);
        preferences.putString(KEY_SELECTED_HORSE, progress.selectedHorse);
        preferences.putString(KEY_SELECTED_RIDER_NAME, progress.selectedRiderName);
        preferences.putString(KEY_SELECTED_PET, progress.selectedPet);
        preferences.putString(KEY_SELECTED_RIDER_COLOR, progress.selectedRiderColor);
        MvpGameConfig.Difficulty difficulty = progress.selectedDifficulty != null
            ? progress.selectedDifficulty
            : MvpGameConfig.Difficulty.EASY;
        preferences.putString(KEY_SELECTED_DIFFICULTY, difficulty.name());
        preferences.putString(KEY_RECORD_TIME, progress.recordTime);
        preferences.putBoolean(KEY_TUTORIAL_COMPLETE, progress.tutorialComplete);
        preferences.putBoolean(KEY_MUTED, progress.muted);
        preferences.flush();
    }

    private MvpGameConfig.Difficulty difficultyFromName(String name, MvpGameConfig.Difficulty fallback) {
        try {
            return MvpGameConfig.Difficulty.valueOf(name);
        } catch (IllegalArgumentException exception) {
            return fallback;
        }
    }
}
