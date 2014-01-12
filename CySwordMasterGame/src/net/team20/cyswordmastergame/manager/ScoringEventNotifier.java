
package net.team20.cyswordmastergame.manager;

import com.badlogic.gdx.utils.Array;

/**
 * scoring event notifier which implements scoringevventlistener interface
 *
 */
public class ScoringEventNotifier implements ScoringEventListener {

	private final Array<ScoringEventListener> listeners;

	/**
	 * initialize array
	 */
	public ScoringEventNotifier () {
		listeners = new Array<ScoringEventListener>();
	}

	/**
	 * add scoring event to listener array
	 * @param listener
	 */
	public void addListener (ScoringEventListener listener) {
		listeners.add(listener);
	}

	@Override
	public void onScoringEvent (float x, float y, int points) {
		for (ScoringEventListener listener : listeners) {
			listener.onScoringEvent(x, y, points);
		}
	}
}
