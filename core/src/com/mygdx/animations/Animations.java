package com.mygdx.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.helper.Constants;

public class Animations {

    private static Animation<Texture> Idle;
    private static Animation<Texture> Starting;
    private static Animation<Texture> Skate;
    private static Animation<Texture> Jump;
    private static Animation<Texture> SuperJump;
    private static Animation<Texture> Fall;
    private static Animation<Texture> PowerUp;
    private final static float fps = 17f;
    private final static float superJumpFps = 14f;

    public static void create() {
        Idle = new AnimateSprite(Constants.IDLE_PATH, fps).getAnimation();
        Starting = new AnimateSprite(Constants.STARTING_PATH, fps).getAnimation();
        Skate = new AnimateSprite(Constants.SKATE_PATH, fps).getAnimation();
        Jump = new AnimateSprite(Constants.JUMP_PATH, fps).getAnimation();
        SuperJump = new AnimateSprite(Constants.SUPER_JUMP_PATH, superJumpFps).getAnimation();
        Fall = new AnimateSprite(Constants.FALL_PATH, superJumpFps).getAnimation();
    }

    public static Animation<Texture> getIdle() {
        if(Idle == null) {
            throw new NullPointerException("Idle animation is null.");
        }
        return Idle;
    }

    public static Animation<Texture> getStarting() {
        if(Starting == null) {
            throw new NullPointerException("Starting animation is null.");
        }
        return Starting;
    }

    public static Animation<Texture> getSkate() {
        if(Skate == null) {
            throw new NullPointerException("Skate animation is null.");
        }
        return Skate;
    }

    public static Animation<Texture> getJump() {
        if(Jump == null) {
            throw new NullPointerException("Jump animation is null.");
        }
        return Jump;
    }

    public static Animation<Texture> getSuperJump() {
        if(SuperJump == null) {
            throw new NullPointerException("Super jump animation is null.");
        }
        return SuperJump;
    }

    public static Animation<Texture> getFall() {
        if(Fall == null) {
            throw new NullPointerException("Fall animation is null.");
        }
        return Fall;
    }

    public static void dispose() {
        disposeFrames(Idle.getKeyFrames());
        disposeFrames(Starting.getKeyFrames());
        disposeFrames(Skate.getKeyFrames());
        disposeFrames(Jump.getKeyFrames());
        disposeFrames(SuperJump.getKeyFrames());
        disposeFrames(Fall.getKeyFrames());
    }

    private static void disposeFrames(Texture[] frames) {
        for(Texture frame : frames) {
            frame.dispose();
        }
    }
}
