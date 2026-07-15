package com.yourstudio.horse.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.yourstudio.horse.HorseGame;
import com.yourstudio.horse.model.MvpProgress;
import com.yourstudio.horse.model.MvpProgressStore;
import com.yourstudio.horse.ui.ScreenNavigator;

public class TutorialScreen extends ScreenAdapter {
    private final HorseGame game;
    private Stage stage;
    private Sound clickSound;
    private MvpProgress progress;
    private MvpProgressStore store;

    public TutorialScreen(HorseGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Skin skin = game.getSkin();
        clickSound = game.getAssets().get("sfx/click.wav", Sound.class);
        store = new MvpProgressStore(Gdx.app.getPreferences(MvpProgressStore.PREFS_NAME));
        progress = store.load();
        Label.LabelStyle titleStyle = skin.get("title", Label.LabelStyle.class);
        Label.LabelStyle labelStyle = skin.get("default", Label.LabelStyle.class);
        TextButton.TextButtonStyle buttonStyle = skin.get("primary", TextButton.TextButtonStyle.class);

        Table layout = new Table();
        layout.setFillParent(true);
        layout.pad(34f);

        Label title = new Label("Gyors gyakorl\u00E1s", titleStyle);
        title.setAlignment(Align.center);
        Label body = new Label(tutorialBodyText(), labelStyle);
        body.setAlignment(Align.center);
        body.setWrap(true);

        TextButton doneButton = new TextButton("Rendben", buttonStyle);
        doneButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playClick();
                progress.tutorialComplete = true;
                store.save(progress);
                ScreenNavigator.toMainMenu(game);
            }
        });

        layout.add(title).padBottom(24f).row();
        layout.add(body).width(560f).padBottom(28f).row();
        layout.add(doneButton).width(260f).height(72f);
        stage.addActor(layout);
        Gdx.input.setInputProcessor(stage);
    }

    private void playClick() {
        if (!progress.muted && clickSound != null) {
            clickSound.play(0.6f);
        }
    }

    static String tutorialBodyText() {
        return "Mozg\u00E1s: h\u00FAzd a joystickot.\n"
            + "Ugr\u00E1s: nyomd meg az Ugr\u00E1s gombot az akad\u00E1lyokn\u00E1l.\n"
            + "Akad\u00E1ly: ha nem ugrasz, a l\u00F3 r\u00F6vid id\u0151re lassul.\n"
            + "Power-up: a s\u00E1rga b\u00F3nusz +20% boost t\u00F6ltetet ad.\n"
            + "Boost: ha van t\u00F6ltet, haszn\u00E1ld a Boost gombot.\n"
            + "Helyez\u00E9s: te \u00E9s 4 ellenf\u00E9l futtok, a dobog\u00F3 a c\u00E9lban l\u00E1tszik.\n"
            + "Jutalom: 3 k\u00F6r ut\u00E1n XP-t \u00E9s aranypatk\u00F3t kapsz.\n"
            + "\u00DAj futam: a c\u00E9l ut\u00E1n r\u00F6gt\u00F6n ind\u00EDthatsz m\u00E1sik versenyt.\n"
            + "Ist\u00E1ll\u00F3: az aranypatk\u00F3b\u00F3l skint, petet \u00E9s upgrade-et vehetsz.";
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.valueOf("f7fbff"));
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
    }
}
