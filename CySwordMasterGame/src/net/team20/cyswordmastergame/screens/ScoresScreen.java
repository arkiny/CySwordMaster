
package net.team20.cyswordmastergame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.team20.cyswordmastergame.Assets;
import net.team20.cyswordmastergame.CySwordMasterGame;
import net.team20.cyswordmastergame.utils.CameraHandler;
import net.team20.cyswordmastergame.utils.CameraHandler.ViewportMode;
import net.team20.cyswordmastergame.screens.GameScreen;
import net.team20.cyswordmastergame.utils.ScoreString;

import static net.team20.cyswordmastergame.Assets.*;

/**
 * score screen for showing top score
 *
 */
public class ScoresScreen extends GameScreen<CySwordMasterGame> {
	private static final String LAST_SCORE = "Score:";
	private static final String TOP_SCORE = "Best: ";
	private static final String TOP2_SCORE = "Best2: ";
	private static final String TOP3_SCORE = "Best3: ";
	private static final String TOP4_SCORE = "Best4: ";

	private static final String SWORDMASTER = "SwordMaster";
	private static final String TOP_SCORE_PREF = "top score";
	private static final String TOP2_SCORE_PREF = "top2 score";
	private static final String TOP3_SCORE_PREF = "top3 score";
	private static final String TOP4_SCORE_PREF = "top4 score";

	private static final String TAP_TO_CONTINUE = "Tap to continue";

	private ScoreString scoreString;
		
	private ScoreString topScoreString;
	private ScoreString top2ScoreString;
	private ScoreString top3ScoreString;
	private ScoreString top4ScoreString;
	
	private OrthographicCamera scoreCam;
	private SpriteBatch spriteBatch;
	private boolean touched;
	private int topScore, top2Score, top3Score, top4Score;
	
	private final Preferences preferences;
	private float stateTime;

	/**
	 * initializing the scorescreen with the game
	 * 
	 * @param game
	 */
	public ScoresScreen (CySwordMasterGame game) {
		super(game);
		scoreCam = CameraHandler.createCamera2(ViewportMode.PIXEL_PERFECT, VIRTUAL_WIDTH, VIRTUAL_HEIGHT, Assets.pixelDensity);
		spriteBatch = new SpriteBatch();
		spriteBatch.setProjectionMatrix(scoreCam.combined);
		scoreString = new ScoreString();
		topScoreString = new ScoreString();
		top2ScoreString = new ScoreString();
		top3ScoreString = new ScoreString();
		top4ScoreString = new ScoreString();
		
		preferences = Gdx.app.getPreferences(SWORDMASTER);
		topScore = preferences.getInteger(TOP_SCORE_PREF, 1000);
		top2Score =preferences.getInteger(TOP2_SCORE_PREF, 800);
		top3Score =preferences.getInteger(TOP3_SCORE_PREF, 600);
		top4Score =preferences.getInteger(TOP4_SCORE_PREF, 400);
		topScoreString.setScore(topScore);
		top2ScoreString.setScore(top2Score);
		top3ScoreString.setScore(top3Score);
		top4ScoreString.setScore(top4Score);
		
		stateTime = 0.0f;
	}

	/**
	 * set score from the input score
	 * @param score
	 */
	public void setScore (int score) {
		scoreString.setScore(score);
		if (score > topScore) {
			topScore = score;
			preferences.putInteger(TOP_SCORE_PREF, topScore);
			preferences.flush();
			topScoreString.setScore(score);
		}
		else if (score > top2Score){
			top2Score = score;
			preferences.putInteger(TOP2_SCORE_PREF, top2Score);
			preferences.flush();
			top2ScoreString.setScore(score);
		}
		else if (score > top3Score){
			top3Score = score;
			preferences.putInteger(TOP3_SCORE_PREF, top3Score);
			preferences.flush();
			top3ScoreString.setScore(score);
		}
		else if (score > top4Score){
			top4Score = score;
			preferences.putInteger(TOP4_SCORE_PREF, top4Score);
			preferences.flush();
			top4ScoreString.setScore(score);
		}
	}

	@Override
	public void render (float delta) {
		stateTime += delta;
		if (Gdx.input.justTouched()) {
			touched = true;
		} else if (touched && !Gdx.input.isTouched()) {
			touched = false;
			game.setScreen(game.mainMenuScreen());
		} else {
			Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			spriteBatch.begin();
			Assets.scoreFont.setColor(Color.WHITE);
			TextBounds b = Assets.scoreFont.getBounds(LAST_SCORE);
			float w = b.width + Assets.scoreFont.getSpaceWidth();
			b = Assets.scoreFont.getBounds(TOP_SCORE);
			float w2 = b.width;
			float x = (VIRTUAL_WIDTH - (w + w2)) / 2.0f;
			float y = 3 * VIRTUAL_HEIGHT / 4.0f;
			Assets.scoreFont.setColor(Color.WHITE);
			Assets.scoreFont.draw(spriteBatch, TOP_SCORE, x, y);
			Assets.scoreFont.draw(spriteBatch, topScoreString, x + w, y);
			
			b = Assets.scoreFont.getBounds(TOP_SCORE);
			w = b.width + Assets.scoreFont.getSpaceWidth();
			b = Assets.scoreFont.getBounds(topScoreString);
			w2 = b.width;
			x = (VIRTUAL_WIDTH - (w + w2)) / 2.0f;
			y -= 2 * b.height;
			Assets.scoreFont.draw(spriteBatch, TOP2_SCORE, x, y);
			Assets.scoreFont.draw(spriteBatch, top2ScoreString, x + w, y);
			
			b = Assets.scoreFont.getBounds(TOP_SCORE);
			w = b.width + Assets.scoreFont.getSpaceWidth();
			w2 = b.width;
			x = (VIRTUAL_WIDTH - (w + w2)) / 2.0f;
			y -= 2 * b.height;
			Assets.scoreFont.draw(spriteBatch, TOP3_SCORE, x, y);
			Assets.scoreFont.draw(spriteBatch, top3ScoreString, x + w, y);
			
			b = Assets.scoreFont.getBounds(TOP_SCORE);
			w = b.width + Assets.scoreFont.getSpaceWidth();
			w2 = b.width;
			x = (VIRTUAL_WIDTH - (w + w2)) / 2.0f;
			y -= 2 * b.height;
			Assets.scoreFont.draw(spriteBatch, TOP4_SCORE, x, y);
			Assets.scoreFont.draw(spriteBatch, top4ScoreString, x + w, y);
			
			b = Assets.scoreFont.getBounds(TOP_SCORE);
			w = b.width + Assets.scoreFont.getSpaceWidth();
			w2 = b.width;
			x = (VIRTUAL_WIDTH - (w + w2)) / 2.0f;
			y -= 2 * b.height;

			Assets.scoreFont.draw(spriteBatch, LAST_SCORE, x, y);
			Assets.scoreFont.draw(spriteBatch, scoreString, x + w, y);
			
			
			if (stateTime % 1.0f < 0.5f) {
				Assets.textFont.setColor(Color.WHITE);
				Assets.textFont.drawWrapped(spriteBatch, TAP_TO_CONTINUE, 0, VIRTUAL_HEIGHT / 4, VIRTUAL_WIDTH, HAlignment.CENTER);
			}
			spriteBatch.end();
		}
	}

	@Override
	public void show () {
		Gdx.input.setCatchBackKey(true);
		stateTime = 0.0f;
	}

	@Override
	public void hide () {
		Gdx.input.setCatchBackKey(false);
	}

	@Override
	public void resume () {
		Gdx.input.setCatchBackKey(true);
	}
}
