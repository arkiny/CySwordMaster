package net.team20.cyswordmastergame.manager;

import com.badlogic.gdx.graphics.Color;
import net.team20.cyswordmastergame.manager.Config;

/**
 * Difficulty manager used to speed up and control amount of ninjas in a room.
 * 
 * This is set up to manage set-number of initial ninjas should the Ninja Admin actor be used.
 * Otherwise a default amount of ninjas, mentioned in StatusManager.java will be used.
 *
 */
public class ScoreBasedDifficultyManager implements DifficultyManager, ScoreListener {	
	// Ninja numbers.
	private static final int MAX_NINJAS = Config.asInt("Global.maxNinjas", 12);
	// "Initial" amount is determined in StatusManager because we want control should the Ninja Admin be playing.
	
	// Shot speeds.
	private static final float SLOW_SHOT_SPEED = Config.asFloat("NinjaShot.slowSpeed", 75.0f);
	private static final float FAST_SHOT_SPEED = Config.asFloat("NinjaShot.fastSpeed", 125.0f);

	// Level progression.
	private static final int NINJA_INCREMENT = Config.asInt("Level.ninjaIncrement", 3);
	private static final int LEVEL_0_NINJA_SHOTS = Config.asInt("Level.0.ninjaShots", 0);
	private static final int LEVEL_1_NINJA_SHOTS = Config.asInt("Level.1.ninjaShots", 1);
	private static final int LEVEL_2_NINJA_SHOTS = Config.asInt("Level.2.ninjaShots", 2);
	private static final int LEVEL_3_NINJA_SHOTS = Config.asInt("Level.3.ninjaShots", 3);
	private static final int LEVEL_4_NINJA_SHOTS = Config.asInt("Level.4.ninjaShots", 4);
	private static final int LEVEL_5_NINJA_SHOTS = Config.asInt("Level.5.ninjaShots", 5);
	private static final int LEVEL_6_NINJA_SHOTS = Config.asInt("Level.6.ninjaShots", 1);
	private static final int LEVEL_7_NINJA_SHOTS = Config.asInt("Level.7.ninjaShots", 2);
	private static final int LEVEL_8_NINJA_SHOTS = Config.asInt("Level.8.ninjaShots", 3);
	private static final int LEVEL_9_NINJA_SHOTS = Config.asInt("Level.9.ninjaShots", 4);
	private static final int LEVEL_10_NINJA_SHOTS = Config.asInt("Level.10.ninjaShots", 5);
	private static final int LEVEL_11_NINJA_SHOTS = Config.asInt("Level.11.ninjaShots", 6);
	private static final int LEVEL_1_SCORE = Config.asInt("Level.1.score", 200);
	private static final int LEVEL_2_SCORE = Config.asInt("Level.2.score", 500);
	private static final int LEVEL_3_SCORE = Config.asInt("Level.3.score", 1000);
	private static final int LEVEL_4_SCORE = Config.asInt("Level.4.score", 2500);
	private static final int LEVEL_5_SCORE = Config.asInt("Level.5.score", 5000);
	private static final int LEVEL_6_SCORE = Config.asInt("Level.6.score", 7500);
	private static final int LEVEL_7_SCORE = Config.asInt("Level.7.score", 10000);
	private static final int LEVEL_8_SCORE = Config.asInt("Level.8.score", 12500);
	private static final int LEVEL_9_SCORE = Config.asInt("Level.9.score", 15000);
	private static final int LEVEL_10_SCORE = Config.asInt("Level.10.score", 17500);
	private static final int LEVEL_11_SCORE = Config.asInt("Level.11.score", 20000);

	// Ninja colours.
	private static final Color DARK_YELLOW = new Color(0.75f, 0.75f, 0.0f, 1.0f);
	private static final Color RED = Color.RED;
	private static final Color DARK_CYAN = new Color(0.0f, 0.75f, 0.75f, 1.0f);
	private static final Color GREEN = Color.GREEN;
	private static final Color DARK_PURPLE = new Color(0.75f, 0.0f, 0.75f, 1.0f);
	private static final Color LIGHT_YELLOW = new Color(1.0f, 1.0f, 0.0f, 1.0f);
	private static final Color WHITE = Color.WHITE;
	private static final Color LIGHT_PURPLE = new Color(1.0f, 0.0f, 1.0f, 1.0f);
	private static final Color GREY = new Color(0.75f, 0.75f, 0.75f, 1.0f);

	private boolean dirty;
	private int score;
	private Color ninjaColor;
	private int numNinjas;
	private int numNinjaShots;
	private float ninjaShotSpeed;

	@Override
	public Color getNinjaColor () {
		updateIfDirty();
		return ninjaColor;
	}

	@Override
	public int getNumberOfNinjas () {
		updateIfDirty();
		return numNinjas;
	}

	@Override
	public int getNumberOfNinjaShots () {
		updateIfDirty();
		return numNinjaShots;
	}

	@Override
	public float getNinjaShotSpeed () {
		updateIfDirty();
		return ninjaShotSpeed;
	}

	@Override
	public void onScoreChanged (int score) {
		dirty = true;
		this.score = score;
	}

	@Override
	public void onLivesChanged (int lives) {
	}

	/**
	 * update when screen is dirty
	 */
	private void updateIfDirty () {
		if (!dirty) return;
		dirty = false;

		Color lastNinjaColor = ninjaColor;
		if (score < LEVEL_1_SCORE) {
			ninjaColor = DARK_YELLOW;
			numNinjaShots = LEVEL_0_NINJA_SHOTS;
			ninjaShotSpeed = SLOW_SHOT_SPEED;
		} else if (score < LEVEL_2_SCORE) {
			ninjaColor = RED;
			numNinjaShots = LEVEL_1_NINJA_SHOTS;
			ninjaShotSpeed = SLOW_SHOT_SPEED;
		} else if (score < LEVEL_3_SCORE) {
			ninjaColor = DARK_CYAN;
			numNinjaShots = LEVEL_2_NINJA_SHOTS;
			ninjaShotSpeed = SLOW_SHOT_SPEED;
		} else if (score < LEVEL_4_SCORE) {
			ninjaColor = GREEN;
			numNinjaShots = LEVEL_3_NINJA_SHOTS;
			ninjaShotSpeed = SLOW_SHOT_SPEED;
		} else if (score < LEVEL_5_SCORE) {
			ninjaColor = DARK_PURPLE;
			numNinjaShots = LEVEL_4_NINJA_SHOTS;
			ninjaShotSpeed = SLOW_SHOT_SPEED;
		} else if (score < LEVEL_6_SCORE) {
			ninjaColor = LIGHT_YELLOW;
			numNinjaShots = LEVEL_5_NINJA_SHOTS;
			ninjaShotSpeed = SLOW_SHOT_SPEED;
		} else if (score < LEVEL_7_SCORE) {
			ninjaColor = WHITE;
			numNinjaShots = LEVEL_6_NINJA_SHOTS;
			ninjaShotSpeed = FAST_SHOT_SPEED;
		} else if (score < LEVEL_8_SCORE) {
			ninjaColor = DARK_CYAN;
			numNinjaShots = LEVEL_7_NINJA_SHOTS;
			ninjaShotSpeed = FAST_SHOT_SPEED;
		} else if (score < LEVEL_9_SCORE) {
			ninjaColor = LIGHT_PURPLE;
			numNinjaShots = LEVEL_8_NINJA_SHOTS;
			ninjaShotSpeed = FAST_SHOT_SPEED;
		} else if (score < LEVEL_10_SCORE) {
			ninjaColor = GREY;
			numNinjaShots = LEVEL_9_NINJA_SHOTS;
			ninjaShotSpeed = FAST_SHOT_SPEED;
		} else if (score < LEVEL_11_SCORE) {
			ninjaColor = DARK_YELLOW;
			numNinjaShots = LEVEL_10_NINJA_SHOTS;
			ninjaShotSpeed = FAST_SHOT_SPEED;
		} else {
			ninjaColor = RED;
			numNinjaShots = LEVEL_11_NINJA_SHOTS;
			ninjaShotSpeed = FAST_SHOT_SPEED;
		}

		if (lastNinjaColor == ninjaColor && score >= LEVEL_1_SCORE) {
			if (numNinjas < MAX_NINJAS) {
				numNinjas += NINJA_INCREMENT;
			}
		} else {
			//numNinjas = INITIAL_NINJAS;
			numNinjas = StatusManager.getNinjaAmount();
		}
	}
}
