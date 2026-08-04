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
import com.badlogic.gdx.math.Vector3;
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
    private static final float HUD_TEXT_WIDTH = 360f;
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
    private final MvpGameConfig.Difficulty difficulty;

    private Stage stage;
    private Texture background;
    private BitmapFont titleFont;
    private BitmapFont bodyFont;
    private Sound clickSound;
    private Sound powerupSound;
    private Sound winSound;
    private Sound jumpSound;
    private Sound obstacleSound;
    private Music raceMusic;
    private Texture hudPanel;
    private Label speedLabel;
    private Label lapLabel;
    private Label raceTimeLabel;
    private Label lapTimeLabel;
    private Label powerupLabel;
    private Label obstacleWarningLabel;
    private Label petBonusLabel;
    private Label jumpLabel;
    private Label npcLabel;
    private Label difficultyLabel;
    private Label resultLabel;
    private TextButton restartButton;
    private TextButton shopButton;
    private TextButton menuButton;
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
    private String[] npcNames;
    private int finalPlacement;
    private final String[] horses = horseNamesFromConfig();
    private final String[] riders = MvpGameConfig.RIDER_NAMES;
    private final String[] pets = MvpGameConfig.PET_LABELS;
    private float elapsedTime;
    private float lapElapsedTime;
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
    private boolean isometricMode = true;
    private static final float ISO_PROJECTION_SCALE = 0.42f;
    private static final float ISO_TRACK_HALF_WIDTH = 120f;
    private static final float ISO_FENCE_OFFSET = 160f;
    private com.badlogic.gdx.graphics.glutils.ShapeRenderer isoTerrain;
    private Array<PowerupDef> powerupDefs = new Array<>();
    private Array<PowerupSpawn> powerupSpawns = new Array<>();
    private Array<ObstacleSpawn> obstacleSpawns = new Array<>();
    private Texture powerupMarker;
    private Texture[] obstacleMarkers;
    private Texture npcMarker;
    private Texture[] forestDecorMarkers;
    private Texture shadowMarker;
    private Texture dustMarker;
    private Texture sparkleMarker;
    private Array<SparkleParticle> sparkleParticles = new Array<>();
    private float sparkleSpawnTimer;
    private Array<DustParticle> dustParticles = new Array<>();
    private float dustSpawnTimer;
    private float spawnTimer;
    private float obstacleSpawnTimer;
    private float nextSpawnDelay = 3.5f;
    private float nextObstacleSpawnDelay = 2.5f;
    private String activePowerupName;
    private float activePowerupTimer;
    private boolean powerupShieldActive;
    private float boostActiveTimer;
    private String activeObstacleName;
    private float activeObstacleTimer;
    private float obstacleSlowTimer;
    private float boundarySlowTimer;
    private float isoTrackBaseY = 64f;
    private static final float ISO_TRACK_SEGMENT_LENGTH = 256f;
    private static final float ISO_TRACK_MAX_OFFSET = 115f;
    private static final float ISO_TRACK_MAX_STEP = 52f;
    private final float[] isoTrackOffsets = new float[64];
    private float isoTrackOriginX = 0f;
    private Texture isoFenceMarker;
    private float boostChargePercent;
    private float petSpeedBonus;
    private float petAccelBonus;
    private float petShieldBonus;
    private float riderAccelerationBonus;
    private float riderBoostChargeBonus;
    private float jumpTimer;
    private float jumpCooldownTimer;
    private float upgradeMaxSpeedBonus;
    private float upgradeTurnMultiplier = 1f;
    private float upgradeJumpCooldownReduction;
    private float upgradeBoostMultiplierBonus;
    private float upgradeObstacleSlowMultiplier = MvpGameConfig.OBSTACLE_SLOWDOWN_MULTIPLIER;
    private boolean victoryPlayed;
    private boolean raceFinished;
    private boolean muted;
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
        this.difficulty = MvpGameConfig.Difficulty.EASY;
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
        this.difficulty = MvpGameConfig.Difficulty.EASY;
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
        this.difficulty = MvpGameConfig.Difficulty.EASY;
    }

    public RaceScreen(HorseGame game, String horseName, String riderName, String petName, String trackName,
                      String horseColor, String maneColor, String saddleColor, String outfitColor) {
        this(game, horseName, riderName, petName, trackName, horseColor, maneColor, saddleColor, outfitColor,
            MvpGameConfig.Difficulty.EASY);
    }

    public RaceScreen(HorseGame game, String horseName, String riderName, String petName, String trackName,
                      String horseColor, String maneColor, String saddleColor, String outfitColor,
                      MvpGameConfig.Difficulty difficulty) {
        this.game = game;
        this.horseName = horseName;
        this.riderName = riderName;
        this.petName = petName;
        this.trackName = trackName;
        this.horseColor = horseColor;
        this.maneColor = maneColor;
        this.saddleColor = saddleColor;
        this.outfitColor = outfitColor;
        this.difficulty = difficulty != null ? difficulty : MvpGameConfig.Difficulty.EASY;
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
        initializeIsoTrack();
        isoTerrain = new com.badlogic.gdx.graphics.glutils.ShapeRenderer();
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
        MvpProgress savedProgress = new MvpProgressStore(Gdx.app.getPreferences(MvpProgressStore.PREFS_NAME)).load();
        muted = savedProgress.muted;
        applyUpgradeBonuses(savedProgress);
        clickSound = game.getAssets().get("sfx/click.wav", Sound.class);
        powerupSound = game.getAssets().get("sfx/powerup.wav", Sound.class);
        winSound = game.getAssets().get("sfx/win.wav", Sound.class);
        jumpSound = clickSound;
        obstacleSound = powerupSound;
        raceMusic = game.getAssets().get("sfx/race_music.wav", Music.class);
        raceMusic.setLooping(true);
        raceMusic.setVolume(muted ? 0f : 0.5f);
        if (!muted) {
            raceMusic.play();
        }
        hudPanel = loadUiTexture("ui/panel_hud.png");
        loadHorseAnimations();
        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);
        runAnimation.setPlayMode(Animation.PlayMode.LOOP);
        powerupMarker = createPowerupMarker();
        obstacleMarkers = createObstacleMarkers();
        npcMarker = createNpcMarker();
        forestDecorMarkers = loadForestDecorMarkers();
        isoFenceMarker = createIsoFenceMarker();
        shadowMarker = createShadowMarker();
        dustMarker = createDustMarker();
        sparkleMarker = createSparkleMarker();
        loadPowerupDefs();
        horseIndex = findIndex(horses, horseName);
        riderIndex = findIndex(riders, riderName);
        petIndex = findIndex(pets, petName);
        npcNames = MvpGameConfig.npcNamesForSeed(trackName != null ? trackName.hashCode() : 0L);
        applyRiderBonus();
        horseTintColor = colorForHorseColor(resolveHorseColor(horseColor, savedProgress));
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
        TextButton boostButton = new TextButton("Boost", buttonStyle);
        TextButton jumpButton = new TextButton("Ugr\u00E1s", buttonStyle);
        restartButton = new TextButton("\u00DAj futam", buttonStyle);
        restartButton.setVisible(false);
        shopButton = new TextButton("Ist\u00E1ll\u00F3", buttonStyle);
        shopButton.setVisible(false);
        menuButton = new TextButton("F\u0151men\u00FC", buttonStyle);
        menuButton.setVisible(false);

        speedLabel = new Label("Sebess\u00E9g: 0 km/h", labelStyle);
        lapLabel = new Label("K\u00F6r: 1/3", labelStyle);
        raceTimeLabel = new Label("Idő: 00:00", labelStyle);
        lapTimeLabel = new Label("Köridő: 00:00", labelStyle);
        powerupLabel = new Label("B\u00F3nusz: --", labelStyle);
        obstacleWarningLabel = new Label("Akadály: nincs a közelben", labelStyle);
        petBonusLabel = new Label("Kedvenc b\u00F3nusz: --", labelStyle);
        jumpLabel = new Label("Ugr\u00E1s: k\u00E9sz", labelStyle);
        npcLabel = new Label(npcLabelText(), labelStyle);
        difficultyLabel = new Label("Neh\u00E9zs\u00E9g: " + difficultyLabelText(), labelStyle);
        resultLabel = new Label("Eredm\u00E9ny: --", labelStyle);
        coinLabel = new Label("\u00C9rm\u00E9k: 0", labelStyle);
        enableHudTextWrap(speedLabel);
        enableHudTextWrap(lapLabel);
        enableHudTextWrap(raceTimeLabel);
        enableHudTextWrap(lapTimeLabel);
        enableHudTextWrap(difficultyLabel);
        enableHudTextWrap(powerupLabel);
        enableHudTextWrap(obstacleWarningLabel);
        enableHudTextWrap(petBonusLabel);
        enableHudTextWrap(jumpLabel);
        enableHudTextWrap(npcLabel);
        enableHudTextWrap(resultLabel);
        enableHudTextWrap(coinLabel);
        // directionLabel = new Label("Ir\u00E1ny:", labelStyle);
            // Joystick control only, remove left/right buttons from UI
            // directionLabel can remain for feedback if desired
        // Joystick input now handled via touchpad knob percent in render().

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!muted && clickSound != null) {
                    clickSound.play(0.6f);
                }
                ScreenNavigator.Selection selection = new ScreenNavigator.Selection(
                    horseName, riderName, petName, horseColor, maneColor, saddleColor, outfitColor, difficulty
                );
                ScreenNavigator.toCharacterSelect(game, selection);
            }
        });
        jumpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                triggerJump();
            }
        });
        boostButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                triggerBoost();
            }
        });
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!muted && clickSound != null) {
                    clickSound.play(0.6f);
                }
                ScreenNavigator.Selection selection = new ScreenNavigator.Selection(
                    horseName, riderName, petName, horseColor, maneColor, saddleColor, outfitColor, difficulty
                );
                ScreenNavigator.toDefaultRace(game, selection);
            }
        });
        shopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!muted && clickSound != null) {
                    clickSound.play(0.6f);
                }
                ScreenNavigator.toShop(game);
            }
        });
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!muted && clickSound != null) {
                    clickSound.play(0.6f);
                }
                ScreenNavigator.toMainMenu(game);
            }
        });

        Table hudTable = new Table();
        hudTable.setFillParent(true);
        hudTable.top().left().pad(16f);
        Table hudContent = new Table();
        hudContent.setBackground(toDrawable(hudPanel));
        hudContent.pad(12f);
        hudContent.add(speedLabel).width(HUD_TEXT_WIDTH).left().row();
        hudContent.add(lapLabel).width(HUD_TEXT_WIDTH).left().padTop(6f).row();
        hudContent.add(raceTimeLabel).width(HUD_TEXT_WIDTH).left().padTop(6f).row();
        hudContent.add(lapTimeLabel).width(HUD_TEXT_WIDTH).left().padTop(6f).row();
        hudContent.add(difficultyLabel).width(HUD_TEXT_WIDTH).left().padTop(6f).row();
        hudContent.add(powerupLabel).width(HUD_TEXT_WIDTH).left().padTop(6f);
        hudContent.row();
        hudContent.add(obstacleWarningLabel).width(HUD_TEXT_WIDTH).left().padTop(6f);
        hudContent.row();
        hudContent.add(petBonusLabel).width(HUD_TEXT_WIDTH).left().padTop(6f);
        hudContent.row();
        hudContent.add(jumpLabel).width(HUD_TEXT_WIDTH).left().padTop(6f);
        hudContent.row();
        hudContent.add(npcLabel).width(HUD_TEXT_WIDTH).left().padTop(6f);
        hudContent.row();
        hudContent.add(resultLabel).width(HUD_TEXT_WIDTH).left().padTop(6f);
        hudContent.row();
        hudContent.add(coinLabel).width(HUD_TEXT_WIDTH).left().padTop(6f);
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
        backButtonTable.add(backButton).width(220f).height(80f).row();
        backButtonTable.add(boostButton).width(220f).height(80f).padTop(12f).row();
        backButtonTable.add(jumpButton).width(220f).height(80f).padTop(12f).row();
        backButtonTable.add(restartButton).width(220f).height(80f).padTop(12f).row();
        backButtonTable.add(shopButton).width(220f).height(80f).padTop(12f).row();
        backButtonTable.add(menuButton).width(220f).height(80f).padTop(12f);
        stage.addActor(backButtonTable);
        stage.addActor(hudTable);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
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
        if (riderName != null) {
            switch (riderName) {
                case "Szandi":
                case "Bogi":
                case "Lili":
                case "Panni":
                case "Zsófi":
                    return "sprites/pixel_rider_girl.png";
                default:
                    break;
            }
        }
        return "sprites/pixel_rider_boy.png";
    }

    private String getPetPreviewAsset(String petName) {
        if ("Cica".equals(petName)) {
            return "sprites/pixel_pet_cat.png";
        }
        if ("Nyuszi".equals(petName)) {
            return "sprites/pixel_pet_rabbit.png";
        }
        if ("Papagáj".equals(petName)) {
            return "sprites/pixel_pet_parrot.png";
        }
        return "sprites/pixel_pet_dog.png";
    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        if (!raceFinished) {
            elapsedTime += delta;
        }
        if (!raceFinished) {
            lapElapsedTime += delta;
        }
        if (Math.abs(joystickX) < 0.01f) {
            joystickX = 0f;
        }
        if (Math.abs(joystickY) < 0.01f) {
            joystickY = 0f;
        }
        boolean accelerating = joystickPointer != -1 && !raceFinished;
        float slowMultiplier = (obstacleSlowTimer > 0f || boundarySlowTimer > 0f) ? upgradeObstacleSlowMultiplier : 1f;
        float boostMultiplier = boostActiveTimer > 0f
            ? MvpGameConfig.BOOST_SPEED_MULTIPLIER + upgradeBoostMultiplierBonus
            : 1f;
        float effectiveMaxSpeed = (maxSpeed + petSpeedBonus + upgradeMaxSpeedBonus) * slowMultiplier * boostMultiplier;
        float effectiveAccel = (acceleration + petAccelBonus) * (1f + riderAccelerationBonus) * boostMultiplier;
        if (raceFinished) {
            speed = Math.max(0f, speed - deceleration * delta);
        } else if (accelerating) {
            speed = Math.min(effectiveMaxSpeed, speed + effectiveAccel * delta);
        } else {
            speed = Math.max(0f, speed - deceleration * delta);
        }
        if (!raceFinished) {
            distance += speed * delta;
            updateRaceCompletion();
        }
        updateJump(delta);
        updateBoost(delta);
        updateObstacleSlowdown(delta);
        updateBoundarySlowdown(delta);
        updateDustParticles(delta);
        if (!raceFinished) {
            updatePowerupSpawns(delta);
            updatePowerupPickup(delta);
            updateObstacleSpawns(delta);
            updateObstacleHits();
        }
        int lap = Math.min(3, 1 + (int) (distance / lapDistance));
        if (lap != currentLap) {
            lapElapsedTime = 0f;
            currentLap = lap;
            if (currentLap == 3 && !victoryPlayed) {
                victoryPlayed = true;
                if (!muted && winSound != null) {
                    winSound.play(0.7f);
                }
                Gdx.input.vibrate(120);
            }
        }
        speedLabel.setText("Sebess\u00E9g: " + (int) speed + " km/h");
        updateRaceTimeLabel();
        if (lapTimeLabel != null) {
            lapTimeLabel.setText("Köridő: " + formatRaceTime(lapElapsedTime));
        }
        lapLabel.setText("K\u00F6r: " + currentLap + "/3");
        if (activeObstacleName != null) {
            powerupLabel.setText("Akad\u00E1ly: " + activeObstacleName + " (" + (int) Math.ceil(activeObstacleTimer) + "s)");
        } else if (activePowerupName != null) {
            powerupLabel.setText("B\u00F3nusz: " + activePowerupName + " (" + (int) Math.ceil(activePowerupTimer) + "s)");
        } else if (boostActiveTimer > 0f) {
            powerupLabel.setText("Boost akt\u00EDv: " + (int) Math.ceil(boostActiveTimer) + "s");
        } else {
            powerupLabel.setText("Boost: " + Math.round(boostChargePercent) + "%");
        }
        if (obstacleWarningLabel != null) {
            obstacleWarningLabel.setText(obstacleWarningText());
        }
        animationTime += delta;
        updateCoinLabel();
        if (isometricMode) {
            updateIsometricMovement(delta);
            renderIsometricScene();
        } else if (mapLoaded && mapRenderer != null && camera != null) {
            // Draw a full-screen background so any unused map area isn't black.
            stage.getBatch().begin();
            drawBackgroundFit(stage.getBatch());
            stage.getBatch().end();

            // Free movement: joystick controls both X and Y
            horseX += speed * delta * joystickX * upgradeTurnMultiplier;
            horseY += speed * delta * joystickY * upgradeTurnMultiplier;
            if (Math.abs(joystickX) > 0.01f || Math.abs(joystickY) > 0.01f) {
                updateHorseDirection(joystickX);
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
            camera.zoom = cameraZoomForSpeed(speed);
            camera.up.set(0f, 1f, 0f).rotate(Vector3.Z, cameraRotationForSpeed(speed));
            camera.update();
            mapRenderer.setView(camera);
            mapRenderer.render();
            mapRenderer.getBatch().begin();
            drawForestDecorations(mapRenderer.getBatch(), true, false);
            drawPowerups(mapRenderer.getBatch());
            drawObstacles(mapRenderer.getBatch());
            drawNpcRacers(mapRenderer.getBatch(), true);
            drawDustParticles(mapRenderer.getBatch(), true);
            drawSparkles(mapRenderer.getBatch(), true);
            drawHorseAnimation(mapRenderer.getBatch(), true);
            drawForestDecorations(mapRenderer.getBatch(), true, true);
            mapRenderer.getBatch().end();
        } else {
            stage.getBatch().begin();
            drawBackgroundFit(stage.getBatch());
            drawForestDecorations(stage.getBatch(), false, false);
            drawPowerups(stage.getBatch());
            drawObstacles(stage.getBatch());
            drawNpcRacers(stage.getBatch(), false);
            drawDustParticles(stage.getBatch(), false);
            drawSparkles(stage.getBatch(), false);
            drawHorseAnimation(stage.getBatch(), false);
            drawForestDecorations(stage.getBatch(), false, true);
            stage.getBatch().end();
        }
        stage.act(delta);
        stage.draw();
    }

    private void updateBoundarySlowdown(float delta) {
        if (boundarySlowTimer > 0f) {
            boundarySlowTimer = Math.max(0f, boundarySlowTimer - delta);
        }
    }

    private void updateHorseDirection(float horizontalInput) {
        if (Math.abs(horizontalInput) > 0.01f) {
            horseDirection = horizontalInput > 0f ? 1f : -1f;
        }
    }

    private void updateIsometricMovement(float delta) {
        horseX += speed * delta * joystickX * upgradeTurnMultiplier;
        horseY += speed * delta * joystickY * upgradeTurnMultiplier;
        if (Math.abs(joystickX) > 0.01f || Math.abs(joystickY) > 0.01f) {
            horseDirection = joystickX >= 0f ? 1f : -1f;
        }
        float trackCenter = isoTrackCenterY(horseX);
        float trackHalfWidth = ISO_TRACK_HALF_WIDTH;
        boolean fenceHit = horseY < trackCenter - trackHalfWidth || horseY > trackCenter + trackHalfWidth;
        if (fenceHit) {
            horseY = MathUtils.clamp(horseY, trackCenter - trackHalfWidth, trackCenter + trackHalfWidth);
            boundarySlowTimer = 1.25f;
            activeObstacleName = "Kerítés érintés";
            activeObstacleTimer = 1.25f;
            playSound(obstacleSound, 0.35f);
        }
        if (mapHasBounds) {
            float minX = mapBoundsMinX + horseBoundsPadding;
            float maxX = mapBoundsMaxX - horseBoundsPadding;
            horseX = MathUtils.clamp(horseX, minX, maxX);
            horseY = MathUtils.clamp(horseY, mapBoundsMinY + horseBoundsPadding, mapBoundsMaxY - horseBoundsPadding);
        }
    }

    private void initializeIsoTrack() {
        // Generate a new world-space centerline for every RaceScreen instance.
        // Bounded steps plus interpolation keep the route smooth and rideable.
        isoTrackOriginX = 0f;
        isoTrackOffsets[0] = 0f;
        for (int i = 1; i < isoTrackOffsets.length; i++) {
            float previous = isoTrackOffsets[i - 1];
            float next = previous + MathUtils.random(-ISO_TRACK_MAX_STEP, ISO_TRACK_MAX_STEP);
            isoTrackOffsets[i] = MathUtils.clamp(next, -ISO_TRACK_MAX_OFFSET, ISO_TRACK_MAX_OFFSET);
        }
    }

    private float isoTrackCenterY(float worldX) {
        float segmentPosition = (worldX - isoTrackOriginX) / ISO_TRACK_SEGMENT_LENGTH;
        if (segmentPosition <= 0f) {
            return isoTrackBaseY + isoTrackOffsets[0];
        }
        int left = MathUtils.floor(segmentPosition);
        if (left >= isoTrackOffsets.length - 1) {
            return isoTrackBaseY + isoTrackOffsets[isoTrackOffsets.length - 1];
        }
        float fraction = segmentPosition - left;
        float offset = MathUtils.lerp(isoTrackOffsets[left], isoTrackOffsets[left + 1], fraction);
        return isoTrackBaseY + offset;
    }

    private void renderIsometricScene() {
        stage.getBatch().begin();
        drawBackgroundFit(stage.getBatch());
        stage.getBatch().end();

        drawIsometricTerrain();
        stage.getBatch().begin();
        drawIsometricFences(stage.getBatch(), false);
        drawForestDecorations(stage.getBatch(), true, false);
        drawPowerups(stage.getBatch());
        drawObstacles(stage.getBatch());
        drawNpcRacers(stage.getBatch(), true);
        drawDustParticles(stage.getBatch(), true);
        drawSparkles(stage.getBatch(), true);
        drawHorseAnimation(stage.getBatch(), true);
        drawForestDecorations(stage.getBatch(), true, true);
        drawIsometricFences(stage.getBatch(), true);
        stage.getBatch().end();
    }

    private com.badlogic.gdx.math.Vector2 projectIso(float worldX, float worldY) {
        float dx = (worldX - horseX) * ISO_PROJECTION_SCALE;
        float dy = (worldY - horseY) * ISO_PROJECTION_SCALE;
        float centerX = stage.getViewport().getWorldWidth() * 0.5f;
        float centerY = stage.getViewport().getWorldHeight() * 0.60f;
        return new com.badlogic.gdx.math.Vector2(centerX + dx - dy, centerY + (dx + dy) * 0.42f);
    }

    private void drawIsometricTerrain() {
        if (isoTerrain == null || stage == null) {
            return;
        }
        float tile = 64f;
        float halfWidth = tile * 0.55f;
        float halfHeight = tile * 0.23f;
        // Derive the world coverage from the viewport. Fixed ranges leave
        // empty bands on wide phones and tablets after isometric projection.
        float viewportWidth = stage.getViewport().getWorldWidth();
        float viewportHeight = stage.getViewport().getWorldHeight();
        int tilesX = Math.max(32, MathUtils.ceil(viewportWidth / (tile * ISO_PROJECTION_SCALE)) + 8);
        int tilesY = Math.max(24, MathUtils.ceil(viewportHeight / (tile * ISO_PROJECTION_SCALE)) + 8);
        isoTerrain.setProjectionMatrix(stage.getCamera().combined);
        isoTerrain.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
        for (int ix = -tilesX; ix <= tilesX; ix++) {
            float worldX = horseX + ix * tile;
            float trackCenter = isoTrackCenterY(worldX);
            for (int iy = -tilesY; iy <= tilesY; iy++) {
                float worldY = horseY + iy * tile;
                com.badlogic.gdx.math.Vector2 p = projectIso(worldX, worldY);
                float lateralDistance = Math.abs(worldY - trackCenter);
                boolean path = lateralDistance <= ISO_TRACK_HALF_WIDTH;
                boolean shoulder = lateralDistance <= ISO_FENCE_OFFSET + 24f;
                float red = path ? 0.50f : (shoulder ? 0.28f : 0.12f);
                float green = path ? 0.34f : (shoulder ? 0.46f : 0.30f);
                float blue = path ? 0.18f : (shoulder ? 0.20f : 0.16f);
                isoTerrain.setColor(red, green, blue, 1f);
                isoTerrain.triangle(p.x, p.y - halfHeight, p.x + halfWidth, p.y,
                    p.x, p.y + halfHeight);
                isoTerrain.triangle(p.x, p.y - halfHeight, p.x, p.y + halfHeight,
                    p.x - halfWidth, p.y);
            }
        }
        isoTerrain.end();
    }

    private void drawIsometricFences(com.badlogic.gdx.graphics.g2d.Batch batch, boolean foreground) {
        if (isoFenceMarker == null) {
            return;
        }
        float tile = 64f;
        float fenceOffset = ISO_FENCE_OFFSET;
        int side = foreground ? 1 : -1;
        int fenceTiles = Math.max(24, MathUtils.ceil(stage.getViewport().getWorldWidth() / (tile * ISO_PROJECTION_SCALE)) + 8);
        for (int ix = -fenceTiles; ix <= fenceTiles; ix++) {
            float worldX = horseX + ix * tile;
            float trackCenter = isoTrackCenterY(worldX);
            float worldY = trackCenter + side * fenceOffset;
            com.badlogic.gdx.math.Vector2 point = projectIso(worldX, worldY);
            float depth = 0.82f + MathUtils.clamp((point.y / stage.getViewport().getWorldHeight()) * 0.22f, 0f, 0.22f);
            float width = 24f * depth;
            float height = 58f * depth;
            batch.draw(isoFenceMarker, point.x - width * 0.5f, point.y - height + 8f, width, height);
        }
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
        finalPlacement = calculatePlacement(elapsedTime);
        int horseshoeReward = MvpGameConfig.horseshoeReward(finalPlacement, difficulty);
        int xpReward = MvpGameConfig.raceXp(finalPlacement, difficulty, recordBroken);
        progress.applyRaceResult(finalPlacement, difficulty, recordBroken);
        if (recordBroken) {
            progress.recordTime = formatRaceTime(elapsedTime);
        }
        progressStore.save(progress);
        playerCoins = progress.horseshoes;
        updateCoinLabel();
        if (resultLabel != null) {
            String bestTimeText = progress.recordTime != null && progress.recordTime.length() > 0
                ? progress.recordTime
                : formatRaceTime(elapsedTime);
            resultLabel.setText(placementHeadline(finalPlacement) + " Eredm\u00E9ny: " + finalPlacement + ". hely, +" + horseshoeReward + " patk\u00F3, +" + xpReward + " XP"
                + ", id\u0151: " + formatRaceTime(elapsedTime)
                + ", legjobb: " + bestTimeText
                + (recordBroken ? ", \u00FAj rekord!" : ""));
        }
        if (npcLabel != null) {
            npcLabel.setText(finishOrderText(elapsedTime));
        }
        if (restartButton != null) {
            restartButton.setVisible(true);
        }
        if (shopButton != null) {
            shopButton.setVisible(true);
        }
        if (menuButton != null) {
            menuButton.setVisible(true);
        }
        if (!muted && winSound != null) {
            winSound.play(0.7f);
        }
        try {
            Gdx.input.vibrate(120);
        } catch (SecurityException ignored) {
            // VIBRATE permission missing or restricted; ignore to avoid crash.
        }
    }

    private void triggerJump() {
        if (jumpCooldownTimer > 0f) {
            return;
        }
        jumpTimer = 0.45f;
        jumpCooldownTimer = Math.max(0.4f, 0.8f - upgradeJumpCooldownReduction);
        playSound(jumpSound, 0.55f);
        if (jumpLabel != null) {
            jumpLabel.setText("Ugr\u00E1s: hopp!");
        }
        try {
            Gdx.input.vibrate(40);
        } catch (SecurityException ignored) {
            // VIBRATE permission missing or restricted; ignore to avoid crash.
        }
    }

    private void updateJump(float delta) {
        if (jumpTimer > 0f) {
            jumpTimer = Math.max(0f, jumpTimer - delta);
        }
        if (jumpCooldownTimer > 0f) {
            jumpCooldownTimer = Math.max(0f, jumpCooldownTimer - delta);
        }
        if (jumpLabel == null) {
            return;
        }
        if (jumpTimer > 0f) {
            jumpLabel.setText("Ugr\u00E1s: hopp!");
        } else if (jumpCooldownTimer > 0f) {
            jumpLabel.setText("Ugr\u00E1s: " + (int) Math.ceil(jumpCooldownTimer) + "s");
        } else {
            jumpLabel.setText("Ugr\u00E1s: k\u00E9sz");
        }
    }

    private void triggerBoost() {
        if (boostActiveTimer > 0f || boostChargePercent < MvpGameConfig.BOOST_ACTIVATION_COST_PERCENT) {
            return;
        }
        boostChargePercent = Math.max(0f, boostChargePercent - MvpGameConfig.BOOST_ACTIVATION_COST_PERCENT);
        boostActiveTimer = MvpGameConfig.BOOST_ACTIVE_SECONDS;
        if (!muted && powerupSound != null) {
            powerupSound.play(0.7f);
        }
        try {
            Gdx.input.vibrate(55);
        } catch (SecurityException ignored) {
            // VIBRATE permission missing or restricted; ignore to avoid crash.
        }
    }

    private void updateBoost(float delta) {
        for (int i = sparkleParticles.size - 1; i >= 0; i--) {
            SparkleParticle particle = sparkleParticles.get(i);
            particle.life -= delta;
            particle.x += particle.velocityX * delta;
            particle.y += particle.velocityY * delta;
            if (particle.life <= 0f) {
                sparkleParticles.removeIndex(i);
            }
        }
        if (boostActiveTimer > 0f) {
            boostActiveTimer = Math.max(0f, boostActiveTimer - delta);
            sparkleSpawnTimer += delta;
            if (sparkleSpawnTimer >= 0.06f && sparkleParticles.size < 24) {
                sparkleSpawnTimer = 0f;
                sparkleParticles.add(new SparkleParticle(
                    horseX - horseDirection * 20f + MathUtils.random(-10f, 10f),
                    horseY + MathUtils.random(-12f, 12f),
                    MathUtils.random(-10f, 10f),
                    MathUtils.random(8f, 18f)
                ));
            }
        } else {
            sparkleSpawnTimer = 0f;
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

    private int calculatePlacement(float playerTimeSeconds) {
        int placement = 1;
        for (int i = 0; i < MvpGameConfig.NPC_COUNT; i++) {
            if (npcFinishTimeSeconds(i) < playerTimeSeconds) {
                placement++;
            }
        }
        return MathUtils.clamp(placement, 1, MvpGameConfig.TOTAL_RACERS);
    }

    private float npcFinishTimeSeconds(int npcIndex) {
        int trackSeed = trackName != null ? trackName.hashCode() : 0;
        int mixedSeed = Math.abs(trackSeed + npcIndex * 97 + difficulty.ordinal() * 193);
        float variance = (mixedSeed % 700) / 100f;
        float baseTime;
        if (difficulty == MvpGameConfig.Difficulty.HARD) {
            baseTime = 29f;
        } else if (difficulty == MvpGameConfig.Difficulty.MEDIUM) {
            baseTime = 37f;
        } else {
            baseTime = 45f;
        }
        return baseTime + variance + npcIndex * 0.8f;
    }

    private String finishOrderText(float playerTimeSeconds) {
        String[] names = new String[MvpGameConfig.TOTAL_RACERS];
        float[] times = new float[MvpGameConfig.TOTAL_RACERS];
        names[0] = "Te";
        times[0] = playerTimeSeconds;
        for (int i = 0; i < MvpGameConfig.NPC_COUNT; i++) {
            names[i + 1] = npcNames != null && i < npcNames.length ? npcNames[i] : "NPC " + (i + 1);
            times[i + 1] = npcFinishTimeSeconds(i);
        }
        sortFinishers(names, times);
        return "Dobog\u00F3: 1. " + names[0] + ", 2. " + names[1] + ", 3. " + names[2];
    }

    private void sortFinishers(String[] names, float[] times) {
        for (int i = 0; i < times.length - 1; i++) {
            for (int j = i + 1; j < times.length; j++) {
                if (times[j] < times[i]) {
                    float time = times[i];
                    times[i] = times[j];
                    times[j] = time;
                    String name = names[i];
                    names[i] = names[j];
                    names[j] = name;
                }
            }
        }
    }

    private float npcRaceProgress(int npcIndex) {
        return MathUtils.clamp(elapsedTime / npcFinishTimeSeconds(npcIndex), 0f, 1f);
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
        if (isoTerrain != null) {
            isoTerrain.dispose();
        }
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
        disposeTextureArray(obstacleMarkers);
        if (npcMarker != null) {
            npcMarker.dispose();
        }
        disposeTextureArray(forestDecorMarkers);
        if (isoFenceMarker != null) {
            isoFenceMarker.dispose();
        }
        if (shadowMarker != null) {
            shadowMarker.dispose();
        }
        if (dustMarker != null) {
            dustMarker.dispose();
        }
        if (sparkleMarker != null) {
            sparkleMarker.dispose();
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
        if ("Cica".equals(petName)) {
            petAccelBonus = 5f;
        } else if ("Nyuszi".equals(petName)) {
            petAccelBonus = 3f;
            petSpeedBonus = 3f;
        } else if ("Papagáj".equals(petName)) {
            petShieldBonus = 1f;
        }
        if (petBonusLabel != null) {
            petBonusLabel.setText("Kedvenc bónusz: " + MvpGameConfig.petBonusDescription(petName));
        }
    }

    private void applyRiderBonus() {
        riderAccelerationBonus = 0f;
        riderBoostChargeBonus = 0f;
        MvpGameConfig.RiderBonus bonus = MvpGameConfig.riderBonusForIndex(riderIndex);
        if (bonus.type == MvpGameConfig.RiderBonusType.ACCELERATION) {
            riderAccelerationBonus = bonus.value;
        } else if (bonus.type == MvpGameConfig.RiderBonusType.BOOST_CHARGE) {
            riderBoostChargeBonus = bonus.value;
        }
    }

    private void applyUpgradeBonuses(MvpProgress progress) {
        upgradeMaxSpeedBonus = 0f;
        upgradeTurnMultiplier = 1f;
        upgradeJumpCooldownReduction = 0f;
        upgradeBoostMultiplierBonus = 0f;
        upgradeObstacleSlowMultiplier = MvpGameConfig.OBSTACLE_SLOWDOWN_MULTIPLIER;
        if (progress == null || progress.upgradeLevels == null) {
            return;
        }
        int speedLevel = upgradeLevel(progress, 0);
        int turningLevel = upgradeLevel(progress, 1);
        int jumpLevel = upgradeLevel(progress, 2);
        int boostLevel = upgradeLevel(progress, 3);
        int slowReductionLevel = upgradeLevel(progress, 4);
        upgradeMaxSpeedBonus = speedLevel * 3f;
        upgradeTurnMultiplier = 1f + turningLevel * 0.06f;
        upgradeJumpCooldownReduction = jumpLevel * 0.12f;
        upgradeBoostMultiplierBonus = boostLevel * 0.08f;
        upgradeObstacleSlowMultiplier = Math.min(0.85f, MvpGameConfig.OBSTACLE_SLOWDOWN_MULTIPLIER + slowReductionLevel * 0.10f);
    }

    private int upgradeLevel(MvpProgress progress, int index) {
        if (index < 0 || index >= progress.upgradeLevels.length) {
            return 0;
        }
        return Math.max(0, progress.upgradeLevels[index]);
    }

    private String npcLabelText() {
        if (npcNames == null || npcNames.length == 0) {
            return "Ellenfelek: --";
        }
        StringBuilder builder = new StringBuilder("Ellenfelek: ");
        for (int i = 0; i < npcNames.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(npcNames[i]);
        }
        return builder.toString();
    }

    private String difficultyLabelText() {
        if (difficulty == MvpGameConfig.Difficulty.HARD) {
            return "Neh\u00E9z";
        }
        if (difficulty == MvpGameConfig.Difficulty.MEDIUM) {
            return "K\u00F6zepes";
        }
        return "K\u00F6nny\u0171";
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
                powerupShieldActive = false;
            }
        }
        for (int i = powerupSpawns.size - 1; i >= 0; i--) {
            PowerupSpawn spawn = powerupSpawns.get(i);
            float dx = spawn.x - horseX;
            float dy = spawn.y - horseY;
            if (dx * dx + dy * dy <= 24f * 24f) {
                powerupSpawns.removeIndex(i);
                if ("speed_burst".equals(spawn.id)) {
                    boostActiveTimer = Math.max(boostActiveTimer, 3f);
                    activePowerupName = "Gyorsító";
                    activePowerupTimer = 3f;
                } else if ("shield".equals(spawn.id)) {
                    powerupShieldActive = true;
                    activePowerupName = "Pajzs";
                    activePowerupTimer = 5f;
                } else {
                    boostChargePercent = Math.min(
                        100f,
                        boostChargePercent + MvpGameConfig.BOOST_POWERUP_CHARGE_PERCENT * (1f + riderBoostChargeBonus)
                    );
                    activePowerupName = "+" + MvpGameConfig.BOOST_POWERUP_CHARGE_PERCENT + "% boost";
                    activePowerupTimer = 1.5f;
                }
                if (!muted && powerupSound != null) {
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

    private void updateObstacleSlowdown(float delta) {
        if (obstacleSlowTimer > 0f) {
            obstacleSlowTimer = Math.max(0f, obstacleSlowTimer - delta);
        }
        if (activeObstacleTimer > 0f) {
            activeObstacleTimer = Math.max(0f, activeObstacleTimer - delta);
            if (activeObstacleTimer == 0f) {
                activeObstacleName = null;
            }
        }
    }

    private void updateObstacleSpawns(float delta) {
        obstacleSpawnTimer += delta;
        if (MvpGameConfig.FOREST_OBSTACLES.length == 0) {
            return;
        }
        if (obstacleSpawnTimer >= nextObstacleSpawnDelay) {
            obstacleSpawnTimer = 0f;
            nextObstacleSpawnDelay = MathUtils.random(4f, 7f);
            if (obstacleSpawns.size < 4) {
                MvpGameConfig.ObstacleType type = MvpGameConfig.FOREST_OBSTACLES[MathUtils.random(MvpGameConfig.FOREST_OBSTACLES.length - 1)];
                float x = horseX + MathUtils.random(180f, 420f);
                float y = horseY + MathUtils.random(-70f, 70f);
                if (mapHasBounds) {
                    x = MathUtils.clamp(x, mapBoundsMinX + horseBoundsPadding, mapBoundsMaxX - horseBoundsPadding);
                    y = MathUtils.clamp(y, mapBoundsMinY + horseBoundsPadding, mapBoundsMaxY - horseBoundsPadding);
                }
                obstacleSpawns.add(new ObstacleSpawn(type.id, type.label, x, y));
            }
        }
    }

    private void updateObstacleHits() {
        for (int i = obstacleSpawns.size - 1; i >= 0; i--) {
            ObstacleSpawn spawn = obstacleSpawns.get(i);
            float dx = spawn.x - horseX;
            float dy = spawn.y - horseY;
            if (dx * dx + dy * dy <= 28f * 28f) {
                obstacleSpawns.removeIndex(i);
                if (powerupShieldActive) {
                    powerupShieldActive = false;
                    activePowerupName = "Pajzs védett";
                    activePowerupTimer = 1.2f;
                    continue;
                }
                if (jumpTimer > 0f) {
                    activeObstacleName = spawn.label + " \u00E1tugorva";
                    activeObstacleTimer = 0.8f;
                } else {
                    speed *= upgradeObstacleSlowMultiplier;
                    obstacleSlowTimer = MvpGameConfig.OBSTACLE_SLOWDOWN_SECONDS;
                    activeObstacleName = spawn.label + " lass\u00EDt";
                    activeObstacleTimer = MvpGameConfig.OBSTACLE_SLOWDOWN_SECONDS;
                    playSound(obstacleSound, 0.45f);
                    try {
                        Gdx.input.vibrate(70);
                    } catch (SecurityException ignored) {
                        // VIBRATE permission missing or restricted; ignore to avoid crash.
                    }
                }
            }
        }
    }

    private float cameraZoomForSpeed(float currentSpeed) {
        float speedRatio = maxSpeed <= 0f ? 0f : MathUtils.clamp(currentSpeed / maxSpeed, 0f, 1f);
        return 1.05f - speedRatio * 0.12f;
    }

    private float cameraRotationForSpeed(float currentSpeed) {
        float speedRatio = maxSpeed <= 0f ? 0f : MathUtils.clamp(currentSpeed / maxSpeed, 0f, 1f);
        return horseDirection * speedRatio * 1.5f;
    }

    private String placementHeadline(int placement) {
        return placement >= 1 && placement <= 3 ? "Dobogó!" : "Futam vége!";
    }

    private void updateRaceTimeLabel() {
        if (raceTimeLabel == null) {
            return;
        }
        String timeText = formatRaceTime(elapsedTime);
        if (raceFinished) {
            raceTimeLabel.setText("Idő: " + timeText);
            return;
        }
        float finishDistance = lapDistance * 3f;
        if (distance >= finishDistance - 90f) {
            raceTimeLabel.setText("Idő: " + timeText + " — CÉL KÖZELEG!");
        } else {
            raceTimeLabel.setText("Idő: " + timeText);
        }
    }

    private String obstacleWarningText() {
        if (raceFinished) {
            return "Akadály: futam vége";
        }
        ObstacleSpawn nearest = null;
        float nearestDistance = Float.MAX_VALUE;
        for (ObstacleSpawn spawn : obstacleSpawns) {
            float dx = spawn.x - horseX;
            float dy = spawn.y - horseY;
            if (dx <= 0f || Math.abs(dy) > 95f) {
                continue;
            }
            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            if (distance < nearestDistance) {
                nearest = spawn;
                nearestDistance = distance;
            }
        }
        if (nearest == null || nearestDistance > 165f) {
            return "Akadály: nincs a közelben";
        }
        if (jumpTimer > 0f) {
            return "Akadály: " + nearest.label + " — ugrás aktív";
        }
        return "Figyelem: " + nearest.label + " közeleg — készülj ugrani!";
    }

    private void playSound(Sound sound, float volume) {
        if (!muted && sound != null) {
            sound.play(volume);
        }
    }

    private void enableHudTextWrap(Label label) {
        if (label != null) {
            label.setWrap(true);
        }
    }

    private void drawPowerups(com.badlogic.gdx.graphics.g2d.Batch batch) {
        if (powerupMarker == null) {
            return;
        }
        float scale = isometricMode ? 1f : (mapLoaded ? mapScale : 1f);
        for (PowerupSpawn spawn : powerupSpawns) {
            com.badlogic.gdx.math.Vector2 point = isometricMode ? projectIso(spawn.x, spawn.y) : null;
            float x = isometricMode ? point.x : spawn.x * scale;
            float y = isometricMode ? point.y : spawn.y * scale;
            drawEntityShadow(batch, x, y - 7f * scale, 14f * scale, 5f * scale, 0.18f);
            batch.draw(powerupMarker, x - 10f * scale, y - 10f * scale, 20f * scale, 20f * scale);
        }
    }

    private void drawObstacles(com.badlogic.gdx.graphics.g2d.Batch batch) {
        if (obstacleMarkers == null) {
            return;
        }
        float scale = isometricMode ? 1f : (mapLoaded ? mapScale : 1f);
        for (ObstacleSpawn spawn : obstacleSpawns) {
            Texture marker = obstacleMarkerFor(spawn.id);
            if (marker == null) {
                continue;
            }
            com.badlogic.gdx.math.Vector2 point = isometricMode ? projectIso(spawn.x, spawn.y) : null;
            float x = isometricMode ? point.x : spawn.x * scale;
            float y = isometricMode ? point.y : spawn.y * scale;
            drawEntityShadow(batch, x, y - 10f * scale, 28f * scale, 8f * scale, 0.22f);
            batch.draw(marker, x - 16f * scale, y - 12f * scale, 32f * scale, 24f * scale);
        }
    }

    private void drawNpcRacers(com.badlogic.gdx.graphics.g2d.Batch batch, boolean mapSpace) {
        if (npcMarker == null || npcNames == null) {
            return;
        }
        float scale = mapSpace ? mapScale : 1f;
        float[] laneOffsets = {-58f, -26f, 28f, 62f};
        for (int i = 0; i < MvpGameConfig.NPC_COUNT; i++) {
            float progress = npcRaceProgress(i);
            float npcDistance = lapDistance * 3f * progress;
            float relativeDistance = npcDistance - distance;
            float x;
            float y;
            if (mapSpace && isometricMode) {
                com.badlogic.gdx.math.Vector2 point = projectIso(
                    horseX + relativeDistance * 0.28f,
                    horseY + laneOffsets[i % laneOffsets.length]);
                x = point.x;
                y = point.y;
                scale = 1f;
            } else if (mapSpace) {
                x = (horseX + relativeDistance * 0.28f) * scale;
                y = (horseY + laneOffsets[i % laneOffsets.length]) * scale;
            } else {
                x = stage.getViewport().getWorldWidth() * 0.5f + relativeDistance * 0.28f;
                y = stage.getViewport().getWorldHeight() * 0.25f + laneOffsets[i % laneOffsets.length];
            }
            drawEntityShadow(batch, x, y - 14f * scale, 34f * scale, 8f * scale, 0.18f);
            batch.draw(npcMarker, x - 20f * scale, y - 18f * scale, 40f * scale, 36f * scale);
        }
    }

    private void drawForestDecorations(com.badlogic.gdx.graphics.g2d.Batch batch, boolean mapSpace, boolean foreground) {
        if (forestDecorMarkers == null || forestDecorMarkers.length == 0) {
            return;
        }
        float scale = mapSpace ? mapScale : 1f;
        float baseX = mapSpace ? 0f : stage.getViewport().getWorldWidth() * 0.5f - 320f;
        float baseY = mapSpace ? 0f : stage.getViewport().getWorldHeight() * 0.25f - 120f;
        float[][] decorations = {
            {90f, 90f, 0.80f}, {210f, 145f, 0.90f}, {365f, 105f, 0.82f},
            {520f, 185f, 1.00f}, {650f, 120f, 0.88f}, {760f, 230f, 1.08f},
            {130f, 315f, 1.10f}, {300f, 365f, 1.18f}, {475f, 330f, 1.08f},
            {620f, 395f, 1.24f}, {820f, 345f, 1.16f}, {945f, 430f, 1.32f}
        };
        for (int i = 0; i < decorations.length; i++) {
            float y = decorations[i][1];
            boolean isForeground = y > 300f;
            if (isForeground != foreground) {
                continue;
            }
            float x;
            float worldY;
            float depthScale = decorations[i][2] * scale;
            if (mapSpace && isometricMode) {
                com.badlogic.gdx.math.Vector2 point = projectIso(baseX + decorations[i][0], baseY + y);
                x = point.x;
                worldY = point.y;
                depthScale = decorations[i][2];
            } else {
                x = baseX + decorations[i][0] * scale;
                worldY = baseY + y * scale;
            }
            float width = 48f * depthScale;
            float height = 64f * depthScale;
            drawEntityShadow(batch, x, worldY - 3f * scale, width * 0.70f, 9f * depthScale, 0.18f);
            Texture marker = forestDecorMarkers[i % forestDecorMarkers.length];
            batch.draw(marker, x - width * 0.5f, worldY - 10f * depthScale, width, height);
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

    private Texture createNpcMarker() {
        Pixmap pixmap = new Pixmap(40, 36, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();
        pixmap.setColor(0.38f, 0.28f, 0.18f, 1f);
        pixmap.fillRectangle(8, 16, 22, 10);
        pixmap.fillRectangle(25, 21, 8, 7);
        pixmap.setColor(0.18f, 0.12f, 0.08f, 1f);
        pixmap.fillRectangle(6, 22, 5, 8);
        pixmap.fillRectangle(12, 8, 4, 10);
        pixmap.fillRectangle(24, 8, 4, 10);
        pixmap.setColor(0.75f, 0.32f, 0.25f, 1f);
        pixmap.fillRectangle(15, 26, 10, 5);
        pixmap.setColor(0.9f, 0.75f, 0.6f, 1f);
        pixmap.fillRectangle(17, 31, 6, 4);
        pixmap.setColor(0.08f, 0.08f, 0.08f, 1f);
        pixmap.drawRectangle(8, 16, 22, 10);
        Texture texture = new Texture(pixmap);
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        pixmap.dispose();
        return texture;
    }

    private Texture[] createObstacleMarkers() {
        Texture[] markers = new Texture[MvpGameConfig.FOREST_OBSTACLES.length];
        for (int i = 0; i < MvpGameConfig.FOREST_OBSTACLES.length; i++) {
            markers[i] = createObstacleMarker(MvpGameConfig.FOREST_OBSTACLES[i].id);
        }
        return markers;
    }

    private Texture obstacleMarkerFor(String obstacleId) {
        int markerIndex = obstacleMarkerIndex(obstacleId);
        if (markerIndex < 0 || markerIndex >= obstacleMarkers.length) {
            return null;
        }
        return obstacleMarkers[markerIndex];
    }

    private int obstacleMarkerIndex(String obstacleId) {
        for (int i = 0; i < MvpGameConfig.FOREST_OBSTACLES.length; i++) {
            if (MvpGameConfig.FOREST_OBSTACLES[i].id.equals(obstacleId)) {
                return i;
            }
        }
        return -1;
    }

    private Texture createObstacleMarker(String obstacleId) {
        Pixmap pixmap = new Pixmap(32, 24, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();
        if ("kerites".equals(obstacleId)) {
            drawFenceMarker(pixmap);
        } else if ("folyo".equals(obstacleId)) {
            drawRiverMarker(pixmap);
        } else if ("pocsolya".equals(obstacleId)) {
            drawPuddleMarker(pixmap);
        } else {
            drawFallenLogMarker(pixmap);
        }
        Texture texture = new Texture(pixmap);
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        pixmap.dispose();
        return texture;
    }

    private void drawFallenLogMarker(Pixmap pixmap) {
        pixmap.setColor(0.42f, 0.24f, 0.12f, 1f);
        pixmap.fillRectangle(4, 9, 24, 8);
        pixmap.setColor(0.25f, 0.14f, 0.08f, 1f);
        pixmap.drawRectangle(4, 9, 24, 8);
        pixmap.fillCircle(7, 13, 3);
        pixmap.fillCircle(25, 13, 3);
        pixmap.setColor(0.18f, 0.42f, 0.2f, 1f);
        pixmap.fillRectangle(2, 5, 6, 4);
        pixmap.fillRectangle(24, 5, 6, 4);
    }

    private void drawFenceMarker(Pixmap pixmap) {
        pixmap.setColor(0.58f, 0.38f, 0.18f, 1f);
        pixmap.fillRectangle(5, 5, 5, 16);
        pixmap.fillRectangle(22, 5, 5, 16);
        pixmap.fillRectangle(3, 9, 26, 4);
        pixmap.fillRectangle(3, 16, 26, 4);
        pixmap.setColor(0.32f, 0.20f, 0.10f, 1f);
        pixmap.drawRectangle(5, 5, 5, 16);
        pixmap.drawRectangle(22, 5, 5, 16);
        pixmap.drawRectangle(3, 9, 26, 4);
        pixmap.drawRectangle(3, 16, 26, 4);
    }

    private void drawRiverMarker(Pixmap pixmap) {
        pixmap.setColor(0.12f, 0.40f, 0.72f, 1f);
        pixmap.fillRectangle(2, 6, 28, 12);
        pixmap.fillCircle(5, 12, 6);
        pixmap.fillCircle(27, 12, 6);
        pixmap.setColor(0.34f, 0.68f, 0.92f, 1f);
        pixmap.fillRectangle(6, 10, 8, 2);
        pixmap.fillRectangle(18, 14, 7, 2);
        pixmap.setColor(0.06f, 0.24f, 0.48f, 1f);
        pixmap.drawRectangle(2, 6, 28, 12);
    }

    private void drawPuddleMarker(Pixmap pixmap) {
        pixmap.setColor(0.18f, 0.36f, 0.55f, 1f);
        pixmap.fillRectangle(7, 9, 18, 8);
        pixmap.fillCircle(8, 13, 4);
        pixmap.fillCircle(24, 13, 4);
        pixmap.setColor(0.45f, 0.70f, 0.88f, 1f);
        pixmap.fillRectangle(11, 13, 8, 2);
        pixmap.setColor(0.08f, 0.20f, 0.32f, 1f);
        pixmap.drawRectangle(7, 9, 18, 8);
    }

    private Texture[] loadForestDecorMarkers() {
        String[] assets = {"oak", "pine", "bush", "sign", "rock"};
        Texture[] textures = new Texture[assets.length];
        for (int i = 0; i < assets.length; i++) {
            try {
                textures[i] = loadUiTexture("sprites/pixel_decor_" + assets[i] + ".png");
            } catch (RuntimeException exception) {
                textures[i] = createTreeMarker();
            }
        }
        return textures;
    }

    private Texture createIsoFenceMarker() {
        Pixmap pixmap = new Pixmap(24, 64, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();
        pixmap.setColor(0.32f, 0.18f, 0.08f, 1f);
        pixmap.fillRectangle(8, 6, 8, 56);
        pixmap.setColor(0.52f, 0.32f, 0.14f, 1f);
        pixmap.fillRectangle(2, 20, 20, 6);
        pixmap.fillRectangle(2, 38, 20, 6);
        pixmap.setColor(0.18f, 0.10f, 0.05f, 1f);
        pixmap.drawRectangle(8, 6, 8, 56);
        pixmap.drawRectangle(2, 20, 20, 6);
        pixmap.drawRectangle(2, 38, 20, 6);
        Texture texture = new Texture(pixmap);
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        pixmap.dispose();
        return texture;
    }

    private Texture createTreeMarker() {
        Pixmap pixmap = new Pixmap(48, 64, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();
        pixmap.setColor(0.32f, 0.18f, 0.08f, 1f);
        pixmap.fillRectangle(21, 24, 7, 28);
        pixmap.setColor(0.20f, 0.42f, 0.20f, 1f);
        pixmap.fillCircle(24, 20, 18);
        pixmap.setColor(0.12f, 0.32f, 0.15f, 1f);
        pixmap.fillCircle(13, 25, 13);
        pixmap.fillCircle(35, 27, 14);
        pixmap.setColor(0.36f, 0.58f, 0.25f, 1f);
        pixmap.fillCircle(25, 13, 10);
        pixmap.setColor(0.10f, 0.24f, 0.12f, 1f);
        pixmap.drawCircle(24, 20, 18);
        Texture texture = new Texture(pixmap);
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        pixmap.dispose();
        return texture;
    }

    private Texture createSparkleMarker() {
        Pixmap pixmap = new Pixmap(8, 8, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();
        pixmap.setColor(1f, 0.94f, 0.42f, 1f);
        pixmap.fillRectangle(3, 0, 2, 8);
        pixmap.fillRectangle(0, 3, 8, 2);
        Texture texture = new Texture(pixmap);
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        pixmap.dispose();
        return texture;
    }

    private void drawSparkles(com.badlogic.gdx.graphics.g2d.Batch batch, boolean mapSpace) {
        if (sparkleMarker == null || sparkleParticles.size == 0) {
            return;
        }
        float scale = mapSpace ? mapScale : 1f;
        Color previous = new Color(batch.getColor());
        for (SparkleParticle particle : sparkleParticles) {
            float alpha = MathUtils.clamp(particle.life / 0.35f, 0f, 1f);
            batch.setColor(1f, 0.94f, 0.42f, alpha);
            com.badlogic.gdx.math.Vector2 point = isometricMode ? projectIso(particle.x, particle.y) : null;
            float x = isometricMode ? point.x : particle.x * scale;
            float y = isometricMode ? point.y : particle.y * scale;
            float size = (4f + (1f - alpha) * 5f) * scale;
            batch.draw(sparkleMarker, x - size * 0.5f, y - size * 0.5f, size, size);
        }
        batch.setColor(previous);
    }

    private static class SparkleParticle {
        float x;
        float y;
        final float velocityX;
        final float velocityY;
        float life = 0.35f;

        SparkleParticle(float x, float y, float velocityX, float velocityY) {
            this.x = x;
            this.y = y;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
        }
    }

    private Texture createDustMarker() {
        Pixmap pixmap = new Pixmap(8, 8, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();
        pixmap.setColor(0.88f, 0.74f, 0.50f, 0.8f);
        pixmap.fillCircle(4, 4, 3);
        Texture texture = new Texture(pixmap);
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        pixmap.dispose();
        return texture;
    }

    private void updateDustParticles(float delta) {
        for (int i = dustParticles.size - 1; i >= 0; i--) {
            DustParticle particle = dustParticles.get(i);
            particle.life -= delta;
            particle.x += particle.velocityX * delta;
            particle.y += particle.velocityY * delta;
            if (particle.life <= 0f) {
                dustParticles.removeIndex(i);
            }
        }
        if (raceFinished || speed < 26f) {
            dustSpawnTimer = 0f;
            return;
        }
        dustSpawnTimer += delta;
        if (dustSpawnTimer < 0.08f || dustParticles.size >= 18) {
            return;
        }
        dustSpawnTimer = 0f;
        dustParticles.add(new DustParticle(
            horseX - horseDirection * 34f + MathUtils.random(-5f, 5f),
            horseY + MathUtils.random(-8f, 8f),
            MathUtils.random(-8f, 8f),
            MathUtils.random(-4f, 4f)
        ));
    }

    private void drawDustParticles(com.badlogic.gdx.graphics.g2d.Batch batch, boolean mapSpace) {
        if (dustMarker == null || dustParticles.size == 0) {
            return;
        }
        float scale = mapSpace ? mapScale : 1f;
        Color previous = new Color(batch.getColor());
        for (DustParticle particle : dustParticles) {
            float alpha = MathUtils.clamp(particle.life / 0.55f, 0f, 1f) * 0.65f;
            batch.setColor(1f, 1f, 1f, alpha);
            com.badlogic.gdx.math.Vector2 point = isometricMode ? projectIso(particle.x, particle.y) : null;
            float x = isometricMode ? point.x : particle.x * scale;
            float y = isometricMode ? point.y : particle.y * scale;
            float size = (5f + (1f - alpha) * 5f) * scale;
            batch.draw(dustMarker, x - size * 0.5f, y - size * 0.5f, size, size);
        }
        batch.setColor(previous);
    }

    private static class DustParticle {
        float x;
        float y;
        final float velocityX;
        final float velocityY;
        float life = 0.55f;

        DustParticle(float x, float y, float velocityX, float velocityY) {
            this.x = x;
            this.y = y;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
        }
    }

    private Texture createShadowMarker() {
        Pixmap pixmap = new Pixmap(32, 12, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();
        pixmap.setColor(0f, 0f, 0f, 0.45f);
        pixmap.fillRectangle(8, 3, 16, 6);
        pixmap.fillCircle(8, 6, 3);
        pixmap.fillCircle(24, 6, 3);
        Texture texture = new Texture(pixmap);
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        pixmap.dispose();
        return texture;
    }

    private void loadPowerupDefs() {
        powerupDefs.clear();
        powerupDefs.add(new PowerupDef("boost_charge", "Boost t\u00f6ltet"));
        powerupDefs.add(new PowerupDef("speed_burst", "Gyorsító"));
        powerupDefs.add(new PowerupDef("shield", "Pajzs"));
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

    private static class ObstacleSpawn {
        final String id;
        final String label;
        final float x;
        final float y;

        ObstacleSpawn(String id, String label, float x, float y) {
            this.id = id;
            this.label = label;
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
        float scale = mapSpace && isometricMode ? 1f : (mapSpace ? mapScale : 1f);
        float size = mapSpace && isometricMode ? 112f : 96f * scale;
        float x;
        float y;
        if (mapSpace && isometricMode) {
            com.badlogic.gdx.math.Vector2 point = projectIso(horseX, horseY);
            x = point.x - size * 0.5f;
            y = point.y - size * 0.5f;
        } else if (mapSpace) {
            x = horseX * scale - size * 0.5f;
            y = horseY * scale - size * 0.5f;
        } else {
            x = stage.getViewport().getWorldWidth() * 0.5f - size * 0.5f;
            y = stage.getViewport().getWorldHeight() * 0.25f - size * 0.5f;
        }
        float jump = jumpOffset() * scale;
        float shadowAlpha = 0.28f - Math.min(0.14f, jump * 0.0035f);
        drawEntityShadow(batch, x + size * 0.5f, y + size * 0.18f, size * 0.58f, size * 0.10f, shadowAlpha);
        y += jump;
        // The procedural frame contains both horse and rider, so mirroring the
        // complete draw rect turns both characters together.
        if (horseDirection >= 0f) {
            batch.draw(frame, x, y, size, size);
        } else {
            batch.draw(frame, x + size, y, -size, size);
        }
        batch.setColor(previousColor);
    }

    private void drawEntityShadow(com.badlogic.gdx.graphics.g2d.Batch batch, float centerX, float centerY,
                                  float width, float height, float alpha) {
        if (shadowMarker == null) {
            return;
        }
        Color previousColor = new Color(batch.getColor());
        batch.setColor(0f, 0f, 0f, alpha);
        batch.draw(shadowMarker, centerX - width * 0.5f, centerY - height * 0.5f, width, height);
        batch.setColor(previousColor);
    }

    private float jumpOffset() {
        if (jumpTimer <= 0f) {
            return 0f;
        }
        float progress = 1f - jumpTimer / 0.45f;
        return MathUtils.sin(progress * MathUtils.PI) * 34f;
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

    private String resolveHorseColor(String explicitHorseColor, MvpProgress progress) {
        if (progress != null && progress.selectedSkinIndex > 0) {
            return MvpGameConfig.skinHorseColor(progress.selectedSkinIndex);
        }
        if (explicitHorseColor != null) {
            return explicitHorseColor;
        }
        if (progress == null) {
            return null;
        }
        return MvpGameConfig.skinHorseColor(progress.selectedSkinIndex);
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
