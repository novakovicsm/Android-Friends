package com.yourstudio.horse.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yourstudio.horse.HorseGame;
import com.yourstudio.horse.model.MvpGameConfig;
import com.yourstudio.horse.model.MvpProgress;
import com.yourstudio.horse.model.MvpProgressStore;
import com.yourstudio.horse.ui.ScreenNavigator;

public class RaceScreen extends ScreenAdapter {
        // Updates the coin label with the current coin count
        private void updateCoinLabel() {
            if (coinLabel != null) {
                coinLabel.setText("\u00C9rm\u00E9k: " + playerCoins);
            }
        }
    // Coin count for player
    private int playerCoins = 0;
    private Label coinLabel;
    // Call this method wherever coins are collected in the game logic
    public void collectCoin(int baseAmount) {
        int amount = Math.round(baseAmount * petCoinMultiplier);
        playerCoins += amount;
        updateCoinLabel();
        // Optionally update UI or play sound here
    }
            // Pet bonus fields
            private float petCoinMultiplier = 1f;
            private float petPowerupDurationMultiplier = 1f;
        // Joystick input state
        private float joystickX = 0f;
        private float joystickY = 0f;
    private static final boolean FORCE_PROCEDURAL_HORSE = true;
    private final HorseGame game;
    private final String horseName;
    private final String riderName;
    private final String petName;
    private final String trackName;
    private final String horseColor;
    private final String maneColor;
    private final String saddleColor;
    private final String outfitColor;

    private Stage stage;
    private Texture background;
    private BitmapFont titleFont;
    private BitmapFont bodyFont;
    private Sound clickSound;
    private Sound powerupSound;
    private Sound winSound;
    private Music raceMusic;
    private Texture hudPanel;
    private Label speedLabel;
    private Label lapLabel;
    private Label powerupLabel;
    private Label petBonusLabel;
    private Image horsePreviewImage;
    private Image riderPreviewImage;
    private Image petPreviewImage;
    private TextureRegion horsePreviewRegion;
    private Texture horsePreviewFallback;
    private Texture[] riderPreviews;
    private Texture[] petPreviews;
    private Texture riderPreviewCustom;
    private Color horseTintColor;
    private Color riderOutfitColor;
    private Color riderHairColor;
    private int horseIndex;
    private int riderIndex;
    private int petIndex;
    private final String[] horses = horseNamesFromConfig();
    private final String[] riders = MvpGameConfig.RIDER_NAMES;
    private final String[] pets = {"Kutya"};
    private float elapsedTime;
    private int currentLap = 1;
    private float speed;
    private float distance;
    private final float maxSpeed = 48f;
    private final float acceleration = 22f;
    private final float deceleration = 18f;
    private final float lapDistance = 300f;
    private float horseX = 64f;
    private float horseY = 64f;
    private float horseDirection = 1f;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private Array<PowerupDef> powerupDefs = new Array<>();
    private Array<PowerupSpawn> powerupSpawns = new Array<>();
    private Texture powerupMarker;
    private float spawnTimer;
    private float nextSpawnDelay = 3.5f;
    private String activePowerupName;
    private float activePowerupTimer;
    private int boostChargePercent;
    private float petSpeedBonus;
    private float petAccelBonus;
    private float petShieldBonus;
    private boolean victoryPlayed;
    private boolean raceFinished;
    private Viewport mapViewport;
    private boolean mapLoaded;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> runAnimation;
    private Texture[] idleFrames;
    private Texture[] runFrames;
    private Texture idleSheet;
    private Texture runSheet;
    private float animationTime;
    private float mapBoundsMinX;
    private float mapBoundsMaxX;
    private float mapBoundsMinY;
    private float mapBoundsMaxY;
    private boolean mapHasBounds;
    private final float horseBoundsPadding = 32f;
    private float mapScale = 1f;
    private float mapPixelWidth;
    private float mapPixelHeight;
    private float scaledMapMinX;
    private float scaledMapMaxX;
    private float scaledMapMinY;
    private float scaledMapMaxY;
    private Image joystickBase;
    private Image joystickKnob;
    private Texture joystickBaseTexture;
    private Texture joystickKnobTexture;
    private float joystickCenterX;
    private float joystickCenterY;
    private float joystickRadius;
    private int joystickPointer = -1;

    public RaceScreen(HorseGame game, String horseName, String riderName, String petName) {
        this.game = game;
        this.horseName = horseName;
        this.riderName = riderName;
        this.petName = petName;
        this.trackName = "Ismeretlen";
        this.horseColor = null;
        this.maneColor = null;
        this.saddleColor = null;
        this.outfitColor = null;
    }
    
    public RaceScreen(HorseGame game, String horseName, String riderName, String petName, String trackName) {
        this.game = game;
        this.horseName = horseName;
        this.riderName = riderName;
        this.petName = petName;
        this.trackName = trackName;
        this.horseColor = null;
        this.maneColor = null;
        this.saddleColor = null;
        this.outfitColor = null;
    }

    public RaceScreen(HorseGame game, String horseName, String riderName, String petName, String trackName,
                      String horseColor, String maneColor, String saddleColor) {
        this.game = game;
        this.horseName = horseName;
        this.riderName = riderName;
        this.petName = petName;
        this.trackName = trackName;
        this.horseColor = horseColor;
        this.maneColor = maneColor;
        this.saddleColor = saddleColor;
        this.outfitColor = null;
    }

    public RaceScreen(HorseGame game, String horseName, String riderName, String petName, String trackName,
                      String horseColor, String maneColor, String saddleColor, String outfitColor) {
        this.game = game;
        this.horseName = horseName;
        this.riderName = riderName;
        this.petName = petName;
        this.trackName = trackName;
        this.horseColor = horseColor;
        this.maneColor = maneColor;
        this.saddleColor = saddleColor;
        this.outfitColor = outfitColor;
    }

    @Override
    public void show() {
                // Set up pet bonus multipliers
                petCoinMultiplier = 1f;
                petPowerupDurationMultiplier = 1f;
                if ("Kapibara".equals(petName)) {
                    petCoinMultiplier = 2f;
                } else if ("Lajhár".equals(petName)) {
                    petPowerupDurationMultiplier = 1.5f;
                }
        stage = new Stage(new ScreenViewport());
        // Floating joystick visuals (with safe fallback if assets are missing).
        joystickBaseTexture = createJoystickTexture(128, 0.25f);
        joystickKnobTexture = loadUiTextureOrFallback("ui/joystick_knob.png", 64);
        joystickBase = new Image(joystickBaseTexture);
        joystickKnob = new Image(joystickKnobTexture);
        joystickBase.setVisible(false);
        joystickKnob.setVisible(false);
        joystickBase.setTouchable(Touchable.disabled);
        joystickKnob.setTouchable(Touchable.disabled);
        stage.addActor(joystickBase);
        stage.addActor(joystickKnob);
        joystickRadius = joystickBaseTexture.getWidth() * 0.5f;
        // ...existing code...
        background = loadUiTexture("ui/bg_race.png");
        clickSound = game.getAssets().get("sfx/click.wav", Sound.class);
        powerupSound = game.getAssets().get("sfx/powerup.wav", Sound.class);
        winSound = game.getAssets().get("sfx/win.wav", Sound.class);
        raceMusic = game.getAssets().get("sfx/race_music.wav", Music.class);
        raceMusic.setLooping(true);
        raceMusic.setVolume(0.5f);
        raceMusic.play();
        hudPanel = loadUiTexture("ui/panel_hud.png");
        loadHorseAnimations();
        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);
        runAnimation.setPlayMode(Animation.PlayMode.LOOP);
        powerupMarker = createPowerupMarker();
        loadPowerupDefs();
        horseIndex = findIndex(horses, horseName);
        riderIndex = findIndex(riders, riderName);
        petIndex = findIndex(pets, petName);
        horseTintColor = colorForHorseColor(horseColor);
        riderOutfitColor = colorForOutfitColor(outfitColor);
        riderHairColor = colorForRiderHair(riderName);
        // Load pixel art assets for previews
        horsePreviewImage = new Image(new Texture(Gdx.files.internal(getHorsePreviewAsset(horseColor))));
        riderPreviewImage = new Image(new Texture(Gdx.files.internal(getRiderPreviewAsset(riderName))));
        petPreviewImage = new Image(new Texture(Gdx.files.internal(getPetPreviewAsset(petName))));




        camera = new OrthographicCamera();
        mapViewport = new ScreenViewport(camera);
        mapViewport.apply();
        try {
            String mapFile = "maps/" + trackName;
            if (!Gdx.files.internal(mapFile).exists()) {
                mapFile = "maps/beach.tmx"; // fallback to a known existing map
            }
            map = new TmxMapLoader().load(mapFile);
            mapRenderer = new OrthogonalTiledMapRenderer(map, 1f);
            MapProperties props = map.getProperties();
            Integer mapWidthTiles = props.get("width", Integer.class);
            Integer mapHeightTiles = props.get("height", Integer.class);
            Integer tileWidth = props.get("tilewidth", Integer.class);
            Integer tileHeight = props.get("tileheight", Integer.class);
            if (mapWidthTiles != null && mapHeightTiles != null && tileWidth != null && tileHeight != null) {
                mapPixelWidth = mapWidthTiles * tileWidth;
                mapPixelHeight = mapHeightTiles * tileHeight;
                mapBoundsMinX = 0f;
                mapBoundsMinY = 0f;
                mapBoundsMaxX = mapPixelWidth;
                mapBoundsMaxY = mapPixelHeight;
                mapHasBounds = true;
                updateMapScale(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
            mapLoaded = true;
        } catch (RuntimeException exception) {
            Gdx.app.error("RaceScreen", "Failed to load map, using fallback background.", exception);
            mapLoaded = false;
        }

        // Initialize bodyFont before using it for LabelStyle and TextButtonStyle
        bodyFont = createUIFont(24, 1.0f);
        Label.LabelStyle labelStyle = new Label.LabelStyle(bodyFont, Color.WHITE);
        Skin skin = game.getSkin();
        TextButton.TextButtonStyle buttonStyle = skin.get("primary", TextButton.TextButtonStyle.class);

        TextButton backButton = new TextButton("Vissza", buttonStyle);

        speedLabel = new Label("Sebess\u00E9g: 0 km/h", labelStyle);
        lapLabel = new Label("K\u00F6r: 1/3", labelStyle);
        powerupLabel = new Label("B\u00F3nusz: --", labelStyle);
        petBonusLabel = new Label("Kedvenc b\u00F3nusz: --", labelStyle);
        coinLabel = new Label("\u00C9rm\u00E9k: 0", labelStyle);
        // directionLabel = new Label("Ir\u00E1ny:", labelStyle);
            // Joystick control only, remove left/right buttons from UI
            // directionLabel can remain for feedback if desired
        // Joystick input now handled via touchpad knob percent in render().

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (clickSound != null) {
                    clickSound.play(0.6f);
                }
                ScreenNavigator.Selection selection = new ScreenNavigator.Selection(
                    horseName, riderName, petName, horseColor, maneColor, saddleColor, outfitColor
                );
                ScreenNavigator.toCharacterSelect(game, selection);
            }
        });

        Table hudTable = new Table();
        hudTable.setFillParent(true);
        hudTable.top().left().pad(16f);
        Table hudContent = new Table();
        hudContent.setBackground(toDrawable(hudPanel));
        hudContent.pad(12f);
        hudContent.add(speedLabel).left().row();
        hudContent.add(lapLabel).left().padTop(6f).row();
        hudContent.add(powerupLabel).left().padTop(6f);
        hudContent.row();
        hudContent.add(petBonusLabel).left().padTop(6f);
        hudContent.row();
        hudContent.add(coinLabel).left().padTop(6f);
        hudContent.row();
        Table previewRow = new Table();
        previewRow.add(horsePreviewImage).size(64f, 48f).padRight(6f);
        previewRow.add(riderPreviewImage).size(64f, 48f).padRight(6f);
        previewRow.add(petPreviewImage).size(64f, 48f);
        hudContent.add(previewRow).left().padTop(8f);
        applyPetBonus();
        hudTable.add(hudContent);
        Table backButtonTable = new Table();
        backButtonTable.setFillParent(true);
        backButtonTable.top().right().pad(16f);
        backButtonTable.add(backButton).width(220f).height(80f);
        stage.addActor(backButtonTable);
        stage.addActor(hudTable);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (button == 0) {
                    com.badlogic.gdx.math.Vector2 stageCoords = stage.screenToStageCoordinates(new com.badlogic.gdx.math.Vector2(screenX, screenY));
                    Actor hit = stage.hit(stageCoords.x, stageCoords.y, true);
                    if (hit != null && hit.isTouchable() && hit instanceof TextButton) {
                        return false;
                    }
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(multiplexer);
    }

    // Helper methods to map selection to asset filenames
    private String getHorsePreviewAsset(String horseColor) {
        if (horseColor == null) return "sprites/horse_idle_bay.png";
        switch (horseColor.toLowerCase()) {
            case "bay": return "sprites/horse_idle_bay.png";
            case "chestnut": return "sprites/horse_idle_chestnut.png";
            case "gray": return "sprites/horse_idle_gray.png";
            case "palomino": return "sprites/horse_idle_palomino.png";
            default: return "sprites/horse_idle_bay.png";
        }
    }

    private String getRiderPreviewAsset(String riderName) {
        // Placeholder: always return a default image, update as needed
        return "ui/panel_logo.png";
    }

    private String getPetPreviewAsset(String petName) {
        // Placeholder: always return a default image, update as needed
        return "ui/panel_menu.png";
    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        elapsedTime += delta;
        if (Math.abs(joystickX) < 0.01f) {
            joystickX = 0f;
        }
        if (Math.abs(joystickY) < 0.01f) {
            joystickY = 0f;
        }
        boolean accelerating = joystickPointer != -1;
        float effectiveMaxSpeed = maxSpeed + petSpeedBonus;
        float effectiveAccel = acceleration + petAccelBonus;
        if (accelerating) {
            speed = Math.min(effectiveMaxSpeed, speed + effectiveAccel * delta);
        } else {
            speed = Math.max(0f, speed - deceleration * delta);
        }
        distance += speed * delta;
        updateRaceCompletion();
        updatePowerupSpawns(delta);
        updatePowerupPickup(delta);
        int lap = Math.min(3, 1 + (int) (distance / lapDistance));
        if (lap != currentLap) {
            currentLap = lap;
            if (currentLap == 3 && !victoryPlayed && winSound != null) {
                victoryPlayed = true;
                winSound.play(0.7f);
                Gdx.input.vibrate(120);
            }
        }
        speedLabel.setText("Sebess\u00E9g: " + (int) speed + " km/h");
        lapLabel.setText("K\u00F6r: " + currentLap + "/3");
        if (activePowerupName != null) {
            powerupLabel.setText("B\u00F3nusz: " + activePowerupName + " (" + (int) Math.ceil(activePowerupTimer) + "s)");
        } else {
            powerupLabel.setText("Boost: " + boostChargePercent + "%");
        }
        animationTime += delta;
        updateCoinLabel();
        if (mapLoaded && mapRenderer != null && camera != null) {
            // Draw a full-screen background so any unused map area isn't black.
            stage.getBatch().begin();
            drawBackgroundFit(stage.getBatch());
            stage.getBatch().end();

            // Free movement: joystick controls both X and Y
            horseX += speed * delta * joystickX;
            horseY += speed * delta * joystickY;
            if (Math.abs(joystickX) > 0.01f || Math.abs(joystickY) > 0.01f) {
                horseDirection = joystickX >= 0f ? 1f : -1f;
            }
            if (mapHasBounds) {
                float minX = mapBoundsMinX + horseBoundsPadding;
                float maxX = mapBoundsMaxX - horseBoundsPadding;
                if (horseX <= minX) {
                    horseX = minX;
                    horseDirection = 1f;
                } else if (horseX >= maxX) {
                    horseX = maxX;
                    horseDirection = -1f;
                }
                horseY = MathUtils.clamp(horseY, mapBoundsMinY + horseBoundsPadding, mapBoundsMaxY - horseBoundsPadding);
            }
            // Ensure the map viewport always fills the screen.
            mapViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
            float renderHorseX = horseX * mapScale;
            float renderHorseY = horseY * mapScale;
            camera.position.set(clampCameraX(renderHorseX), clampCameraY(renderHorseY), 0f);
            camera.update();
            mapRenderer.setView(camera);
            mapRenderer.render();
            mapRenderer.getBatch().begin();
            drawPowerups(mapRenderer.getBatch());
            drawHorseAnimation(mapRenderer.getBatch(), true);
            mapRenderer.getBatch().end();
        } else {
            stage.getBatch().begin();
            drawBackgroundFit(stage.getBatch());
            drawPowerups(stage.getBatch());
            drawHorseAnimation(stage.getBatch(), false);
            stage.getBatch().end();
        }
        stage.act(delta);
        stage.draw();
    }

    private void updateRaceCompletion() {
        if (raceFinished || distance < lapDistance * 3f) {
            return;
        }
        raceFinished = true;
        victoryPlayed = true;
        MvpProgressStore progressStore = new MvpProgressStore(Gdx.app.getPreferences(MvpProgressStore.PREFS_NAME));
        MvpProgress progress = progressStore.load();
        boolean recordBroken = isRecordBroken(progress.recordTime, elapsedTime);
        progress.applyRaceResult(1, MvpGameConfig.Difficulty.MEDIUM, recordBroken);
        if (recordBroken) {
            progress.recordTime = formatRaceTime(elapsedTime);
        }
        progressStore.save(progress);
        playerCoins = progress.horseshoes;
        updateCoinLabel();
        if (winSound != null) {
            winSound.play(0.7f);
        }
        try {
            Gdx.input.vibrate(120);
        } catch (SecurityException ignored) {
            // VIBRATE permission missing or restricted; ignore to avoid crash.
        }
    }

    private boolean isRecordBroken(String previousRecord, float raceTimeSeconds) {
        if (previousRecord == null || previousRecord.length() == 0) {
            return true;
        }
        return raceTimeSeconds < parseRaceTime(previousRecord);
    }

    private float parseRaceTime(String value) {
        String[] parts = value.split(":");
        if (parts.length != 2) {
            return Float.MAX_VALUE;
        }
        try {
            return Integer.parseInt(parts[0]) * 60f + Integer.parseInt(parts[1]);
        } catch (NumberFormatException ignored) {
            return Float.MAX_VALUE;
        }
    }

    private String formatRaceTime(float seconds) {
        int totalSeconds = Math.max(0, Math.round(seconds));
        int minutes = totalSeconds / 60;
        int remainder = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, remainder);
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }
        if (mapViewport != null) {
            mapViewport.update(width, height, true);
        }
        if (mapHasBounds) {
            updateMapScale(width, height);
        }
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
        if (titleFont != null) {
            titleFont.dispose();
        }
        if (bodyFont != null) {
            bodyFont.dispose();
        }
        if (raceMusic != null) {
            raceMusic.stop();
        }
        if (background != null) {
            background.dispose();
        }
        if (powerupMarker != null) {
            powerupMarker.dispose();
        }
        if (hudPanel != null) {
            hudPanel.dispose();
        }
        if (mapRenderer != null) {
            mapRenderer.dispose();
        }
        if (map != null) {
            map.dispose();
        }
        if (idleSheet != null) {
            idleSheet.dispose();
        }
        if (runSheet != null) {
            runSheet.dispose();
        }
        if (horsePreviewFallback != null) {
            horsePreviewFallback.dispose();
        }
        if (riderPreviewCustom != null) {
            riderPreviewCustom.dispose();
        }
        disposeTextureArray(riderPreviews);
        disposeTextureArray(petPreviews);
    }

    private Texture createColorTexture(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private Drawable toDrawable(Texture texture) {
        return new TextureRegionDrawable(texture);
    }

    private Texture loadUiTexture(String path) {
        Texture texture = new Texture(path);
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        return texture;
    }

    private Texture loadUiTextureOrFallback(String path, int size) {
        if (Gdx.files.internal(path).exists()) {
            return loadUiTexture(path);
        }
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();
        pixmap.setColor(0.1f, 0.1f, 0.1f, 0.5f);
        pixmap.fillCircle(size / 2, size / 2, size / 3);
        pixmap.setColor(1f, 1f, 1f, 0.8f);
        pixmap.drawCircle(size / 2, size / 2, size / 3);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        return texture;
    }

    private Texture createJoystickTexture(int size, float alpha) {
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();
        pixmap.setColor(0.1f, 0.1f, 0.1f, alpha);
        pixmap.fillCircle(size / 2, size / 2, size / 2);
        pixmap.setColor(1f, 1f, 1f, alpha * 0.6f);
        pixmap.drawCircle(size / 2, size / 2, size / 2 - 2);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        return texture;
    }

    private BitmapFont createUIFont(int size, float fallbackScale) {
        FileHandle fontFile = Gdx.files.internal("fonts/ui.ttf");
        if (fontFile.exists()) {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
            FreeTypeFontParameter parameter = new FreeTypeFontParameter();
            parameter.size = size;
            parameter.minFilter = TextureFilter.Linear;
            parameter.magFilter = TextureFilter.Linear;
            parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "\u00C1\u00C9\u00CD\u00D3\u00D6\u0150\u00DA\u00DC\u0170\u00E1\u00E9\u00ED\u00F3\u00F6\u0151\u00FA\u00FC\u0171";
            BitmapFont font = generator.generateFont(parameter);
            generator.dispose();
            return font;
        }
        BitmapFont font = new BitmapFont();
        font.getData().setScale(fallbackScale);
        return font;
    }

    private int findIndex(String[] values, String target) {
        if (target == null) {
            return 0;
        }
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(target)) {
                return i;
            }
        }
        return 0;
    }

    private static String[] horseNamesFromConfig() {
        String[] names = new String[MvpGameConfig.HORSES.length];
        for (int i = 0; i < MvpGameConfig.HORSES.length; i++) {
            names[i] = MvpGameConfig.HORSES[i].name;
        }
        return names;
    }

    private Texture[] createRiderPreviews() {
        Color[] outfits = {
            new Color(0.35f, 0.6f, 0.85f, 1f),
            new Color(0.6f, 0.45f, 0.8f, 1f),
            new Color(0.2f, 0.7f, 0.45f, 1f),
            new Color(0.85f, 0.4f, 0.4f, 1f)
        };
        Color[] hair = {
            new Color(0.2f, 0.15f, 0.1f, 1f),
            new Color(0.4f, 0.25f, 0.1f, 1f),
            new Color(0.1f, 0.08f, 0.05f, 1f),
            new Color(0.7f, 0.55f, 0.3f, 1f)
        };
        Texture[] previews = new Texture[riders.length];
        for (int i = 0; i < riders.length; i++) {
            previews[i] = createRiderPreview(outfits[i % outfits.length], hair[i % hair.length]);
        }
        return previews;
    }

    private Texture[] createPetPreviews() {
        // Colors: Kutya, Cica, Nyuszi, Papagáj, Kapibara, Lajhár
        Color[] petColors = {
            new Color(0.85f, 0.65f, 0.4f, 1f),   // Kutya
            new Color(0.6f, 0.6f, 0.65f, 1f),    // Cica
            new Color(0.95f, 0.9f, 0.75f, 1f),   // Nyuszi
            new Color(0.2f, 0.75f, 0.45f, 1f),   // Papagáj
            new Color(0.7f, 0.5f, 0.3f, 1f),     // Kapibara
            new Color(0.6f, 0.7f, 0.5f, 1f)      // Lajhár
        };
        Texture[] previews = new Texture[petColors.length];
        for (int i = 0; i < petColors.length; i++) {
            previews[i] = createPetPreview(petColors[i]);
        }
        return previews;
    }

    private Texture createHorsePreview(Color body, Color mane) {
        Pixmap pixmap = createPreviewPanel();
        pixmap.setColor(body);
        pixmap.fillRectangle(26, 48, 86, 32);
        pixmap.fillRectangle(32, 32, 14, 20);
        pixmap.fillRectangle(62, 32, 14, 20);
        pixmap.fillRectangle(94, 32, 14, 20);
        pixmap.fillCircle(126, 64, 16);
        pixmap.setColor(mane);
        pixmap.fillRectangle(108, 78, 24, 8);
        pixmap.fillRectangle(36, 78, 18, 10);
        return finalizePreviewTexture(pixmap);
    }

    private Texture createRiderPreview(Color outfit, Color hair) {
        Pixmap pixmap = createPreviewPanel();
        pixmap.setColor(outfit);
        pixmap.fillRectangle(60, 44, 40, 48);
        pixmap.fillRectangle(50, 56, 14, 24);
        pixmap.fillRectangle(96, 56, 14, 24);
        pixmap.setColor(new Color(0.9f, 0.75f, 0.6f, 1f));
        pixmap.fillCircle(80, 98, 12);
        pixmap.setColor(hair);
        pixmap.fillRectangle(68, 104, 24, 6);
        return finalizePreviewTexture(pixmap);
    }

    private Texture createPetPreview(Color fur) {
        Pixmap pixmap = createPreviewPanel();
        pixmap.setColor(fur);
        pixmap.fillCircle(80, 64, 20);
        pixmap.fillCircle(60, 72, 10);
        pixmap.fillCircle(100, 72, 10);
        pixmap.setColor(new Color(0.2f, 0.2f, 0.2f, 1f));
        pixmap.fillCircle(72, 68, 3);
        pixmap.fillCircle(88, 68, 3);
        pixmap.fillRectangle(78, 56, 4, 6);
        return finalizePreviewTexture(pixmap);
    }

    private Pixmap createPreviewPanel() {
        int width = 160;
        int height = 120;
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.94f, 0.91f, 0.85f, 1f);
        pixmap.fill();
        pixmap.setColor(0.35f, 0.26f, 0.18f, 1f);
        pixmap.drawRectangle(0, 0, width, height);
        pixmap.drawRectangle(1, 1, width - 2, height - 2);
        return pixmap;
    }

    private Texture finalizePreviewTexture(Pixmap pixmap) {
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private void disposeTextureArray(Texture[] textures) {
        if (textures == null) {
            return;
        }
        for (Texture texture : textures) {
            if (texture != null) {
                texture.dispose();
            }
        }
    }

    private void applyPetBonus() {
        petSpeedBonus = 0f;
        petAccelBonus = 0f;
        petShieldBonus = 0f;
        String bonusText = "csak t\u00e1rs";
        if ("Kutya_DISABLED".equals(petName)) {
            petSpeedBonus = 6f;
            bonusText = "+6 km/h végsebesség";
        } else if ("Cica".equals(petName)) {
            petAccelBonus = 5f;
            bonusText = "+5 gyorsulás";
        } else if ("Nyuszi".equals(petName)) {
            petAccelBonus = 3f;
            petSpeedBonus = 3f;
            bonusText = "+3 gyorsulás, +3 km/h";
        } else if ("Papagáj".equals(petName)) {
            petShieldBonus = 1f;
            bonusText = "+1 pajzs";
        } else if ("Kapibara".equals(petName)) {
            // Coin multiplier bonus (handled in coin collection logic)
            bonusText = "Érme szorzó: x2";
        } else if ("Lajhár".equals(petName)) {
            // Power-up duration increase (handled in power-up logic)
            bonusText = "Power-up idő: x1.5";
        }
        if (petBonusLabel != null) {
            petBonusLabel.setText("Kedvenc bónusz: " + bonusText);
        }
    }

    private void updatePowerupSpawns(float delta) {
        spawnTimer += delta;
        if (powerupDefs.size == 0) {
            return;
        }
        if (spawnTimer >= nextSpawnDelay) {
            spawnTimer = 0f;
            nextSpawnDelay = MathUtils.random(3f, 6f);
            if (powerupSpawns.size < 5) {
                PowerupDef def = powerupDefs.random();
                float x = horseX + MathUtils.random(120f, 360f);
                float y = 80f + MathUtils.random(-40f, 40f);
                if (mapHasBounds) {
                    x = MathUtils.clamp(x, mapBoundsMinX + horseBoundsPadding, mapBoundsMaxX - horseBoundsPadding);
                    y = MathUtils.clamp(y, mapBoundsMinY + horseBoundsPadding, mapBoundsMaxY - horseBoundsPadding);
                }
                powerupSpawns.add(new PowerupSpawn(def.id, x, y));
            }
        }
    }

    private void updatePowerupPickup(float delta) {
        if (activePowerupTimer > 0f) {
            activePowerupTimer = Math.max(0f, activePowerupTimer - delta);
            if (activePowerupTimer == 0f) {
                activePowerupName = null;
            }
        }
        for (int i = powerupSpawns.size - 1; i >= 0; i--) {
            PowerupSpawn spawn = powerupSpawns.get(i);
            float dx = spawn.x - horseX;
            float dy = spawn.y - horseY;
            if (dx * dx + dy * dy <= 24f * 24f) {
                powerupSpawns.removeIndex(i);
                boostChargePercent = Math.min(100, boostChargePercent + MvpGameConfig.BOOST_POWERUP_CHARGE_PERCENT);
                activePowerupName = "+" + MvpGameConfig.BOOST_POWERUP_CHARGE_PERCENT + "% boost";
                activePowerupTimer = 1.5f;
                if (powerupSound != null) {
                    powerupSound.play(0.7f);
                }
                try {
                    Gdx.input.vibrate(60);
                } catch (SecurityException ignored) {
                    // VIBRATE permission missing or restricted; ignore to avoid crash.
                }
            }
        }
    }

    private void drawPowerups(com.badlogic.gdx.graphics.g2d.Batch batch) {
        if (powerupMarker == null) {
            return;
        }
        float scale = mapLoaded ? mapScale : 1f;
        for (PowerupSpawn spawn : powerupSpawns) {
            float x = spawn.x * scale;
            float y = spawn.y * scale;
            batch.draw(powerupMarker, x - 10f * scale, y - 10f * scale, 20f * scale, 20f * scale);
        }
    }

    private Texture createPowerupMarker() {
        Pixmap pixmap = new Pixmap(24, 24, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.95f, 0.8f, 0.2f, 1f);
        pixmap.fillCircle(12, 12, 10);
        pixmap.setColor(0.35f, 0.25f, 0.05f, 1f);
        pixmap.drawCircle(12, 12, 10);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private void loadPowerupDefs() {
        powerupDefs.clear();
        powerupDefs.add(new PowerupDef("boost_charge", "Boost t\u00f6ltet"));
    }

    private static class PowerupDef {
        final String id;
        final String name;

        PowerupDef(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    private static class PowerupSpawn {
        final String id;
        final float x;
        final float y;

        PowerupSpawn(String id, float x, float y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }
    }

    private void drawHorseAnimation(com.badlogic.gdx.graphics.g2d.Batch batch, boolean mapSpace) {
        boolean running = speed > 26f;
        Animation<TextureRegion> animation = running ? runAnimation : idleAnimation;
        if (animation == null) {
            return;
        }
        TextureRegion frame = animation.getKeyFrame(animationTime);
        Color previousColor = new Color(batch.getColor());
        if (horseTintColor != null) {
            batch.setColor(horseTintColor);
        }
        float scale = mapSpace ? mapScale : 1f;
        float size = 96f * scale;
        float x;
        float y;
        if (mapSpace) {
            x = horseX * scale - size * 0.5f;
            y = horseY * scale - size * 0.5f;
        } else {
            x = stage.getViewport().getWorldWidth() * 0.5f - size * 0.5f;
            y = stage.getViewport().getWorldHeight() * 0.25f - size * 0.5f;
        }
        if (horseDirection >= 0f) {
            batch.draw(frame, x, y, size, size);
        } else {
            batch.draw(frame, x + size, y, -size, size);
        }
        batch.setColor(previousColor);
    }

    private void refreshRiderPreview() {
        if (riderPreviewImage == null || riderOutfitColor == null || riderHairColor == null) {
            return;
        }
        if (riderPreviewCustom != null) {
            riderPreviewCustom.dispose();
        }
        riderPreviewCustom = createRiderPreview(riderOutfitColor, riderHairColor);
        riderPreviewImage.setDrawable(toDrawable(riderPreviewCustom));
    }

    private Color colorForHorseColor(String value) {
        if ("Meleg barna".equals(value)) {
            return new Color(0.64f, 0.38f, 0.2f, 1f);
        }
        if ("Arany".equals(value)) {
            return new Color(0.85f, 0.72f, 0.42f, 1f);
        }
        if ("Hamvas".equals(value)) {
            return new Color(0.7f, 0.72f, 0.78f, 1f);
        }
        if ("S\u00F6t\u00E9t".equals(value)) {
            return new Color(0.25f, 0.2f, 0.15f, 1f);
        }
        return null;
    }

    private Color colorForOutfitColor(String value) {
        if ("Piros".equals(value)) {
            return new Color(0.75f, 0.2f, 0.2f, 1f);
        }
        if ("K\u00E9k".equals(value)) {
            return new Color(0.2f, 0.4f, 0.8f, 1f);
        }
        if ("Z\u00F6ld".equals(value)) {
            return new Color(0.2f, 0.6f, 0.35f, 1f);
        }
        if ("Lila".equals(value)) {
            return new Color(0.55f, 0.3f, 0.75f, 1f);
        }
        return null;
    }

    private Color colorForRiderHair(String value) {
        if ("Lili".equals(value)) {
            return new Color(0.2f, 0.15f, 0.1f, 1f);
        }
        if ("Noel".equals(value)) {
            return new Color(0.4f, 0.25f, 0.1f, 1f);
        }
        if ("Mira".equals(value)) {
            return new Color(0.1f, 0.08f, 0.05f, 1f);
        }
        if ("\u00C1ron".equals(value)) {
            return new Color(0.7f, 0.55f, 0.3f, 1f);
        }
        return new Color(0.2f, 0.15f, 0.1f, 1f);
    }

    private void loadHorseAnimations() {
        if (FORCE_PROCEDURAL_HORSE) {
            idleFrames = createHorseIdleFrames();
            runFrames = createHorseRunFrames();
            idleAnimation = new Animation<>(0.6f, toRegions(idleFrames));
            runAnimation = new Animation<>(0.12f, toRegions(runFrames));
            return;
        }
        String variant = horseNameToVariant();
        try {
            idleSheet = new Texture("sprites/horse_idle_" + variant + ".png");
            runSheet = new Texture("sprites/horse_run_" + variant + ".png");
            TextureRegion[][] idleSplit = TextureRegion.split(idleSheet, 128, 128);
            TextureRegion[][] runSplit = TextureRegion.split(runSheet, 128, 128);
            idleAnimation = new Animation<>(0.6f, idleSplit[0]);
            runAnimation = new Animation<>(0.12f, runSplit[0]);
        } catch (RuntimeException exception) {
            Gdx.app.error("RaceScreen", "Failed to load horse sprites, using fallback.", exception);
            idleFrames = createHorseIdleFrames();
            runFrames = createHorseRunFrames();
            idleAnimation = new Animation<>(0.6f, toRegions(idleFrames));
            runAnimation = new Animation<>(0.12f, toRegions(runFrames));
        }
    }

    private String horseNameToVariant() {
        if ("Gesztenye".equals(horseName)) {
            return "chestnut";
        }
        if ("Pej".equals(horseName)) {
            return "bay";
        }
        if ("Sz\u00FCrke".equals(horseName)) {
            return "gray";
        }
        if ("Palomino".equals(horseName)) {
            return "palomino";
        }
        return "chestnut";
    }

    private Texture[] createHorseIdleFrames() {
        Texture[] frames = new Texture[2];
        Color body = new Color(0.92f, 0.86f, 0.78f, 1f);
        Color mane = resolveManeColor();
        frames[0] = createHorseFrame(body, mane, 0);
        frames[1] = createHorseFrame(body, mane, 1);
        return frames;
    }

    private Texture[] createHorseRunFrames() {
        Texture[] frames = new Texture[4];
        Color body = new Color(0.92f, 0.86f, 0.78f, 1f);
        Color mane = resolveManeColor();
        for (int i = 0; i < frames.length; i++) {
            frames[i] = createHorseFrame(body, mane, i + 2);
        }
        return frames;
    }

    private Texture createHorseFrame(Color body, Color mane, int variant) {
        int size = 64;
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();

        Color outline = darken(body, 0.48f);
        Color bodyShade = darken(body, 0.18f);
        Color maneShade = darken(mane, 0.22f);
        Color hoof = darken(body, 0.6f);
        Color riderOutfit = riderOutfitColor != null ? riderOutfitColor : new Color(0.35f, 0.6f, 0.85f, 1f);
        Color riderHair = riderHairColor != null ? riderHairColor : new Color(0.2f, 0.15f, 0.1f, 1f);
        Color riderSkin = new Color(0.92f, 0.78f, 0.62f, 1f);
        Color petFur = resolvePetFurColor();

        int bob = variant % 2 == 0 ? 0 : 1;
        int runPhase = Math.max(0, variant - 2);
        int legSwing = (runPhase % 2 == 0) ? 4 : -4;

        // Shadow
        pixmap.setColor(0f, 0f, 0f, 0.22f);
        pixmap.fillRectangle(10, 8, 44, 6);
        pixmap.fillCircle(10, 11, 3);
        pixmap.fillCircle(54, 11, 3);

        // Body (flatter back, more horse-like proportions)
        pixmap.setColor(body);
        pixmap.fillRectangle(16, 26 + bob, 30, 13); // torso
        pixmap.fillRectangle(28, 35 + bob, 16, 7); // back
        pixmap.fillCircle(46, 40 + bob, 8); // rump

        // Neck (thinner, more forward)
        pixmap.fillRectangle(24, 40 + bob, 4, 8);
        pixmap.fillRectangle(28, 46 + bob, 4, 8);

        pixmap.setColor(bodyShade);
        pixmap.fillRectangle(18, 24 + bob, 26, 3);
        pixmap.fillRectangle(30, 33 + bob, 12, 2);
        pixmap.fillRectangle(42, 36 + bob, 6, 2);
        pixmap.fillRectangle(24, 38 + bob, 4, 2);

        // Head (smaller, more horse-like)
        pixmap.setColor(body);
        pixmap.fillRectangle(44, 52 + bob, 7, 5); // head forward
        pixmap.fillRectangle(50, 50 + bob, 6, 4); // muzzle forward
        pixmap.fillRectangle(42, 56 + bob, 4, 4); // ear base
        pixmap.setColor(bodyShade);
        pixmap.fillRectangle(50, 52 + bob, 5, 2);

        // Legs
        int baseY = 12 + bob;
        pixmap.setColor(bodyShade);
        pixmap.fillRectangle(18, baseY + legSwing, 4, 16);
        pixmap.fillRectangle(28, baseY - legSwing, 4, 16);
        pixmap.fillRectangle(38, baseY + legSwing, 4, 16);
        pixmap.fillRectangle(46, baseY - legSwing, 3, 14);
        pixmap.setColor(hoof);
        pixmap.fillRectangle(18, baseY - 2 + legSwing, 4, 3);
        pixmap.fillRectangle(28, baseY - 2 - legSwing, 4, 3);
        pixmap.fillRectangle(38, baseY - 2 + legSwing, 4, 3);
        pixmap.fillRectangle(46, baseY - 2 - legSwing, 3, 3);

        // Mane + tail
        pixmap.setColor(mane);
        pixmap.fillRectangle(26, 50 + bob, 7, 6); // mane front
        pixmap.fillRectangle(30, 46 + bob, 9, 6); // mane back
        pixmap.fillRectangle(10, 34 + bob, 5, 12); // tail base (from rump)
        pixmap.setColor(maneShade);
        pixmap.fillRectangle(8, 32 + bob, 3, 12); // tail tip
        pixmap.fillRectangle(44, 58 + bob, 3, 4); // forelock

        // Saddle
        pixmap.setColor(darken(body, 0.3f));
        pixmap.fillRectangle(28, 38 + bob, 12, 5);
        pixmap.fillRectangle(26, 36 + bob, 6, 4);

        // Rider
        pixmap.setColor(riderOutfit);
        pixmap.fillRectangle(28, 44 + bob, 10, 6); // torso
        pixmap.fillRectangle(30, 40 + bob, 4, 5); // leg
        pixmap.fillRectangle(34, 40 + bob, 4, 5); // leg
        pixmap.setColor(riderSkin);
        pixmap.fillRectangle(30, 50 + bob, 6, 5); // head
        pixmap.setColor(riderHair);
        pixmap.fillRectangle(30, 54 + bob, 6, 3); // hair

        // Pet on saddle
        pixmap.setColor(petFur);
        pixmap.fillRectangle(36, 48 + bob, 6, 4); // body
        pixmap.fillRectangle(40, 50 + bob, 4, 3); // head
        pixmap.setColor(darken(petFur, 0.2f));
        pixmap.fillRectangle(38, 52 + bob, 2, 2); // ear

        // Outline (simple pass)
        pixmap.setColor(outline);
        pixmap.drawRectangle(16, 26 + bob, 31, 14);
        pixmap.drawRectangle(28, 35 + bob, 17, 9);
        pixmap.drawCircle(46, 40 + bob, 8);
        pixmap.drawRectangle(24, 40 + bob, 5, 9);

        // Eye highlight
        pixmap.setColor(Color.WHITE);
        pixmap.fillRectangle(48, 54 + bob, 2, 2);

        // Flip the pixmap vertically so the horse is upright in libGDX
        Pixmap flipped = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                flipped.drawPixel(x, y, pixmap.getPixel(x, size - 1 - y));
            }
        }
        Texture texture = new Texture(flipped);
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        pixmap.dispose();
        flipped.dispose();
        return texture;
    }

    private Color resolvePetFurColor() {
        if ("Kutya".equals(petName)) {
            return new Color(0.85f, 0.65f, 0.4f, 1f);
        }
        if ("Cica".equals(petName)) {
            return new Color(0.6f, 0.6f, 0.65f, 1f);
        }
        if ("Nyuszi".equals(petName)) {
            return new Color(0.95f, 0.9f, 0.75f, 1f);
        }
        if ("Papagáj".equals(petName)) {
            return new Color(0.2f, 0.75f, 0.45f, 1f);
        }
        if ("Kapibara".equals(petName)) {
            return new Color(0.7f, 0.5f, 0.3f, 1f);
        }
        if ("Lajhár".equals(petName)) {
            return new Color(0.6f, 0.7f, 0.5f, 1f);
        }
        return new Color(0.7f, 0.7f, 0.7f, 1f);
    }

    private Color resolveManeColor() {
        if ("Fekete".equals(maneColor)) {
            return new Color(0.12f, 0.12f, 0.14f, 1f);
        }
        if ("Csokoládé".equals(maneColor)) {
            return new Color(0.28f, 0.16f, 0.1f, 1f);
        }
        if ("Szürke".equals(maneColor)) {
            return new Color(0.56f, 0.56f, 0.6f, 1f);
        }
        if ("Szőke".equals(maneColor)) {
            return new Color(0.85f, 0.78f, 0.5f, 1f);
        }
        return new Color(0.32f, 0.26f, 0.2f, 1f);
    }

    private Color darken(Color color, float amount) {
        return new Color(
            Math.max(0f, color.r - amount),
            Math.max(0f, color.g - amount),
            Math.max(0f, color.b - amount),
            color.a
        );
    }

    private TextureRegion[] toRegions(Texture[] textures) {
        TextureRegion[] regions = new TextureRegion[textures.length];
        for (int i = 0; i < textures.length; i++) {
            regions[i] = new TextureRegion(textures[i]);
        }
        return regions;
    }

    private float clampCameraX(float targetX) {
        if (!mapHasBounds || camera == null) {
            return targetX;
        }
        float halfWidth = camera.viewportWidth * 0.5f;
        float min = scaledMapMinX + halfWidth;
        float max = scaledMapMaxX - halfWidth;
        if (min > max) {
            return (scaledMapMinX + scaledMapMaxX) * 0.5f;
        }
        return MathUtils.clamp(targetX, min, max);
    }

    private float clampCameraY(float targetY) {
        if (!mapHasBounds || camera == null) {
            return targetY;
        }
        float halfHeight = camera.viewportHeight * 0.5f;
        float min = scaledMapMinY + halfHeight;
        float max = scaledMapMaxY - halfHeight;
        if (min > max) {
            return (scaledMapMinY + scaledMapMaxY) * 0.5f;
        }
        return MathUtils.clamp(targetY, min, max);
    }

    private void updateMapScale(int screenWidth, int screenHeight) {
        if (mapPixelWidth <= 0f || mapPixelHeight <= 0f) {
            return;
        }
        float scaleX = screenWidth / mapPixelWidth;
        float scaleY = screenHeight / mapPixelHeight;
        float newScale = Math.min(scaleX, scaleY);
        if (Math.abs(newScale - mapScale) > 0.001f) {
            mapScale = newScale;
            if (mapRenderer != null) {
                mapRenderer.dispose();
            }
            mapRenderer = new OrthogonalTiledMapRenderer(map, mapScale);
        }
        scaledMapMinX = 0f;
        scaledMapMinY = 0f;
        scaledMapMaxX = mapPixelWidth * mapScale;
        scaledMapMaxY = mapPixelHeight * mapScale;
    }

    private void drawBackgroundFit(com.badlogic.gdx.graphics.g2d.Batch batch) {
        if (background == null || stage == null) {
            return;
        }
        float worldWidth = stage.getViewport().getWorldWidth();
        float worldHeight = stage.getViewport().getWorldHeight();
        float texWidth = background.getWidth();
        float texHeight = background.getHeight();
        float scale = Math.max(worldWidth / texWidth, worldHeight / texHeight);
        float drawWidth = texWidth * scale;
        float drawHeight = texHeight * scale;
        float x = (worldWidth - drawWidth) * 0.5f;
        float y = (worldHeight - drawHeight) * 0.5f;
        batch.draw(background, x, y, drawWidth, drawHeight);
    }
}
