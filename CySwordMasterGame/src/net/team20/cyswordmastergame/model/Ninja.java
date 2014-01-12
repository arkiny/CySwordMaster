package net.team20.cyswordmastergame.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import net.team20.cyswordmastergame.manager.Config;
import net.team20.cyswordmastergame.Assets;

import static net.team20.cyswordmastergame.utils.MathUtils.*;

/**
 * Model class for ninja
 *
 */
public class Ninja extends GameObject {

	public static final int SCANNING = INACTIVE + 1;
	public static final int WALKING_RIGHT = SCANNING + 1;
	public static final int WALKING_RIGHT_DIAGONAL = WALKING_RIGHT + 1;
	public static final int WALKING_LEFT = WALKING_RIGHT_DIAGONAL + 1;
	public static final int WALKING_LEFT_DIAGONAL = WALKING_LEFT + 1;

	private static final float WALKING_SPEED = Config.asFloat("Robot.speed", 1.25f);

	private GameObject player;
	private Array<Rectangle> walls;
	private final float distance;
	private final float fudge;
	private float respawnX;
	private float respawnY;

	/**
	 * initialize ninja model
	 */
	public Ninja() {
		width = Assets.ninjaWidth;
		height = Assets.ninjaHeight;
		distance = max(width, height);
		fudge = distance * 0.25f;
		setState(INACTIVE);
	}

	/**
	 * setPlayer as player
	 * @param player
	 */
	public void setPlayer (GameObject player) {
		this.player = player;
	}

	/**
	 * set walls from walls array
	 * @param walls
	 */
	public void setWalls (Array<Rectangle> walls) {
		this.walls = walls;
	}

	public void update (float delta) {
		stateTime += delta;
		moveNinja(delta);
	}

	/**
	 * moving of ninja based on the time
	 * @param delta
	 */
	private void moveNinja (float delta) {
		float dx = (player.x + player.width / 2) - (x + width / 2);
		float dy = (player.y + player.height / 2) - (y + height / 2);
		dx = abs(dx) >= 2 ? sgn(dx) : 0.0f;
		dy = abs(dy) >= 2 ? sgn(dy) : 0.0f;
		float ax = 0.0f;
		float ay = 0.0f;
		if (!wouldHitWall(dx, dy)) {
			ax = dx;
			ay = dy;
		} else if (dx != 0 && !wouldHitWall(dx, 0)) {
			ax = dx;
		} else if (dy != 0 && !wouldHitWall(0, dy)) {
			ay = dy;
		}
		dx = ax * WALKING_SPEED;
		dy = ay * WALKING_SPEED;
		x += dx * delta;
		y += dy * delta;

		int newState = getMovementState(dx, dy);
		if (newState != state) {
			setState(newState);
		}
	}

	/**
	 * get movement state from its state
	 * @param dx
	 * @param dy
	 * @return
	 */
	private int getMovementState (float dx, float dy) {
		if (dx == 0.0f && dy == 0.0f) {
			return SCANNING;
		} else if (dx > 0) {
			return (dy == 0) ? WALKING_RIGHT : WALKING_RIGHT_DIAGONAL;
		} else {
			return (dy == 0) ? WALKING_LEFT : WALKING_LEFT_DIAGONAL;
		}
	}

	private boolean wouldHitWall (float dx, float dy) {
		float x1 = x + width / 2;
		float y1 = y + height / 2;
		float x2 = x1 + dx * distance;
		float y2 = y1 + dy * distance;

		for (int i = 0; i < walls.size; i++) {
			Rectangle wall = walls.get(i);
			if (doesLineHitWall(wall, x1, y1, x2, y2)) {
				return true;
			}
		}
		return false;
	}

	private boolean doesLineHitWall (Rectangle rect, float x1, float y1, float x2, float y2) {
		// Does not intersect if minimum y coordinate is below the rectangle.
		float minY = min(y1, y2);
		if (minY >= rect.y + rect.height + fudge) return false;

		// Does not intersect if maximum y coordinate is above the rectangle.
		float maxY = max(y1, y2);
		if (maxY < rect.y - fudge) return false;

		// Does not intersect if minimum x coordinate is to the right of the rectangle.
		float minX = min(x1, x2);
		if (minX >= rect.x + rect.width + fudge) return false;

		// Does not intersect if maximum x coordinate is to the left of the rectangle.
		float maxX = max(x1, x2);
		if (maxX < rect.x - fudge) return false;

		// And that's good enough, because the robots need to be a bit stupid
		// when they're near the ends of walls.
		return true;
	}

	/**
	 * set respawnpoint with inputed coordinate
	 * @param x
	 * @param y
	 */
	public void setRespawnPoint (float x, float y) {
		respawnX = x;
		respawnY = y;
	}

	public void respawn () {
		x = respawnX;
		y = respawnY;
	}
}
