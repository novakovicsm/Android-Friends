package com.yourstudio.horse.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MvpGameConfigTest {
    @Test
    public void hasTenPresetRiderNames() {
        assertEquals(10, MvpGameConfig.RIDER_NAMES.length);
    }

    @Test
    public void hasFourHorseProfiles() {
        assertEquals(4, MvpGameConfig.HORSES.length);
    }

    @Test
    public void hasFourForestObstacles() {
        assertEquals(4, MvpGameConfig.FOREST_OBSTACLES.length);
    }

    @Test
    public void hasFiveUpgradeCategoriesWithTwoUpgradesEach() {
        assertEquals(5, MvpGameConfig.UPGRADE_CATEGORIES.length);
        for (MvpGameConfig.UpgradeCategory category : MvpGameConfig.UPGRADE_CATEGORIES) {
            assertEquals(2, category.upgradeCount);
        }
    }

    @Test
    public void placementHorseshoeRewardsMatchMvpSpec() {
        assertEquals(10, MvpGameConfig.placementHorseshoeReward(1));
        assertEquals(7, MvpGameConfig.placementHorseshoeReward(2));
        assertEquals(5, MvpGameConfig.placementHorseshoeReward(3));
        assertEquals(3, MvpGameConfig.placementHorseshoeReward(4));
        assertEquals(1, MvpGameConfig.placementHorseshoeReward(5));
    }

    @Test
    public void horseshoeRewardsUseDifficultyMultipliers() {
        assertEquals(3, MvpGameConfig.horseshoeReward(1, MvpGameConfig.Difficulty.EASY));
        assertEquals(6, MvpGameConfig.horseshoeReward(1, MvpGameConfig.Difficulty.MEDIUM));
        assertEquals(10, MvpGameConfig.horseshoeReward(1, MvpGameConfig.Difficulty.HARD));
    }

    @Test
    public void raceXpUsesDifficultyPlacementRecordAndParticipation() {
        assertEquals(32, MvpGameConfig.raceXp(1, MvpGameConfig.Difficulty.HARD, false));
        assertEquals(38, MvpGameConfig.raceXp(1, MvpGameConfig.Difficulty.HARD, true));
        assertEquals(20, MvpGameConfig.raceXp(3, MvpGameConfig.Difficulty.HARD, false));
    }

    @Test
    public void playerLevelRequirementsMatchMvpSpec() {
        assertEquals(10, MvpGameConfig.playerLevelXpRequirement(1));
        assertEquals(15, MvpGameConfig.playerLevelXpRequirement(2));
        assertEquals(23, MvpGameConfig.playerLevelXpRequirement(3));
        assertEquals(35, MvpGameConfig.playerLevelXpRequirement(4));
    }

    @Test
    public void upgradeCostsMatchMvpSpec() {
        assertEquals(10, MvpGameConfig.upgradeCost(1));
        assertEquals(10, MvpGameConfig.upgradeCost(3));
        assertEquals(15, MvpGameConfig.upgradeCost(4));
        assertEquals(15, MvpGameConfig.upgradeCost(6));
        assertEquals(20, MvpGameConfig.upgradeCost(7));
        assertEquals(20, MvpGameConfig.upgradeCost(10));
    }

    @Test
    public void petAndPowerupValuesMatchMvpSpec() {
        assertEquals(10, MvpGameConfig.MAX_PET_LEVEL);
        assertEquals(100, MvpGameConfig.PET_XP_PER_LEVEL);
        assertEquals(20, MvpGameConfig.BOOST_POWERUP_CHARGE_PERCENT);
    }
}
