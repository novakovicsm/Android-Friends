package com.yourstudio.horse.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class MvpProgressTest {
    @Test
    public void newGameUsesMvpDefaults() {
        MvpProgress progress = MvpProgress.newGame();

        assertEquals(0, progress.horseshoes);
        assertEquals(0, progress.playerXp);
        assertEquals(1, progress.playerLevel);
        assertEquals(0, progress.petXp);
        assertEquals(1, progress.petLevel);
        assertEquals(MvpGameConfig.UPGRADE_CATEGORIES.length, progress.upgradeLevels.length);
        assertEquals(0, progress.upgradeLevels[0]);
        assertEquals(MvpGameConfig.SKIN_LABELS.length, progress.unlockedSkins.length);
        assertEquals(true, progress.unlockedSkins[0]);
        assertFalse(progress.unlockedSkins[1]);
        assertEquals("Vill\u00e1m", progress.selectedHorse);
        assertEquals("Peti", progress.selectedRiderName);
        assertEquals("Kutya", progress.selectedPet);
        assertEquals(MvpGameConfig.Difficulty.EASY, progress.selectedDifficulty);
        assertFalse(progress.tutorialComplete);
        assertFalse(progress.muted);
    }

    @Test
    public void applyRaceResultAddsRewardsXpAndPetXp() {
        MvpProgress progress = MvpProgress.newGame();

        progress.applyRaceResult(1, MvpGameConfig.Difficulty.HARD, true);

        assertEquals(10, progress.horseshoes);
        assertEquals(38, progress.playerXp);
        assertEquals(4, progress.playerLevel);
        assertEquals(30, progress.petXp);
        assertEquals(1, progress.petLevel);
    }

    @Test
    public void petLevelCapsAtMvpMaximum() {
        assertEquals(10, MvpProgress.calculatePetLevel(5000));
    }

    @Test
    public void playerLevelUsesConfiguredThresholds() {
        assertEquals(1, MvpProgress.calculatePlayerLevel(0));
        assertEquals(1, MvpProgress.calculatePlayerLevel(10));
        assertEquals(2, MvpProgress.calculatePlayerLevel(15));
        assertEquals(3, MvpProgress.calculatePlayerLevel(23));
        assertEquals(4, MvpProgress.calculatePlayerLevel(35));
    }

    @Test
    public void purchaseUpgradeSpendsHorseshoesAndIncreasesCategoryLevel() {
        MvpProgress progress = MvpProgress.newGame();
        progress.horseshoes = 25;

        assertEquals(true, progress.purchaseUpgrade(0));
        assertEquals(15, progress.horseshoes);
        assertEquals(1, progress.upgradeLevels[0]);

        assertEquals(true, progress.purchaseUpgrade(0));
        assertEquals(5, progress.horseshoes);
        assertEquals(2, progress.upgradeLevels[0]);

        assertEquals(false, progress.purchaseUpgrade(0));
        assertEquals(5, progress.horseshoes);
        assertEquals(2, progress.upgradeLevels[0]);
    }

    @Test
    public void purchaseSkinSpendsHorseshoesAndUnlocksOnce() {
        MvpProgress progress = MvpProgress.newGame();
        progress.horseshoes = 20;

        assertEquals(true, progress.purchaseSkin(1));
        assertEquals(10, progress.horseshoes);
        assertEquals(true, progress.unlockedSkins[1]);

        assertEquals(false, progress.purchaseSkin(1));
        assertEquals(10, progress.horseshoes);
    }
}
