package com.mygdx.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.animations.Animations;
import com.mygdx.audio.MusicList;
import com.mygdx.audio.SoundList;
import com.mygdx.background.*;
import com.mygdx.gamemanager.GameStateManager;
import com.mygdx.helper.Constants;

import java.util.Iterator;

public class Player extends State {
    GameStateManager gsm;
    public Animation<Texture> playerAnimation;
    private int startingFrameCount;
    private int fallingFrameCount;
    public Body playerBody;
    public Background background;
    private EndScreen endScreen;
    private BodyDef playerBodyDef;
    public static float playerX, playerY;
    private boolean isJumping = false;
    private boolean isLanding = false;
    private boolean playedFallSound = false;
    private float fallSpeed = Constants.PLAYER_SPEED;
    public float verticalSpeed;
    public float verticalSpeedOffset = 1;
    private float elapsed;
    float startSpeed = 0.01f;
    public boolean isFalling = false;
    private float fallTimer = 0f;
    public static boolean canDrawUI = true;
    private static boolean isSuperJump;
    private static int jumpCounter;
    private boolean exitTouchDown;
    private boolean pauseTouchDown;
    private boolean returnTouchDown;

    public Player(GameStateManager gsm) {
        super(gsm);
        this.gsm = gsm;
        background = new Background(gsm);

        exitTouchDown = false;
        pauseTouchDown = false;
        returnTouchDown = false;

        create();
    }

    public void create () {
        elapsed = 0;
        isSuperJump = false;
        playerAnimation = Animations.getStarting();
        startingFrameCount = Animations.getStarting().getKeyFrames().length;
        fallingFrameCount = Animations.getFall().getKeyFrames().length;
        endScreen = new EndScreen();

        setPlayerBody();

        playerX = Constants.PLAYER_X; // Initial X position of the player
        playerY = Constants.PLAYER_Y; // Initial Y position of the player
        setInputProcessor();

        MusicList.playBackgroundMusic();
    }

    private void setPlayerBody() {
        playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.position.set(Constants.PLAYER_X, Constants.PLAYER_Y);
        playerBody = GameStateManager.World.createBody(playerBodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(
                (float) playerAnimation.getKeyFrames()[0].getWidth() / 3.5f,
                (float) playerAnimation.getKeyFrames()[0].getHeight() / 3.5f
        );
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        playerBody.createFixture(fixtureDef);
        shape.dispose();
    }

    public void render (SpriteBatch batch) {
        batch.begin();
        background.render(batch, playerX);
        if(playerAnimation == Animations.getStarting()) {
            batch.draw(
                    playerAnimation.getKeyFrame(elapsed, true),
                    playerX - 19.5f,
                    playerY - 2.5f
            );
        } else if(!isFalling) {
            batch.draw(
                    playerAnimation.getKeyFrame(elapsed, true),
                    playerX,
                    playerY
            );
        } else {
            batch.draw(
                    playerAnimation.getKeyFrame(elapsed, false),
                    playerX,
                    playerY
            );
            if(fallTimer >= 3f) {
                if(!EndScreen.getAccess()) EndScreen.setAccess();
                if(!endScreen.getScoreSet()) {
                    endScreen.setScores();
                    endScreen.setScoreSet();
                }
                endScreen.render(batch);
            }
        }
        batch.end();
    }

    public void update(float dt) {
        background.update(dt);

        if(playerAnimation == Animations.getStarting()) {
            if(playerAnimation.getKeyFrameIndex(elapsed) == startingFrameCount - 1) {
                elapsed = 0;
                playerAnimation = Animations.getSkate();
            } else {
                playerX += startSpeed * Gdx.graphics.getDeltaTime();
                startSpeed *= 1.1f;
                if(startSpeed > Constants.PLAYER_SPEED) startSpeed = Constants.PLAYER_SPEED;
            }
            setElapsed(dt);
        } else if(playerAnimation == Animations.getFall()) {
            if((isLanding && fallTimer >= 0.5f && !playedFallSound) || (fallTimer >= 0.15f && !playedFallSound)) {
                SoundList.playGameOverSound();
                SoundList.playFallSound();
                playedFallSound = true;
            }
            if(playerAnimation.getKeyFrameIndex(elapsed) < fallingFrameCount - 1) {
                setElapsed(dt);
            }
            fall();
            if(endScreen.getScoreSet()) endScreen.update();
        } else {
            playerX += Constants.PLAYER_SPEED * Gdx.graphics.getDeltaTime();

            setElapsed(dt);
            handleJump();
        }
        if(isFalling) fallTimer += dt;

        if(!GameStateManager.World.isLocked() && playerBody != null) {
            if(isJumping || isLanding) {
                playerBodyDef.position.set(
                        playerX + Constants.PLAYER_BOX_OFFSET + playerAnimation.getKeyFrames()[0].getWidth() / 3f,
                        playerY + playerAnimation.getKeyFrames()[0].getHeight() / 3f + Constants.PLAYER_BOX_OFFSET
                );
            } else {
                playerBodyDef.position.set(
                        playerX + Constants.PLAYER_BOX_OFFSET + playerAnimation.getKeyFrames()[0].getWidth() / 3f,
                        playerY + playerAnimation.getKeyFrames()[0].getHeight() / 3f
                );
            }
            playerBody.setTransform(playerBodyDef.position, 0);
        }

        GameStateManager.Camera.position.set(
                playerX + GameStateManager.Camera.viewportWidth / 2f - Constants.CAMERA_PLAYER_OFFSET,
                GameStateManager.Camera.viewportHeight / 2f,
                0
        );
        GameStateManager.Camera.update();
    }

    private void handleJump() {
        if(playerAnimation == Animations.getStarting()) {
            return;
        }
        if (!isJumping && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            isJumping = true;
            elapsed = 0;
            if(isSuperJump) {
                SoundList.playSuperJumpSound();
                playerAnimation = Animations.getSuperJump();
                verticalSpeed = Constants.PLAYER_SUPER_JUMP_SPEED;
                superJump();
            } else {
                SoundList.playJumpSound();
                playerAnimation = Animations.getJump();
                verticalSpeed = Constants.PLAYER_JUMP_SPEED;
                jump();
            }
        }

        if(isJumping && !isLanding) {
            if(isSuperJump) {
                jump();
            } else {
                superJump();
            }
        }

        if(isLanding) {
            if(isSuperJump && jumpCounter == 1) {
                highLand();
            } else {
                land();
            }
        }
    }

    private void jump() {
        if (verticalSpeed >= 0 && verticalSpeedOffset >= 0) {
            playerY += verticalSpeed;
            verticalSpeed -= verticalSpeedOffset;
            verticalSpeedOffset -= 0.05f;
        } else {
            verticalSpeed = 0;
            verticalSpeedOffset = 0;
            isLanding = true;
        }
    }

    private void superJump() {
        if (verticalSpeed >= 0 && verticalSpeedOffset >= 0) {
            playerY += verticalSpeed;
            verticalSpeed -= verticalSpeedOffset;
            verticalSpeedOffset -= 0.05f;
        } else {
            verticalSpeed = 0;
            verticalSpeedOffset = 0;
            isLanding = true;
        }
    }

    private void fall() {
        playerX += fallSpeed * Gdx.graphics.getDeltaTime();
        fallSpeed /= 1.1f;
        if(playerX < 0) {
            playerX = 0;
        }

        if(playerY == Constants.PLAYER_FALL_Y) return;
        if(playerY < Constants.PLAYER_FALL_Y) {
            playerY = Constants.PLAYER_FALL_Y;
        } else {
            playerY -= Constants.FALL_SPEED;
        }
    }

    private void land() {
        if(playerY < Constants.PLAYER_Y) {
            isLanding = false;
            verticalSpeedOffset = 1;
            isJumping = false;
            playerY = Constants.PLAYER_Y;
            elapsed = 0;
            playerAnimation = Animations.getSkate();
            jumpCounter--;
        } else {
            playerY -= verticalSpeed;
            verticalSpeed += verticalSpeedOffset;
            verticalSpeedOffset += 0.02f;
        }
    }

    private void highLand() {
        if(playerY < Constants.PLAYER_Y) {
            isLanding = false;
            verticalSpeedOffset = 1;
            isJumping = false;
            playerY = Constants.PLAYER_Y;
            elapsed = 0;
            playerAnimation = Animations.getSkate();
            jumpCounter--;
            isSuperJump = false;
        } else {
            playerY -= verticalSpeed;
            verticalSpeed += verticalSpeedOffset;
            verticalSpeedOffset += 0.005f;
        }
    }

    public void dispose () {
        background.dispose();
        playerBody.destroyFixture(playerBody.getFixtureList().first());
        GameStateManager.World.destroyBody(playerBody);
        playerBody = null;
        playerBodyDef = null;
        isFalling = false;
        fallTimer = 0f;
    }

    private void destroyBodies() {
        for(PowerUp powerUp: Background.powerUps) {
            GameStateManager.World.destroyBody(powerUp.getPowerUpBody());
            powerUp.setPowerUpBodyNull();
        }
        for(Obstacle obstacle: Background.obstacles) {
            GameStateManager.World.destroyBody(obstacle.getObstacleBody());
            obstacle.setObstacleBodyNull();
        }
    }

    public void setFallAnimation() {
        elapsed = 0;
        playerAnimation = Animations.getFall();
        isFalling = true;
        canDrawUI = false;
    }

    private void setElapsed(float dt) {
        elapsed += dt;
    }

    public static void setIsSuperJump() {
        isSuperJump = true;
        jumpCounter = 2;
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
                float worldX = screenX * Gdx.graphics.getWidth() / (float) Gdx.graphics.getBackBufferWidth();
                float worldY = (Gdx.graphics.getHeight() - screenY) * Gdx.graphics.getHeight() / (float) Gdx.graphics.getBackBufferHeight();
                if(PauseButton.pauseButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                    pauseTouchDown = true;
                    SoundList.playClickSound();
                }
                if(ExitButton.exitButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                    exitTouchDown = true;
                    SoundList.playClickSound();
                }
                if(EndScreen.getAccess()) {
                    if(endScreen.getReturnButtonBounds().contains(touchPoint.x, touchPoint.y)) {
                        returnTouchDown = true;
                        SoundList.playClickSound();
                    }
                }

                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                Vector3 touchPoint = new Vector3(screenX, screenY, 0);
                GameStateManager.Camera.unproject(touchPoint);
                float worldX = screenX * Gdx.graphics.getWidth() / (float) Gdx.graphics.getBackBufferWidth();
                float worldY = (Gdx.graphics.getHeight() - screenY) * Gdx.graphics.getHeight() / (float) Gdx.graphics.getBackBufferHeight();
                if(pauseTouchDown && PauseButton.pauseButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                    gsm.push(new Pause(gsm));
                }
                if(exitTouchDown && ExitButton.exitButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                    restart();
                }
                if(returnTouchDown && EndScreen.getAccess()) {
                    if(endScreen.getReturnButtonBounds().contains(touchPoint.x, touchPoint.y)) {
                        restart();
                    }
                }

                pauseTouchDown = false;
                exitTouchDown = false;
                returnTouchDown = false;
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

            private void restart() {
                GameStateManager.Camera.position.set(
                        GameStateManager.Camera.viewportWidth / 2f,
                        GameStateManager.Camera.viewportHeight / 2f,
                        0
                );
                GameStateManager.Camera.update();
                destroyBodies();
                dispose();
                Player.canDrawUI = true;
                MusicList.stopBackgroundMusic();
                MusicList.playMenuMusic();
                gsm.pop();
            }
        };
        Gdx.input.setInputProcessor(inputProcessor);
    }
}
