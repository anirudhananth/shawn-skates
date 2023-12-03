package com.mygdx.background;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.gamemanager.GameStateManager;
import com.mygdx.helper.Constants;
import com.mygdx.states.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Background {
    GameStateManager gsm;
    public Texture bgTexture;
    public Texture powerUp;
    private final String bgPath;
    public Body powerUpBody;
    public Body ground;
    public static ArrayList<PowerUp> powerUps;
    private float timeSinceLastPowerUpSpawn = 0;
    private float spawnInterval = 20.0f; // spawn a power-up every 3 seconds, for example
    private final Random random = new Random();

    public Background(GameStateManager gsm) {
        this.gsm = gsm;
        bgPath = Constants.BG_PATH;
        powerUps = new ArrayList<>();
        create();
    }

    public void create() {
        bgTexture = new Texture(bgPath);
        powerUp = new Texture("assets/testorb.png");

        setPowerUpBody();
        setGround();

        PauseButton.create();
        ExitButton.create();
        powerUps.add(new PowerUp());
        for(PowerUp powerUp : powerUps) {
            powerUp.create();
        }
    }

    private void setPowerUpBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(1200 + powerUp.getWidth()/2f, 75 + powerUp.getHeight()/2f);
        powerUpBody = GameStateManager.World.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((float) powerUp.getWidth() / 2, (float) powerUp.getHeight() / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        powerUpBody.createFixture(fixtureDef);
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
        for(PowerUp powerUp : powerUps) {
            if(!powerUp.isCollected()) {
                powerUp.render(batch);
            }
        }
        batch.draw(powerUp, 1200, 75);
    }

    public void update(float dt) {
        PowerUp.update(dt);
        timeSinceLastPowerUpSpawn += dt;
        if(timeSinceLastPowerUpSpawn >= spawnInterval) {
            spawnPowerUp();
            timeSinceLastPowerUpSpawn = 0;
            spawnInterval = 15f + random.nextFloat() * 15f;
        }

        Iterator<PowerUp> iterator = powerUps.iterator();
        while (iterator.hasNext()) {
            PowerUp powerUp = iterator.next();
            if (powerUp.isOffScreen() || powerUp.isCollected()) {
                iterator.remove();
            }
        }
    }

    private void spawnPowerUp() {
        float x = GameStateManager.Camera.position.x + GameStateManager.Camera.viewportWidth + random.nextFloat() * 1000;
        PowerUp powerUp = new PowerUp(x);
        powerUp.create();
        powerUps.add(powerUp);
    }

    public void dispose() {
        bgTexture.dispose();
        PauseButton.dispose();
        ExitButton.dispose();
        PowerUp.dispose();
        GameStateManager.World.destroyBody(powerUpBody);
        GameStateManager.World.destroyBody(ground);
        ground = null;
        powerUpBody = null;
    }
}
