package com.yourstudio.horse.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public final class UiFactory {
    private UiFactory() {
    }

    public static Label label(String text, Label.LabelStyle style) {
        return new Label(text, style);
    }

    public static TextButton button(String text, TextButton.TextButtonStyle style, Runnable onClick) {
        TextButton button = new TextButton(text, style);
        if (onClick != null) {
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    onClick.run();
                }
            });
        }
        return button;
    }
}
