package com.yourstudio.horse.screens;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RaceScreenTest {
                    @Test
                    public void testPetSpeedBonusField() {
                        setField(raceScreen, "petSpeedBonus", 5f);
                        assertEquals(5f, getFloatField(raceScreen, "petSpeedBonus"), 0.01f);
                    }

                    @Test
                    public void testCollectCoinWithZeroMultiplier() {
                        setPetCoinMultiplier(0f);
                        int before = getPlayerCoins();
                        raceScreen.collectCoin(10);
                        int after = getPlayerCoins();
                        assertEquals(before, after);
                    }

                    @Test
                    public void testCollectCoinWithNegativeMultiplier() {
                        setPetCoinMultiplier(-1f);
                        int before = getPlayerCoins();
                        raceScreen.collectCoin(10);
                        int after = getPlayerCoins();
                        assertTrue(after < before);
                    }
                @Test
                public void testJoystickInputAffectsHorsePosition() {
                    setField(raceScreen, "joystickX", 1f);
                    setField(raceScreen, "joystickY", 0.5f);
                    setField(raceScreen, "speed", 10f);
                    float beforeX = getFloatField(raceScreen, "horseX");
                    float beforeY = getFloatField(raceScreen, "horseY");
                    invokeRender(raceScreen, 0.1f);
                    float afterX = getFloatField(raceScreen, "horseX");
                    float afterY = getFloatField(raceScreen, "horseY");
                    assertTrue(afterX > beforeX);
                    assertTrue(afterY > beforeY);
                }

                @Test
                public void testLapChangeEdgeCase() {
                    setField(raceScreen, "distance", 900f); // 3 laps
                    setField(raceScreen, "lapDistance", 300f);
                    setField(raceScreen, "currentLap", 1);
                    invokeRender(raceScreen, 0.1f);
                    int lap = getIntField(raceScreen, "currentLap");
                    assertEquals(3, lap);
                }
            @Test
            public void testSpeedLabelUpdates() {
                setField(raceScreen, "speed", 42f);
                invokeShow(raceScreen);
                invokeRender(raceScreen, 0.1f);
                String text = getLabelText(raceScreen, "speedLabel");
                assertTrue(text.contains("42"));
            }

            @Test
            public void testLapLabelUpdates() {
                setField(raceScreen, "currentLap", 2);
                invokeShow(raceScreen);
                invokeRender(raceScreen, 0.1f);
                String text = getLabelText(raceScreen, "lapLabel");
                assertTrue(text.contains("2"));
            }

            @Test
            public void testPowerupLabelUpdates() {
                setField(raceScreen, "activePowerupName", "SpeedBoost");
                setField(raceScreen, "activePowerupTimer", 5f);
                invokeShow(raceScreen);
                invokeRender(raceScreen, 0.1f);
                String text = getLabelText(raceScreen, "powerupLabel");
                assertTrue(text.contains("SpeedBoost"));
            }

            @Test
            public void testCoinLabelUpdates() {
                setPlayerCoins(99);
                invokeShow(raceScreen);
                invokeRender(raceScreen, 0.1f);
                String text = getLabelText(raceScreen, "coinLabel");
                assertTrue(text.contains("99"));
            }

            // Helper to get label text
            private String getLabelText(Object obj, String fieldName) {
                try {
                    java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Object label = field.get(obj);
                    if (label == null) return "";
                    java.lang.reflect.Method getText = label.getClass().getMethod("getText");
                    Object text = getText.invoke(label);
                    return text.toString();
                } catch (Exception e) {
                    return "";
                }
            }
        @Test
        public void testConstructorSetsNames() {
            RaceScreen screen = new RaceScreen(null, "Pej", "Noel", "Cica");
            assertEquals("Pej", getField(screen, "horseName"));
            assertEquals("Noel", getField(screen, "riderName"));
            assertEquals("Cica", getField(screen, "petName"));
        }

        @Test
        public void testKapibaraPetSetsCoinMultiplier() {
            RaceScreen screen = new RaceScreen(null, "Pej", "Noel", "Kapibara");
            invokeShow(screen);
            float multiplier = getFloatField(screen, "petCoinMultiplier");
            assertEquals(2.0f, multiplier, 0.01f);
        }

        @Test
        public void testLajharPetSetsPowerupMultiplier() {
            RaceScreen screen = new RaceScreen(null, "Pej", "Noel", "Lajhár");
            invokeShow(screen);
            float multiplier = getFloatField(screen, "petPowerupDurationMultiplier");
            assertEquals(1.5f, multiplier, 0.01f);
        }

        @Test
        public void testLapChangeLogic() {
            setField(raceScreen, "distance", 600f); // 2 laps
            setField(raceScreen, "lapDistance", 300f);
            setField(raceScreen, "currentLap", 1);
            invokeRender(raceScreen, 0.1f);
            int lap = getIntField(raceScreen, "currentLap");
            assertEquals(3, lap);
        }

        // Reflection helpers
        private Object getField(Object obj, String fieldName) {
            try {
                java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        private float getFloatField(Object obj, String fieldName) {
            try {
                java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.getFloat(obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        private int getIntField(Object obj, String fieldName) {
            try {
                java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.getInt(obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        private void setField(Object obj, String fieldName, Object value) {
            try {
                java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                if (value instanceof Float) field.setFloat(obj, (Float)value);
                else if (value instanceof Integer) field.setInt(obj, (Integer)value);
                else field.set(obj, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        private void invokeShow(Object obj) {
            try {
                java.lang.reflect.Method method = obj.getClass().getDeclaredMethod("show");
                method.setAccessible(true);
                method.invoke(obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        private void invokeRender(Object obj, float delta) {
            try {
                java.lang.reflect.Method method = obj.getClass().getDeclaredMethod("render", float.class);
                method.setAccessible(true);
                method.invoke(obj, delta);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    private RaceScreen raceScreen;

    @Before
    public void setUp() {
        // You may need to mock HorseGame and other dependencies for real tests
        raceScreen = new RaceScreen(null, "Gesztenye", "Lili", "Kutya");
    }

    @Test
    public void testInitialCoinCountIsZero() {
        assertEquals(0, getPlayerCoins());
    }

    @Test
    public void testCollectCoinIncreasesCoinCount() {
        int before = getPlayerCoins();
        raceScreen.collectCoin(5);
        int after = getPlayerCoins();
        assertTrue(after > before);
    }

    @Test
    public void testPetCoinMultiplierAffectsCoinCollection() {
        setPetCoinMultiplier(2.0f);
        int before = getPlayerCoins();
        raceScreen.collectCoin(10);
        int after = getPlayerCoins();
        assertEquals(before + 20, after);
    }

    @Test
    public void testUpdateCoinLabelSetsCorrectText() {
        setPlayerCoins(42);
        invokeUpdateCoinLabel();
        String labelText = getCoinLabelText();
        assertTrue(labelText.contains("42"));
    }

    // Helper to access private petCoinMultiplier
    private void setPetCoinMultiplier(float multiplier) {
        try {
            java.lang.reflect.Field field = RaceScreen.class.getDeclaredField("petCoinMultiplier");
            field.setAccessible(true);
            field.set(raceScreen, multiplier);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper to set playerCoins
    private void setPlayerCoins(int coins) {
        try {
            java.lang.reflect.Field field = RaceScreen.class.getDeclaredField("playerCoins");
            field.setAccessible(true);
            field.setInt(raceScreen, coins);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper to invoke updateCoinLabel
    private void invokeUpdateCoinLabel() {
        try {
            java.lang.reflect.Method method = RaceScreen.class.getDeclaredMethod("updateCoinLabel");
            method.setAccessible(true);
            method.invoke(raceScreen);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper to get coinLabel text
    private String getCoinLabelText() {
        try {
            java.lang.reflect.Field field = RaceScreen.class.getDeclaredField("coinLabel");
            field.setAccessible(true);
            Object label = field.get(raceScreen);
            if (label == null) return "";
            java.lang.reflect.Method getText = label.getClass().getMethod("getText");
            Object text = getText.invoke(label);
            return text.toString();
        } catch (Exception e) {
            return "";
        }
    }

    // Helper to access private playerCoins (reflection, for demonstration)
    private int getPlayerCoins() {
        try {
            java.lang.reflect.Field field = RaceScreen.class.getDeclaredField("playerCoins");
            field.setAccessible(true);
            return field.getInt(raceScreen);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
