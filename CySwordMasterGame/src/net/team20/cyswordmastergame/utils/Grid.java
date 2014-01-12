package net.team20.cyswordmastergame.utils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import static net.team20.cyswordmastergame.utils.MathUtils.*;

/**
 * Grid class which make a grid of the game screen
 * @author Arkiny
 *
 */
public class Grid {

	private final int cols;
	private final int rows;
	private final float cellWidth;
	private final float cellHeight;
	private final Array<Array<Rectangle>> grid;
	private Array<Rectangle> result;

	/**
	 * make a grids with input columns, rows, width, height
	 * @param cols 
	 * @param rows
	 * @param width
	 * @param height
	 */
	public Grid (int cols, int rows, float width, float height) {
		this.cols = cols;
		this.rows = rows;
		this.cellWidth = width / cols;
		this.cellHeight = height / rows;
		this.grid = new Array<Array<Rectangle>>(cols * rows);
		for (int i = 0; i < cols * rows; i++) {
			grid.add(new Array<Rectangle>());
		}
		this.result = new Array<Rectangle>();
	}

	/**
	 * clear the grid
	 */
	public void clear () {
		for (Array<Rectangle> v : grid) {
			v.clear();
		}
	}

	/**
	 * add rectangle to the grid cell
	 * @param r
	 */
	public void add (Rectangle r) {
		int minX = max(0, cellX(r.x));
		int maxX = min(cols - 1, cellX(r.x + r.width));
		int minY = max(0, cellY(r.y));
		int maxY = min(rows - 1, cellY(r.y + r.height));
		for (int y = minY; y <= maxY; y++) {
			for (int x = minX; x <= maxX; x++) {
				int shv = getGridCell(x, y);
				Array<Rectangle> values = grid.get(shv);
				values.add(r);
			}
		}
	}

	/**
	 * return grid list
	 * @param r
	 * @return
	 */
	public Array<Rectangle> get (Rectangle r) {
		result.clear();
		int minX = max(0, cellX(r.x));
		int maxX = min(cols - 1, cellX(r.x + r.width));
		int minY = max(0, cellY(r.y));
		int maxY = min(rows - 1, cellY(r.y + r.height));
		for (int y = minY; y <= maxY; y++) {
			for (int x = minX; x <= maxX; x++) {
				int shv = getGridCell(x, y);
				result.addAll(grid.get(shv));
			}
		}
		return result;
	}

	private int cellX (float x) {
		return (int)(x / cellWidth);
	}

	private int cellY (float y) {
		return (int)(y / cellHeight);
	}

	private int getGridCell (int x, int y) {
		return x + y * cols;
	}
}
