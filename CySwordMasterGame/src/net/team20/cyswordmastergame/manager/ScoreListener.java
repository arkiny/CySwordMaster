package net.team20.cyswordmastergame.manager;

/**
 * ScoreListner interface
 * @author Arkiny
 *
 */
public interface ScoreListener {
	/**
	 * change(update) score
	 * @param score
	 */
	void onScoreChanged (int score);
	
	/**
	 * change(update) lives
	 * @param lives
	 */
	void onLivesChanged (int lives);
}
