package com.mygdx.helper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;
import java.util.Objects;


public class Constants {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int FOREGROUND_FPS = 60;
    public static final String TITLE = "ShawnSkates";
    public static final float GRAVITY = -15f;
    public static final int BG_SPEED = 200;
    public static final float CAMERA_PLAYER_OFFSET = 90f;
    public static final float PLAYER_BOX_OFFSET = 20f;
    public static final int PLAYER_X = 100;
    public static final int PLAYER_Y = 75;
    public static final int PLAYER_FALL_Y = 60;
    public static final float PLAYER_SPEED = 350f;
    public static final float FALL_SPEED = 3f;
    public static final float POWER_UP_HEIGHT = 400f;
    public static final float OBSTACLE_HEIGHT = 75f;
    public static final float PLAYER_JUMP_SPEED = 18f;
    public static final float PLAYER_SUPER_JUMP_SPEED = 22f;
    public static final int OBSTACLE_COUNT = countFilesInDirectory("assets/obstacles/");
    public static final String BG_PATH = "assets/background.png";
    public static final String PB_PATH = "assets/buttons/play.png";
    public static final String EB_PATH = "assets/buttons/exit.png";
    public static final String IDLE_PATH = "assets/idle/";
    public static final String STARTING_PATH = "assets/starting/";
    public static final String SKATE_PATH = "assets/skate/";
    public static final String JUMP_PATH = "assets/jump/";
    public static final String SUPER_JUMP_PATH = "assets/super-jump/";
    public static final String FALL_PATH = "assets/fall/";
    public static final String POWER_UP_PATH = "assets/power-up/";

    private static int countFilesInDirectory(String directoryPath) {
        FileHandle dirHandle = Gdx.files.internal(directoryPath);

        if (dirHandle.isDirectory()) {
            FileHandle[] fileHandles = dirHandle.list();
            return fileHandles.length - 1;
        }
        return 0;
    }
}
