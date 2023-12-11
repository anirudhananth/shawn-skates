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
    public static final String TITLE_PATH = "main-images/title.png";
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
    public static final int SCORE_RATE = 800;
    public static final float MINIMUM_OBSTACLE_SPAWN_INTERVAL = 2f;
    public static final float RANDOM_OBSTACLE_SPAWN_INTERVAL = 3f;
    public static final float MINIMUM_OBSTACLE_SPAWN_OFFSET = 50f;
    public static final float RANDOM_OBSTACLE_SPAWN_OFFSET = 50f;
    public static final float MINIMUM_POWER_UP_SPAWN_INTERVAL = 15f;
    public static final float RANDOM_POWER_UP_SPAWN_INTERVAL = 15f;
    public static final int OBSTACLE_COUNT = 16;//countFilesInDirectory("assets/obstacles/");
    public static final int IDLE_COUNT = 40;
    public static final int FALL_COUNT = 18;
    public static final int JUMP_COUNT = 16;
    public static final int SUPER_JUMP_COUNT = 17;
    public static final int SKATE_COUNT = 27;
    public static final int STARTING_COUNT = 54;
    public static final int POWER_UP_COUNT = 6;
    public static final String BG_PATH = "main-images/background.png";
    public static final String PB_PATH = "buttons/play.png";
    public static final String EB_PATH = "buttons/exit.png";
    public static final String PAUSE_PATH = "buttons/pause.png";
    public static final String QUIT_PATH = "buttons/quit.png";
    public static final String RESUME_PATH = "buttons/resume.png";
    public static final String RETURN_PATH = "buttons/return.png";
    public static final String IDLE_PATH = "idle/";
    public static final String STARTING_PATH = "starting/";
    public static final String SKATE_PATH = "skate/";
    public static final String JUMP_PATH = "jump/";
    public static final String SUPER_JUMP_PATH = "super-jump/";
    public static final String FALL_PATH = "fall/";
    public static final String POWER_UP_PATH = "power-up/";
    public static final String MENU_MUSIC = "audio/music/menu.mp3";
    public static final String SKATE_MUSIC = "audio/music/skate.mp3";
    public static final String BACKGROUND_MUSIC = "audio/music/theme.mp3";
    public static final String FALL_SOUND = "audio/sound/fall.mp3";
    public static final String JUMP_SOUND = "audio/sound/jump.mp3";
    public static final String SUPER_JUMP_SOUND = "audio/sound/super-jump.mp3";
    public static final String GAME_OVER_SOUND = "audio/sound/game-over.mp3";
    public static final String POWER_UP_SOUND = "audio/sound/power-up.mp3";
    public static final String VOLUME_PATH = "buttons/volume.png";
    public static final String CLICK_PATH = "audio/sound/click.mp3";


    private static int countFilesInDirectory(String directoryPath) {
        FileHandle dirHandle = Gdx.files.internal(directoryPath);

        if (dirHandle.isDirectory()) {
            FileHandle[] fileHandles = dirHandle.list();
            return fileHandles.length - 1;
        }
        return 0;
    }
}
