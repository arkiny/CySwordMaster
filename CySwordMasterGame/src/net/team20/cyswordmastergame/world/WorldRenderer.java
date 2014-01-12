package net.team20.cyswordmastergame.world;

//import net.team20.swordmaster.manager.SoundManager;
import net.team20.cyswordmastergame.CySwordMasterGame;
import net.team20.cyswordmastergame.manager.ScoreBasedDifficultyManager;
import net.team20.cyswordmastergame.manager.ScoreListener;
import net.team20.cyswordmastergame.manager.StatusManager;
import net.team20.cyswordmastergame.manager.StatusView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import net.team20.cyswordmastergame.screens.GameScreen;
import net.team20.cyswordmastergame.model.Player;

public class WorldRenderer extends GameScreen<CySwordMasterGame> implements WorldView.Presenter, ScoreListener {

	private static final float MAX_DELTA = 0.1f;

	private final World world;
	private final WorldView worldView;
	private final StatusView statusView;
//	private final SoundManager soundManager;
	public final StatusManager statusManager;
	private final ScoreBasedDifficultyManager difficultyManager;
	private int score;
	private boolean isDead;
	private boolean wasBackPressed;

	/** Constructs a new WorldPresenter.
	 * 
	 * @param game the game, used primarily for switching between screens. */
	public WorldRenderer (CySwordMasterGame game) {
		super(game);
		difficultyManager = new ScoreBasedDifficultyManager();
		world = new World(difficultyManager);
		statusManager = new StatusManager();
		worldView = new WorldView(world, statusManager, this);
		statusView = new StatusView(world);
//		soundManager = new SoundManager();
		statusManager.addScoreListener(statusView);
		statusManager.addScoreListener(this);
		statusManager.addScoreListener(difficultyManager);
//		statusManager.addScoreListener(soundManager);
		statusManager.addAchievementsListener(statusView);
		//statusManager.testerMode = test;
//		statusManager.addAchievementsListener(soundManager);
		statusManager.addAchievementsListener(game);
//		world.addWorldListener(soundManager);
		world.addWorldListener(statusManager);
	}

	@Override
	public void show () {
		Gdx.input.setCatchBackKey(true);
		wasBackPressed = false;
		world.reset();
		world.resume();
	}

	@Override
	public void pause () {
		world.pause();
	}

	@Override
	public void resume () {
		Gdx.input.setCatchBackKey(true);
		world.resume();
	}

	@Override
	public void hide () {
		Gdx.input.setCatchBackKey(false);
	}

	/** Called by libgdx when this screen should render itself. It responds to a request to render by updating the controls,
	 * updating the world and the managers, then drawing the views.
	 * 
	 * @param delta the time in seconds since the last time <code>render</code> was called. */
	@Override
	public void render (float delta) {
		// Update time.
		if (delta >= MAX_DELTA) delta = MAX_DELTA;

		// Ask the view to update the controls.
		worldView.updateControls(delta);

		// If we're not paused then update the world and the subsystems.
		world.update(delta);
		if (!world.isPaused()) {
			statusManager.update(delta);
			worldView.update(delta);
//			soundManager.update(delta);
		}

		// Clear the screen and draw the views.
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		worldView.render(delta);
		statusView.render(delta);

		if (isDead && world.getState() == World.PLAYING) {
			game.submitScore(score);
			game.setScreen(game.scoresScreen);
			game.scoresScreen.setScore(score);
		}
		boolean isBackPressed = Gdx.input.isKeyPressed(Input.Keys.BACK);
		if (!wasBackPressed && isBackPressed) {
			if (!world.isPaused()) {
				world.pause();
			} else {
				game.setScreen(game.mainMenuScreen());
			}
		}
		wasBackPressed = isBackPressed;
	}

	/** Called by the {@link WorldView} when the player wants to move.
	 * 
	 * @param x the x value of the controller.
	 * @param y the y value of the controller. */
	@Override
	public void setController (float x, float y) {
		Player player = world.getPlayer();
		player.setController(x, y);
	}

	/** Called by the {@link WorldView} when the player wants to fire.
	 * 
	 * @param x the x value of the controller.
	 * @param y the y value of the controller. */
	@Override
	public void setFiringController (float x, float y) {
		Player player = world.getPlayer();
		world.firePlayerShot.fire(player, x, y);
	}

	@Override
	public void onScoreChanged (int score) {
		this.score = score;
	}

	@Override
	public void onLivesChanged (int lives) {
		isDead = (lives == 0);
	}
}
