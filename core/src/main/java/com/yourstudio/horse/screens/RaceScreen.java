package com.yourstudio.horse.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
import com.yourstudio.horse.ui.ScreenNavigator;

public class RaceScreen extends ScreenAdapter {
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
    private BitmapFont font;
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
        font = new BitmapFont();
        buttonUp = createColorTexture(new Color(0.29f, 0.6f, 0.85f, 1f));
        buttonDown = createColorTexture(new Color(0.2f, 0.48f, 0.7f, 1f));
        background = createColorTexture(new Color(0.2f, 0.12f, 0.08f, 1f));
        clickSound = game.getAssets().get("sfx/click.wav", Sound.class);
        powerupSound = game.getAssets().get("sfx/powerup.wav", Sound.class);
        winSound = game.getAssets().get("sfx/win.wav", Sound.class);
        raceMusic = game.getAssets().get("sfx/race_music.wav", Music.class);
        raceMusic.setLooping(true);
        raceMusic.setVolume(0.5f);
        raceMusic.play();
        hudPanel = createPanelTexture(new Color(0.12f, 0.12f, 0.16f, 0.85f), new Color(0.35f, 0.35f, 0.45f, 1f), 260, 130);
        loadHorseAnimations();
        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);
        runAnimation.setPlayMode(Animation.PlayMode.LOOP);
        powerupMarker = createPowerupMarker();
        loadPowerupDefs();

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

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = toDrawable(buttonUp);
        buttonStyle.down = toDrawable(buttonDown);
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;

        Label title = new Label("Verseny (helykitöltő)", labelStyle);
        Label selection = new Label("Ló: " + horseName + " | Lovas: " + riderName + " | Kedvenc: " + petName, labelStyle);
        String horseCustomization = "Lószín: " + safeLabel(horseColor) + " | Sörény: " + safeLabel(maneColor) + " | Nyereg: " + safeLabel(saddleColor) + " | Ruházat: " + safeLabel(outfitColor);
        Label customization = new Label(horseCustomization, labelStyle);
        Label trackLabel = new Label("Pálya: " + trackName, labelStyle);
        TextButton backButton = new TextButton("Vissza", buttonStyle);

        speedLabel = new Label("Sebesség: 0 km/h", labelStyle);
        lapLabel = new Label("Kör: 1/3", labelStyle);
        powerupLabel = new Label("Bónusz: --", labelStyle);
    petBonusLabel = new Label("Kedvenc bónusz: --", labelStyle);

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

        stage.addActor(layout);
        stage.addActor(hudTable);
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
        speedLabel.setText("Sebesség: " + (int) speed + " km/h");
        lapLabel.setText("Kör: " + currentLap + "/3");
        if (activePowerupName != null) {
            powerupLabel.setText("Bónusz: " + activePowerupName + " (" + (int) Math.ceil(activePowerupTimer) + "s)");
        } else {
            powerupLabel.setText("Bónusz: --");
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
        if (font != null) {
            font.dispose();
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

    private String safeLabel(String value) {
        return value == null || value.isEmpty() ? "-" : value;
    }

    private void applyPetBonus() {
        petSpeedBonus = 0f;
        petAccelBonus = 0f;
        petShieldBonus = 0f;
        String bonusText = "--";
        if ("Kutya".equals(petName)) {
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
                PowerupDef def = findPowerupDef(spawn.id);
                activePowerupName = def != null ? def.name : "Bónusz";
                activePowerupTimer = 4f;
                if (powerupSound != null) {
                    powerupSound.play(0.7f);
                }
                Gdx.input.vibrate(60);
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
            JsonValue root = new JsonReader().parse(Gdx.files.internal("data/powerups.json"));
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
            powerupDefs.add(new PowerupDef("gyorsitas", "Gyorsítás"));
            powerupDefs.add(new PowerupDef("pajzs", "Pajzs"));
            powerupDefs.add(new PowerupDef("villam", "Villám"));
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
    }

    private void loadHorseAnimations() {
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
        if ("Szürke".equals(horseName)) {
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

        pixmap.setColor(body);
        pixmap.fillRectangle(10, 24, 34, 18);
        pixmap.fillCircle(46, 36, 8);

        int legOffset = variant % 2 == 0 ? 0 : 3;
        pixmap.fillRectangle(16, 10 + legOffset, 6, 14);
        pixmap.fillRectangle(26, 10, 6, 14 + legOffset);
        pixmap.fillRectangle(36, 10 + legOffset, 6, 14);

        pixmap.setColor(mane);
        pixmap.fillRectangle(38, 42, 12, 6);
        pixmap.fillRectangle(12, 42, 10, 6);

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
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
