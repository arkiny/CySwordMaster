package net.team20.cyswordmastergame.world;

import static com.badlogic.gdx.math.MathUtils.random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import net.team20.cyswordmastergame.utils.Colliders;
import net.team20.cyswordmastergame.world.DoorPositions;
import net.team20.cyswordmastergame.utils.Colliders.ColliderHandler;
import net.team20.cyswordmastergame.utils.Colliders.RemovalHandler;
import net.team20.cyswordmastergame.utils.Colliders.SceneryHandler;
import net.team20.cyswordmastergame.utils.Pools;
import net.team20.cyswordmastergame.model.BaseShot;
import net.team20.cyswordmastergame.world.WorldListener;
import net.team20.cyswordmastergame.utils.Grid;
import net.team20.cyswordmastergame.manager.DifficultyManager;
import net.team20.cyswordmastergame.manager.RoomBuilder;
import net.team20.cyswordmastergame.world.WorldNotifier;
import net.team20.cyswordmastergame.model.GameObject;
import net.team20.cyswordmastergame.model.Player;
import net.team20.cyswordmastergame.model.PlayerShot;
import net.team20.cyswordmastergame.model.Ninja;
import net.team20.cyswordmastergame.manager.Config;

/**
 * This is how the game is basically ran. Most objects are initiated here.
 *
 */
public class World {	
	
	public static interface FireCommand{
		/**
		 * Attack command
		 * @param firer
		 * @param dx
		 * @param dy
		 */
		void fire (GameObject firer, float dx, float dy);
	}
	
	//map proportion
	private static final int VCELLS = Config.asInt("Maze.vCells", 3);
	private static final int HCELLS = Config.asInt("Maze.hCells", 5);

	// Wall sizes.
	public static final float WALL_HEIGHT = 0.25f;
	public static final float WALL_WIDTH = 6.0f;
	public static final float OUTER_WALL_ADJUST = WALL_HEIGHT;
	
	private static final int MAX_PLAYER_SHOTS = Config.asInt("Player.maxShots", 4);
	private static final int MAX_NINJAS = Config.asInt("Global.maxNinjas", 12);
	private static final float PLAYER_DEAD_INTERVAL = Config.asFloat("Global.deadTime", 2.0f);
	private static final float FIRING_INTERVAL = Config.asFloat("Player.firingInterval", 0.25f);
	public static final float ROOM_TRANSITION_TIME = Config.asFloat("Global.roomTransitionTime", 0.5f);

	// Game states.
	public static final int RESETTING = 1;
	public static final int ENTERED_ROOM = 2;
	public static final int PLAYING = 3;
	public static final int PLAYER_DEAD = 4;

	private final Pool<PlayerShot> shotPool;
	private final Pool<Ninja> ninjaPool;
	private final Grid roomGrid;
	private final RoomBuilder roomBuilder;
	private final Rectangle roomBounds;
	private final float minX;
	private final float maxX;
	private final float minY;
	private final float maxY;
	private final WorldNotifier notifier;
	private final DifficultyManager difficultyManager;
	private long roomSeed;
	private int roomX;
	private int roomY;
	private float playingTime;
	private float nextFireTime;
	private int numNinjas;
	private Vector2 playerPos;
	private float now;
	private Array<Rectangle> doorRects;
	private Array<Rectangle> wallRects;
	private int doorPosition;
	private Player player;
	private Array<PlayerShot> playerShots;
	private Array<Ninja> ninjas;
	private int state;
	private float stateTime;
	private Color ninjaColor;
	private boolean isPaused;
	private float pausedTime;

	/**
	 * pause the game
	 */
	public void pause () {
		isPaused = true;
		pausedTime = 0.0f;
	}

	/** resume the game
	 * 
	 */
	public void resume () {
		isPaused = false;
	}

	/** return if the game is paused
	 * 
	 * @return boolean ispaused
	 */
	public boolean isPaused () {
		return isPaused;
	}

	/** return paused game time
	 * 
	 * @return float paused time
	 */
	public float getPausedTime () {
		return pausedTime;
	}

	/** return state of the game
	 * 
	 * @return integer state number
	 */
	public int getState () {
		return state;
	}
	
	/** setState with inputed state
	 * 
	 * @param newState
	 */
	private void setState (int newState) {
		state = newState;
		stateTime = 0.0f;
	}

	/** return stateTime
	 * 
	 * @return state time spent
	 */
	public float getStateTime () {
		return stateTime;
	}

	/** return Ninja color
	 * 
	 * @return color of ninja
	 */
	public Color getNinjaColor () {
		return ninjaColor;
	}
	
	/**
	 * return door position
	 * @return position of the door
	 */
	public int getDoorPosition () {
		return doorPosition;
	}

	/**
	 * return array of door rectangles
	 * @return array of door rectangles
	 */
	public Array<Rectangle> getDoorRects () {
		return doorRects;
	}

	/**
	 * return array of wall rectangles
	 * @return array of wall rectangles
	 */
	public Array<Rectangle> getWallRects () {
		return wallRects;
	}

	/**
	 * getter of room bounds
	 * @return room bounds
	 */
	public Rectangle getRoomBounds () {
		return roomBounds;
	}

	/**
	 * getter for player
	 * @return player modle
	 */
	public Player getPlayer () {
		return player;
	}

	/**
	 * getter for player shots
	 * @return player shots
	 */
	public Array<PlayerShot> getPlayerShots () {
		return playerShots;
	}

	/**
	 * getter for ninjas
	 * @return ninjas
	 */
	public Array<Ninja> getNinjas () {
		return ninjas;
	}

	/**
	 * add WorldListener to notifier
	 * @param listener
	 */
	public void addWorldListener (WorldListener listener) {
		notifier.addListener(listener);
	}
	
	/**
	 * firecommand actiavted when player shot
	 */
	public final FireCommand firePlayerShot = new FireCommand() {
		@Override
		public void fire (GameObject firer, float dx, float dy) {
			if (now >= nextFireTime) {
				addPlayerShot(dx, dy);
				nextFireTime = now + FIRING_INTERVAL;
			}
		}
	};
	
	
	private void addPlayerShot (float dx, float dy) {
		if (state == PLAYING && playerShots.size < MAX_PLAYER_SHOTS) {
			PlayerShot shot = shotPool.obtain();
			shot.inCollision = false;
			float x = player.x + player.width / 2 - shot.width / 2;
			float y = player.y + player.height / 2 - shot.height / 2;
			shot.fire(x, y, dx, dy);
			playerShots.add(shot);
			//notifier.onPlayerFired();
		}
	}
	
	private final RemovalHandler<Ninja> ninjaRemovalHandler = new RemovalHandler<Ninja>() {
		@Override
		public void onRemove (Ninja ninja) {
			notifier.onNinjaDestroyed(ninja);
		}
	};
	
	private final RemovalHandler<BaseShot> shotRemovalHandler = new RemovalHandler<BaseShot>() {
		@Override
		public void onRemove (BaseShot shot) {
		//	notifier.onShotDestroyed(shot);
		}
	};
		
	private final ColliderHandler<Player, GameObject> playerNinjaCollisionHandler = new ColliderHandler<Player, GameObject>() {
		@Override
		public void onCollision (Player player, GameObject go) {
			player.inCollision = true;
		}
	};
	
	private final ColliderHandler<PlayerShot, Ninja> shotNinjaCollisionHandler = new ColliderHandler<PlayerShot, Ninja>() {
		@Override
		public void onCollision (PlayerShot shot, Ninja ninja) {
			if (!ninja.inCollision) {
				//notifier.onNinjaHit(ninja);
			}
			shot.inCollision = true;
			ninja.inCollision = true;
		}
	};
	
	private final SceneryHandler<Player> playerSceneryHandler = new SceneryHandler<Player>() {
		@Override
		public void onCollision (Player player, Rectangle r) {
			player.inCollision = true;
		}
	};

	private final SceneryHandler<GameObject> gameObjectSceneryHandler = new SceneryHandler<GameObject>() {
		@Override
		public void onCollision (GameObject t, Rectangle r) {
			t.inCollision = true;
		}
	};
	
	
	/** constructs a new world with difficulty manager's difficulty
	 * 
	 */
	public World (DifficultyManager difficultyManager){
		this.difficultyManager = difficultyManager;
		notifier = new WorldNotifier();
		minX = 0;
		maxX = WALL_WIDTH * HCELLS;
		minY = 0;
		maxY = WALL_WIDTH * VCELLS;
		roomBounds = new Rectangle(minX, minY, maxX - minX, maxY - minY);
		player = new Player();
		playerPos = new Vector2();
		roomBuilder = new RoomBuilder(HCELLS, VCELLS);
		roomGrid = new Grid(HCELLS * 2, VCELLS * 2, maxX, maxY);

		shotPool = new Pool<PlayerShot>(MAX_PLAYER_SHOTS, MAX_PLAYER_SHOTS) {
			@Override
			protected PlayerShot newObject () {
				return new PlayerShot();
			}
		};

		ninjaPool = new Pool<Ninja>(MAX_NINJAS, MAX_NINJAS) {
			@Override
			protected Ninja newObject () {
				return new Ninja();
			}
		};		
	}
	
	/** Resets the {@link World} to its starting state. */
	public void reset () {
		setState(RESETTING);
	}
	
	/** Called when the {@link World} is to be updated.
	 * 
	 * @param delta the time in seconds since the last render. */
	public void update (float delta) {
		if (!isPaused) {
			now += delta;
			stateTime += delta;
			switch (state) {
			case RESETTING:
				updateResetting();
				break;
			case ENTERED_ROOM:
				updateEnteredRoom();
				break;
			case PLAYING:
				updatePlaying(delta);
				break;
			case PLAYER_DEAD:
				updatePlayerDead(delta);
				break;
			}
		} else {
			pausedTime += delta;
		}
	}
	
	private void updateResetting () {
		notifier.onWorldReset();
		roomSeed = System.currentTimeMillis();
		roomX = 0;
		roomY = 0;
		populateRoom(DoorPositions.MIN_Y);
	}

	private void updateEnteredRoom () {
		if (stateTime >= ROOM_TRANSITION_TIME) {
			setState(PLAYING);
		}
	}

	private void updatePlaying (float delta) {
		player.update(delta);
		updateMobiles(delta);
		checkForCollisions();
		checkForLeavingRoom();
	}

	private void updatePlayerDead (float delta) {
		updateMobiles(delta);
		checkForCollisions();
		if (now >= playingTime) {
			resetRoom();
			setState(PLAYING);
		}
	}

	private void populateRoom (int doorPos) {
		doorPosition = doorPos;
		setRandomSeedFromRoom();
		createMap();
		placePlayer();
		createNinjas();
		createPlayerShots();
		setState(ENTERED_ROOM);
		notifier.onEnteredRoom(now, numNinjas);
	}
	
	private void createMap () {
		roomBuilder.build(doorPosition);
		wallRects = roomBuilder.getWalls();
		doorRects = roomBuilder.getDoors();
		roomGrid.clear();
		for (Rectangle r : wallRects) {
			roomGrid.add(r);
		}
	}

	private void setRandomSeedFromRoom () {
		long seed = roomSeed + ((roomX & 0xff) | ((roomY & 0xff) << 8));
		random.setSeed(seed);
	}

	private void placePlayer () {
		player.inCollision = false;

		switch (doorPosition) {

		case DoorPositions.MIN_X:
			player.x = minX + player.width / 2;
			player.y = (maxY + minY) / 2 - player.height / 2;
			break;

		case DoorPositions.MAX_X:
			player.x = maxX - player.width - player.width / 2;
			player.y = (maxY + minY) / 2 - player.height / 2;
			break;

		default:
			player.x = (maxX + minX) / 2 - player.width / 2;
			player.y = minY + player.height / 3;
			break;
		}

		player.setState(Player.FACING_RIGHT);
		notifier.onPlayerSpawned();
	}

	private void createNinjas () {
		ninjaColor = difficultyManager.getNinjaColor();
		numNinjas = difficultyManager.getNumberOfNinjas();

		final float minXSpawn = minX + WALL_HEIGHT;
		final float minYSpawn = minY + WALL_HEIGHT;
		final float maxXSpawn = maxX - WALL_HEIGHT;
		final float maxYSpawn = maxY - WALL_HEIGHT;
		ninjas = Pools.makeArrayFromPool(ninjas, ninjaPool, MAX_NINJAS);
		playerPos.set(player.x, player.y);
		for (int i = 0; i < numNinjas; i++) {
			Ninja ninja = ninjaPool.obtain();
			ninja.inCollision = false;
			do {
				ninja.x = random(minXSpawn, maxXSpawn - ninja.width);
				ninja.y = random(minYSpawn, maxYSpawn - ninja.height);
			} while (!canSpawnHere(ninja));
			ninja.setRespawnPoint(ninja.x, ninja.y);
			ninja.setPlayer(player);
			ninja.setWalls(wallRects);
			ninjas.add(ninja);
		}
	}
	
	private boolean canSpawnHere (Ninja ninja) {
		return !(intersectsWalls(ninja) || intersectsNinjas(ninja) || playerPos.dst(ninja.x, ninja.y) < WALL_WIDTH);
	}

	private boolean intersectsWalls (Ninja ninja) {
		return Colliders.intersects(ninja.bounds(), wallRects);
	}

	private boolean intersectsNinjas (Ninja ninja) {
		for (int i = 0; i < ninjas.size; i++) {
			if (ninja.boundsIntersect(ninjas.get(i))) {
				return true;
			}
		}
		return false;
	}

	private void createPlayerShots () {
		playerShots = Pools.makeArrayFromPool(playerShots, shotPool, MAX_PLAYER_SHOTS);
	}

	private void updateMobiles (float delta) {
		update(ninjas, delta);
		update(playerShots, delta);
	}

	private void resetRoom () {
		placePlayer();
		for (Ninja ninja : ninjas) {
			ninja.respawn();
		}
		playerShots.clear();
	}


	private void update (Array<? extends GameObject> gos, float delta) {
		for (GameObject go : gos) {
			go.update(delta);
		}
	}

	private void checkForCollisions () {
		checkMobileMobileCollisions();
		checkMobileSceneryCollisions();
		removeMarkedMobiles();
		if (state == PLAYING && player.inCollision == true) {
			doPlayerHit();
		}
	}

	private void checkMobileMobileCollisions () {
		Colliders.collide(playerShots, ninjas, shotNinjaCollisionHandler);		
		Colliders.collide(player, ninjas, playerNinjaCollisionHandler);		
	}

	private void checkMobileSceneryCollisions () {
		Colliders.collide(player, roomGrid.get(player.bounds()), playerSceneryHandler);
		markSceneryCollisions(ninjas, gameObjectSceneryHandler);
		markSceneryCollisions(playerShots, gameObjectSceneryHandler);
	}

	private <U extends GameObject, T extends U> void markSceneryCollisions (Array<T> gos, SceneryHandler<U> handler) {
		for (int i = 0; i < gos.size; i++) {
			T go = gos.get(i);
			Colliders.collide(go, roomGrid.get(go.bounds()), handler);
		}
	}

	private void removeMarkedMobiles () {
		Colliders.removeOutOfBounds(shotPool, playerShots, roomBounds);
		Colliders.removeMarkedCollisions(shotPool, playerShots, shotRemovalHandler);
		Colliders.removeMarkedCollisions(ninjaPool, ninjas, ninjaRemovalHandler);
	}

	private void checkForLeavingRoom () {
		int newDoor = -1;
		if (player.x + player.width / 2 < minX) {
			roomX--;
			newDoor = DoorPositions.MAX_X;
		} else if (player.x + player.width / 2 > maxX) {
			roomX++;
			newDoor = DoorPositions.MIN_X;
		} else if (player.y + player.height / 2 < minY) {
			roomY--;
			newDoor = DoorPositions.MAX_Y;
		} else if (player.y + player.height / 2 > maxY) {
			roomY++;
			newDoor = DoorPositions.MIN_Y;
		}
		if (newDoor != -1) {
			doLeftRoom(newDoor);
		}
	}

	private void doPlayerHit () {
		notifier.onPlayerHit();
		setState(PLAYER_DEAD);
		playingTime = now + PLAYER_DEAD_INTERVAL;
	}

	private void doLeftRoom (int newDoor) {
		notifier.onExitedRoom(now, ninjas.size);
		populateRoom(newDoor);
	}

}