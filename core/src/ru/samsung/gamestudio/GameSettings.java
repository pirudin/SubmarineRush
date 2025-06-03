package ru.samsung.gamestudio;

public class GameSettings {

    // Device settings

    public static final int SCREEN_WIDTH = 1920;
    public static final int SCREEN_HEIGHT = 1080;

    // Physics settings

    public static final float STEP_TIME = 1f / 60f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 6;
    public static final float SCALE = 0.05f;

    public static float SHIP_FORCE_RATIO = 10;
    public static long STARTING_ENEMY_SHIP_APPEARANCE_COOL_DOWN = 2000;
    public static int SHOOTING_COOL_DOWN = 2000;

    public static final short ENEMY_SHIP_BIT = 2;
    public static final short SHIP_BIT = 4;
    public static final short TORPEDO_BIT = 8;

    // Object sizes

    public static final int SHIP_WIDTH = 200;
    public static final int SHIP_HEIGHT = 150;
    public static final int ENEMY_SHIP_WIDTH = 140;
    public static final int ENEMY_SHIP_HEIGHT = 100;
    public static final int TORPEDO_WIDTH = 100;
    public static final int TORPEDO_HEIGHT = 55;

    public static final int SHIP_TORPEDO_X_SPEED = 50;
}