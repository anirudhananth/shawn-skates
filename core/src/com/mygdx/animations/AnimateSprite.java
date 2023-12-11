package com.mygdx.animations;

import java.util.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class AnimateSprite extends Sprite
{
    // Attributes
    private Animation<Texture> animation;
    private Texture[] frames; // Your frames
    public int currentFrame; // This value will iterate over frames to display the right frame
    private final float fps; // Your frame rate in frame per second
    private float timer;
    private String directoryPath;
    private int frameCount;

    // Constructor
    public AnimateSprite(String directoryPath, float framePerSecond, int frameCount) {
        super(new Texture(Gdx.files.internal(directoryPath + "0.png"))); // Init your sprite with the first frame texture

        this.frameCount = frameCount;
        this.directoryPath = directoryPath;
        fps = framePerSecond;
        currentFrame = 0;
        timer = 0;
        loadFrames();
    }
    public AnimateSprite(Texture[] frames, float framePerSecond)
    {
        super(frames[0]); // Init your sprite with the first frame texture

        fps = framePerSecond;
        currentFrame = 0;
        timer = 0;
    }

    private void sortFileByName(FileHandle[] imageFiles) {
        Arrays.sort(imageFiles, new Comparator<FileHandle>() {
            @Override
            public int compare(FileHandle file1, FileHandle file2) {
                // Extract numeric values from file names and compare
                int number1 = extractNumber(file1.nameWithoutExtension());
                int number2 = extractNumber(file2.nameWithoutExtension());

                return Integer.compare(number1, number2);
            }

            private int extractNumber(String name) {
                try {
                    return Integer.parseInt(name.replaceAll("\\D", ""));
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        });
    }

    public void loadFrames() {
        Texture[] frames = new Texture[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = new Texture(Gdx.files.internal(directoryPath + i + ".png"));
        }

        animation = new Animation<>(1f/fps, frames);
    }

    public Animation<Texture> getAnimation() {
        return animation;
    }

    public void update(float elapsedTime)
    {
        if (timer < 1.0f / fps)
            timer += elapsedTime;
        else
        {
            timer -= 1.0f / fps;
            nextFrame();
        }
    }

    public void nextFrame()
    {
        // Change frame
        if (currentFrame < frames.length)
            currentFrame++;
        else
            currentFrame = 0;

        // Set the good frame texture
        setTexture(frames[currentFrame]);
    }

    public void resetAnimation() {
        currentFrame = 0;
        timer = 0;
    }

    public void dispose() {
        for(Texture frame : frames) {
            frame.dispose();
        }
    }
}