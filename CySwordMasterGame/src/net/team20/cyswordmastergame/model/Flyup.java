package net.team20.cyswordmastergame.model;

import net.team20.cyswordmastergame.Assets;
import net.team20.cyswordmastergame.manager.Config;
import net.team20.cyswordmastergame.utils.ScoreString;

/**
 * Flyup class for score screen flyup
 *
 */
public class Flyup {

	private static final float LIFE_TIME = Config.asFloat("flyup.lifetime", 1.0f);
	private static final float X_OFFSET = (Assets.flyupFont.getBounds("888").width / 2) / Assets.pixelDensity;
	private static final float Y_OFFSET = (Assets.flyupFont.getBounds("888").height) / Assets.pixelDensity;
	private static final float SPEED = Config.asFloat("flyup.speed", 25.0f);

	public final ScoreString scoreString;
	public boolean active;
	public float x;
	public float y;
	private float stateTime;

	/**
	 * initialize flyup scorestring
	 */
	public Flyup () {
		scoreString = new ScoreString(3);
	}

	/**
	 * update it based on the time
	 * @param delta
	 */
	public void update (float delta) {
		stateTime += delta;
		active = stateTime < LIFE_TIME;
		y += SPEED * delta;
	}

	/**
	 * spawn flyup with the coordinate
	 * @param x
	 * @param y
	 * @param points
	 */
	public void spawn (float x, float y, int points) {
		active = true;
		stateTime = 0.0f;
		scoreString.setScore(points);
		this.x = x - X_OFFSET;
		this.y = y + Y_OFFSET;
	}
}
