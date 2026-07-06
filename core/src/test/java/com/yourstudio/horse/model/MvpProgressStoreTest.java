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
        progress.selectedHorse = "Pihe";
        progress.selectedRiderName = "Szandi";
        progress.selectedPet = "Kutya";
        progress.selectedRiderColor = "K\u00e9k";
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
        assertEquals("Pihe", loaded.selectedHorse);
        assertEquals("Szandi", loaded.selectedRiderName);
        assertEquals("Kutya", loaded.selectedPet);
        assertEquals("K\u00e9k", loaded.selectedRiderColor);
        assertEquals("01:23", loaded.recordTime);
        assertTrue(loaded.tutorialComplete);
        assertTrue(loaded.muted);
        assertTrue(preferences.flushed);
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
