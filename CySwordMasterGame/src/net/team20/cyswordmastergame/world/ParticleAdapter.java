
package net.team20.cyswordmastergame.world;

import net.team20.cyswordmastergame.manager.ParticleManager;
import net.team20.cyswordmastergame.model.BaseShot;
import net.team20.cyswordmastergame.model.Ninja;
import net.team20.cyswordmastergame.model.Player;

import com.badlogic.gdx.graphics.Color;

/**
 * ParticlaAdapter which will be adapterd to world listener
 * @author Arkiny
 *
 */
class ParticleAdapter implements WorldListener {

	final int PARTICLES = 32;

	final private World world;
	final private ParticleManager particleManager;
	private Color ninjaColor;
	private Color shotExplosionColor;

	/**
	 * initialize particle adapter with world and particle manager
	 * @param world
	 * @param particleManager
	 */
	public ParticleAdapter (World world, ParticleManager particleManager) {
		this.world = world;
		this.particleManager = particleManager;
		shotExplosionColor = new Color(1.0f, 0.5f, 0.0f, 1.0f);
	}

	/**
	 * set ninja color with inputed color
	 * @param color
	 */
	public void setNinjaColor (Color color) {
		this.ninjaColor = color;
	}

	/**
	 * update particle manager updated based on the time delta
	 * @param delta
	 */
	public void update (float delta) {
		particleManager.update(delta);
	}

	@Override
	public void onEnteredRoom (float time, int ninjas) {
		particleManager.clear();
	}

	@Override
	public void onPlayerHit () {
		Player player = world.getPlayer();
		float x = player.x + player.width / 2;
		float y = player.y + player.height / 2;
		particleManager.add(x, y, 2 * PARTICLES, Color.WHITE);
	}

	@Override
	public void onNinjaDestroyed (Ninja ninja) {
		float x = ninja.x + ninja.width / 2;
		float y = ninja.y + ninja.height / 2;
		particleManager.add(x, y, PARTICLES, ninjaColor);
	}

	public void onShotDestroyed (BaseShot shot) {
		float x = shot.x + shot.width / 2;
		float y = shot.y + shot.height / 2;
		particleManager.add(x, y, PARTICLES / 8, shotExplosionColor);
	}

	@Override
	public void onExitedRoom (float time, int ninjas) {
	}

	public void onPlayerFired () {
	}

	@Override
	public void onPlayerSpawned () {
	}

	@Override
	public void onWorldReset () {
	}

	public void onNinjaFired(Ninja ninja) {
		
	}

	public void onNinjaHit(Ninja ninja) {
		
	}

}
