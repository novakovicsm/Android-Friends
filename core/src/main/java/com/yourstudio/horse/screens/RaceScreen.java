package com.yourstudio.horse.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yourstudio.horse.HorseGame;
import com.yourstudio.horse.ui.PixelArtFactory;
import com.yourstudio.horse.ui.ScreenNavigator;

public class RaceScreen extends ScreenAdapter {
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
    private Texture buttonUp;
    private Texture buttonDown;
    private Sound clickSound;
    private Sound powerupSound;
    private Sound winSound;
    private Music raceMusic;
    private Texture hudPanel;
    private Label speedLabel;
    private Label lapLabel;
    private Label powerupLabel;
    private Label petBonusLabel;
    private Label directionLabel;
    private TextButton leftButton;
    private TextButton rightButton;
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
    private final String[] horses = {"Gesztenye", "Pej", "Sz\u00FCrke", "Palomino"};
    private final String[] riders = {"Lili", "Noel", "Mira", "\u00c1ron"};
    private final String[] pets = {"Kutya", "Cica", "Nyuszi", "Papag\u00E1j"};
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
    private float petSpeedBonus;
    private float petAccelBonus;
    private float petShieldBonus;
    private boolean victoryPlayed;
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
        stage = new Stage(new ScreenViewport());
        titleFont = createUIFont(54, 3.1f);
        bodyFont = createUIFont(28, 1.45f);
        buttonUp = PixelArtFactory.createPixelButton(
            220,
            88,
            new Color(0.26f, 0.56f, 0.86f, 1f),
            new Color(0.12f, 0.26f, 0.46f, 0.18f),
            new Color(0.9f, 0.92f, 0.96f, 1f),
            new Color(0.08f, 0.14f, 0.22f, 1f),
            false
        );
        buttonDown = PixelArtFactory.createPixelButton(
            220,
            88,
            new Color(0.2f, 0.46f, 0.72f, 1f),
            new Color(0.1f, 0.22f, 0.38f, 0.18f),
            new Color(0.9f, 0.92f, 0.96f, 1f),
            new Color(0.08f, 0.14f, 0.22f, 1f),
            true
        );
        background = PixelArtFactory.createPixelBackground(
            360,
            200,
            new Color(0.2f, 0.16f, 0.2f, 1f),
            new Color(0.36f, 0.28f, 0.22f, 1f),
            new Color(0.18f, 0.14f, 0.1f, 1f),
            new Color(0.12f, 0.1f, 0.08f, 1f)
        );
        clickSound = game.getAssets().get("sfx/click.wav", Sound.class);
        powerupSound = game.getAssets().get("sfx/powerup.wav", Sound.class);
        winSound = game.getAssets().get("sfx/win.wav", Sound.class);
        raceMusic = game.getAssets().get("sfx/race_music.wav", Music.class);
        raceMusic.setLooping(true);
        raceMusic.setVolume(0.5f);
        raceMusic.play();
        hudPanel = PixelArtFactory.createPixelPanel(
            320,
            190,
            new Color(0.12f, 0.12f, 0.16f, 0.9f),
            new Color(0.05f, 0.05f, 0.08f, 0.2f),
            new Color(0.62f, 0.62f, 0.7f, 1f),
            new Color(0.2f, 0.2f, 0.28f, 1f)
        );
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
        horsePreviewRegion = idleAnimation != null ? idleAnimation.getKeyFrame(0f) : null;
        if (horsePreviewRegion == null) {
            horsePreviewFallback = createHorsePreview(new Color(0.65f, 0.44f, 0.3f, 1f), new Color(0.25f, 0.16f, 0.1f, 1f));
            horsePreviewRegion = new TextureRegion(horsePreviewFallback);
        }
        riderPreviews = createRiderPreviews();
        petPreviews = createPetPreviews();
        horsePreviewImage = new Image(horsePreviewRegion);
        riderPreviewImage = new Image(toDrawable(riderPreviews[riderIndex]));
        petPreviewImage = new Image(toDrawable(petPreviews[petIndex]));
        refreshRiderPreview();

        camera = new OrthographicCamera();
        mapViewport = new FitViewport(640f, 360f, camera);
        mapViewport.apply();
        try {
            map = new TmxMapLoader().load("maps/forest.tmx");
            mapRenderer = new OrthogonalTiledMapRenderer(map, 1f);
            MapProperties props = map.getProperties();
            Integer mapWidthTiles = props.get("width", Integer.class);
            Integer mapHeightTiles = props.get("height", Integer.class);
            Integer tileWidth = props.get("tilewidth", Integer.class);
            Integer tileHeight = props.get("tileheight", Integer.class);
            if (mapWidthTiles != null && mapHeightTiles != null && tileWidth != null && tileHeight != null) {
                mapBoundsMinX = 0f;
                mapBoundsMinY = 0f;
                mapBoundsMaxX = mapWidthTiles * tileWidth;
                mapBoundsMaxY = mapHeightTiles * tileHeight;
                mapHasBounds = true;
            }
            mapLoaded = true;
        } catch (RuntimeException exception) {
            Gdx.app.error("RaceScreen", "Failed to load map, using fallback background.", exception);
            mapLoaded = false;
        }

        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        Label.LabelStyle labelStyle = new Label.LabelStyle(bodyFont, Color.WHITE);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = toDrawable(buttonUp);
        buttonStyle.down = toDrawable(buttonDown);
        buttonStyle.font = bodyFont;
        buttonStyle.fontColor = Color.WHITE;

        Label title = new Label("Verseny - UI v2", titleStyle);
        Label selection = new Label("L\u00F3: " + horseName + " | Lovas: " + riderName + " | Kedvenc: " + petName, labelStyle);
        String horseCustomization = "L\u00F3sz\u00EDn: " + safeLabel(horseColor) + " | S\u00F6r\u00E9ny: " + safeLabel(maneColor) + " | Nyereg: " + safeLabel(saddleColor) + " | Ruh\u00E1zat: " + safeLabel(outfitColor);
        Label customization = new Label(horseCustomization, labelStyle);
        Label trackLabel = new Label("P\u00E1lya: " + trackName, labelStyle);
        TextButton backButton = new TextButton("Vissza", buttonStyle);

        speedLabel = new Label("Sebess\u00E9g: 0 km/h", labelStyle);
        lapLabel = new Label("K\u00F6r: 1/3", labelStyle);
        powerupLabel = new Label("B\u00F3nusz: --", labelStyle);
        petBonusLabel = new Label("Kedvenc b\u00F3nusz: --", labelStyle);
        directionLabel = new Label("Ir\u00E1ny:", labelStyle);
        leftButton = new TextButton("Balra", buttonStyle);
        rightButton = new TextButton("Jobbra", buttonStyle);
        leftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (clickSound != null) {
                    clickSound.play(0.6f);
                }
                horseDirection = -1f;
            }
        });
        rightButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (clickSound != null) {
                    clickSound.play(0.6f);
                }
                horseDirection = 1f;
            }
        });

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
        Table previewRow = new Table();
        previewRow.add(horsePreviewImage).size(64f, 48f).padRight(6f);
        previewRow.add(riderPreviewImage).size(64f, 48f).padRight(6f);
        previewRow.add(petPreviewImage).size(64f, 48f);
        hudContent.add(previewRow).left().padTop(8f);
        applyPetBonus();
        hudTable.add(hudContent);
        Table layout = new Table();
        layout.setFillParent(true);
        layout.pad(24f);
        layout.add(title).padBottom(20f);
        layout.row();
        layout.add(selection).padBottom(30f);
        layout.row();
        layout.add(customization).padBottom(18f);
        layout.row();
        layout.add(trackLabel).padBottom(30f);
        layout.row();
        layout.add(backButton).width(220f).height(80f);

        Table directionTable = new Table();
        directionTable.setFillParent(true);
        directionTable.bottom().pad(24f);
        Table directionRow = new Table();
        directionRow.add(directionLabel).padRight(12f);
        directionRow.add(leftButton).width(140f).height(72f).padRight(12f);
        directionRow.add(rightButton).width(140f).height(72f);
        directionTable.add(directionRow);

        stage.addActor(layout);
        stage.addActor(hudTable);
        stage.addActor(directionTable);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        elapsedTime += delta;
        boolean accelerating = Gdx.input.isTouched();
        float effectiveMaxSpeed = maxSpeed + petSpeedBonus;
        float effectiveAccel = acceleration + petAccelBonus;
        if (accelerating) {
            speed = Math.min(effectiveMaxSpeed, speed + effectiveAccel * delta);
        } else {
            speed = Math.max(0f, speed - deceleration * delta);
        }
        distance += speed * delta;
        updatePowerupSpawns(delta);
        updatePowerupPickup(delta);
        int lap = 1 + ((int) (distance / lapDistance) % 3);
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
            powerupLabel.setText("B\u00F3nusz: --");
        }
        animationTime += delta;
        if (mapLoaded && mapRenderer != null && camera != null) {
            horseX += speed * delta * horseDirection;
            horseY = 80f + 24f * (float) Math.sin(distance * 0.03f);
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
            camera.position.set(clampCameraX(horseX), clampCameraY(horseY), 0f);
            camera.update();
            mapRenderer.setView(camera);
            mapRenderer.render();
            mapRenderer.getBatch().begin();
            drawPowerups(mapRenderer.getBatch());
            drawHorseAnimation(mapRenderer.getBatch(), true);
            mapRenderer.getBatch().end();
        } else {
            stage.getBatch().begin();
            stage.getBatch().draw(background, 0, 0, stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
            drawPowerups(stage.getBatch());
            drawHorseAnimation(stage.getBatch(), false);
            stage.getBatch().end();
        }
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }
        if (mapViewport != null) {
            mapViewport.update(width, height, true);
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
        if (buttonUp != null) {
            buttonUp.dispose();
        }
        if (buttonDown != null) {
            buttonDown.dispose();
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

    private Texture createPanelTexture(Color fillColor, Color borderColor, int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(fillColor);
        pixmap.fill();
        pixmap.setColor(borderColor);
        for (int i = 0; i < 3; i++) {
            pixmap.drawRectangle(i, i, width - (i * 2), height - (i * 2));
        }
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private BitmapFont createUIFont(int size, float fallbackScale) {
        FileHandle fontFile = Gdx.files.internal("fonts/ArchitectsDaughter.ttf");
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

    private String safeLabel(String value) {
        return value == null || value.isEmpty() ? "-" : value;
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
        Texture[] previews = new Texture[outfits.length];
        for (int i = 0; i < outfits.length; i++) {
            previews[i] = createRiderPreview(outfits[i], hair[i]);
        }
        return previews;
    }

    private Texture[] createPetPreviews() {
        Color[] pets = {
            new Color(0.85f, 0.65f, 0.4f, 1f),
            new Color(0.6f, 0.6f, 0.65f, 1f),
            new Color(0.95f, 0.9f, 0.75f, 1f),
            new Color(0.2f, 0.75f, 0.45f, 1f)
        };
        Texture[] previews = new Texture[pets.length];
        for (int i = 0; i < pets.length; i++) {
            previews[i] = createPetPreview(pets[i]);
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
        String bonusText = "--";
        if ("Kutya".equals(petName)) {
            petSpeedBonus = 6f;
            bonusText = "+6 km/h v\u00E9gsebess\u00E9g";
        } else if ("Cica".equals(petName)) {
            petAccelBonus = 5f;
            bonusText = "+5 gyorsul\u00E1s";
        } else if ("Nyuszi".equals(petName)) {
            petAccelBonus = 3f;
            petSpeedBonus = 3f;
            bonusText = "+3 gyorsul\u00E1s, +3 km/h";
        } else if ("Papag\u00E1j".equals(petName)) {
            petShieldBonus = 1f;
            bonusText = "+1 pajzs";
        }
        if (petBonusLabel != null) {
            petBonusLabel.setText("Kedvenc b\u00F3nusz: " + bonusText);
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
                PowerupDef def = findPowerupDef(spawn.id);
                activePowerupName = def != null ? def.name : "B\u00F3nusz";
                activePowerupTimer = 4f;
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

    private PowerupDef findPowerupDef(String id) {
        for (PowerupDef def : powerupDefs) {
            if (def.id.equals(id)) {
                return def;
            }
        }
        return null;
    }

    private void drawPowerups(com.badlogic.gdx.graphics.g2d.Batch batch) {
        if (powerupMarker == null) {
            return;
        }
        for (PowerupSpawn spawn : powerupSpawns) {
            batch.draw(powerupMarker, spawn.x - 10f, spawn.y - 10f, 20f, 20f);
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
        try {
            FileHandle powerupsFile = Gdx.files.internal("data/powerups.json");
            JsonValue root = new JsonReader().parse(powerupsFile.readString("UTF-8"));
            JsonValue list = root.get("powerups");
            if (list != null) {
                for (JsonValue entry : list) {
                    String id = entry.getString("id", "");
                    String name = entry.getString("name", id);
                    powerupDefs.add(new PowerupDef(id, name));
                }
            }
        } catch (RuntimeException exception) {
            Gdx.app.error("RaceScreen", "Failed to load powerups.json", exception);
        }
        if (powerupDefs.size == 0) {
            powerupDefs.add(new PowerupDef("gyorsitas", "Gyors\u00EDt\u00E1s"));
            powerupDefs.add(new PowerupDef("pajzs", "Pajzs"));
            powerupDefs.add(new PowerupDef("villam", "Vill\u00E1m"));
        }
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
        float size = 96f;
        float x;
        float y;
        if (mapSpace) {
            x = horseX - size * 0.5f;
            y = horseY - size * 0.5f;
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
        frames[0] = createHorseFrame(new Color(0.65f, 0.44f, 0.3f, 1f), new Color(0.25f, 0.16f, 0.1f, 1f), 0);
        frames[1] = createHorseFrame(new Color(0.65f, 0.44f, 0.3f, 1f), new Color(0.25f, 0.16f, 0.1f, 1f), 1);
        return frames;
    }

    private Texture[] createHorseRunFrames() {
        Texture[] frames = new Texture[4];
        Color body = new Color(0.65f, 0.44f, 0.3f, 1f);
        Color mane = new Color(0.25f, 0.16f, 0.1f, 1f);
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

        Color bodyShade = darken(body, 0.18f);
        Color hoof = darken(body, 0.35f);
        Color maneDark = darken(mane, 0.15f);

        pixmap.setColor(0f, 0f, 0f, 0.25f);
        pixmap.fillRectangle(12, 8, 40, 6);
        pixmap.fillCircle(12, 11, 3);
        pixmap.fillCircle(52, 11, 3);

        pixmap.setColor(body);
        pixmap.fillRectangle(14, 26, 28, 14);
        pixmap.fillRectangle(30, 32, 16, 10);
        pixmap.fillCircle(48, 38, 7);

        pixmap.setColor(bodyShade);
        pixmap.fillRectangle(16, 24, 24, 4);
        pixmap.fillRectangle(30, 30, 14, 3);
        pixmap.fillRectangle(44, 34, 6, 3);

        int legOffset = variant % 2 == 0 ? 0 : 3;
        pixmap.setColor(bodyShade);
        pixmap.fillRectangle(18, 12 + legOffset, 5, 14);
        pixmap.fillRectangle(28, 12, 5, 14 + legOffset);
        pixmap.fillRectangle(38, 12 + legOffset, 5, 14);
        pixmap.fillRectangle(46, 12, 4, 12 + legOffset);
        pixmap.setColor(hoof);
        pixmap.fillRectangle(18, 10 + legOffset, 5, 3);
        pixmap.fillRectangle(28, 10, 5, 3);
        pixmap.fillRectangle(38, 10 + legOffset, 5, 3);
        pixmap.fillRectangle(46, 10, 4, 3);

        pixmap.setColor(mane);
        pixmap.fillRectangle(30, 40, 12, 6);
        pixmap.fillRectangle(18, 40, 10, 5);
        pixmap.setColor(maneDark);
        pixmap.fillRectangle(12, 28, 6, 10);
        pixmap.fillRectangle(44, 42, 4, 3);

        pixmap.setColor(Color.WHITE);
        pixmap.fillRectangle(50, 38, 2, 2);

        // Flip the pixmap vertically so the horse is upright in libGDX
        Pixmap flipped = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                flipped.drawPixel(x, y, pixmap.getPixel(x, size - 1 - y));
            }
        }
        Texture texture = new Texture(flipped);
        pixmap.dispose();
        flipped.dispose();
        return texture;
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
        float min = mapBoundsMinX + halfWidth;
        float max = mapBoundsMaxX - halfWidth;
        if (min > max) {
            return (mapBoundsMinX + mapBoundsMaxX) * 0.5f;
        }
        return MathUtils.clamp(targetX, min, max);
    }

    private float clampCameraY(float targetY) {
        if (!mapHasBounds || camera == null) {
            return targetY;
        }
        float halfHeight = camera.viewportHeight * 0.5f;
        float min = mapBoundsMinY + halfHeight;
        float max = mapBoundsMaxY - halfHeight;
        if (min > max) {
            return (mapBoundsMinY + mapBoundsMaxY) * 0.5f;
        }
        return MathUtils.clamp(targetY, min, max);
    }
}
