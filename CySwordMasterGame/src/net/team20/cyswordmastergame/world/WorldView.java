package net.team20.cyswordmastergame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import net.team20.cyswordmastergame.Assets;
import net.team20.cyswordmastergame.utils.CameraHandler;
import net.team20.cyswordmastergame.utils.CameraHandler.ViewportMode;

import net.team20.cyswordmastergame.manager.Config;
import net.team20.cyswordmastergame.manager.FlyupManager;
import net.team20.cyswordmastergame.manager.ParticleManager;
import net.team20.cyswordmastergame.manager.StatusManager;
import net.team20.cyswordmastergame.model.Flyup;
import net.team20.cyswordmastergame.model.GameObject;
import net.team20.cyswordmastergame.model.Particle;
import net.team20.cyswordmastergame.model.Player;
import net.team20.cyswordmastergame.model.Ninja;
import net.team20.cyswordmastergame.model.PlayerShot;

import static net.team20.cyswordmastergame.Assets.*;

public class WorldView {
	public static interface Presenter {
		void setController (float x, float y);

		void setFiringController (float dx, float dy);

		void pause ();

		void resume ();
	}
	
	private static final float PARTICLE_SIZE = Config.asFloat("particle.size", 0.1875f);

	private static final int SPRITE_CACHE_SIZE = 128;
	private static final float FIRING_DEAD_ZONE = 0.125f;
	private static final float JOYSTICK_DISTANCE_MULTIPLIER = 0.2f; 

	private static final int MAX_PARTICLES = 256;

	private final World world;
	private final Presenter presenter;
	private OrthographicCamera worldCam;
	private SpriteCache spriteCache;
	private SpriteCache prevSpriteCache;
	private int cacheId;
	private int prevCacheId;
	private Matrix4 prevCacheTransform;
	private Matrix4 cacheTransform;
	private SpriteBatch spriteBatch;
	private Vector3 touchPoint;
	private Vector3 dragPoint;
	private Vector2 joystick;
	private final ParticleManager particleManager;
	private final ParticleAdapter particleAdapter;
	private final FlyupManager flyupManager;
	private final float worldMinY;
	private final float worldHeight;
	private final float worldMaxY;

	/** Constructs a new WorldView.
	 * 
	 * @param world the {@link World} that this is a view of.
	 * @param presenter the interface by which this <code>WorldView</code> communicates the state of its controls. */
	public WorldView (World world, StatusManager statusManager, Presenter presenter) {
		this.world = world;
		this.presenter = presenter;
		Rectangle bounds = world.getRoomBounds();
		worldMinY = bounds.y;
		worldHeight = bounds.height;
		worldMaxY = worldMinY + worldHeight;
		worldCam = CameraHandler.createCamera2(ViewportMode.STRETCH_TO_SCREEN, VIRTUAL_WIDTH, VIRTUAL_HEIGHT, Assets.pixelDensity);
		worldCam.update();
		spriteBatch = new SpriteBatch();
		spriteCache = new SpriteCache(SPRITE_CACHE_SIZE, true);
		spriteBatch.setProjectionMatrix(worldCam.combined);
		spriteCache.setProjectionMatrix(worldCam.combined);
		prevSpriteCache = new SpriteCache(SPRITE_CACHE_SIZE, true);
		prevSpriteCache.setProjectionMatrix(worldCam.combined);
		cacheTransform = new Matrix4();
		prevCacheTransform = new Matrix4();
		touchPoint = new Vector3();
		dragPoint = new Vector3();
		joystick = new Vector2();
		particleManager = new ParticleManager(MAX_PARTICLES, PARTICLE_SIZE);
		particleAdapter = new ParticleAdapter(world, particleManager);
		world.addWorldListener(particleAdapter);
		flyupManager = new FlyupManager();
		statusManager.addScoringEventListener(flyupManager);
		resetCaches();
	}

	public void update (float delta) {
		particleAdapter.update(delta);
		flyupManager.update(delta);
	}

	/** Called when the view should be rendered.
	 * 
	 * @param delta the time in seconds since the last render. */
	public void render (float delta) {
		switch (world.getState()) {

		case World.RESETTING:
			resetCaches();
			break;

		case World.ENTERED_ROOM:
			if (world.getStateTime() == 0.0f) {
				createMazeContent();
				particleAdapter.setNinjaColor(world.getNinjaColor());
			}
			drawWallsAndDoors();
			drawMobiles();
			break;

		case World.PLAYING:
			if (world.getStateTime() == 0.0f) {
				cacheTransform.idt();
				spriteCache.setTransformMatrix(cacheTransform);
				spriteBatch.setTransformMatrix(cacheTransform);
			}
		case World.PLAYER_DEAD:
			drawWallsAndDoors();
			drawMobiles();
			break;
		}
	}

	private void resetCaches () {
		cacheId = -1;
		cacheTransform.idt();
		prevCacheTransform.idt();
	}

	private void createMazeContent () {
		cycleCaches();
		cacheId = createWallsAndDoors(spriteCache);
	}

	private void cycleCaches () {
		SpriteCache tempCache = prevSpriteCache;
		prevSpriteCache = spriteCache;
		spriteCache = tempCache;
		prevCacheId = cacheId;
	}

	private int createWallsAndDoors (SpriteCache sc) {
		// Walls and doors never move, so we put them into a sprite cache.
		sc.clear();
		sc.beginCache();
		sc.setColor(Color.BLUE);
		Array<Rectangle> rects = world.getWallRects();
		for (int i = 0; i < rects.size; i++) {
			Rectangle rect = rects.get(i);
			sc.add(Assets.pureWhiteTextureRegion, rect.x, rect.y, rect.width, rect.height);
		}
		sc.setColor(1, 1, 0, 1);
		for (int i = 0; i < rects.size; i++) {
			Rectangle rect = rects.get(i);
			sc.add(Assets.pureWhiteTextureRegion, rect.x, rect.y, rect.width, rect.height);
		}
		return sc.endCache();
	}

	private void drawWallsAndDoors () {
		// Draw the old room if it is scrolling off.
		if (world.getState() == World.ENTERED_ROOM && prevCacheId != -1) {
			prevSpriteCache.begin();
			prevSpriteCache.draw(prevCacheId);
			prevSpriteCache.end();
		}

		// Draw the current room.
		spriteCache.begin();
		spriteCache.draw(cacheId);
		spriteCache.end();
	}

	private void drawMobiles () {
		spriteBatch.setProjectionMatrix(worldCam.combined);
		spriteBatch.begin();
		spriteBatch.setColor(Color.WHITE);
		drawPlayersShots();
		drawNinjas();
		drawPlayer();
		drawParticles();
		drawFlyups();
		spriteBatch.end();
	}

	private void drawNinjas () {
		spriteBatch.setColor(world.getNinjaColor());
		for (Ninja ninja : world.getNinjas()) {
			drawNinja(ninja);
		}
		spriteBatch.setColor(Color.WHITE);
	}

	private void drawPlayersShots () {
		Player player = world.getPlayer();
		for (PlayerShot shot : world.getPlayerShots()) {
			switch (player.state) {
			case Player.FACING_LEFT:
				draw(shot, Assets.playerShotLeft1);
				break;
			case Player.WALKING_LEFT:
				draw(shot, Assets.playerShotLeft1);
				break;
			case Player.FACING_RIGHT:
				draw(shot, Assets.playerShotRight1);
				break;
			case Player.WALKING_RIGHT:
				draw(shot, Assets.playerShotRight1);
				break;	
			}			
		}
	}

	private void drawPlayer () {
		Player player = world.getPlayer();
		switch (player.state) {
		case Player.WALKING_LEFT:
			draw(player, Assets.playerWalkingLeftAnimation.getKeyFrame(player.stateTime, true));
			break;
		case Player.WALKING_RIGHT:
			draw(player, Assets.playerWalkingRightAnimation.getKeyFrame(player.stateTime, true));
			break;
		case Player.FACING_LEFT:
			draw(player, Assets.playerWalkingLeft2);
			break;
		case Player.FACING_RIGHT:
			draw(player, Assets.playerWalkingRight2);
			break;
		}
	}

	private void drawNinja (Ninja ninja) {
		Animation ninjaAnimation = null;
		switch (ninja.state) {
		case Ninja.WALKING_LEFT:
			ninjaAnimation = Assets.ninjaWalkingLeftAnimation;
			break;
		case Ninja.WALKING_LEFT_DIAGONAL:
			ninjaAnimation = Assets.ninjaWalkingLeftAnimation;
			break;
		case Ninja.WALKING_RIGHT:
			ninjaAnimation = Assets.ninjaWalkingRightAnimation;
			break;
		case Ninja.WALKING_RIGHT_DIAGONAL:
			ninjaAnimation = Assets.ninjaWalkingRightAnimation;
			break;
		default:
			ninjaAnimation = Assets.ninjaWalkingRightAnimation;
		}
		draw(ninja, ninjaAnimation.getKeyFrame(ninja.stateTime, true));
	}

	private void draw (GameObject go, TextureRegion region) {
		spriteBatch.draw(region, go.x, go.y, go.width, go.height);
	}

	private void drawParticles () {
		spriteBatch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		for (Particle particle : particleManager.getParticles()) {
			if (particle.active) {
				spriteBatch.setColor(particle.color);
				spriteBatch.draw(Assets.pureWhiteTextureRegion, particle.x, particle.y, particle.size, particle.size);
			}
		}
		spriteBatch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	}

	private void drawFlyups () {
		BitmapFont font = Assets.flyupFont;
		float scale = font.getScaleX();
		font.setScale(1.0f / Assets.pixelDensity);
		spriteBatch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		for (Flyup flyup : flyupManager.flyups()) {
			if (flyup.active) {
				font.draw(spriteBatch, flyup.scoreString, flyup.x, flyup.y);
			}
		}
		spriteBatch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		font.setScale(scale);
	}
	
	

	/** Updates the state of the on-screen controls.
	 * 
	 * @param delta time in seconds since the last render. */
	@SuppressWarnings("deprecation")
	public void updateControls (float delta) {
		presenter.setController(0.0f, 0.0f);
		if (Gdx.input.justTouched()) {
			worldCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			if (world.isPaused()) {
				presenter.resume();
			} else if (touchPoint.y >= worldMaxY) {
				presenter.pause();
			}
		} else if (Gdx.input.isTouched()) {
			worldCam.unproject(dragPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			float dx = dragPoint.x - touchPoint.x;
			float dy = dragPoint.y - touchPoint.y;
			joystick.set(dx, dy).mul(JOYSTICK_DISTANCE_MULTIPLIER);
			float len = joystick.len();
			if (len > 1) {
				joystick.nor();
			}
			if (presenter != null) {
				presenter.setController(joystick.x, joystick.y);
				if (len >= FIRING_DEAD_ZONE) {
					joystick.nor();
					presenter.setFiringController(joystick.x, joystick.y);
				}
			}
		}		
	}
}
