package net.team20.cyswordmastergame.manager;

import net.team20.cyswordmastergame.manager.StatusManager.Achievements;

import com.badlogic.gdx.utils.Array;

/**
 * Achivement notifier which implements achievementslistner
 *
 */
public class AchievementsNotifier implements AchievementsListener {

	private final Array<AchievementsListener> listeners;

	/**
	 * Constructor setting up the AchievementsNotifier, which is eventually used to check for Achievements during gameplay.
	 */
	public AchievementsNotifier () {
		this.listeners = new Array<AchievementsListener>();
	}

	/**
	 * addLisnter to array
	 * @param listener
	 */
	public void addListener (AchievementsListener listener) {
		listeners.add(listener);
	}

	@Override
	public void onAttained (Achievements achievement) {
		for (AchievementsListener listener : listeners) {
			listener.onAttained(achievement);
		}
	}
}
