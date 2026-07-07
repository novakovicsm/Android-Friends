package com.yourstudio.horse.model;

public final class MvpProgress {
    public int horseshoes;
    public int playerXp;
    public int playerLevel;
    public int petXp;
    public int petLevel;
    public String selectedHorse;
    public String selectedRiderName;
    public String selectedPet;
    public String selectedRiderColor;
    public MvpGameConfig.Difficulty selectedDifficulty;
    public String recordTime;
    public boolean tutorialComplete;
    public boolean muted;

    public static MvpProgress newGame() {
        MvpProgress progress = new MvpProgress();
        progress.horseshoes = 0;
        progress.playerXp = 0;
        progress.playerLevel = 1;
        progress.petXp = 0;
        progress.petLevel = 1;
        progress.selectedHorse = MvpGameConfig.HORSES[0].name;
        progress.selectedRiderName = MvpGameConfig.RIDER_NAMES[0];
        progress.selectedPet = "Kutya";
        progress.selectedRiderColor = "Piros";
        progress.selectedDifficulty = MvpGameConfig.Difficulty.EASY;
        progress.recordTime = "";
        progress.tutorialComplete = false;
        progress.muted = false;
        return progress;
    }

    public void applyRaceResult(int placement, MvpGameConfig.Difficulty difficulty, boolean recordBroken) {
        horseshoes += MvpGameConfig.horseshoeReward(placement, difficulty);
        playerXp += MvpGameConfig.raceXp(placement, difficulty, recordBroken);
        playerLevel = calculatePlayerLevel(playerXp);
        petXp += difficulty.baseXp;
        petLevel = calculatePetLevel(petXp);
    }

    public static int calculatePlayerLevel(int xp) {
        int level = 1;
        for (int candidate = 1; candidate <= MvpGameConfig.MAX_PLAYER_LEVEL; candidate++) {
            if (xp >= MvpGameConfig.playerLevelXpRequirement(candidate)) {
                level = candidate;
            }
        }
        return level;
    }

    public static int calculatePetLevel(int xp) {
        int level = 1 + (xp / MvpGameConfig.PET_XP_PER_LEVEL);
        if (level < 1) {
            return 1;
        }
        return Math.min(level, MvpGameConfig.MAX_PET_LEVEL);
    }
}
