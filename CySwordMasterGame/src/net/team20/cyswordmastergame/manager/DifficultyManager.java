package net.team20.cyswordmastergame.manager;

import com.badlogic.gdx.graphics.Color;

/**
 * Interface for DiffcultyManager.
 * Class for game difficulty management
 *
 */
public interface DifficultyManager {
	
	/**
	 * Get ninja color by difficulty level
	 */
	Color getNinjaColor ();

	/**
	 * Get max number of enemy ninja by difficulty level
	 * @return max number of enemy
	 */
	int getNumberOfNinjas ();

	/**
	 * Get max number of enemy shriken
	 * for further update
	 * @return max number of shriken in the level
	 */
	int getNumberOfNinjaShots ();

	
	/**
	 * Get max ninja shot speed by difficulty level
	 * @return max speed of enemy
	 */
	float getNinjaShotSpeed ();
}
