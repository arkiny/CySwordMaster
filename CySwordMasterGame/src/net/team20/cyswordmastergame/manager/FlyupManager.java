
package net.team20.cyswordmastergame.manager;

import net.team20.cyswordmastergame.model.Flyup;

import com.badlogic.gdx.utils.Array;

/**
 * Flyup class for scoring event
 *
 */
public class FlyupManager implements ScoringEventListener {

	private static final int MAX_FLYUPS = 16;

	final Array<Flyup> flyups;
	private int index;

	/**
	 * Constructor initializing the FlyupManager
	 * 
	 */
	public FlyupManager () {
		flyups = new Array<Flyup>(MAX_FLYUPS);
		for (int i = 0; i < MAX_FLYUPS; i++) {
			flyups.add(new Flyup());
		}
		index = 0;
	}

	@Override
	public void onScoringEvent (float x, float y, int points) {
		flyups.get(index).spawn(x, y, points);
		if (++index == MAX_FLYUPS) {
			index = 0;
		}
	}

	/**
	 * Update Flyup by time delta
	 * @param delta time
	 */
	public void update (float delta) {
		for (Flyup flyup : flyups) {
			if (flyup.active) {
				flyup.update(delta);
			}
		}
	}
	
	/**
	 * Getter function
	 * @return flyups array
	 */
	public Array<Flyup> flyups(){
		return flyups;
	}
}
