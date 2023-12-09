package com.mygdx.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.animations.Animations;
import com.mygdx.audio.SoundList;
import com.mygdx.gamemanager.GameStateManager;
import com.mygdx.helper.Constants;

public class Pause extends State {
    private final GameStateManager gsm;
    private Texture background;
    private Texture screen;
    private Texture resumeButton;
    private Circle resumeButtonBounds;
    private float elapsed;
    private boolean startCountDown = false;

    public Pause(GameStateManager gsm) {
        super(gsm);
        this.gsm = gsm;
        create();
    }

    public void create() {
        elapsed = 0;

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 0.2f);
        pixmap.fill();

        screen = new Texture(pixmap);
        pixmap.dispose();
        background = new Texture(Constants.BG_PATH);
        resumeButton = new Texture(Constants.RESUME_PATH);
        resumeButtonBounds = new Circle(
                GameStateManager.Camera.position.x,
                GameStateManager.Camera.position.y,
                resumeButton.getWidth() / 2f
        );

        setInputProcessor();
    }

    public void render(SpriteBatch batch) {
        batch.begin();

        // Background
        batch.draw(
                background,
                GameStateManager.Camera.position.x - GameStateManager.Camera.viewportWidth / 2f,
                GameStateManager.Camera.position.y - GameStateManager.Camera.viewportHeight / 2f,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );

        // Pause button
        batch.draw(
                screen,
                GameStateManager.Camera.position.x - GameStateManager.Camera.viewportWidth/2f,
                GameStateManager.Camera.position.y - GameStateManager.Camera.viewportHeight / 2f,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight()
        );

        if(!startCountDown) {
            // Resume button
            batch.draw(
                    resumeButton,
                    GameStateManager.Camera.position.x - resumeButton.getWidth() / 2f,
                    GameStateManager.Camera.position.y - resumeButton.getHeight() / 2f
            );
        } else {
            // Countdown gif animation
            batch.draw(
                    Animations.getCountdown().getKeyFrame(elapsed, false),
                    GameStateManager.Camera.position.x - Animations.getCountdown().getKeyFrame(elapsed, true).getRegionWidth() / 2f,
                    GameStateManager.Camera.position.y - Animations.getCountdown().getKeyFrame(elapsed, true).getRegionHeight() / 2f
            );
        }
        batch.end();
    }

    @Override
    public void update(float dt) {
        elapsed += dt;

        if(startCountDown && Animations.getCountdown().isAnimationFinished(elapsed)) {
            startCountDown = false;
            dispose();
            gsm.pop();
        }
    }

    @Override
    public void dispose() {
        background.dispose();
        screen.dispose();
        resumeButton.dispose();
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
                Vector3 touchPoint = new Vector3(screenX, screenY, 0);
                GameStateManager.Camera.unproject(touchPoint);
                if(!startCountDown && resumeButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                    SoundList.playClickSound();
                }
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                Vector3 touchPoint = new Vector3(screenX, screenY, 0);
                GameStateManager.Camera.unproject(touchPoint);
                if(!startCountDown && resumeButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                    elapsed = 0;
                    startCountDown = true;
                }
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
