package com.yourstudio.horse.screens;

/** Small state holder for the two-step rider -> horse selection flow. */
public final class CharacterSelectionFlow {
    public enum Step {
        RIDER,
        HORSE
    }

    private Step step = Step.RIDER;

    public Step getStep() {
        return step;
    }

    public boolean isRiderStep() {
        return step == Step.RIDER;
    }

    public boolean isHorseStep() {
        return step == Step.HORSE;
    }

    public void next() {
        step = Step.HORSE;
    }

    public void back() {
        step = Step.RIDER;
    }

    public String stepLabel() {
        return isRiderStep() ? "1/2 Lovas" : "2/2 Lo";
    }
}
