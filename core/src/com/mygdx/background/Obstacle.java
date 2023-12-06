package com.mygdx.background;

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

    public Obstacle() {
        index = random.nextInt(Constants.OBSTACLE_COUNT);
        obstacleTexture = new Texture("assets/obstacles/" + index + ".png");
        this.x = GameStateManager.Camera.position.x + GameStateManager.Camera.viewportWidth + 250f + random.nextFloat() * 750f;
        this.y = Constants.OBSTACLE_HEIGHT;
        create();
    }

    private void create() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x + obstacleTexture.getWidth()/2f, y + obstacleTexture.getHeight()/2f);
        obstacleBody = GameStateManager.World.createBody(bodyDef);

        switch(index) {
            case 2:
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
    }

    private void setPolygonBody() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((float) obstacleTexture.getWidth() / 2f, (float) obstacleTexture.getHeight() / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        obstacleBody.createFixture(fixtureDef);
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

    public boolean isOffScreen() {
        return x + obstacleTexture.getWidth() < GameStateManager.Camera.position.x - GameStateManager.Camera.viewportWidth / 2;
    }
}
