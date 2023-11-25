package com.mygdx.shawnskates;

import com.mygdx.animations.Animations;
import com.mygdx.gamemanager.GameStateManager;
import com.mygdx.states.Menu;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Launcher extends ApplicationAdapter {
	private SpriteBatch batch;
	private GameStateManager gsm;
	float elapsed;

	@Override
	public void create () {
		Animations.create();
		gsm = new GameStateManager();
		batch = new SpriteBatch();

		gsm.push(new Menu(gsm));
	}

	@Override
	public void render () {

		elapsed += Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(GameStateManager.Camera.combined);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
		update();

	}

	private void update() {
		GameStateManager.Camera.update();
	}

	@Override
	public void dispose () {
		GameStateManager.dispose();
		batch.dispose();
	}

	public void resize(int width, int height) {
//		GameStateManager.Camera.setToOrtho(false, width, height);
		GameStateManager.Camera.viewportWidth = width;
		GameStateManager.Camera.viewportHeight = height;
		GameStateManager.Camera.update();
		batch.setProjectionMatrix(GameStateManager.Camera.combined);
	}
}
