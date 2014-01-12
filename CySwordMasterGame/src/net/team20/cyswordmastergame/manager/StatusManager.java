package net.team20.cyswordmastergame.manager;

import java.util.EnumSet;

import com.badlogic.gdx.Gdx;
import net.team20.cyswordmastergame.manager.Config;
import net.team20.cyswordmastergame.model.Ninja;
import net.team20.cyswordmastergame.world.WorldListener;

/**
 * Player status manager which implements WorldListener
 *
 */
public class StatusManager implements WorldListener {
	public enum Achievements {
		PERFECTIONIST("Perfectionist", "Clear " + CLEAR_LEVELS + " rooms in a row."), DAREDEVIL(
			"Daredevil", "Survive for " + DAREDEVIL_SECONDS), ADDICT("Addict", "Play " + ADDICT_GAMES + " games."), NIJOOCIDE("Robocide", "Destroyed "
			+ NIJOOCIDE_NINJAS + " robots"), IMMORTAL("Immortal", "Survive for " + IMMORTAL_ROOMS + " levels without being hit.");

		
		private final String summary;
		private final String text;

		private Achievements (String summary, String text) {
			this.summary = summary;
			this.text = text;
		}

		public String summary () {
			return this.summary;
		}

		public String text () {
			return this.text;
		}
	}

	// Achievements.
	private static final int CLEAR_LEVELS = Config.asInt("achievements.cleanRoomsForPerfectionist", 5);
	private static final int NIJOOCIDE_NINJAS = Config.asInt("achievements.robotsForRobocide", 50);
	private static final int ADDICT_GAMES = Config.asInt("achievements.gamesForAddict", 5);
	private static final float DAREDEVIL_SECONDS = Config.asInt("achievements.daredevilSeconds", 10);
	private static final int IMMORTAL_ROOMS = Config.asInt("achievements.roomsForLuckyJim", 10);
	
	// Lives and scoring.
	private static final int INITIAL_LIVES = Config.asInt("Player.lives", 3);
	private static final int ROBOT_SCORE = Config.asInt("Robot.score", 50);
	private static final int ROBOT_BONUS_SCORE = Config.asInt("Robot.bonusScore", 100);
	private static final int EXTRA_LIFE_SCORE_1 = Config.asInt("Player.firstExtraLife", 10000);
	private static final int EXTRA_LIFE_SCORE_2 = Config.asInt("Player.secondExtraLife", 50000);

	private final ScoreNotifier scoreNotifier;
	private final ScoringEventNotifier scoringEventNotifier;
	private final AchievementsNotifier achievementsNotifier;
	private final EnumSet<Achievements> achieved;
	
	// These variables are used for other two other actors that can control stuff around the game.
	private static boolean testerMode = false;
	private static int ninjaCount = 6; // Six is our default.
	
	/** The number of lives that the player has. */
	private int lives;

	/** The player's score. */
	private int score;

	/** The number of robots that have been destroyed in this room (including those that have been shot). */
	private int robotsDestroyed;

	/** The number of times the player has been hit in this room. */
	private int playerHits;

	/** The number of robots that have been destroyed in this game (including those that have been shot). */
	private int gameRobotsDestroyed;

	/** The number of times the player has cleared a room in a row. */
	private int clearlevels;

	/** The number of robots in the room. */
	private int numNinja;
	
	/** The number of rooms that the player has got through without dying. */
	private int gameLevelWithoutDying;

	/** The time at which the daredevil achievement is fired. */
	private float daredevilTime;

	/** True if the player was hit. */
	private boolean isPlayerHit;

	private float now;

	/**
	 * initialize statusmansger
	 */
	public StatusManager() {
		scoreNotifier = new ScoreNotifier();
		achievementsNotifier = new AchievementsNotifier();
		scoringEventNotifier = new ScoringEventNotifier();
		achieved = EnumSet.noneOf(Achievements.class);
	}

	/**
	 * update by time
	 * @param delta
	 */
	public void update (float delta) {
		now += delta;
		if (daredevilTime != 0.0f && now >= daredevilTime) {
			achievement(Achievements.DAREDEVIL);
			daredevilTime = 0.0f;
		}
	}

	/**
	 * add scorelistener to scoreNotifier 
	 * @param listener
	 */
	public void addScoreListener (ScoreListener listener) {
		scoreNotifier.addListener(listener);
	}

	/**
	 * add achievementlistener to achivementsNotifier
	 * @param listener
	 */
	public void addAchievementsListener (AchievementsListener listener) {
		achievementsNotifier.addListener(listener);
	}

	/**
	 * add scoringeventlistener to scoring event notifier
	 * @param listener
	 */
	public void addScoringEventListener (ScoringEventListener listener) {
		scoringEventNotifier.addListener(listener);
	}
	
	
	/**
	 * set number of lives
	 * @param lives
	 */
	private void setLives (int lives) {
		this.lives = lives;
		scoreNotifier.onLivesChanged(lives);
	}

	/**
	 * add lives
	 * @param inc
	 */
	private void addLives (int inc) {
		setLives(lives + inc);
	}

	/**
	 * set score  
	 * @param newScore
	 */
	private void setScore (int newScore) {
		this.score = newScore;
		scoreNotifier.onScoreChanged(newScore);
	}

	/**
	 * add ingame score
	 * @param inc
	 */
	private void addScore (int inc) {
		int oldScore = score;
		setScore(score + inc);
		if (oldScore < EXTRA_LIFE_SCORE_1 && score >= EXTRA_LIFE_SCORE_1) {
			addLives(1);
		} else if (oldScore < EXTRA_LIFE_SCORE_2 && score >= EXTRA_LIFE_SCORE_2) {
			addLives(1);
		}
	}

	@Override
	public void onWorldReset () {
		gameRobotsDestroyed = 0;
		gameLevelWithoutDying = 0;
		clearlevels = 0;
		isPlayerHit = false;
		now = 0.0f;
		setLives(INITIAL_LIVES);
		setScore(0);
	}

	@Override
	public void onEnteredRoom (float time, int robots) {
		robotsDestroyed = 0;
		playerHits = 0;
		numNinja = robots;
	}

	@Override
	public void onExitedRoom (float time, int robots) {
		if (robots == 0) {
			clearlevels++;
			if (clearlevels == CLEAR_LEVELS) {
				achievement(Achievements.PERFECTIONIST);
			}
		}
		
		if (playerHits == 0) {
			gameLevelWithoutDying++;
			if (gameLevelWithoutDying == IMMORTAL_ROOMS) {
				achievement(Achievements.IMMORTAL);
			}
		} else {
			gameLevelWithoutDying = 0;
		}
		Gdx.app.log("Metrics", "FPS = " + Gdx.graphics.getFramesPerSecond());
	}

	@Override
	public void onNinjaDestroyed (Ninja ninja) {
		robotsDestroyed++;
		gameRobotsDestroyed++;
		int robotScore = ROBOT_SCORE;
		if (robotsDestroyed == numNinja) {
			robotScore += ROBOT_BONUS_SCORE;
		}
		addScore(robotScore);
		scoringEventNotifier.onScoringEvent(ninja.x + ninja.width / 2, ninja.y + ninja.height / 2, robotScore);
		if (gameRobotsDestroyed == NIJOOCIDE_NINJAS) {
			achievement(Achievements.NIJOOCIDE);
		}
	}

	@Override
	public void onPlayerHit () {
		daredevilTime = 0.0f;
		playerHits++;
		isPlayerHit = true;
	}

	public void onPlayerSpawned () {
		if (isPlayerHit) {		
			if(!testerMode){			
			addLives(-1);}
			isPlayerHit = false;
		}
	}

	/**
	 * Set the tester mode true or false and remove certain 'features' that normal players get.
	 * @param tMode - True means it is a tester.
	 */
	public static void setTestMode(boolean tMode) {
		testerMode = tMode;
	}
	
	/**
	 * Set how many ninjas we will start with.
	 * @param ninjaCnt
	 */
	public static void setNinjaAmount(int ninjaAmt) {
		ninjaCount = ninjaAmt;
	}
	
	/**
	 * Used to determine initial ninja amount.
	 * @return ninjaCount. Either the default or what the ninjaAdmin will provide.
	 */
	public static int getNinjaAmount() {
		return ninjaCount;
	}
	
	private void achievement (Achievements achievement) {
		if (!achieved.contains(achievement)) {
			achieved.add(achievement);
			achievementsNotifier.onAttained(achievement);
		}
	}
}