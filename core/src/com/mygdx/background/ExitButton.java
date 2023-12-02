package com.mygdx.background;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.mygdx.gamemanager.GameStateManager;

public class ExitButton {
    private static Texture button;
    public static Circle exitButtonBounds;

    public static void create() {
        button = new Texture("assets/buttons/quit.png");
        exitButtonBounds = new Circle(
                GameStateManager.Camera.position.x - GameStateManager.Camera.viewportWidth / 2f + button.getWidth() * 1.5f,
                GameStateManager.Camera.viewportHeight - button.getHeight() * 1.5f,
                button.getWidth() / 2f
        );
    }

    public static void render(SpriteBatch batch) {
        batch.draw(
                button,
                GameStateManager.Camera.position.x - GameStateManager.Camera.viewportWidth / 2f + button.getWidth() * 1.5f,
                GameStateManager.Camera.viewportHeight - button.getHeight() * 1.5f,
                button.getWidth(),
                button.getHeight()
        );
        update();
    }

    private static void update() {
        exitButtonBounds.set(
                GameStateManager.Camera.position.x - GameStateManager.Camera.viewportWidth / 2f + button.getWidth() * 1.5f + button.getWidth() / 2f,
                GameStateManager.Camera.viewportHeight - button.getHeight() * 1.5f + button.getHeight() / 2f,
                button.getWidth() / 2f
        );
    }

    public static void dispose() {
        button.dispose();
    }
}
