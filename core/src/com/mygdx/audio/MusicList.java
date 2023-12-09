package com.mygdx.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.mygdx.helper.Constants;

public class MusicList {
    private static Music menu, skate, background;
    public static boolean canPlay;

    public static void create() {
        canPlay = true;

        menu = Gdx.audio.newMusic(Gdx.files.internal(Constants.MENU_MUSIC));
        skate = Gdx.audio.newMusic(Gdx.files.internal(Constants.SKATE_MUSIC));
        background = Gdx.audio.newMusic(Gdx.files.internal(Constants.BACKGROUND_MUSIC));

        menu.setLooping(true);
        skate.setLooping(true);
        background.setLooping(true);
    }

    public static Music getMenu() {
        return menu;
    }

    public static Music getSkate() {
        return skate;
    }

    public static Music getBackground() {
        return background;
    }

    public static void playMenuMusic() {
        if(canPlay) {
            menu.setVolume(0.5f);
            menu.play();
        }
    }

    public static void playSkateMusic() {
        if(canPlay) {
            skate.setVolume(0.5f);
            skate.play();
        }
    }

    public static void playBackgroundMusic() {
        if(canPlay) {
            background.setVolume(0.25f);
            background.play();
        }
    }

    public static void stopMenuMusic() {
        if(!menu.isPlaying()) return;
        float volume = menu.getVolume();
        while (volume >= 0) {
            menu.setVolume(volume);
            volume -= 0.001f;
        }
        menu.stop();
    }

    public static void stopSkateMusic() {
        if(!skate.isPlaying()) return;
        float volume = skate.getVolume();
        while (volume >= 0) {
            skate.setVolume(volume);
            volume -= 0.001f;
        }
        skate.stop();
    }

    public static void stopBackgroundMusic() {
        if(!background.isPlaying()) return;
        float volume = background.getVolume();
        while (volume >= 0) {
            background.setVolume(volume);
            volume -= 0.001f;
        }
        background.stop();
    }

    public static void dispose() {
        menu.dispose();
        skate.dispose();
        background.dispose();
    }

    public static void setPlay() {
        canPlay = !canPlay;
        if(!canPlay) {
            menu.pause();
        } else {
            menu.play();
        }
    }
}
