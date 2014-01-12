package net.team20.cyswordmastergame.world;

import net.team20.cyswordmastergame.model.Ninja;
import com.badlogic.gdx.utils.Array;

/**
 * notifier class which implment world listener
 *
 */
class WorldNotifier implements WorldListener {

	private final Array<WorldListener> listeners;

	/**
	 * constructor
	 * initialize array of listeners
	 */
	public WorldNotifier () {
		listeners = new Array<WorldListener>();
	}

	/**
	 * add listener to array
	 * @param listener
	 */
	public void addListener (WorldListener listener) {
		listeners.add(listener);
	}

	@Override
	public void onEnteredRoom (float time, int ninjas) {
		for (WorldListener listener : listeners) {
			listener.onEnteredRoom(time, ninjas);
		}
	}

	@Override
	public void onExitedRoom (float time, int ninjas) {
		for (WorldListener listener : listeners) {
			listener.onExitedRoom(time, ninjas);
		}
	}



	@Override
	public void onPlayerHit () {
		for (WorldListener listener : listeners) {
			listener.onPlayerHit();
		}
	}

	@Override
	public void onPlayerSpawned () {
		for (WorldListener listener : listeners) {
			listener.onPlayerSpawned();
		}
	}


	@Override
	public void onWorldReset () {
		for (WorldListener listener : listeners) {
			listener.onWorldReset();
		}
	}

	@Override
	public void onNinjaDestroyed(Ninja ninja) {
		for (WorldListener listener : listeners) {
			listener.onNinjaDestroyed(ninja);
		}		
	}
}
