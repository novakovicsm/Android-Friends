package com.yourstudio.horse.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
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
import com.yourstudio.horse.model.MvpGameConfig;
import com.yourstudio.horse.model.MvpProgress;
import com.yourstudio.horse.model.MvpProgressStore;
import com.yourstudio.horse.ui.ScreenNavigator;

public class ShopScreen extends ScreenAdapter {
    private final HorseGame game;
    private Stage stage;
    private MvpProgressStore progressStore;
    private MvpProgress progress;
    private Label horseshoeLabel;
    private final Label[] upgradeLabels = new Label[MvpGameConfig.UPGRADE_CATEGORIES.length];

    public ShopScreen(HorseGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Skin skin = game.getSkin();
        progressStore = new MvpProgressStore(Gdx.app.getPreferences(MvpProgressStore.PREFS_NAME));
        progress = progressStore.load();

        Label.LabelStyle titleStyle = skin.get("title", Label.LabelStyle.class);
        Label.LabelStyle labelStyle = skin.get("default", Label.LabelStyle.class);
        TextButton.TextButtonStyle buttonStyle = skin.get("primary", TextButton.TextButtonStyle.class);

        Table layout = new Table();
        layout.setFillParent(true);
        layout.pad(28f);

        Label title = new Label("Ist\u00E1ll\u00F3 fejleszt\u00E9sek", titleStyle);
        title.setAlignment(Align.center);
        horseshoeLabel = new Label("", labelStyle);
        horseshoeLabel.setAlignment(Align.center);

        layout.add(title).colspan(3).padBottom(18f).row();
        layout.add(horseshoeLabel).colspan(3).padBottom(20f).row();

        for (int i = 0; i < MvpGameConfig.UPGRADE_CATEGORIES.length; i++) {
            final int categoryIndex = i;
            upgradeLabels[i] = new Label("", labelStyle);
            TextButton buyButton = new TextButton("V\u00E1s\u00E1rl\u00E1s", buttonStyle);
            buyButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (progress.purchaseUpgrade(categoryIndex)) {
                        progressStore.save(progress);
                        refreshLabels();
                    }
                }
            });

            layout.add(upgradeLabels[i]).width(360f).left().padBottom(12f);
            layout.add(buyButton).width(220f).height(58f).padBottom(12f).row();
        }

        TextButton backButton = new TextButton("Vissza", buttonStyle);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenNavigator.toMainMenu(game);
            }
        });

        layout.add(backButton).width(260f).height(70f).colspan(3).padTop(16f);
        stage.addActor(layout);
        Gdx.input.setInputProcessor(stage);
        refreshLabels();
    }

    private void refreshLabels() {
        horseshoeLabel.setText("Aranypatk\u00F3: " + progress.horseshoes);
        for (int i = 0; i < MvpGameConfig.UPGRADE_CATEGORIES.length; i++) {
            MvpGameConfig.UpgradeCategory category = MvpGameConfig.UPGRADE_CATEGORIES[i];
            int level = progress.upgradeLevels[i];
            String costText = level >= category.upgradeCount
                ? "max"
                : MvpGameConfig.upgradeCost(nextUpgradeNumber(i, level)) + " patk\u00F3";
            upgradeLabels[i].setText(category.label + ": " + level + "/" + category.upgradeCount + " - " + costText);
        }
    }

    private int nextUpgradeNumber(int categoryIndex, int currentLevel) {
        int upgradeNumber = currentLevel + 1;
        for (int i = 0; i < categoryIndex; i++) {
            upgradeNumber += MvpGameConfig.UPGRADE_CATEGORIES[i].upgradeCount;
        }
        return upgradeNumber;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.valueOf("eef6ff"));
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
