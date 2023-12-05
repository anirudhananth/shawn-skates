package com.mygdx.background;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.gamemanager.GameStateManager;
import com.mygdx.helper.Constants;
import com.mygdx.states.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Background {
    GameStateManager gsm;
    public Texture bgTexture;
    private final String bgPath;
    public Body ground;
    public static ArrayList<PowerUp> powerUps;
    public static ArrayList<Obstacle> obstacles;
    private float timeSinceLastPowerUpSpawn = 0f;
    private float timeSinceLastObstacleSpawn = 0f;
    private float powerUpSpawnInterval = 0.0f;
    private float obstacleSpawnInterval = 0f;
    private final Random random = new Random();

    public Background(GameStateManager gsm) {
        this.gsm = gsm;
        bgPath = Constants.BG_PATH;
        powerUps = new ArrayList<>();
        obstacles = new ArrayList<>();
        create();
    }

    public void create() {
        bgTexture = new Texture(bgPath);

        setGround();

        PauseButton.create();
        ExitButton.create();
    }

    private void setGround() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);
        ground = GameStateManager.World.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(100000, 75);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        ground.createFixture(fixtureDef);
    }

    public void render(SpriteBatch batch, float playerX) {
        int offset = (int) playerX / bgTexture.getWidth();
        batch.draw(bgTexture, (offset - 1) * bgTexture.getWidth() + 10f, 0);
        batch.draw(bgTexture, offset * bgTexture.getWidth() + 10f, 0);
        batch.draw(bgTexture, (offset + 1) * bgTexture.getWidth() + 10f, 0);
        if(Player.canDrawUI) {
            PauseButton.render(batch);
            ExitButton.render(batch);
        }
        for(Obstacle obstacle : obstacles) {
            obstacle.render(batch);
        }
        for(PowerUp powerUp : powerUps) {
            if(!powerUp.isCollected()) {
                powerUp.render(batch);
            }
        }
    }

    public void update(float dt) {
        PowerUp.update(dt);
        timeSinceLastPowerUpSpawn += dt;
        timeSinceLastObstacleSpawn += dt;

        if(timeSinceLastPowerUpSpawn >= powerUpSpawnInterval) {
            spawnPowerUp();
            timeSinceLastPowerUpSpawn = 0;
            powerUpSpawnInterval = 15f + random.nextFloat() * 15f;
        }

        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp powerUp = powerUpIterator.next();
            if (powerUp.isOffScreen() || powerUp.isCollected()) {
                powerUpIterator.remove();
            }
        }

        if(timeSinceLastObstacleSpawn >= obstacleSpawnInterval) {
            spawnObstacle();
            timeSinceLastObstacleSpawn = 0f;
            obstacleSpawnInterval = 5f + random.nextFloat() * 5f;
        }

        Iterator<Obstacle> obstacleIterator = obstacles.iterator();
        while (obstacleIterator.hasNext()) {
            Obstacle obstacle = obstacleIterator.next();
            if (obstacle.isOffScreen()) {
                obstacleIterator.remove();
            }
        }
    }

    private void spawnPowerUp() {
        PowerUp powerUp = new PowerUp();
        powerUps.add(powerUp);
    }

    private void spawnObstacle() {
        Obstacle obstacle = new Obstacle();
        obstacles.add(obstacle);
    }

    public void dispose() {
        bgTexture.dispose();
        PauseButton.dispose();
        ExitButton.dispose();
        PowerUp.dispose();
        GameStateManager.World.destroyBody(ground);
        ground = null;
    }
}
