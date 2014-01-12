package net.team20.cyswordmastergame.model;

import com.badlogic.gdx.math.Rectangle;
import net.team20.cyswordmastergame.utils.Colliders;
import net.team20.cyswordmastergame.utils.CollisionGeometry;

/**
 * A superclass of many game objects used throughout.
 *
 */
public class GameObject {

	/** The default state for any new game object. */
	public static final int INACTIVE = -1;

	/** This game object's x coordinate in world space. */
	public float x;

	/** This game object's y coordinate in world space. */
	public float y;

	/** This game object's width in world units. */
	public float width;

	/** This game object's height in world units. */
	public float height;

	/** This game object's collision geometry (if it has any) in local coordinates. */
	public CollisionGeometry geometry;

	/** This game object's current state. */
	public int state;

	/** How long this game object has been in its current state (in seconds). */
	public float stateTime;

	/** Will be true if this game object is in collision. */
	public boolean inCollision;

	// Holds this game object's bounding rectangle in world space.
	private final Rectangle bounds;

	/**initialize game
	 * 
	 */
	public GameObject () {
		stateTime = 0.0f;
		inCollision = false;
		bounds = new Rectangle();
	}

	/** Assigns collision geometry to this <code>GameObject</code>.
	 * @param geometry the new collision geometry. */
	public void setGeometry (CollisionGeometry geometry) {
		this.geometry = geometry;
	}

	/** Returns this <code>GameObject</code>'s bounding rectangle.
	 * @return the bounding rectangle. */
	public Rectangle bounds () {
		bounds.x = x;
		bounds.y = y;
		bounds.width = width;
		bounds.height = height;
		return bounds;
	}

	/** Switches this game object into a new state and resets {@link stateTime}.
	 * @param state the new state. */
	public void setState (int state) {
		this.state = state;
		stateTime = 0.0f;
	}

	/** Returns true if this game object's bounds intersect with the given rectangle.
	 * 
	 * @param r the rectangle to intersect.
	 * @return true if the bounds intersect. */
	public boolean boundsIntersect (Rectangle r) {
		return Colliders.intersects(bounds(), r);
	}

	/** Returns true if this game object's bounds intersect with the given game object.
	 * 
	 * @param go the other game object.
	 * @return true if the bounds intersect. */
	public boolean boundsIntersect (GameObject go) {
		return Colliders.intersects(bounds(), go.bounds());
	}

	/** Returns true if this game object's collision geometry intersects with the given rectangle.
	 * 
	 * @param r the rectangle to intersect.
	 * @return true if the geometry intersects with the rectangle. */
	public boolean geometryIntersects (Rectangle r) {
		return geometry.intersects(r, x, y);
	}

	/** Returns true if this game object's collision geometry intersects with another game object's collision geometry.
	 * 
	 * @param go the other game object.
	 * @return true if the geometries intersect. */
	public boolean geometryIntersects (GameObject go) {
		return geometry.intersects(x, y, go.geometry, go.x, go.y);
	}

	/** Returns true if this game object is in collision with a rectangle. It first does a simple box test against this game
	 * object's bounds, then, if that's true, tests its collision geometry against the rectangle.
	 * 
	 * @param r the rectangle to intersect.
	 * @return true if this game object intersects the rectangle. */
	public boolean intersects (Rectangle r) {
		return boundsIntersect(r) && (geometry == null || geometryIntersects(r));
	}

	/** Returns true if this game object is in collision with another game object. It first does a bounds test, then, if that's
	 * true, tests its collision geometry against the other game object's collision geometry. */
	public boolean intersects (GameObject go) {
		if (!boundsIntersect(go)) {
			return false;
		}
		if (geometry == null) {
			return go.geometry == null || go.geometryIntersects(bounds());
		} else if (go.geometry == null) {
			return geometryIntersects(go.bounds());
		}
		return geometryIntersects(go);
	}

	public void update(float delta) { }
	
}
