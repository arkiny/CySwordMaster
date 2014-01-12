package net.team20.cyswordmastergame.model;

import net.team20.cyswordmastergame.manager.Config;
import net.team20.cyswordmastergame.Assets;

/**
 *  Deals with the Player's sword attacks.
 *
 */
public class PlayerShot extends BaseShot {

	private static final float SHOT_SPEED = Config.asFloat("PlayerShot.speed", 90f);

	/**
	 * initialize playershot with its asset and shot speed
	 */
	public PlayerShot () {
		width = Assets.playerShotWidth;
		height = Assets.playerShotHeight;
		setShotSpeed(SHOT_SPEED);
	}
}
