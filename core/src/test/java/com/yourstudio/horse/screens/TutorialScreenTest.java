package com.yourstudio.horse.screens;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TutorialScreenTest {
    @Test
    public void tutorialTextCoversCurrentMvpMechanics() {
        String text = TutorialScreen.tutorialBodyText();

        assertTrue(text.contains("Ugr\u00E1s"));
        assertTrue(text.contains("+20% boost"));
        assertTrue(text.contains("4 ellenf\u00E9l"));
        assertTrue(text.contains("dobog\u00F3"));
        assertTrue(text.contains("\u00DAj futam"));
    }
}
