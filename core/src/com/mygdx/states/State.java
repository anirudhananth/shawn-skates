package com.mygdx.states;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.gamemanager.GameStateManager;

public abstract class State {
    protected GameStateManager gsm;

    public InputProcessor inputProcessor;
    public State(GameStateManager gsm) {
        this.gsm = gsm;
    }

    public abstract void update(float dt);
    public abstract void render(SpriteBatch batch);
    public abstract void dispose();
}
