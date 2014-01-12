
package net.team20.cyswordmastergame.screens;

import com.badlogic.gdx.Screen;

/**
 * Game screen which implements screen of game engine
 * @author Arkiny
 *
 * @param <T>
 */
public class GameScreen<T> implements Screen {
	protected T game;

	/**
	 * initialize the Gamescreen with game object
	 * @param game
	 */
	public GameScreen (T game) {
		this.game = game;
	}

	@Override
	public void dispose () {
	}

	@Override
	public void hide () {
	}

	@Override
	public void pause () {
	}

	@Override
	public void render (float delta) {
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void resume () {
	}

	@Override
	public void show () {
	}
}
