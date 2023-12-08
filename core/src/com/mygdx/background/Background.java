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
    private float powerUpSpawnInterval = 5.0f;
    private float obstacleSpawnInterval = 2f;
    private final Random random = new Random();
    private Score score;
    private float scoreTimer;
    private int obstaclesCrossed;

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
        score = new Score();
        obstaclesCrossed = 0;
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
        score.render(batch);
    }

    public void update(float dt) {
        PowerUp.update(dt);
        updateScore();
        timeSinceLastPowerUpSpawn += dt;
        timeSinceLastObstacleSpawn += dt;

        if(timeSinceLastPowerUpSpawn >= powerUpSpawnInterval) {
            spawnPowerUp();
            timeSinceLastPowerUpSpawn = 0;
            powerUpSpawnInterval = Constants.MINIMUM_POWER_UP_SPAWN_INTERVAL + random.nextFloat() * Constants.RANDOM_POWER_UP_SPAWN_INTERVAL;
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
            obstacleSpawnInterval = Constants.MINIMUM_OBSTACLE_SPAWN_INTERVAL + random.nextFloat() * Constants.RANDOM_OBSTACLE_SPAWN_INTERVAL;
        }

        Iterator<Obstacle> obstacleIterator = obstacles.iterator();
        while (obstacleIterator.hasNext()) {
            Obstacle obstacle = obstacleIterator.next();
            if(!obstacle.isPlayerCrossed() && obstacle.getX() < Player.playerX) {
                obstacle.setPlayerCrossed();
                obstaclesCrossed++;
            }
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

    private void updateScore() {
        float distanceDiff = Player.playerX - Constants.PLAYER_X;
        int scoreValue = (int) distanceDiff / Constants.SCORE_RATE;
        scoreValue += obstaclesCrossed * 5;
        score.setScore(scoreValue);
    }

    public void dispose() {
        bgTexture.dispose();
        PauseButton.dispose();
        ExitButton.dispose();
        PowerUp.dispose();
        GameStateManager.World.destroyBody(ground);
        ground = null;
        powerUps = null;
        obstacles = null;
    }
}
