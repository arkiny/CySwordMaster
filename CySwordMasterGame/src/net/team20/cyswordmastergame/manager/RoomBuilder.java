package net.team20.cyswordmastergame.manager;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.Pool;
import net.team20.cyswordmastergame.utils.Pools;
import net.team20.cyswordmastergame.world.MapGenerator;
import net.team20.cyswordmastergame.world.World;

/**
 * Builder for each level(rooms)
 *
 */
public class RoomBuilder {
	private static final float WALL_WIDTH = World.WALL_WIDTH;
	private static final float WALL_HEIGHT = World.WALL_HEIGHT;
	private static final float HALF_HEIGHT = WALL_HEIGHT / 2;
	private static final float ADJUSTMENT = World.OUTER_WALL_ADJUST;
	private static final int MAX_RECTANGLES = 256;
	private static final int MAX_DOORS = 4;
	private static final int MAX_WALLS = MAX_RECTANGLES - MAX_DOORS;

	private final MapGenerator mapGenerator;
	private final Pool<Rectangle> rectanglePool;
	private Array<Rectangle> doorRects;
	private Array<Rectangle> wallRects;
	private final int hcells;
	private final int vcells;

	/**
	 * Roombuilder constructor
	 * @param hcells horizontal cells
	 * @param vcells vertical cells
	 */
	public RoomBuilder (int hcells, int vcells) {
		this.hcells = hcells;
		this.vcells = vcells;
		mapGenerator = new MapGenerator(hcells, vcells);
		rectanglePool = new Pool<Rectangle>(MAX_RECTANGLES) {
			@Override
			protected Rectangle newObject () {
				return new Rectangle();
			}
		};
	}

	/**
	 * build room with doorPosition 
	 * @param doorPosition number of door position
	 */
	public void build (int doorPosition) {
		mapGenerator.rebuild(doorPosition);
		createWallsAndDoors();
	}

	/**
	 * return array of walls rectangle
	 * @return array of walls
	 */
	public Array<Rectangle> getWalls () {
		return wallRects;
	}

	/**
	 * return array of doors rectangle
	 * @return array of doors
	 */
	public Array<Rectangle> getDoors () {
		return doorRects;
	}

	/**
	 * create walls and doors on the room
	 */
	private void createWallsAndDoors () {
		createWalls();
	}

	/**
	 * create walls
	 */
	private void createWalls () {
		IntArray wallDefs = mapGenerator.getWalls();
		int n = wallDefs.size / 4;
		wallRects = Pools.makeArrayFromPool(wallRects, rectanglePool, MAX_WALLS);
		for (int i = 0, j = 0; i < n; i++, j += 4) {
			Rectangle wallRect = createWall(wallDefs, j);
			wallRects.add(wallRect);
		}
	}

	/**
	 * create a wall 
	 * @param wallDefs
	 * @param i
	 * @return
	 */
	private Rectangle createWall (IntArray wallDefs, int i) {
		int x1 = wallDefs.get(i);
		int y1 = wallDefs.get(i + 1);
		int x2 = wallDefs.get(i + 2);
		int y2 = wallDefs.get(i + 3);
		return (x1 == x2) ? createVWall(x1, y1, y2) : createHWall(x1, x2, y1);
	}

	/**
	 * create horizontal wall
	 * @param x1
	 * @param x2
	 * @param y1
	 * @return wall by rectangle
	 */
	private Rectangle createHWall (int x1, int x2, int y1) {
		float x = coordMinusHalfHeight(x1, hcells);
		float y = coordMinusHalfHeight(y1, vcells);
		float t = coordMinusHalfHeight(x2, hcells);
		float w = (t - x) + WALL_HEIGHT;
		float h = WALL_HEIGHT;
		return newRectangle(x, y, w, h);
	}
	
	/**
	 * create vertical wall
	 * @param x1
	 * @param y1
	 * @param y2
	 * @return wall by rectangle
	 */
	private Rectangle createVWall (int x1, int y1, int y2) {
		float x = coordMinusHalfHeight(x1, hcells);
		float y = coordMinusHalfHeight(y1, vcells);
		float t = coordMinusHalfHeight(y2, vcells);
		float w = WALL_HEIGHT;
		float h = (t - y) + WALL_HEIGHT;
		return newRectangle(x, y, w, h);
	}

	/**
	 * coordinate minus half height
	 * @param c
	 * @param limit
	 * @return
	 */
	private float coordMinusHalfHeight (int c, int limit) {
		float n = ADJUSTMENT;
		if (c == 0) {
			return n - HALF_HEIGHT;
		} else if (c < limit) {
			return n + WALL_WIDTH - ADJUSTMENT + ((c - 1) * WALL_WIDTH) - HALF_HEIGHT;
		} else {
			return n + 2 * (WALL_WIDTH - ADJUSTMENT) + ((c - 2) * WALL_WIDTH) - HALF_HEIGHT;
		}
	}

	/**
	 * make a new rectangle
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 */
	private Rectangle newRectangle (float x, float y, float w, float h) {
		Rectangle r = rectanglePool.obtain();
		r.x = x;
		r.y = y;
		r.width = w;
		r.height = h;
		return r;
	}	
}