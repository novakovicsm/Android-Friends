package com.yourstudio.horse.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
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
    private Sound purchaseSound;
    private Label horseshoeLabel;
    private final Label[] skinLabels = new Label[MvpGameConfig.SKIN_LABELS.length];
    private final Label[] petLabels = new Label[MvpGameConfig.PET_LABELS.length];
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
        purchaseSound = game.getAssets().get("sfx/powerup.wav", Sound.class);

        Label.LabelStyle titleStyle = skin.get("title", Label.LabelStyle.class);
        Label.LabelStyle labelStyle = skin.get("default", Label.LabelStyle.class);
        TextButton.TextButtonStyle buttonStyle = skin.get("primary", TextButton.TextButtonStyle.class);

        Table root = new Table();
        root.setFillParent(true);
        root.pad(28f);

        Table layout = new Table();
        layout.defaults().padBottom(10f);

        Label title = new Label("Ist\u00E1ll\u00F3 fejleszt\u00E9sek", titleStyle);
        title.setAlignment(Align.center);
        horseshoeLabel = new Label("", labelStyle);
        horseshoeLabel.setAlignment(Align.center);

        layout.add(title).colspan(2).padBottom(18f).row();
        layout.add(horseshoeLabel).colspan(2).padBottom(20f).row();

        Label skinTitle = new Label("Skinek", labelStyle);
        layout.add(skinTitle).colspan(2).left().padBottom(12f).row();

        for (int i = 0; i < MvpGameConfig.SKIN_LABELS.length; i++) {
            final int skinIndex = i;
            skinLabels[i] = new Label("", labelStyle);
            TextButton buyButton = new TextButton("V\u00E1s\u00E1rl\u00E1s", buttonStyle);
            buyButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (progress.purchaseSkin(skinIndex)) {
                        progressStore.save(progress);
                        playPurchaseSound();
                        refreshLabels();
                    }
                }
            });

            layout.add(skinLabels[i]).width(360f).left();
            layout.add(buyButton).width(220f).height(52f).row();
        }

        Label petTitle = new Label("Kedvencek", labelStyle);
        layout.add(petTitle).colspan(2).left().padTop(8f).padBottom(12f).row();

        for (int i = 0; i < MvpGameConfig.PET_LABELS.length; i++) {
            final int petIndex = i;
            petLabels[i] = new Label("", labelStyle);
            TextButton buyButton = new TextButton("V\u00E1s\u00E1rl\u00E1s", buttonStyle);
            buyButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (progress.purchasePet(petIndex)) {
                        progressStore.save(progress);
                        playPurchaseSound();
                        refreshLabels();
                    }
                }
            });

            layout.add(petLabels[i]).width(360f).left();
            layout.add(buyButton).width(220f).height(52f).row();
        }

        Label upgradeTitle = new Label("Upgrade-ek", labelStyle);
        layout.add(upgradeTitle).colspan(2).left().padTop(8f).padBottom(12f).row();

        for (int i = 0; i < MvpGameConfig.UPGRADE_CATEGORIES.length; i++) {
            final int categoryIndex = i;
            upgradeLabels[i] = new Label("", labelStyle);
            TextButton buyButton = new TextButton("V\u00E1s\u00E1rl\u00E1s", buttonStyle);
            buyButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (progress.purchaseUpgrade(categoryIndex)) {
                        progressStore.save(progress);
                        playPurchaseSound();
                        refreshLabels();
                    }
                }
            });

            layout.add(upgradeLabels[i]).width(360f).left();
            layout.add(buyButton).width(220f).height(52f).row();
        }

        TextButton backButton = new TextButton("Vissza", buttonStyle);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenNavigator.toMainMenu(game);
            }
        });

        layout.add(backButton).width(260f).height(64f).colspan(2).padTop(16f);
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFadeScrollBars(false);
        root.add(scrollPane).expand().fill();
        stage.addActor(root);
        Gdx.input.setInputProcessor(stage);
        refreshLabels();
    }

    private void refreshLabels() {
        horseshoeLabel.setText("Aranypatk\u00F3: " + progress.horseshoes);
        for (int i = 0; i < MvpGameConfig.SKIN_LABELS.length; i++) {
            boolean unlocked = i == 0 || (progress.unlockedSkins != null
                && i < progress.unlockedSkins.length
                && progress.unlockedSkins[i]);
            String statusText = unlocked ? "megvan" : MvpGameConfig.skinPrice(i) + " patk\u00F3";
            skinLabels[i].setText(MvpGameConfig.SKIN_LABELS[i] + " - " + statusText);
        }
        for (int i = 0; i < MvpGameConfig.PET_LABELS.length; i++) {
            boolean unlocked = i == 0 || (progress.unlockedPets != null
                && i < progress.unlockedPets.length
                && progress.unlockedPets[i]);
            String statusText = unlocked ? "megvan" : MvpGameConfig.PET_UNLOCK_PRICE + " patk\u00F3";
            petLabels[i].setText(MvpGameConfig.PET_LABELS[i] + " - " + statusText);
        }
        for (int i = 0; i < MvpGameConfig.UPGRADE_CATEGORIES.length; i++) {
            MvpGameConfig.UpgradeCategory category = MvpGameConfig.UPGRADE_CATEGORIES[i];
            int level = progress.upgradeLevels[i];
            String costText = level >= category.upgradeCount
                ? "max"
                : MvpGameConfig.upgradeCost(nextUpgradeNumber(i, level)) + " patk\u00F3";
            upgradeLabels[i].setText(category.label + ": " + level + "/" + category.upgradeCount + " - " + costText);
        }
    }

    private void playPurchaseSound() {
        if (!progress.muted && purchaseSound != null) {
            purchaseSound.play(0.7f);
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
