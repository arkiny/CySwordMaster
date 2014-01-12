package net.team20.cyswordmastergame.manager;

import net.team20.cyswordmastergame.model.Particle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

/**
 * Manage particle stubs in the game
 *
 */
public class ParticleManager {

	private final Array<Particle> particles;
	private final int maxParticles;
	private int index;

	/**
	 * Constructor for particles
	 * @param maxParticles number of max particles
	 * @param size particle's size
	 */
	public ParticleManager (int maxParticles, float size) {
		particles = new Array<Particle>(maxParticles);
		this.maxParticles = maxParticles;
		for (int i = 0; i < maxParticles; i++) {
			particles.add(new Particle(size));
		}
		index = 0;
	}

	/**
	 * Getter of the particles
	 * @return array of particles
	 */
	public Array<Particle> getParticles () {
		return particles;
	}

	/**
	 * clear particles
	 */
	public void clear () {
		for (Particle particle : particles) {
			particle.active = false;
		}
	}

	/**
	 * add particle
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param n number of particle
	 * @param c color of particle
	 */
	public void add (float x, float y, int n, Color c) {
		for (int i = 0; i < n; i++) {
			particles.get(index).spawn(c, x, y);
			if (++index == maxParticles) {
				index = 0;
			}
		}
	}

	/**
	 * update by time
	 * @param delta
	 */
	public void update (float delta) {
		for (Particle particle : particles) {
			if (particle.active) {
				particle.update(delta);
			}
		}
	}
}
