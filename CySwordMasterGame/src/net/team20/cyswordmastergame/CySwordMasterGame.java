package net.team20.cyswordmastergame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import net.team20.cyswordmastergame.manager.AchievementsListener;
import net.team20.cyswordmastergame.manager.StatusManager.Achievements;
import net.team20.cyswordmastergame.screens.IShowScores;
import net.team20.cyswordmastergame.screens.ISubmitScores;
import net.team20.cyswordmastergame.screens.MainMenuScreen;
import net.team20.cyswordmastergame.screens.NinjaAdminScreen;
import net.team20.cyswordmastergame.screens.ScoresScreen;
import net.team20.cyswordmastergame.world.WorldRenderer;

/**
 * Class that sets up our game. Creates the screens we will need.
 *
 */
public class CySwordMasterGame extends Game implements AchievementsListener {
	Screen mainMenuScreen;
	Screen playingScreen;
	public Screen adminScreen;
	public ScoresScreen scoresScreen;
	IShowScores scoreDisplayer;
	ISubmitScores scoreSubmitter;
	AchievementsListener achievementsListener;
	boolean testermode;
	
	/**
	 * Constructor that takes in and sets if we are in tester mode.
	 * @param testermode
	 * This testermode determines if we are going to show the two tester actors we have created.
	 */
	public CySwordMasterGame(boolean testermode){
		this.testermode = testermode;
	}
	
	/**
	 * Check to see if we are a tester. Used to determine if we will show the tester/admin buttons within mainmenuscreen.
	 * @return
	 */
	public boolean isTester(){
		return testermode;
	}
	
    /** 
     * Creates all the screens that the game will need, then switches to the main menu.
     */
	@Override
	public void create() {
		Assets.load();
		mainMenuScreen = new MainMenuScreen(this);
		playingScreen = new WorldRenderer(this);
		scoresScreen = new ScoresScreen(this);
		adminScreen = new NinjaAdminScreen(this);
		setScreen(mainMenuScreen);
	}

	@Override
	public void onAttained(Achievements achievement) {
		if (achievementsListener != null) {
			achievementsListener.onAttained(achievement);
		}
	}	
	
	/**
	 * Submit scores
	 * @param score
	 */
	public void submitScore (int score) {
		if (scoreSubmitter != null) {
			scoreSubmitter.submitScore(score);
		}
	}

	/**
	 * Show scores when it can be shown
	 */
	public void showScores () {
		if (scoreDisplayer != null) {
			scoreDisplayer.showScores();
		}
	}

	/**
	 * set Score displayer with inputed scoreDisplayer
	 * @param scoreDisplayer
	 */
	public void setScoreDisplayer (IShowScores scoreDisplayer) {
		this.scoreDisplayer = scoreDisplayer;
	}

	/**
	 * set Score submitter with inputed scoreSubmitter
	 * @param scoreSubmitter
	 */
	public void setScoreSubmitter (ISubmitScores scoreSubmitter) {
		this.scoreSubmitter = scoreSubmitter;
	}

	/**
	 * set achievementListener with inputed AchievementListener
	 * @param listener
	 */
	public void setAchievementsListener (AchievementsListener listener) {
		this.achievementsListener = listener;
	}

	/**
	 * Getter of playing Screen
	 * @return playingScreen
	 */
	public Screen playingScreen(){		
		return playingScreen;
	}
	
	/**
	 * Getter of main Menu Screen
	 * @return main Menu Screen
	 */
	public Screen mainMenuScreen(){
		return mainMenuScreen;
	}
}
