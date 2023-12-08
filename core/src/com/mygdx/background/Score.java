package com.mygdx.background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.gamemanager.GameStateManager;
import com.mygdx.states.Player;

public class Score {
    private final BitmapFont score;
    private final BitmapFont scoreShade;
    public static String scoreString;
    GlyphLayout layout;

    public Score() {
        score = new BitmapFont(Gdx.files.internal("font/score-font.fnt"));

        scoreShade = new BitmapFont(Gdx.files.internal("font/score-font.fnt"));
        scoreShade.setColor(0.1f, 0.1f, 0.1f, 0.9f);
        scoreString = "";

        layout = new GlyphLayout();
        layout.setText(score, scoreString);
    }

    public void render(SpriteBatch batch) {
        if(Player.canDrawUI) {
            scoreShade.draw(
                    batch,
                    scoreString,
                    GameStateManager.Camera.position.x - 10f - layout.width / 2f,
                    GameStateManager.Camera.position.y + GameStateManager.Camera.viewportHeight / 2f - 44f
            );
            score.draw(
                    batch,
                    scoreString,
                    GameStateManager.Camera.position.x - 5f - layout.width / 2f,
                    GameStateManager.Camera.position.y + GameStateManager.Camera.viewportHeight / 2f - 42f
            );
        }
    }

    public void setScore(int score) {
        this.scoreString = String.valueOf(score);
        layout.setText(this.score, scoreString);
    }
}
