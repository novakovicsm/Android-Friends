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
}
