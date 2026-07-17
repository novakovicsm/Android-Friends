package com.yourstudio.horse.model;

import com.badlogic.gdx.Preferences;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MvpProgressStoreTest {
    @Test
    public void loadReturnsDefaultsWhenPreferencesAreEmpty() {
        MvpProgressStore store = new MvpProgressStore(new FakePreferences());

        MvpProgress progress = store.load();

        assertEquals("Vill\u00e1m", progress.selectedHorse);
        assertEquals("Peti", progress.selectedRiderName);
        assertEquals("Kutya", progress.selectedPet);
        assertEquals(MvpGameConfig.Difficulty.EASY, progress.selectedDifficulty);
        assertEquals(0, progress.upgradeLevels[0]);
        assertEquals(0, progress.selectedSkinIndex);
        assertTrue(progress.unlockedSkins[0]);
        assertTrue(progress.unlockedPets[0]);
        assertEquals(1, progress.playerLevel);
    }

    @Test
    public void saveAndLoadRoundTripsProgress() {
        FakePreferences preferences = new FakePreferences();
        MvpProgressStore store = new MvpProgressStore(preferences);
        MvpProgress progress = MvpProgress.newGame();
        progress.horseshoes = 12;
        progress.playerXp = 38;
        progress.playerLevel = 4;
        progress.petXp = 200;
        progress.petLevel = 3;
        progress.selectedSkinIndex = 2;
        progress.upgradeLevels[0] = 2;
        progress.upgradeLevels[3] = 1;
        progress.unlockedSkins[2] = true;
        progress.unlockedPets[1] = true;
        progress.selectedHorse = "Pihe";
        progress.selectedRiderName = "Szandi";
        progress.selectedPet = "Kutya";
        progress.selectedRiderColor = "K\u00e9k";
        progress.selectedDifficulty = MvpGameConfig.Difficulty.HARD;
        progress.recordTime = "01:23";
        progress.tutorialComplete = true;
        progress.muted = true;

        store.save(progress);
        MvpProgress loaded = store.load();

        assertEquals(12, loaded.horseshoes);
        assertEquals(38, loaded.playerXp);
        assertEquals(4, loaded.playerLevel);
        assertEquals(200, loaded.petXp);
        assertEquals(3, loaded.petLevel);
        assertEquals(2, loaded.selectedSkinIndex);
        assertEquals(2, loaded.upgradeLevels[0]);
        assertEquals(1, loaded.upgradeLevels[3]);
        assertTrue(loaded.unlockedSkins[0]);
        assertTrue(loaded.unlockedSkins[2]);
        assertTrue(loaded.unlockedPets[0]);
        assertTrue(loaded.unlockedPets[1]);
        assertEquals("Pihe", loaded.selectedHorse);
        assertEquals("Szandi", loaded.selectedRiderName);
        assertEquals("Kutya", loaded.selectedPet);
        assertEquals("K\u00e9k", loaded.selectedRiderColor);
        assertEquals(MvpGameConfig.Difficulty.HARD, loaded.selectedDifficulty);
        assertEquals("01:23", loaded.recordTime);
        assertTrue(loaded.tutorialComplete);
        assertTrue(loaded.muted);
        assertTrue(preferences.flushed);
    }

    @Test
    public void loadSanitizesInvalidCharacterSelections() {
        FakePreferences preferences = new FakePreferences();
        preferences.putString("selectedHorse", "ismeretlen");
        preferences.putString("selectedRiderName", "  Túl hosszú lovasnév  ");
        preferences.putString("selectedRiderColor", "  Kék  ");
        MvpProgressStore store = new MvpProgressStore(preferences);

        MvpProgress loaded = store.load();

        assertEquals("Villám", loaded.selectedHorse);
        assertEquals("Túl hosszú lovas", loaded.selectedRiderName);
        assertEquals("Kék", loaded.selectedRiderColor);
    }

    @Test
    public void loadFallsBackWhenSelectedSkinIsLocked() {
        FakePreferences preferences = new FakePreferences();
        preferences.putInteger("selectedSkinIndex", 2);
        preferences.putString("unlockedSkins", "1,0,0,0");
        MvpProgressStore store = new MvpProgressStore(preferences);

        MvpProgress loaded = store.load();

        assertEquals(0, loaded.selectedSkinIndex);
    }

    @Test
    public void loadFallsBackWhenSelectedPetIsLocked() {
        FakePreferences preferences = new FakePreferences();
        preferences.putString("selectedPet", "Cica");
        preferences.putString("unlockedPets", "1,0,0,0");
        MvpProgressStore store = new MvpProgressStore(preferences);

        MvpProgress loaded = store.load();

        assertEquals("Kutya", loaded.selectedPet);
    }

    private static final class FakePreferences implements Preferences {
        private final Map<String, Object> values = new HashMap<>();
        private boolean flushed;

        @Override
        public Preferences putBoolean(String key, boolean val) {
            values.put(key, val);
            return this;
        }

        @Override
        public Preferences putInteger(String key, int val) {
            values.put(key, val);
            return this;
        }

        @Override
        public Preferences putLong(String key, long val) {
            values.put(key, val);
            return this;
        }

        @Override
        public Preferences putFloat(String key, float val) {
            values.put(key, val);
            return this;
        }

        @Override
        public Preferences putString(String key, String val) {
            values.put(key, val);
            return this;
        }

        @Override
        public Preferences put(Map<String, ?> vals) {
            values.putAll(vals);
            return this;
        }

        @Override
        public boolean getBoolean(String key) {
            return getBoolean(key, false);
        }

        @Override
        public int getInteger(String key) {
            return getInteger(key, 0);
        }

        @Override
        public long getLong(String key) {
            return getLong(key, 0L);
        }

        @Override
        public float getFloat(String key) {
            return getFloat(key, 0f);
        }

        @Override
        public String getString(String key) {
            return getString(key, "");
        }

        @Override
        public boolean getBoolean(String key, boolean defValue) {
            Object value = values.get(key);
            return value instanceof Boolean ? (Boolean) value : defValue;
        }

        @Override
        public int getInteger(String key, int defValue) {
            Object value = values.get(key);
            return value instanceof Integer ? (Integer) value : defValue;
        }

        @Override
        public long getLong(String key, long defValue) {
            Object value = values.get(key);
            return value instanceof Long ? (Long) value : defValue;
        }

        @Override
        public float getFloat(String key, float defValue) {
            Object value = values.get(key);
            return value instanceof Float ? (Float) value : defValue;
        }

        @Override
        public String getString(String key, String defValue) {
            Object value = values.get(key);
            return value instanceof String ? (String) value : defValue;
        }

        @Override
        public Map<String, ?> get() {
            return values;
        }

        @Override
        public boolean contains(String key) {
            return values.containsKey(key);
        }

        @Override
        public void clear() {
            values.clear();
        }

        @Override
        public void remove(String key) {
            values.remove(key);
        }

        @Override
        public void flush() {
            flushed = true;
        }
    }
}
