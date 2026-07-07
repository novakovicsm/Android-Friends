package com.yourstudio.horse.model;

public final class MvpGameConfig {
    private MvpGameConfig() {
    }

    public static final String DEFAULT_TRACK = "forest.tmx";
    public static final int PLAYER_COUNT = 1;
    public static final int NPC_COUNT = 4;
    public static final int TOTAL_RACERS = PLAYER_COUNT + NPC_COUNT;
    public static final int MAX_CUSTOM_RIDER_NAME_LENGTH = 15;
    public static final int BOOST_POWERUP_CHARGE_PERCENT = 20;
    public static final int MAX_PLAYER_LEVEL = 4;
    public static final int MAX_PET_LEVEL = 10;
    public static final int PET_XP_PER_LEVEL = 100;
    public static final int BOOST_ACTIVATION_COST_PERCENT = 20;
    public static final float BOOST_ACTIVE_SECONDS = 1.1f;
    public static final float BOOST_SPEED_MULTIPLIER = 1.35f;
    public static final float OBSTACLE_SLOWDOWN_MULTIPLIER = 0.55f;
    public static final float OBSTACLE_SLOWDOWN_SECONDS = 1.2f;

    public static final String[] RIDER_NAMES = {
        "Peti",
        "Szandi",
        "Bogi",
        "M\u00e1t\u00e9",
        "Lili",
        "Dani",
        "Panni",
        "Marci",
        "Zs\u00f3fi",
        "Levi"
    };

    public static final String[] NPC_NAMES = {
        "Anna",
        "Bence",
        "Dorka",
        "Misi",
        "Nori",
        "Tomi",
        "Fanni",
        "Balazs",
        "Reka",
        "Zalan",
        "Emma",
        "Samu"
    };

    public static final HorseProfile[] HORSES = {
        new HorseProfile("Vill\u00e1m", StatFocus.SPEED, "Nagyon gyors, de kicsit nehezebben fordul.", 5, 3, 3, 3),
        new HorseProfile("Pihe", StatFocus.TURNING, "K\u00f6nnyen ir\u00e1ny\u00edthat\u00f3, nyugodt versenyt\u00e1rs.", 3, 5, 3, 3),
        new HorseProfile("Csillag", StatFocus.BOOST, "Gyorsabban t\u00f6lti a boostot, \u00fcgyes mindenes.", 3, 3, 3, 5),
        new HorseProfile("Fut\u00f3", StatFocus.ACCELERATION, "Gyorsan indul, j\u00f3l kapja el a rajtot.", 3, 3, 5, 3)
    };

    public static final RiderBonus[] RIDER_BONUSES = {
        new RiderBonus(RiderBonusType.ACCELERATION, 0.01f),
        new RiderBonus(RiderBonusType.BOOST_CHARGE, 0.01f)
    };

    public static final ObstacleType[] FOREST_OBSTACLES = {
        new ObstacleType("kidolt_fa", "Kid\u0151lt fa"),
        new ObstacleType("kerites", "Ker\u00edt\u00e9s"),
        new ObstacleType("folyo", "Foly\u00f3"),
        new ObstacleType("pocsolya", "Pocsolya")
    };

    public static final UpgradeCategory[] UPGRADE_CATEGORIES = {
        new UpgradeCategory("gyorsasag", "Gyorsas\u00e1g", 2),
        new UpgradeCategory("fordulas", "Fordul\u00e1s", 2),
        new UpgradeCategory("ugras", "Ugr\u00e1s", 2),
        new UpgradeCategory("boost", "Boost", 2),
        new UpgradeCategory("lassitas_csokkentes", "Lass\u00edt\u00e1s cs\u00f6kkent\u00e9se", 2)
    };

    public static int placementHorseshoeReward(int placement) {
        switch (placement) {
            case 1:
                return 10;
            case 2:
                return 7;
            case 3:
                return 5;
            case 4:
                return 3;
            case 5:
                return 1;
            default:
                throw new IllegalArgumentException("Placement must be between 1 and 5.");
        }
    }

    public static int horseshoeReward(int placement, Difficulty difficulty) {
        return Math.round(placementHorseshoeReward(placement) * difficulty.horseshoeMultiplier);
    }

    public static int raceXp(int placement, Difficulty difficulty, boolean recordBroken) {
        float xp = difficulty.baseXp * placementMultiplier(placement);
        if (recordBroken) {
            xp += difficulty.baseXp * 0.20f;
        }
        xp += difficulty.baseXp * 0.05f;
        return Math.round(xp);
    }

    public static float placementMultiplier(int placement) {
        switch (placement) {
            case 1:
                return 1.00f;
            case 2:
                return 0.80f;
            case 3:
                return 0.60f;
            case 4:
                return 0.40f;
            case 5:
                return 0.20f;
            default:
                throw new IllegalArgumentException("Placement must be between 1 and 5.");
        }
    }

    public static int playerLevelXpRequirement(int level) {
        switch (level) {
            case 1:
                return 10;
            case 2:
                return 15;
            case 3:
                return 23;
            case 4:
                return 35;
            default:
                throw new IllegalArgumentException("Player level must be between 1 and 4.");
        }
    }

    public static int upgradeCost(int upgradeNumber) {
        if (upgradeNumber < 1 || upgradeNumber > 10) {
            throw new IllegalArgumentException("Upgrade number must be between 1 and 10.");
        }
        if (upgradeNumber <= 3) {
            return 10;
        }
        if (upgradeNumber <= 6) {
            return 15;
        }
        return 20;
    }

    public static RiderBonus riderBonusForIndex(int riderIndex) {
        if (riderIndex < 0) {
            throw new IllegalArgumentException("Rider index must be zero or greater.");
        }
        return RIDER_BONUSES[riderIndex % RIDER_BONUSES.length];
    }

    public static String[] npcNamesForSeed(long seed) {
        String[] names = new String[NPC_COUNT];
        int start = (int) Math.floorMod(seed, NPC_NAMES.length);
        for (int i = 0; i < NPC_COUNT; i++) {
            names[i] = NPC_NAMES[(start + i * 3) % NPC_NAMES.length];
        }
        return names;
    }

    public enum Difficulty {
        EASY(10, 0.3f),
        MEDIUM(20, 0.6f),
        HARD(30, 1.0f);

        public final int baseXp;
        public final float horseshoeMultiplier;

        Difficulty(int baseXp, float horseshoeMultiplier) {
            this.baseXp = baseXp;
            this.horseshoeMultiplier = horseshoeMultiplier;
        }
    }

    public enum RiderBonusType {
        ACCELERATION,
        BOOST_CHARGE
    }

    public enum StatFocus {
        SPEED,
        TURNING,
        BOOST,
        ACCELERATION
    }

    public static final class HorseProfile {
        public final String name;
        public final StatFocus focus;
        public final String description;
        public final int speed;
        public final int turning;
        public final int acceleration;
        public final int boost;

        private HorseProfile(String name, StatFocus focus, String description,
                             int speed, int turning, int acceleration, int boost) {
            this.name = name;
            this.focus = focus;
            this.description = description;
            this.speed = speed;
            this.turning = turning;
            this.acceleration = acceleration;
            this.boost = boost;
        }
    }

    public static final class RiderBonus {
        public final RiderBonusType type;
        public final float value;

        private RiderBonus(RiderBonusType type, float value) {
            this.type = type;
            this.value = value;
        }
    }

    public static final class ObstacleType {
        public final String id;
        public final String label;

        private ObstacleType(String id, String label) {
            this.id = id;
            this.label = label;
        }
    }

    public static final class UpgradeCategory {
        public final String id;
        public final String label;
        public final int upgradeCount;

        private UpgradeCategory(String id, String label, int upgradeCount) {
            this.id = id;
            this.label = label;
            this.upgradeCount = upgradeCount;
        }
    }
}
