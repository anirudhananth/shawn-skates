package com.mygdx.background;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.animations.Animations;
import com.mygdx.gamemanager.GameStateManager;
import com.mygdx.helper.Constants;

public class PowerUp {
    public static Animation<Texture> powerUpAnimation;
    private static float elapsed;
    public Body powerUpBody;
    private boolean isCollected;
    private final float powerUpX, powerUpY;

    PowerUp() {
        powerUpX = 1200f;
        powerUpY = 400f;
    }

    PowerUp(float X) {
        powerUpX = X;
        powerUpY = Constants.POWER_UP_HEIGHT;
    }

    public void create() {
        elapsed = 0;
        powerUpAnimation = Animations.getPowerUp();
        isCollected = false;
        setPowerUpBody();
    }

    private void setPowerUpBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(powerUpX + powerUpAnimation.getKeyFrames()[0].getWidth()/2f, powerUpY + powerUpAnimation.getKeyFrames()[0].getHeight()/2f);
        powerUpBody = GameStateManager.World.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(powerUpAnimation.getKeyFrames()[0].getWidth() / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        powerUpBody.createFixture(fixtureDef);
    }

    public boolean isOffScreen() {
        Camera camera = GameStateManager.Camera;
        return powerUpX + powerUpAnimation.getKeyFrames()[0].getWidth() / 2f < camera.position.x - camera.viewportWidth / 2;
    }

    public void render(SpriteBatch batch) {
        batch.draw(
            powerUpAnimation.getKeyFrame(elapsed, true),
            powerUpX,
            powerUpY
        );
    }

    public static void update(float dt) {
        elapsed += dt;
    }

    public static void dispose() {
        powerUpAnimation = null;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean b) {
        isCollected = b;
    }
}
