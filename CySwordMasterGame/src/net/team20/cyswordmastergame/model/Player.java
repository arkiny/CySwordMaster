package net.team20.cyswordmastergame.model;

import net.team20.cyswordmastergame.Assets;
import net.team20.cyswordmastergame.manager.Config;

/**
 * Player model class
 *
 */
public class Player extends GameObject{
	
	public static final int STANDING = INACTIVE + 1;
	public static final int WALKING_LEFT = STANDING + 1;
	public static final int WALKING_RIGHT = WALKING_LEFT + 1;
	public static final int FACING_LEFT = WALKING_RIGHT + 1;
	public static final int FACING_RIGHT = FACING_LEFT + 1;
	
	private static final float SPEED = Config.asFloat("Player.speed", 0);

	private float dx;
	private float dy;

	/**
	 * Initialize the player model with its asset on the asset class
	 */
	public Player () {
		width = Assets.playerWidth;
		height = Assets.playerHeight;
		geometry = Assets.playerGeometry;
	}

	public void update (float delta) {
		if (dx > 0.0f) {
			if (state != WALKING_RIGHT) {
				setState(WALKING_RIGHT);
			}
		} else if (dx < 0.0f) {
			if (state != WALKING_LEFT) {
				setState(WALKING_LEFT);
			}
		} else if (state == WALKING_RIGHT) {
			setState(FACING_RIGHT);
		} else if (state == WALKING_LEFT) {
			setState(FACING_LEFT);
		}
		stateTime += delta;
		float d = delta * SPEED;
		x += dx * d;
		y += dy * d;
	}

	/**
	 * set controller
	 * @param x
	 * @param y
	 */
	public void setController (float x, float y) {
		dx = x;
		dy = y;
	}	
}