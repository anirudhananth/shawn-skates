package com.mygdx.states;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.animations.Animations;
import com.mygdx.audio.MusicList;
import com.mygdx.audio.SoundList;
import com.mygdx.gamemanager.GameStateManager;
import com.mygdx.helper.Constants;
import com.badlogic.gdx.math.Rectangle;

import javax.swing.plaf.IconUIResource;

public class Menu extends State {
    private Texture title;
    private Texture background;
    private Texture playBtn;
    private Texture exitBtn;
    private Texture volumeBtn;
    private final String titlePath;
    private final String bgPath;
    private final String playBtnPath;
    private final String exitBtnPath;
    private final String volumeBtnPath;
    private Rectangle playButtonBounds;
    private Rectangle exitButtonBounds;
    private Circle volumeButtonBounds;
    private boolean playTouchDown;
    private boolean exitTouchDown;
    private boolean volumeTouchDown;
    private final float BUTTON_GAP = 2f;
    private float elapsed;
    public Menu(GameStateManager gsm) {
        super(gsm);

        titlePath = Constants.TITLE_PATH;
        bgPath = Constants.BG_PATH;
        playBtnPath = Constants.PB_PATH;
        exitBtnPath = Constants.EB_PATH;
        volumeBtnPath = Constants.VOLUME_PATH;

        playTouchDown = false;
        exitTouchDown = false;
        volumeTouchDown = false;

        create();
    }

    private void create() {
        title = new Texture(titlePath);
        background = new Texture(bgPath);
        playBtn = new Texture(playBtnPath);
        exitBtn = new Texture(exitBtnPath);
        volumeBtn = new Texture(volumeBtnPath);

        playButtonBounds = new Rectangle(
                Gdx.graphics.getWidth() / 2f - playBtn.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f + BUTTON_GAP,
                playBtn.getWidth(),
                playBtn.getHeight()
        );
        exitButtonBounds = new Rectangle(
                Gdx.graphics.getWidth() / 2f - exitBtn.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f - BUTTON_GAP - exitBtn.getHeight(),
                exitBtn.getWidth(),
                exitBtn.getHeight()
        );
        volumeButtonBounds = new Circle(
                GameStateManager.Camera.position.x - GameStateManager.Camera.viewportWidth / 2f + Constants.PLAYER_X + 10f + volumeBtn.getWidth() / 2f,
                GameStateManager.Camera.position.y - GameStateManager.Camera.viewportHeight / 2f + Constants.PLAYER_Y + 175f + volumeBtn.getHeight() / 2f,
                volumeBtn.getWidth() / 2f
        );

        setInputProcessor();
        elapsed = 0;

        MusicList.create();
        SoundList.create();
        MusicList.playMenuMusic();
    }

    @Override
    public void render(SpriteBatch batch) {
        elapsed += Gdx.graphics.getDeltaTime();
        batch.begin();
        batch.draw(
                background,
                GameStateManager.Camera.position.x - GameStateManager.Camera.viewportWidth / 2f,
                GameStateManager.Camera.position.y - GameStateManager.Camera.viewportHeight / 2f
        );
        batch.draw(
                Animations.getIdle().getKeyFrame(elapsed, true),
                GameStateManager.Camera.position.x - GameStateManager.Camera.viewportWidth / 2f + Constants.PLAYER_X - 28f,
                GameStateManager.Camera.position.y - GameStateManager.Camera.viewportHeight / 2f + Constants.PLAYER_Y
        );
        batch.draw(
                playBtn,
                GameStateManager.Camera.position.x - playBtn.getWidth() / 2f,
                GameStateManager.Camera.position.y + BUTTON_GAP
        );
        batch.draw(
                exitBtn,
                GameStateManager.Camera.position.x - exitBtn.getWidth() / 2f,
                GameStateManager.Camera.position.y - BUTTON_GAP - exitBtn.getHeight()
        );
        batch.draw(
                volumeBtn,
                GameStateManager.Camera.position.x - GameStateManager.Camera.viewportWidth / 2f + Constants.PLAYER_X + 10f,
                GameStateManager.Camera.position.y - GameStateManager.Camera.viewportHeight / 2f + Constants.PLAYER_Y + 175f
        );
        batch.draw(
                title,
                GameStateManager.Camera.position.x - GameStateManager.Camera.viewportWidth / 2f + 20f,
                GameStateManager.Camera.position.y + GameStateManager.Camera.viewportHeight / 2f - title.getHeight() - 20f
        );
        batch.end();
    }

    @Override
    public void update(float dt) {
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            gsm.push(new Player(gsm));
        }
        playButtonBounds.set(
                GameStateManager.Camera.position.x - playBtn.getWidth() / 2f,
                GameStateManager.Camera.position.y + BUTTON_GAP,
                playBtn.getWidth(),
                playBtn.getHeight()
        );
        exitButtonBounds.set(
                GameStateManager.Camera.position.x - exitBtn.getWidth() / 2f,
                GameStateManager.Camera.position.y - BUTTON_GAP - exitBtn.getHeight(),
                exitBtn.getWidth(),
                exitBtn.getHeight()
        );
        volumeButtonBounds.set(
                GameStateManager.Camera.position.x - GameStateManager.Camera.viewportWidth / 2f + Constants.PLAYER_X + 10f + volumeBtn.getWidth() / 2f,
                GameStateManager.Camera.position.y - GameStateManager.Camera.viewportHeight / 2f + Constants.PLAYER_Y + 175f + volumeBtn.getHeight() / 2f,
                volumeBtn.getWidth() / 2f
        );
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
                    playTouchDown = true;
                    SoundList.playClickSound();
                }

                if (exitButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                    exitTouchDown = true;
                    SoundList.playClickSound();
                }

                if(volumeButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                    volumeTouchDown = true;
                    SoundList.playClickSound();
                }

                return true; // Return true to indicate that the touch is handled
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                float worldX = screenX * Gdx.graphics.getWidth() / (float) Gdx.graphics.getBackBufferWidth();
                float worldY = (Gdx.graphics.getHeight() - screenY) * Gdx.graphics.getHeight() / (float) Gdx.graphics.getBackBufferHeight();

                Vector3 touchPoint = new Vector3(screenX, screenY, 0);
                GameStateManager.Camera.unproject(touchPoint);
                if (playTouchDown && playButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                    MusicList.stopMenuMusic();
                    gsm.push(new Player(gsm));
                }

                if(exitTouchDown && exitButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                    MusicList.dispose();
                    Gdx.app.exit();
                    System.exit(0);
                }

                if(volumeTouchDown && volumeButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                    MusicList.setPlay();
                }

                playTouchDown = false;
                exitTouchDown = false;
                volumeTouchDown = false;
                return true;
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
