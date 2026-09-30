package com.yourstudio.horse.screens;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CharacterSelectionFlowTest {
    @Test
    public void startsOnRiderStep() {
        CharacterSelectionFlow flow = new CharacterSelectionFlow();
        assertTrue(flow.isRiderStep());
        assertEquals("1/2 Lovas", flow.stepLabel());
    }

    @Test
    public void nextMovesToHorseStep() {
        CharacterSelectionFlow flow = new CharacterSelectionFlow();
        flow.next();
        assertTrue(flow.isHorseStep());
        assertEquals("2/2 Lo", flow.stepLabel());
    }

    @Test
    public void backReturnsToRiderStep() {
        CharacterSelectionFlow flow = new CharacterSelectionFlow();
        flow.next();
        flow.back();
        assertTrue(flow.isRiderStep());
    }
}
