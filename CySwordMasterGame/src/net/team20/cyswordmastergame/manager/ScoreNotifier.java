package net.team20.cyswordmastergame.manager;

import com.badlogic.gdx.utils.Array;

/**
 * Score notifier which implements score Listener
 *
 */
public class ScoreNotifier implements ScoreListener {

	private Array<ScoreListener> listeners;

	/**
	 * initialze array
	 */
	public ScoreNotifier () {
		listeners = new Array<ScoreListener>();
	}

	/**
	 * add scorelistener to listeners array 
	 * @param listener
	 */
	public void addListener (ScoreListener listener) {
		listeners.add(listener);
	}

	@Override
	public void onScoreChanged (int score) {
		for (ScoreListener listener : listeners) {
			listener.onScoreChanged(score);
		}
	}

	@Override
	public void onLivesChanged (int lives) {
		for (ScoreListener listener : listeners) {
			listener.onLivesChanged(lives);
		}
	}
}
