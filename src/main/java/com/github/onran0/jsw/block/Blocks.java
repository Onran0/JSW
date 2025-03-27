package com.github.onran0.jsw.block;

import com.github.onran0.jsw.io.ISerializable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Blocks {

    public static final int MAX_SUPPORTED_VERSION = 6;
    public static final int MIN_SUPPORTED_VERSION = 6;

    public static final int
            METAL_BLOCK = 0,
            WOOD_BLOCK = 1,
            SMALL_JET_ENGINE = 2,
            MONITOR = 4,
            SEAT = 5,
            TABLE = 6,
            ADJUSTABLE_PONTOON = 8,
            SWITCH = 9,
            BUTTON = 10,
            SLIDER = 11,
            BEARING = 12,
            WHEEL = 13,
            BEARING_CONTROLLER = 14,
            ENGINE = 15,
            SUBMARINE_PROPELLER = 16,
            SMALL_WHEEL = 17,
            LARGE_ENGINE = 18,
            PONTOON = 19,
            AIR_PROPELLER = 20,
            SMALL_AIR_PROPELLER = 21,
            SIGNAL_TURNER = 22,
            LARGE_ROCKET_ENGINE = 23,
            PISTON = 24,
            PISTON_REPLYING_BLOCK = 25,
            TANK_CHASSIS_CONTROLLER = 26,
            GLASS_BLOCK = 28,
            DECORATIVE_FAN = 29,
            PROJECTOR = 30,
            MEDIUM_GUN = 31,
            LIGHT_GUN = 32,
            METAL_HALF_BLOCK_0 = 33,
            METAL_HALF_BLOCK_1 = 34,
            METAL_HALF_BLOCK_2 = 35,
            METAL_HALF_BLOCK_3 = 36,
            METAL_HALF_BLOCK_4 = 37,
            METAL_HALF_BLOCK_5 = 38,
            MEDIUM_AMMO_RACK = 39,
            WING = 40,
            SMALL_WING = 41,
            FLIGHT_CONTROLLER = 42,
            MEDIUM_ROCKET_ENGINE = 43,
            SMALL_FLARE_GUN = 44,
            BOMB = 45,
            DISCONNECTOR = 46,
            QUEUE_SWITCH = 47,
            MEDIUM_FLARE_GUN = 48,
            MACHINE_GUN = 49,
            SAW = 51,
            HEAVY_GUN = 52,
            CAMERA = 53,
            LADDER = 56,
            FILTER = 57,
            METAL_HALF_BLOCK_6 = 59,
            HOVER = 60,
            BEAM = 61,
            GLASS_HALF_BLOCK_0 = 62,
            GLASS_HALF_BLOCK_1 = 63,
            GLASS_HALF_BLOCK_2 = 64,
            GLASS_HALF_BLOCK_3 = 65,
            GLASS_HALF_BLOCK_4 = 66,
            GLASS_HALF_BLOCK_5 = 67,
            GLASS_HALF_BLOCK_6 = 68,
            WOOD_HALF_BLOCK_0 = 69,
            WOOD_HALF_BLOCK_1 = 70,
            WOOD_HALF_BLOCK_2 = 71,
            WOOD_HALF_BLOCK_3 = 72,
            WOOD_HALF_BLOCK_4 = 73,
            WOOD_HALF_BLOCK_5 = 74,
            WOOD_HALF_BLOCK_6 = 75,
            PIXEL = 76,
            AND = 77,
            OR = 78,
            NAND = 79,
            NOR = 80,
            XOR = 81,
            XNOR = 82,
            SMOOTHER = 83,
            TIMER = 84,
            DISTANCE_SENSOR = 85,
            METAL_HALF_BLOCK_7 = 86,
            GLASS_HALF_BLOCK = 87,
            WOOD_HALF_BLOCK = 88,
            SMALL_AMMO_RACK = 89,
            LARGE_AMMO_RACK = 90,
            SIGN = 91,
            TILT_STABILIZER = 92,
            ALTITUDE_STABILIZER = 93,
            SMALL_ROCKET_ENGINE = 94,
            RAILGUN = 95,
            LARGE_ION_ENGINE = 96,
            FIVE_SHOT_GUN = 97,
            MEDIUM_ION_ENGINE = 98,
            AUTOMATIC_CANNON = 99,
            REACTIVE_ENGINE = 100,
            SMOKE_GRENADE_LAUNCHER = 101,
            FLAMETHROWER = 102,
            NIGHT_VISION_CAMERA = 103,
            SMALL_ION_ENGINE = 104,
            MEDIUM_WHEEL = 105,
            VERY_SMALL_WHEEL = 106,
            MAGNET = 107,
            RANGEFINDER = 108,
            CUSTOM_BLOCK = 109,
            TURBOJET_ENGINE = 110,
            LARGE_TURBOJET_ENGINE = 111,
            STICKER = 113,
            BIG_FLARE_GUN = 114,
            HEAT_TRAPS = 115,
            BALLAST = 116,
            SPEED_STABILIZER = 117,
            CUSTOM_PONTOON = 118,
            TANK_ROLLER = 119,
            WOODEN_CUSTOM_BLOCK = 120,
            GLASS_CUSTOM_BLOCK = 121,
            DYNAMIC_PROTECTION = 122,
            ANTI_AIRCRAFT_GUN = 123,
            HALLOWEEN_FLAMETHROWER = 124,
            TONE_GENERATOR = 125,
            TEXT_STICKER = 126,
            SPEEDOMETER = 127,
            CUSTOM_WHEEL = 128,
            MATH_BLOCK = 129,
            CUSTOM_LIGHTING_BLOCK = 130,
            SMALL_PISTON = 131;

    private static final Map<Integer, Function<Integer, Boolean>> isCustomMap = new HashMap<>();
    private static final Map<Integer, Function<Integer, Boolean>> isInteractableMap = new HashMap<>();
    private static final Map<Integer, Function<Integer, Supplier<ISerializable>>> customMetadataMap = new HashMap<>();

    private static void checkVersion(int version) {
        if(!isSupportedVersion(version))
            throw new IllegalArgumentException("Unsupported blocks table version: " + version);
    }

    public static boolean isSupportedVersion(final int version) {
        return version >= MIN_SUPPORTED_VERSION && version <= MAX_SUPPORTED_VERSION;
    }

    public static boolean isCustomBlock(int id, int version) {
        checkVersion(version);

        return isCustomMap.get(id).apply(version);
    }

    public static boolean isInteractableBlock(int id, int version) {
        checkVersion(version);

        return isInteractableMap.get(id).apply(version);
    }

    public static Supplier<ISerializable> getBlockCustomMetadata(int id, int version) {
        checkVersion(version);

        return customMetadataMap.get(id).apply(version);
    }

    static {
        for (int i = 0;i <= 6;i++) {
            isCustomMap.put(i, V0::isCustomBlock);
            isInteractableMap.put(i, V0::isInteractableBlock);
            customMetadataMap.put(i, V0::getBlockCustomMetadata);
        }
    }

    public static final class V0 {

        private static final List<Integer> customs = Arrays.asList(
                CUSTOM_LIGHTING_BLOCK,
                WOODEN_CUSTOM_BLOCK,
                GLASS_CUSTOM_BLOCK,
                CUSTOM_BLOCK
        );

        private static final List<Integer> notInteractableBlocks = Arrays.asList(
                METAL_BLOCK,
                WOOD_BLOCK,
                TABLE,

                WHEEL,
                SUBMARINE_PROPELLER,
                SMALL_WHEEL,
                PONTOON,
                AIR_PROPELLER,
                SMALL_AIR_PROPELLER,
                PISTON_REPLYING_BLOCK,
                GLASS_BLOCK,
                DECORATIVE_FAN,

                METAL_HALF_BLOCK_0,
                METAL_HALF_BLOCK_1,
                METAL_HALF_BLOCK_2,
                METAL_HALF_BLOCK_3,
                METAL_HALF_BLOCK_4,
                METAL_HALF_BLOCK_5,
                METAL_HALF_BLOCK_6,
                METAL_HALF_BLOCK_7,

                GLASS_HALF_BLOCK,
                GLASS_HALF_BLOCK_0,
                GLASS_HALF_BLOCK_1,
                GLASS_HALF_BLOCK_2,
                GLASS_HALF_BLOCK_3,
                GLASS_HALF_BLOCK_4,
                GLASS_HALF_BLOCK_5,
                GLASS_HALF_BLOCK_6,

                WOOD_HALF_BLOCK,
                WOOD_HALF_BLOCK_0,
                WOOD_HALF_BLOCK_1,
                WOOD_HALF_BLOCK_2,
                WOOD_HALF_BLOCK_3,
                WOOD_HALF_BLOCK_4,
                WOOD_HALF_BLOCK_5,
                WOOD_HALF_BLOCK_6,

                MEDIUM_WHEEL,
                VERY_SMALL_WHEEL,
                STICKER,
                BALLAST,
                CUSTOM_PONTOON,
                TANK_ROLLER,
                DYNAMIC_PROTECTION,
                TEXT_STICKER

        );

        public static boolean isCustomBlock(int id) {
            return customs.contains(id);
        }

        public static boolean isInteractableBlock(int id) {
            return !notInteractableBlocks.contains(id);
        }

        public static Supplier<ISerializable> getBlockCustomMetadata(int id) {
            return null;
        }
    }
}