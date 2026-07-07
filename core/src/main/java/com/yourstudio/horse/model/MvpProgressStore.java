package com.yourstudio.horse.model;

import com.badlogic.gdx.Preferences;

public final class MvpProgressStore {
    public static final String PREFS_NAME = "versenylovak_mvp_progress";

    private static final String KEY_HORSESHOES = "horseshoes";
    private static final String KEY_PLAYER_XP = "playerXp";
    private static final String KEY_PLAYER_LEVEL = "playerLevel";
    private static final String KEY_PET_XP = "petXp";
    private static final String KEY_PET_LEVEL = "petLevel";
    private static final String KEY_SELECTED_SKIN_INDEX = "selectedSkinIndex";
    private static final String KEY_UPGRADE_LEVELS = "upgradeLevels";
    private static final String KEY_UNLOCKED_SKINS = "unlockedSkins";
    private static final String KEY_UNLOCKED_PETS = "unlockedPets";
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
        progress.upgradeLevels = parseUpgradeLevels(preferences.getString(KEY_UPGRADE_LEVELS, ""));
        progress.unlockedSkins = parseUnlockedSkins(preferences.getString(KEY_UNLOCKED_SKINS, ""));
        progress.selectedSkinIndex = safeSelectedSkinIndex(
            preferences.getInteger(KEY_SELECTED_SKIN_INDEX, defaults.selectedSkinIndex),
            progress.unlockedSkins
        );
        progress.unlockedPets = parseUnlockedPets(preferences.getString(KEY_UNLOCKED_PETS, ""));
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
        preferences.putInteger(KEY_SELECTED_SKIN_INDEX, safeSelectedSkinIndex(progress.selectedSkinIndex, progress.unlockedSkins));
        preferences.putString(KEY_UPGRADE_LEVELS, formatUpgradeLevels(progress.upgradeLevels));
        preferences.putString(KEY_UNLOCKED_SKINS, formatUnlockedSkins(progress.unlockedSkins));
        preferences.putString(KEY_UNLOCKED_PETS, formatUnlockedPets(progress.unlockedPets));
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

    private int[] parseUpgradeLevels(String value) {
        int[] levels = new int[MvpGameConfig.UPGRADE_CATEGORIES.length];
        if (value == null || value.length() == 0) {
            return levels;
        }
        String[] parts = value.split(",");
        for (int i = 0; i < levels.length && i < parts.length; i++) {
            try {
                int parsed = Integer.parseInt(parts[i]);
                int maxLevel = MvpGameConfig.UPGRADE_CATEGORIES[i].upgradeCount;
                levels[i] = Math.max(0, Math.min(parsed, maxLevel));
            } catch (NumberFormatException ignored) {
                levels[i] = 0;
            }
        }
        return levels;
    }

    private String formatUpgradeLevels(int[] levels) {
        int[] safeLevels = levels != null ? levels : new int[0];
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < MvpGameConfig.UPGRADE_CATEGORIES.length; i++) {
            if (i > 0) {
                builder.append(',');
            }
            int level = i < safeLevels.length ? safeLevels[i] : 0;
            int maxLevel = MvpGameConfig.UPGRADE_CATEGORIES[i].upgradeCount;
            builder.append(Math.max(0, Math.min(level, maxLevel)));
        }
        return builder.toString();
    }

    private int safeSelectedSkinIndex(int skinIndex, boolean[] unlockedSkins) {
        if (skinIndex < 0 || skinIndex >= MvpGameConfig.SKIN_LABELS.length) {
            return 0;
        }
        if (unlockedSkins == null || skinIndex >= unlockedSkins.length || !unlockedSkins[skinIndex]) {
            return 0;
        }
        return skinIndex;
    }

    private boolean[] parseUnlockedSkins(String value) {
        boolean[] skins = new boolean[MvpGameConfig.SKIN_LABELS.length];
        skins[0] = true;
        if (value == null || value.length() == 0) {
            return skins;
        }
        String[] parts = value.split(",");
        for (int i = 0; i < skins.length && i < parts.length; i++) {
            skins[i] = "1".equals(parts[i]) || "true".equalsIgnoreCase(parts[i]);
        }
        skins[0] = true;
        return skins;
    }

    private String formatUnlockedSkins(boolean[] skins) {
        boolean[] safeSkins = skins != null ? skins : new boolean[0];
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < MvpGameConfig.SKIN_LABELS.length; i++) {
            if (i > 0) {
                builder.append(',');
            }
            boolean unlocked = i == 0 || (i < safeSkins.length && safeSkins[i]);
            builder.append(unlocked ? '1' : '0');
        }
        return builder.toString();
    }

    private boolean[] parseUnlockedPets(String value) {
        boolean[] pets = new boolean[MvpGameConfig.PET_LABELS.length];
        pets[0] = true;
        if (value == null || value.length() == 0) {
            return pets;
        }
        String[] parts = value.split(",");
        for (int i = 0; i < pets.length && i < parts.length; i++) {
            pets[i] = "1".equals(parts[i]) || "true".equalsIgnoreCase(parts[i]);
        }
        pets[0] = true;
        return pets;
    }

    private String formatUnlockedPets(boolean[] pets) {
        boolean[] safePets = pets != null ? pets : new boolean[0];
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < MvpGameConfig.PET_LABELS.length; i++) {
            if (i > 0) {
                builder.append(',');
            }
            boolean unlocked = i == 0 || (i < safePets.length && safePets[i]);
            builder.append(unlocked ? '1' : '0');
        }
        return builder.toString();
    }
}
