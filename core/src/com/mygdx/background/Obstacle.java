package com.mygdx.background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.gamemanager.GameStateManager;
import com.mygdx.helper.Constants;
import java.util.Random;

public class Obstacle {
    private final Texture obstacleTexture;
    private Body obstacleBody;
    private final float x, y;
    Random random = new Random();
    private final int index;
    private boolean playerCrossed;

    public Obstacle() {
        index = random.nextInt(Constants.OBSTACLE_COUNT);
        obstacleTexture = new Texture(Gdx.files.internal("obstacles/" + index + ".png"));
        this.x =
                GameStateManager.Camera.position.x
                + GameStateManager.Camera.viewportWidth
                + Constants.MINIMUM_OBSTACLE_SPAWN_OFFSET
                + random.nextFloat() * Constants.RANDOM_OBSTACLE_SPAWN_OFFSET;
        this.y = Constants.OBSTACLE_HEIGHT;
        playerCrossed = false;
        create();
    }

    private void create() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x + obstacleTexture.getWidth()/2f, y + obstacleTexture.getHeight()/2f);
        obstacleBody = GameStateManager.World.createBody(bodyDef);

        switch(index) {
            case 1:
            case 2:
            case 8:
            case 11:
            case 14:
                setCircularBody();
                break;
            default:
                setPolygonBody();
                break;
        }

    }

    private void setCircularBody() {
        CircleShape shape = new CircleShape();
        shape.setRadius((float) obstacleTexture.getHeight() / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        obstacleBody.createFixture(fixtureDef);
        shape.dispose();
    }

    private void setPolygonBody() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((float) obstacleTexture.getWidth() / 2f, (float) obstacleTexture.getHeight() / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        obstacleBody.createFixture(fixtureDef);
        shape.dispose();
    }

    public void render(SpriteBatch batch) {
        batch.draw(
                obstacleTexture,
                x,
                y,
                obstacleTexture.getWidth(),
                obstacleTexture.getHeight()
        );
    }

    public void dispose() {
        obstacleTexture.dispose();
    }

    public Body getObstacleBody() {
        return obstacleBody;
    }

    public void setObstacleBodyNull() {
        obstacleBody = null;
    }

    public boolean isPlayerCrossed() {
        return playerCrossed;
    }

    public void setPlayerCrossed() {
        playerCrossed = true;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean isOffScreen() {
        return x + obstacleTexture.getWidth() < GameStateManager.Camera.position.x - GameStateManager.Camera.viewportWidth / 2;
    }
}
