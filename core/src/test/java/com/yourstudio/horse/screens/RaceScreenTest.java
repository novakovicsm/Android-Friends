package com.yourstudio.horse.screens;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import com.yourstudio.horse.model.MvpProgress;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RaceScreenTest {
    private RaceScreen raceScreen;

    @Before
    public void setUp() {
        raceScreen = new RaceScreen(null, "Villam", "Lili", "Kutya");
    }

    @Test
    public void constructorStoresSelectionNames() {
        RaceScreen screen = new RaceScreen(null, "Pihe", "Peti", "Cica");

        assertEquals("Pihe", getObjectField(screen, "horseName"));
        assertEquals("Peti", getObjectField(screen, "riderName"));
        assertEquals("Cica", getObjectField(screen, "petName"));
    }

    @Test
    public void coinCollectionUsesPetMultiplier() {
        setFloatField(raceScreen, "petCoinMultiplier", 2f);

        raceScreen.collectCoin(10);

        assertEquals(20, getIntField(raceScreen, "playerCoins"));
    }

    @Test
    public void coinCollectionAllowsZeroMultiplier() {
        setFloatField(raceScreen, "petCoinMultiplier", 0f);

        raceScreen.collectCoin(10);

        assertEquals(0, getIntField(raceScreen, "playerCoins"));
    }

    @Test
    public void dogPetHasNoMvpBonus() {
        invokePrivate(raceScreen, "applyPetBonus");

        assertEquals(0f, getFloatField(raceScreen, "petSpeedBonus"), 0.01f);
        assertEquals(0f, getFloatField(raceScreen, "petAccelBonus"), 0.01f);
        assertEquals(0f, getFloatField(raceScreen, "petShieldBonus"), 0.01f);
    }

    @Test
    public void catPetAppliesAccelerationBonus() {
        RaceScreen screen = new RaceScreen(null, "Villam", "Lili", "Cica");

        invokePrivate(screen, "applyPetBonus");

        assertEquals(5f, getFloatField(screen, "petAccelBonus"), 0.01f);
    }

    @Test
    public void bunnyPetAppliesSpeedAndAccelerationBonus() {
        RaceScreen screen = new RaceScreen(null, "Villam", "Lili", "Nyuszi");

        invokePrivate(screen, "applyPetBonus");

        assertEquals(3f, getFloatField(screen, "petSpeedBonus"), 0.01f);
        assertEquals(3f, getFloatField(screen, "petAccelBonus"), 0.01f);
    }

    @Test
    public void riderBonusAlternatesByRiderIndex() {
        setIntField(raceScreen, "riderIndex", 1);

        invokePrivate(raceScreen, "applyRiderBonus");

        assertEquals(0f, getFloatField(raceScreen, "riderAccelerationBonus"), 0.01f);
        assertTrue(getFloatField(raceScreen, "riderBoostChargeBonus") > 0f);
    }

    @Test
    public void selectedSkinProvidesFallbackHorseColor() {
        MvpProgress progress = MvpProgress.newGame();
        progress.selectedSkinIndex = 1;

        Object resolved = invokePrivate(raceScreen, "resolveHorseColor",
            new Class<?>[] {String.class, MvpProgress.class},
            new Object[] {null, progress});

        assertEquals("Arany", resolved);
    }

    @Test
    public void explicitHorseColorOverridesSelectedSkin() {
        MvpProgress progress = MvpProgress.newGame();

        Object resolved = invokePrivate(raceScreen, "resolveHorseColor",
            new Class<?>[] {String.class, MvpProgress.class},
            new Object[] {"Hamvas", progress});

        assertEquals("Hamvas", resolved);
    }

    @Test
    public void purchasedSkinOverridesExplicitHorseColor() {
        MvpProgress progress = MvpProgress.newGame();
        progress.selectedSkinIndex = 1;

        Object resolved = invokePrivate(raceScreen, "resolveHorseColor",
            new Class<?>[] {String.class, MvpProgress.class},
            new Object[] {"Hamvas", progress});

        assertEquals("Arany", resolved);
    }

    @Test
    public void obstacleMarkersFollowMvpObstacleOrder() {
        assertEquals(0, invokePrivateInt(raceScreen, "obstacleMarkerIndex",
            new Class<?>[] {String.class}, new Object[] {"kidolt_fa"}));
        assertEquals(1, invokePrivateInt(raceScreen, "obstacleMarkerIndex",
            new Class<?>[] {String.class}, new Object[] {"kerites"}));
        assertEquals(2, invokePrivateInt(raceScreen, "obstacleMarkerIndex",
            new Class<?>[] {String.class}, new Object[] {"folyo"}));
        assertEquals(3, invokePrivateInt(raceScreen, "obstacleMarkerIndex",
            new Class<?>[] {String.class}, new Object[] {"pocsolya"}));
        assertEquals(-1, invokePrivateInt(raceScreen, "obstacleMarkerIndex",
            new Class<?>[] {String.class}, new Object[] {"ismeretlen"}));
    }

    private void invokePrivate(Object target, String methodName) {
        invokePrivate(target, methodName, new Class<?>[0], new Object[0]);
    }

    private Object invokePrivate(Object target, String methodName, Class<?>[] parameterTypes, Object[] args) {
        try {
            Method method = target.getClass().getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method.invoke(target, args);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private int invokePrivateInt(Object target, String methodName, Class<?>[] parameterTypes, Object[] args) {
        return (Integer) invokePrivate(target, methodName, parameterTypes, args);
    }

    private Object getObjectField(Object target, String fieldName) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private float getFloatField(Object target, String fieldName) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getFloat(target);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private int getIntField(Object target, String fieldName) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getInt(target);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private void setFloatField(Object target, String fieldName, float value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.setFloat(target, value);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private void setIntField(Object target, String fieldName, int value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.setInt(target, value);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
