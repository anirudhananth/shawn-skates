package com.mygdx.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.animations.Animations;
import com.mygdx.background.Background;
import com.mygdx.gamemanager.GameStateManager;
import com.mygdx.helper.Constants;

public class Player extends State {
    GameStateManager gsm;
    public Animation<Texture> animation;
    private int startingFrameCount;
    private int fallingFrameCount;
    public Body playerBody;
    public Background background;
    private BodyDef playerBodyDef;
    public static float playerX, playerY;
    private boolean isJumping = false;
    private boolean isLanding = false;
    private final float jumpSpeed = 17;
    private final float superJumpSpeed = 20;
    private float fallSpeed = Constants.PLAYER_SPEED;
    public float speed = jumpSpeed;
    public float speedOffset = 1;
    private float elapsed;
    float startSpeed = 0.01f;
    public boolean isFalling = false;

    public Player(GameStateManager gsm) {
        super(gsm);
        this.gsm = gsm;
        background = new Background(gsm);

        create();
    }

    public void create () {
        elapsed = 0;
//        animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets/shawn3.gif").read());
        animation = Animations.getStarting();
        startingFrameCount = Animations.getStarting().getKeyFrames().length;
        fallingFrameCount = Animations.getFall().getKeyFrames().length;

        setPlayerBody();

        playerX = Constants.PLAYER_X; // Initial X position of the player
        playerY = Constants.PLAYER_Y; // Initial Y position of the player
    }

    private void setPlayerBody() {
        playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.position.set(Constants.PLAYER_X, Constants.PLAYER_Y);
        playerBody = GameStateManager.World.createBody(playerBodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(
                (float) animation.getKeyFrames()[0].getWidth() / 3.5f,
                (float) animation.getKeyFrames()[0].getHeight() / 3.5f
        );
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        playerBody.createFixture(fixtureDef);
    }

    public void render (SpriteBatch batch) {
        batch.begin();
        background.render(batch, playerX);
        if(animation == Animations.getStarting()) {
            batch.draw(
                    animation.getKeyFrame(elapsed, true),
                    playerX - 19.5f,
                    playerY - 2.5f
            );
        } else if(!isFalling) {
            batch.draw(
                    animation.getKeyFrame(elapsed, true),
                    playerX,
                    playerY
            );
        } else {
            batch.draw(
                    animation.getKeyFrame(elapsed, false),
                    playerX,
                    playerY
            );
        }
        batch.end();
    }

    public void update(float dt) {
        background.update(dt);

        if(animation == Animations.getStarting()) {
            if(animation.getKeyFrameIndex(elapsed) == startingFrameCount - 1) {
                elapsed = 0;
                animation = Animations.getSkate();
            } else {
                playerX += startSpeed * Gdx.graphics.getDeltaTime();
                startSpeed *= 1.1f;
                if(startSpeed > Constants.PLAYER_SPEED) startSpeed = Constants.PLAYER_SPEED;
            }
            setElapsed(dt);
        } else if(animation == Animations.getFall()) {
            if(animation.getKeyFrameIndex(elapsed) < fallingFrameCount - 1) {
                setElapsed(dt);
            }
            fall();
        } else {
            playerX += Constants.PLAYER_SPEED * Gdx.graphics.getDeltaTime();

            setElapsed(dt);
            handleJump();
        }

        if(!GameStateManager.World.isLocked() && playerBody != null) {
            playerBodyDef.position.set(
                    playerX + Constants.PLAYER_BOX_OFFSET + animation.getKeyFrames()[0].getWidth() / 3f,
                    playerY + animation.getKeyFrames()[0].getHeight() / 3f
            );
            playerBody.setTransform(playerBodyDef.position, 0);
        }

        GameStateManager.Camera.position.set(
                playerX + Constants.CAMERA_PLAYER_OFFSET,
                Gdx.graphics.getHeight() / 2f, 0
        );
        GameStateManager.Camera.update();
    }

    private void handleJump() {
        if(animation == Animations.getStarting()) {
            return;
        }
        if (!isJumping && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            isJumping = true;
            animation = Animations.getJump();
            elapsed = 0;
            jump();
        }

        if(isJumping && !isLanding) {
            jump();
        }

        if(isLanding || isFalling) {
            land();
        }
    }

    private void jump() {
        if (speed >= 0 && speedOffset >= 0) {
            playerY += speed;
            speed -= speedOffset;
            speedOffset -= 0.05f;
        } else {
            speed = 0;
            speedOffset = 0;
            isLanding = true;
        }
    }

    private void fall() {
        playerX += fallSpeed * Gdx.graphics.getDeltaTime();
        fallSpeed /= 1.1f;
        if(playerX < 0) {
            playerX = 0;
        }

        if(playerY == Constants.PLAYER_Y) return;
        if(playerY < Constants.PLAYER_Y) {
            isFalling = false;
            playerY = Constants.PLAYER_Y;
        } else {
            playerY -= Constants.FALL_SPEED;
        }
    }

    private void land() {
        if(playerY < Constants.PLAYER_Y) {
            isLanding = false;
            speed = jumpSpeed;
            speedOffset = 1;
            isJumping = false;
            playerY = Constants.PLAYER_Y;
            animation = Animations.getSkate();
            elapsed = 0;
        } else {
            playerY -= speed;
            speed += speedOffset;
            speedOffset += 0.02f;
        }
    }

    public void dispose () {
        Animations.dispose();
        background.dispose();
    }

    public void setFallAnimation() {
        animation = Animations.getFall();
        elapsed = 0;
        isFalling = true;
    }

    private void setElapsed(float dt) {
        elapsed += dt;
    }
}
