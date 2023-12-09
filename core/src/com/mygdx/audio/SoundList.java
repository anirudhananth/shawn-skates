package com.mygdx.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.helper.Constants;

public class SoundList {
    private static Sound fall, jump, superJump, gameOver, powerUp, click;

    public static void create() {
        fall = Gdx.audio.newSound(Gdx.files.internal(Constants.FALL_SOUND));
        jump = Gdx.audio.newSound(Gdx.files.internal(Constants.JUMP_SOUND));
        superJump = Gdx.audio.newSound(Gdx.files.internal(Constants.SUPER_JUMP_SOUND));
        gameOver = Gdx.audio.newSound(Gdx.files.internal(Constants.GAME_OVER_SOUND));
        powerUp = Gdx.audio.newSound(Gdx.files.internal(Constants.POWER_UP_SOUND));
        click = Gdx.audio.newSound(Gdx.files.internal(Constants.CLICK_PATH));
    }

    public static Sound getFall() {
        return fall;
    }

    public static Sound getJump() {
        return jump;
    }

    public static Sound getSuperJump() {
        return superJump;
    }

    public static Sound getGameOver() {
        return gameOver;
    }

    public static Sound getPowerUp() {
        return powerUp;
    }

    public static Sound getClick() {
        return click;
    }

    public static void playFallSound() {
        long soundId = fall.play();
        fall.setVolume(soundId, 0.1f);
    }

    public static void playJumpSound() {
        long soundId = jump.play();
        jump.setVolume(soundId, 0.5f);
    }

    public static void playSuperJumpSound() {
        long soundId = superJump.play();
        superJump.setVolume(soundId, 0.35f);
    }

    public static void playGameOverSound() {
        long soundId = gameOver.play();
        gameOver.setVolume(soundId, 0.05f);
    }

    public static void playPowerUpSound() {
        long soundId = powerUp.play();
        powerUp.setVolume(soundId, 0.25f);
    }

    public static void playClickSound() {
        long soundId = click.play();
        click.setVolume(soundId, 1.0f);
    }
}
