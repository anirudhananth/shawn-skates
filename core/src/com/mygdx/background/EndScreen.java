package com.mygdx.background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.mygdx.audio.SoundList;
import com.mygdx.gamemanager.GameStateManager;
import com.mygdx.helper.Constants;

public class EndScreen {
    private Texture scorePanel;
    private Texture highScorePanel;
    private Texture screen;
    private Texture returnButton;
    private Circle returnButtonBounds;
    private static boolean canAccess;
    private BitmapFont score;
    private BitmapFont scoreShade;
    GlyphLayout scoreLayout;
    private BitmapFont highScore;
    private BitmapFont highScoreShade;
    GlyphLayout highScoreLayout;
    private String scoreString;
    private String highScoreString;
    private Preferences scorePref;
    private boolean isScoreSet;

    public EndScreen() {
        create();
    }
    public void create() {
        canAccess = false;
        isScoreSet = false;

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 0.2f);
        pixmap.fill();

        screen = new Texture(pixmap);
        returnButton = new Texture(Constants.RETURN_PATH);
        returnButtonBounds = new Circle(
                GameStateManager.Camera.position.x,
                GameStateManager.Camera.position.y,
                returnButton.getWidth() / 2f
        );

        pixmap.dispose();
    }

    public void setScores() {
        scoreString = Score.scoreString;
        scorePref = Gdx.app.getPreferences("GamePref");
        highScoreString = scorePref.getString("highScore", "0");

        int scoreVal, highScoreVal;
        scoreVal = Integer.parseInt(scoreString);
        highScoreVal = Integer.parseInt(highScoreString);
        if(highScoreVal < scoreVal) {
            highScoreVal = scoreVal;
            highScoreString = String.valueOf(highScoreVal);
            scorePref.putString("highScore", highScoreString);
            scorePref.flush();
        }

        score = new BitmapFont(Gdx.files.internal("font/score-font.fnt"));
        scoreShade = new BitmapFont(Gdx.files.internal("font/score-font.fnt"));
        scoreShade.setColor(0.1f, 0.1f, 0.1f, 0.9f);

        highScore = new BitmapFont(Gdx.files.internal("font/score-font.fnt"));
        highScoreShade = new BitmapFont(Gdx.files.internal("font/score-font.fnt"));
        highScoreShade.setColor(0.1f, 0.1f, 0.1f, 0.9f);

        scoreLayout = new GlyphLayout();
        scoreLayout.setText(score, scoreString);

        highScoreLayout = new GlyphLayout();
        highScoreLayout.setText(highScore, scoreString);

        scoreString = "Score: " + scoreString;
        highScoreString = "High Score: " + highScoreString;

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(136/255f,93/255f,63/255f, 1f);
        pixmap.fill();

        scorePanel = new Texture(pixmap);
        pixmap.dispose();

        pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(136/255f,93/255f,63/255f, 1f);
        pixmap.fill();

        highScorePanel = new Texture(pixmap);
        pixmap.dispose();
    }

    public void render(SpriteBatch batch) {
        batch.draw(
                screen,
                GameStateManager.Camera.position.x - GameStateManager.Camera.viewportWidth / 2f,
                GameStateManager.Camera.position.y - GameStateManager.Camera.viewportHeight / 2f,
                GameStateManager.Camera.viewportWidth,
                GameStateManager.Camera.viewportHeight
        );
        batch.draw(
                returnButton,
                GameStateManager.Camera.position.x - returnButton.getWidth() / 2f,
                GameStateManager.Camera.position.y - returnButton.getHeight() / 2f,
                returnButton.getWidth(),
                returnButton.getHeight()
        );
        returnButtonBounds.set(
                GameStateManager.Camera.position.x,
                GameStateManager.Camera.position.y,
                returnButton.getWidth() / 2f
        );

        //Setting scores
        batch.draw(
                scorePanel,
                GameStateManager.Camera.position.x - scoreLayout.width / 2f,
                GameStateManager.Camera.position.y + 148f,
                scoreLayout.width - 5f,
                scoreLayout.height
        );
        scoreShade.draw(
                batch,
                scoreString,
                GameStateManager.Camera.position.x - scoreLayout.width / 2f - 5f,
                GameStateManager.Camera.position.y + scoreLayout.height + 148f
        );
        score.draw(
                batch,
                scoreString,
                GameStateManager.Camera.position.x - scoreLayout.width / 2f,
                GameStateManager.Camera.position.y + scoreLayout.height + 150f
        );
        batch.draw(
                scorePanel,
                GameStateManager.Camera.position.x - highScoreLayout.width / 2f,
                GameStateManager.Camera.position.y + 100f,
                highScoreLayout.width - 5f,
                highScoreLayout.height
        );
        highScoreShade.draw(
                batch,
                highScoreString,
                GameStateManager.Camera.position.x - highScoreLayout.width / 2f - 5f,
                GameStateManager.Camera.position.y + highScoreLayout.height + 98f
        );
        highScore.draw(
                batch,
                highScoreString,
                GameStateManager.Camera.position.x - highScoreLayout.width / 2f,
                GameStateManager.Camera.position.y + highScoreLayout.height + 100f
        );
    }

    public void update() {
        scoreLayout.setText(score, scoreString);
        highScoreLayout.setText(highScore, highScoreString);
    }

    public static void setAccess() {
        canAccess = true;
    }

    public static boolean getAccess() {
        return canAccess;
    }

    public boolean getScoreSet() {
        return isScoreSet;
    }

    public void setScoreSet() {
        isScoreSet = true;
    }

    public Circle getReturnButtonBounds() {
        return returnButtonBounds;
    }
}
