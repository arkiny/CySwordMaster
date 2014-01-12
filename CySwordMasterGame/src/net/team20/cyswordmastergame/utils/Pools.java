package net.team20.cyswordmastergame.utils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/** Pool helper functions.
 * @author Rod */
public class Pools {
	private Pools () {
	};

	/** Frees the items in an array to a pool.
	 * @param <T> the type of item allocated in the array.
	 * @param array the array of items to free.
	 * @param pool the pool that the items are to be released to. */
	public static <T> void freeArrayToPool (Array<T> array, Pool<T> pool) {
		pool.freeAll(array);
		array.clear();
	}

	/** Creates an array from a pool, freeing its items if it already exists.
	 * @param <T> the type of item allocated in the array.
	 * @param array the array of items to (re)create.
	 * @param pool the pool that the items are to be allocated from / released to.
	 * @param size the array's capacity.
	 * @return the input array */
	@SuppressWarnings("unchecked")
	public static <T> Array<T> makeArrayFromPool (Array<T> array, Pool<T> pool, int size) {
		if (array == null) {
			// Do this so that array.items can be used.
			T t = pool.obtain();
			array = new Array<T>(false, size, (Class<T>)t.getClass());
			pool.free(t);
		} else {
			freeArrayToPool(array, pool);
		}
		return array;
	}
}
