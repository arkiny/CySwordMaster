package net.team20.cyswordmastergame.manager;

import net.team20.cyswordmastergame.manager.StatusManager.Achievements;

/**
 * interface for achievement listener
 *
 */
public interface AchievementsListener {
	/**
	 * achievement on attained
	 * @param achievement which is attained
	 */
	void onAttained (Achievements achievement);
}
