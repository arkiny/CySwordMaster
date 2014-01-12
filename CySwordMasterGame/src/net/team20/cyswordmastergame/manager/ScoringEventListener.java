package net.team20.cyswordmastergame.manager;

/**
 * Listener Interface for scoring event
 *
 */
public interface ScoringEventListener {
	/**
	 * listen scoring event from the coordinate
	 * @param x
	 * @param y
	 * @param points
	 */
	void onScoringEvent (float x, float y, int points);
}
