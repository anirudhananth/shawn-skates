package com.mygdx.background;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.mygdx.gamemanager.GameStateManager;
import com.mygdx.helper.Constants;

public class EndScreen {
    private Texture screen;
    private Texture returnButton;
    private Circle returnButtonBounds;
    private static boolean canAccess;

    public EndScreen() {
        create();
    }
    public void create() {
        canAccess = false;

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 0.2f);
        pixmap.fill();

        screen = new Texture(pixmap);
        returnButton = new Texture(Constants.RETURN_PATH);
        returnButtonBounds = new Circle(
                GameStateManager.Camera.position.x,
                GameStateManager.Camera.position.y,
                returnButton.getWidth() / 2f
        );

        pixmap.dispose();
    }

    public void render(SpriteBatch batch) {
        batch.draw(
                screen,
                GameStateManager.Camera.position.x - GameStateManager.Camera.viewportWidth / 2f,
                GameStateManager.Camera.position.y - GameStateManager.Camera.viewportHeight / 2f,
                GameStateManager.Camera.viewportWidth,
                GameStateManager.Camera.viewportHeight
        );
        batch.draw(
                returnButton,
                GameStateManager.Camera.position.x - returnButton.getWidth() / 2f,
                GameStateManager.Camera.position.y - returnButton.getHeight() / 2f,
                returnButton.getWidth(),
                returnButton.getHeight()
        );
        returnButtonBounds.set(
                GameStateManager.Camera.position.x,
                GameStateManager.Camera.position.y,
                returnButton.getWidth() / 2f
        );
    }

    public static void setAccess() {
        canAccess = true;
    }

    public static boolean getAccess() {
        return canAccess;
    }

    public Circle getReturnButtonBounds() {
        return returnButtonBounds;
    }
}
