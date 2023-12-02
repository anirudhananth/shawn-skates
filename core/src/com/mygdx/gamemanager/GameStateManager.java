package com.mygdx.gamemanager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.animations.Animations;
import com.mygdx.background.Background;
import com.mygdx.helper.Constants;
import com.mygdx.states.Player;
import com.mygdx.states.State;

import java.util.Stack;

public class GameStateManager {
    public static OrthographicCamera Camera;
    public static World World;
    private static Stack<State> states;
    Box2DDebugRenderer debugRenderer;

    public GameStateManager() {
        states = new Stack<State>();
        GameStateManager.World = new World(new Vector2(0, Constants.GRAVITY), false);
        GameStateManager.Camera = new OrthographicCamera();
        GameStateManager.Camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        setContactListeners();
        debugRenderer = new Box2DDebugRenderer();
    }

    public void push(State state) {
        states.push(state);
    }

    public void pop() {
        if(!states.empty()) {
            states.pop();
            Gdx.input.setInputProcessor(states.peek().inputProcessor);
        }
    }

    public void set(State state) {
        states.pop();
        states.push(state);
    }

    public void update(float dt) {
        float timeStep = 1/60f; // 60 times per second
        int velocityIterations = 6;
        int positionIterations = 2;

// Inside your game loop
        World.step(timeStep, velocityIterations, positionIterations);
        states.peek().update(dt);
    }

    public void render(SpriteBatch batch) {
        states.peek().render(batch);
        debugRenderer.render(World, Camera.combined);
    }

    public static void dispose() {
        for (State state : states) {
            state.dispose();
        }
        World.dispose();
        Animations.dispose();
    }

    private void setContactListeners() {
        World.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if(states.peek() instanceof Player) {
                    Player player = (Player)states.peek();
                    Background background = player.background;
                    if (contact.getFixtureA().getBody() == player.playerBody) {
                        if (contact.getFixtureB().getBody() == background.powerUpBody) {
                            player.setFallAnimation();
                        }
                    }
                    if (contact.getFixtureB().getBody() == player.playerBody) {
                        if (contact.getFixtureA().getBody() == background.powerUpBody) {
                            player.setFallAnimation();
                        }
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }
}
