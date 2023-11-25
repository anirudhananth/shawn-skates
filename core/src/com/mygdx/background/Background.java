package com.mygdx.background;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.gamemanager.GameStateManager;
import com.mygdx.helper.Constants;

public class Background {
    GameStateManager gsm;
    public Texture bgTexture;
    public Texture powerUp;
    private final String bgPath;
    public Body powerUpBody;

    public Background(GameStateManager gsm) {
        this.gsm = gsm;
        bgPath = Constants.BG_PATH;
        create();
    }

    public void create() {
        bgTexture = new Texture(bgPath);
        powerUp = new Texture("assets/orb2.png");

        setPowerUpBody();

        PauseButton.create();
        Gdx.input.setInputProcessor(new InputHandler());
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

    public void render(SpriteBatch batch, float playerX) {
        int offset = (int) playerX / bgTexture.getWidth();
        batch.draw(bgTexture, (offset - 1) * bgTexture.getWidth() + 10f, 0);
        batch.draw(bgTexture, offset * bgTexture.getWidth() + 10f, 0);
        batch.draw(bgTexture, (offset + 1) * bgTexture.getWidth() + 10f, 0);
        PauseButton.render(batch);
        batch.draw(powerUp, 1200, 75);
    }

    public void update(float dt) {
    }

    public void dispose() {
        bgTexture.dispose();
        PauseButton.dispose();
    }


    static class InputHandler implements InputProcessor {
        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            Vector3 touchPoint = new Vector3(screenX, screenY, 0);
            GameStateManager.Camera.unproject(touchPoint);
            float worldX = screenX * Gdx.graphics.getWidth() / (float) Gdx.graphics.getBackBufferWidth();
            float worldY = (Gdx.graphics.getHeight() - screenY) * Gdx.graphics.getHeight() / (float) Gdx.graphics.getBackBufferHeight();
            if(PauseButton.pauseButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                System.out.println("pause button clicked");
            }
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    }
}
