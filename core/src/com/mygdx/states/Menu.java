package com.mygdx.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.animations.Animations;
import com.mygdx.gamemanager.GameStateManager;
import com.mygdx.helper.Constants;
import com.badlogic.gdx.math.Rectangle;

public class Menu extends State {
    private Texture background;
    private Texture playBtn;
    private final String bgPath;
    private final String playBtnPath;
    private Rectangle playButtonBounds;
    private float elapsed;
    public Menu(GameStateManager gsm) {
        super(gsm);
        bgPath = Constants.BG_PATH;
        playBtnPath = Constants.PB_PATH;
        create();
    }

    private void create() {
        background = new Texture(bgPath);
        playBtn = new Texture(playBtnPath);
        playButtonBounds = new Rectangle(
                Gdx.graphics.getWidth() / 2f - playBtn.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f - playBtn.getHeight() / 2f,
                playBtn.getWidth(), playBtn.getHeight());
        setInputProcessor();
        elapsed = 0;
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void render(SpriteBatch batch) {
        elapsed += Gdx.graphics.getDeltaTime();
        batch.begin();
        batch.draw(background, 0, 0);
        batch.draw(Animations.getIdle().getKeyFrame(elapsed, true), Constants.PLAYER_X - 15f, Constants.PLAYER_Y);
        batch.draw(playBtn, Gdx.graphics.getWidth() / 2f - playBtn.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - playBtn.getHeight() / 2f);
        batch.end();
    }

    @Override
    public void dispose() {
    }

    private void setInputProcessor() {
        inputProcessor = new InputProcessor() {
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
                float worldX = screenX * Gdx.graphics.getWidth() / (float) Gdx.graphics.getBackBufferWidth();
                float worldY = (Gdx.graphics.getHeight() - screenY) * Gdx.graphics.getHeight() / (float) Gdx.graphics.getBackBufferHeight();

                Vector3 touchPoint = new Vector3(screenX, screenY, 0);
                GameStateManager.Camera.unproject(touchPoint);
                if (playButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                    gsm.push(new Player(gsm));

                }

                return true; // Return true to indicate that the touch is handled
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
        };
        Gdx.input.setInputProcessor(inputProcessor);
    }
}
