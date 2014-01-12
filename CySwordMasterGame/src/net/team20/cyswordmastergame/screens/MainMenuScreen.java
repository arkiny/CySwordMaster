package net.team20.cyswordmastergame.screens;

import static net.team20.cyswordmastergame.Assets.VIRTUAL_HEIGHT;
import static net.team20.cyswordmastergame.Assets.VIRTUAL_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import net.team20.cyswordmastergame.utils.CameraHandler;
import net.team20.cyswordmastergame.utils.CameraHandler.ViewportMode;
import net.team20.cyswordmastergame.Assets;
import net.team20.cyswordmastergame.CySwordMasterGame;
import net.team20.cyswordmastergame.manager.StatusManager;
import net.team20.cyswordmastergame.screens.GameScreen;
import net.team20.cyswordmastergame.utils.Button;

/**
 * MainMenuScreen for the game which extends gamescreen of cywordmaster
 * 
 */
public class MainMenuScreen extends GameScreen<CySwordMasterGame> {

	final String TITLE = "SWORDMASTER";
	private OrthographicCamera menuCam;
	SpriteBatch spriteBatch;
	private Button playButton;
	private Button testButton;
	private Button ninjaAdminButton;
	private Button scoresButton;
	private Vector3 touchPoint;
	private boolean wasTouched;
	public static boolean neverdie = true;

	//
	/**
	 * initialize game screen with camerahandler
	 * 
	 * @param game
	 */
	public MainMenuScreen(CySwordMasterGame game) {
		super(game);
		menuCam = CameraHandler.createCamera2(ViewportMode.PIXEL_PERFECT,
				VIRTUAL_WIDTH, VIRTUAL_HEIGHT, Assets.pixelDensity);
		spriteBatch = new SpriteBatch();
		spriteBatch.setProjectionMatrix(menuCam.combined);
		touchPoint = new Vector3();
		createButtons();
	}

	private void createButtons() {
		playButton = createPlayButton();
		playButton.setWidth(VIRTUAL_WIDTH / 4);
		playButton.setHeight(VIRTUAL_HEIGHT / 4);

		if (game.isTester()) {
			testButton = createTestButton();
			testButton.setWidth(VIRTUAL_WIDTH / 4);
			testButton.setHeight(VIRTUAL_HEIGHT / 4);
			ninjaAdminButton = createNinjaAdminButton();
			ninjaAdminButton.setWidth(VIRTUAL_WIDTH / 4);
			ninjaAdminButton.setHeight(VIRTUAL_HEIGHT / 4);
		}
		
		
		
		// if (game.canShowScores()) {
		scoresButton = createShowScoresButton();
		scoresButton.setWidth(VIRTUAL_WIDTH / 4);
		scoresButton.setHeight(VIRTUAL_HEIGHT / 4);
		scoresButton.rightOn(VIRTUAL_WIDTH);
		scoresButton.bottomOn(1.0f);
		playButton.leftOn(0);
		playButton.bottomOn(1.0f);
		
		if (game.isTester()) {
			testButton.leftOn((VIRTUAL_WIDTH / 2) - (VIRTUAL_WIDTH / 8));
			testButton.bottomOn(1.0f);
			ninjaAdminButton.leftOn((VIRTUAL_WIDTH / 2) - (VIRTUAL_WIDTH / 8));
			ninjaAdminButton.bottomOn(2.0f + (VIRTUAL_HEIGHT / 4));
		}

		// } else {
		// playButton.bottomOn(1.0f);
		// playButton.centerHorizontallyOn(VIRTUAL_WIDTH / 2);
		// }
	}

	private Button createTestButton() {
		return new Button("TEST", Assets.textFont);
	}

	private Button createPlayButton() {
		return new Button("PLAY", Assets.textFont);
	}

	private Button createNinjaAdminButton() {
		return new Button("NINJA ADMIN", Assets.textFont);
	}
	
	private Button createShowScoresButton() {
		return new Button("SCORES", Assets.textFont);
	}

	@Override
	public void render(float delta) {
		updateButtons(delta);
		if (game.isTester()) {
			if (playButton.wasPressed()) {
				startGame();
			} else if (scoresButton.wasPressed()) {
				showScores();
			} else if (testButton.wasPressed()) {
				startTest();
			} else if (ninjaAdminButton.wasPressed()) {
				showAdmin();
			} else {
				Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
				Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
				spriteBatch.begin();
				Assets.scoreFont.setColor(Color.WHITE);
				float y = VIRTUAL_HEIGHT - Assets.scoreFont.getCapHeight()
						/ 2.0f;
				Assets.scoreFont.drawWrapped(spriteBatch, TITLE, 0, y,
						VIRTUAL_WIDTH, HAlignment.CENTER);
				drawButtons();
				spriteBatch.end();
			}
		} else {
			if (playButton.wasPressed()) {
				startGame();
			} else if (scoresButton.wasPressed()) {
				showScores();
			} else {
				Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
				Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
				spriteBatch.begin();
				Assets.scoreFont.setColor(Color.WHITE);
				float y = VIRTUAL_HEIGHT - Assets.scoreFont.getCapHeight()
						/ 2.0f;
				Assets.scoreFont.drawWrapped(spriteBatch, TITLE, 0, y,
						VIRTUAL_WIDTH, HAlignment.CENTER);
				drawButtons();
				spriteBatch.end();
			}
		}
	}

	private void startGame() {
		StatusManager.setTestMode(false);
		game.setScreen(game.playingScreen());
	}

	private void startTest() {
		StatusManager.setTestMode(true);
		game.setScreen(game.playingScreen());
	}

	private void showScores() {
		// game.showScores();
		game.setScreen(game.scoresScreen);
	}
	
	private void showAdmin() {
		// game.showScores();
		game.setScreen(game.adminScreen);
	}

	private void updateButtons(float delta) {
		touchPoint = screenToViewport(Gdx.input.getX(), Gdx.input.getY());
		boolean justTouched = Gdx.input.justTouched();
		boolean isTouched = Gdx.input.isTouched();
		boolean justReleased = wasTouched && !isTouched;
		wasTouched = isTouched;
		playButton.update(delta, justTouched, isTouched, justReleased,
				touchPoint.x, touchPoint.y);
		// if (game.canShowScores()) {
		if (game.isTester()) {
			testButton.update(delta, justTouched, isTouched, justReleased,
					touchPoint.x, touchPoint.y);
			ninjaAdminButton.update(delta, justTouched, isTouched, justReleased,
					touchPoint.x, touchPoint.y);
		}
		scoresButton.update(delta, justTouched, isTouched, justReleased,
				touchPoint.x, touchPoint.y);
		// }
	}

	private void drawButtons() {
		playButton.draw(spriteBatch);
		// if (game.canShowScores()) {
		scoresButton.draw(spriteBatch);
		if (game.isTester()) {
			testButton.draw(spriteBatch);
			ninjaAdminButton.draw(spriteBatch);
		}
		// }
	}

	private Vector3 screenToViewport(float x, float y) {
		menuCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		return touchPoint;
	}
}
