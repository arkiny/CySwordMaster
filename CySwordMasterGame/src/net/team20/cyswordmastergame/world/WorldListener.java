package net.team20.cyswordmastergame.world;

import net.team20.cyswordmastergame.model.Ninja;

/**
 * Interface class for world
 *
 */
public interface WorldListener {

	/**
	 * activated when enter the room
	 * @param time entred time
	 * @param ninjas number of ninjas
	 */
	public void onEnteredRoom (float time, int ninjas);

	/**
	 * activated when exit the room
	 * @param time exited time
	 * @param ninjas number of ninja left
	 */
	public void onExitedRoom (float time, int ninjas);

	/**
	 * player fired
	 */
	//public void onPlayerFired ();

	/**
	 * when activated player hit
	 */
	public void onPlayerHit ();

	/**
	 * world changed when player spawnd
	 */
	public void onPlayerSpawned ();

	/**
	 * world act when ninja die
	 * @param ninja
	 */
	public void onNinjaDestroyed (Ninja ninja);

	/**
	 * world reset
	 */
	public void onWorldReset ();
}
