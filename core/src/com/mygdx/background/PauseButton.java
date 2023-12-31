package com.mygdx.background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.mygdx.gamemanager.GameStateManager;
import com.mygdx.helper.Constants;

public class PauseButton {
    public static Texture button;
    private static float x;
    private static float y;
    public static Circle pauseButtonBounds;

    public static void create() {
        button = new Texture("buttons/pause.png");
        x = GameStateManager.Camera.viewportWidth/2f - button.getWidth()*1.5f;
        y = GameStateManager.Camera.viewportHeight - button.getHeight()*1.5f;
        pauseButtonBounds = new Circle(
            GameStateManager.Camera.position.x + x + button.getWidth()/2f,
            y + button.getHeight()/2f,
            button.getWidth()/2f
        );
    }

    public static void render(SpriteBatch batch) {
        batch.draw(
                button,
                GameStateManager.Camera.position.x + GameStateManager.Camera.viewportWidth / 2f - button.getWidth() - Constants.CAMERA_PLAYER_OFFSET,
                GameStateManager.Camera.viewportHeight - button.getHeight() * 1.5f,
                button.getWidth(),
                button.getHeight()
        );
        update();
    }

    private static void update() {
        pauseButtonBounds.set(
                GameStateManager.Camera.position.x + GameStateManager.Camera.viewportWidth/2f - button.getWidth() - Constants.CAMERA_PLAYER_OFFSET + button.getWidth()/2f,
                GameStateManager.Camera.viewportHeight - button.getHeight()*1.5f + button.getHeight()/2f,
                button.getWidth()/2f
        );
    }

    public static void dispose() {
        button.dispose();
    }
}
