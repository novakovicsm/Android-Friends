package com.yourstudio.horse.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MvpGameConfigTest {
    @Test
    public void hasTenPresetRiderNames() {
        assertEquals(10, MvpGameConfig.RIDER_NAMES.length);
    }

    @Test
    public void createsFourDeterministicNpcNamesFromSeed() {
        String[] first = MvpGameConfig.npcNamesForSeed(7L);
        String[] second = MvpGameConfig.npcNamesForSeed(7L);

        assertEquals(MvpGameConfig.NPC_COUNT, first.length);
        assertEquals(first[0], second[0]);
        assertEquals(first[1], second[1]);
        assertEquals(first[2], second[2]);
        assertEquals(first[3], second[3]);
    }

    @Test
    public void defaultTrackStartsTheForestRace() {
        assertEquals("forest.tmx", MvpGameConfig.DEFAULT_TRACK);
    }

    @Test
    public void hasFourHorseProfiles() {
        assertEquals(4, MvpGameConfig.HORSES.length);
    }

    @Test
    public void horseProfilesExposeMvpStatBars() {
        assertEquals(5, MvpGameConfig.HORSES[0].speed);
        assertEquals(5, MvpGameConfig.HORSES[1].turning);
        assertEquals(5, MvpGameConfig.HORSES[2].boost);
        assertEquals(5, MvpGameConfig.HORSES[3].acceleration);
    }

    @Test
    public void riderBonusesAlternateBetweenAccelerationAndBoostCharge() {
        assertEquals(MvpGameConfig.RiderBonusType.ACCELERATION, MvpGameConfig.riderBonusForIndex(0).type);
        assertEquals(0.01f, MvpGameConfig.riderBonusForIndex(0).value, 0.0001f);
        assertEquals(MvpGameConfig.RiderBonusType.BOOST_CHARGE, MvpGameConfig.riderBonusForIndex(1).type);
        assertEquals(0.01f, MvpGameConfig.riderBonusForIndex(1).value, 0.0001f);
        assertEquals(MvpGameConfig.RiderBonusType.ACCELERATION, MvpGameConfig.riderBonusForIndex(2).type);
    }

    @Test
    public void hasFourForestObstacles() {
        assertEquals(4, MvpGameConfig.FOREST_OBSTACLES.length);
    }

    @Test
    public void obstaclesSlowWithoutPointPenaltyInMvp() {
        assertEquals(0.55f, MvpGameConfig.OBSTACLE_SLOWDOWN_MULTIPLIER, 0.0001f);
        assertEquals(1.2f, MvpGameConfig.OBSTACLE_SLOWDOWN_SECONDS, 0.0001f);
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
    public void skinPricesMatchMvpSpec() {
        assertEquals(4, MvpGameConfig.SKIN_LABELS.length);
        assertEquals(4, MvpGameConfig.SKIN_HORSE_COLORS.length);
        assertEquals(5, MvpGameConfig.skinPrice(0));
        assertEquals(10, MvpGameConfig.skinPrice(1));
        assertEquals(15, MvpGameConfig.skinPrice(2));
        assertEquals(20, MvpGameConfig.skinPrice(3));
        assertEquals("Meleg barna", MvpGameConfig.skinHorseColor(0));
        assertEquals("Arany", MvpGameConfig.skinHorseColor(1));
    }

    @Test
    public void petAndPowerupValuesMatchMvpSpec() {
        assertEquals(10, MvpGameConfig.MAX_PET_LEVEL);
        assertEquals(100, MvpGameConfig.PET_XP_PER_LEVEL);
        assertEquals(20, MvpGameConfig.PET_UNLOCK_PRICE);
        assertEquals(4, MvpGameConfig.PET_LABELS.length);
        assertEquals(20, MvpGameConfig.BOOST_POWERUP_CHARGE_PERCENT);
        assertEquals(20, MvpGameConfig.BOOST_ACTIVATION_COST_PERCENT);
        assertEquals(1.1f, MvpGameConfig.BOOST_ACTIVE_SECONDS, 0.0001f);
        assertEquals(1.35f, MvpGameConfig.BOOST_SPEED_MULTIPLIER, 0.0001f);
    }
}
