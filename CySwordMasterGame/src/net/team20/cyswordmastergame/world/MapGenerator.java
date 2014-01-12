package net.team20.cyswordmastergame.world;

import com.badlogic.gdx.utils.IntArray;

/**
 * Level generator for the game
 *
 */
public final class MapGenerator {

	private int width;
	private int height;

	private IntArray walls;
	private IntArray doors;

	/**
	 * Generate map with input width and height
	 * @param width
	 * @param height
	 */
	public MapGenerator (int width, int height) {
		// Pre: width is odd
		// Pre: height is odd
		// Pre: width > 2
		// Pre: height > 2
		this.width = width;
		this.height = height;
		walls = new IntArray();
//		doors = new IntArray();
	}

	/**
	 * Rebuild map with input door pos
	 * @param doorPos
	 * @return
	 */
	public MapGenerator rebuild (int doorPos) {
		createMaze(doorPos);
//		this.doorPos = doorPos;
		return this;
	}

	/**
	 * return width
	 * @return
	 */
	public int getWidth () {
		return width;
	}

	/**
	 * return height
	 * @return
	 */
	public int getHeight () {
		return height;
	}

//	public int getDoorPos () {
//		return doorPos;
//	}

	/**
	 * return array of walls
	 * @return
	 */
	public IntArray getWalls () {
		return walls;
	}

	/**
	 * return array of doors
	 * @return
	 */
	public IntArray getDoors () {
		return doors;
	}

	/**
	 * create maze with inputed door position
	 * @param doorPos
	 */
	private void createMaze (int doorPos) {
		walls.clear();
//		doors.clear();
		addOuterWalls();
//		addDoors(doorPos);
//		subdivideChamber(0, 0, width, height);
	}

	/**
	 * add outher walls to level
	 */
	private void addOuterWalls () {
		addHWall(0, width / 2, 0);
		addHWall(width / 2 + 1, width, 0);
		addHWall(0, width / 2, height);
		addHWall(width / 2 + 1, width, height);
		addVWall(0, 0, height / 2);
		addVWall(0, height / 2 + 1, height);
		addVWall(width, 0, height / 2);
		addVWall(width, height / 2 + 1, height);
	}

	/**
	 * add horizontal walls
	 * @param x1
	 * @param x2
	 * @param y
	 */
	private void addHWall (int x1, int x2, int y) {
		if (x2 <= x1) return;
		addWall(x1, y, x2, y);
	}

	/**
	 * add vertical walss
	 * @param x
	 * @param y1
	 * @param y2
	 */
	private void addVWall (int x, int y1, int y2) {
		if (y2 <= y1) return;
		addWall(x, y1, x, y2);
	}

	/** add walls
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	private void addWall (int x1, int y1, int x2, int y2) {
		walls.add(x1);
		walls.add(y1);
		walls.add(x2);
		walls.add(y2);
	}

//	private void addDoors (int doorPos) {
//		if ((doorPos & DoorPositions.MIN_Y) == DoorPositions.MIN_Y) {
//			addHDoor(width / 2, 0);
//		}
//		if ((doorPos & DoorPositions.MAX_Y) == DoorPositions.MAX_Y) {
//			addHDoor(width / 2, height);
//		}
//		if ((doorPos & DoorPositions.MIN_X) == DoorPositions.MIN_X) {
//			addVDoor(0, height / 2);
//		}
//		if ((doorPos & DoorPositions.MAX_X) == DoorPositions.MAX_X) {
//			addVDoor(width, height / 2);
//		}
//	}
//
//	private void addHDoor (int x, int y) {
//		addDoor(x, y, x + 1, y);
//	}
//
//	private void addVDoor (int x, int y) {
//		addDoor(x, y, x, y + 1);
//	}
//
//	private void addDoor (int x1, int y1, int x2, int y2) {
//		doors.add(x1);
//		doors.add(y1);
//		doors.add(x2);
//		doors.add(y2);
//	}
}
