package net.team20.cyswordmastergame.model;

import net.team20.cyswordmastergame.model.GameObject;
/**
 * class for baseshot which extends gameobect class
 *
 */
public class BaseShot extends GameObject {

	private float dx;
	private float dy;
	private float shotSpeed;

	/**
	 * constructor
	 */
	public BaseShot () {
		super();
	}

	/**
	 * set shot speed
	 * @param shotSpeed
	 */
	public void setShotSpeed (float shotSpeed) {
		this.shotSpeed = shotSpeed;
	}

	/**
	 * fire the shot with coordinate
	 * @param x
	 * @param y
	 * @param dx
	 * @param dy
	 */
	public void fire (float x, float y, float dx, float dy) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
	}

	public void update (float delta) {
		stateTime += delta;
		float n = shotSpeed * delta;
		x += dx * n;
		y += dy * n;
	}
}