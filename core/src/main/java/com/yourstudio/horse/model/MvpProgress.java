package com.yourstudio.horse.model;

public final class MvpProgress {
    public int horseshoes;
    public int playerXp;
    public int playerLevel;
    public int petXp;
    public int petLevel;
    public int[] upgradeLevels;
    public boolean[] unlockedSkins;
    public boolean[] unlockedPets;
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
        progress.upgradeLevels = new int[MvpGameConfig.UPGRADE_CATEGORIES.length];
        progress.unlockedSkins = new boolean[MvpGameConfig.SKIN_LABELS.length];
        progress.unlockedSkins[0] = true;
        progress.unlockedPets = new boolean[MvpGameConfig.PET_LABELS.length];
        progress.unlockedPets[0] = true;
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

    public boolean purchaseUpgrade(int categoryIndex) {
        if (categoryIndex < 0 || categoryIndex >= MvpGameConfig.UPGRADE_CATEGORIES.length) {
            throw new IllegalArgumentException("Upgrade category index is out of range.");
        }
        ensureUpgradeLevels();
        int currentLevel = upgradeLevels[categoryIndex];
        int maxLevel = MvpGameConfig.UPGRADE_CATEGORIES[categoryIndex].upgradeCount;
        if (currentLevel >= maxLevel) {
            return false;
        }
        int upgradeNumber = nextUpgradeNumber(categoryIndex, currentLevel);
        int cost = MvpGameConfig.upgradeCost(upgradeNumber);
        if (horseshoes < cost) {
            return false;
        }
        horseshoes -= cost;
        upgradeLevels[categoryIndex] = currentLevel + 1;
        return true;
    }

    public boolean purchaseSkin(int skinIndex) {
        if (skinIndex < 0 || skinIndex >= MvpGameConfig.SKIN_LABELS.length) {
            throw new IllegalArgumentException("Skin index is out of range.");
        }
        ensureUnlockedSkins();
        if (unlockedSkins[skinIndex]) {
            return false;
        }
        int cost = MvpGameConfig.skinPrice(skinIndex);
        if (horseshoes < cost) {
            return false;
        }
        horseshoes -= cost;
        unlockedSkins[skinIndex] = true;
        return true;
    }

    public boolean purchasePet(int petIndex) {
        if (petIndex < 0 || petIndex >= MvpGameConfig.PET_LABELS.length) {
            throw new IllegalArgumentException("Pet index is out of range.");
        }
        ensureUnlockedPets();
        if (unlockedPets[petIndex]) {
            return false;
        }
        if (horseshoes < MvpGameConfig.PET_UNLOCK_PRICE) {
            return false;
        }
        horseshoes -= MvpGameConfig.PET_UNLOCK_PRICE;
        unlockedPets[petIndex] = true;
        return true;
    }

    private void ensureUpgradeLevels() {
        if (upgradeLevels == null || upgradeLevels.length != MvpGameConfig.UPGRADE_CATEGORIES.length) {
            upgradeLevels = new int[MvpGameConfig.UPGRADE_CATEGORIES.length];
        }
    }

    private void ensureUnlockedSkins() {
        if (unlockedSkins == null || unlockedSkins.length != MvpGameConfig.SKIN_LABELS.length) {
            boolean[] oldSkins = unlockedSkins;
            unlockedSkins = new boolean[MvpGameConfig.SKIN_LABELS.length];
            unlockedSkins[0] = true;
            if (oldSkins != null) {
                for (int i = 0; i < unlockedSkins.length && i < oldSkins.length; i++) {
                    unlockedSkins[i] = unlockedSkins[i] || oldSkins[i];
                }
            }
        }
    }

    private void ensureUnlockedPets() {
        if (unlockedPets == null || unlockedPets.length != MvpGameConfig.PET_LABELS.length) {
            boolean[] oldPets = unlockedPets;
            unlockedPets = new boolean[MvpGameConfig.PET_LABELS.length];
            unlockedPets[0] = true;
            if (oldPets != null) {
                for (int i = 0; i < unlockedPets.length && i < oldPets.length; i++) {
                    unlockedPets[i] = unlockedPets[i] || oldPets[i];
                }
            }
        }
    }

    private int nextUpgradeNumber(int categoryIndex, int currentLevel) {
        int upgradeNumber = currentLevel + 1;
        for (int i = 0; i < categoryIndex; i++) {
            upgradeNumber += MvpGameConfig.UPGRADE_CATEGORIES[i].upgradeCount;
        }
        return upgradeNumber;
    }
}
