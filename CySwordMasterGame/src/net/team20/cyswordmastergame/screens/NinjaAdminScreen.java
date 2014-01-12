package net.team20.cyswordmastergame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import net.team20.cyswordmastergame.Assets;
import net.team20.cyswordmastergame.CySwordMasterGame;
import net.team20.cyswordmastergame.utils.Button;
import net.team20.cyswordmastergame.utils.CameraHandler;
import net.team20.cyswordmastergame.utils.CameraHandler.ViewportMode;
import net.team20.cyswordmastergame.manager.StatusManager;
import net.team20.cyswordmastergame.screens.GameScreen;

import static net.team20.cyswordmastergame.Assets.*;

/**
 * Allow user to change amount of ninjas.
 * @author Sam Reeves
 *
 */
public class NinjaAdminScreen extends GameScreen<CySwordMasterGame> {
	final String TITLE = "SWORDMASTER - Ninja Admin Screen";
	final String DESC = "Pick how many ninjas you would like to start out with.";
	
	private OrthographicCamera adminCam;
	SpriteBatch spriteBatch;
	
	private Vector3 touchPoint;
	private boolean wasTouched;	
	
	// Get buttons ready to go.
	private Button bThree;
	private Button bFour;
	private Button bFive;
	private Button bSix;
	private Button bSeven;
	private Button bEight;
	private Button bNine;
	private Button bTen;
	private Button bEleven;
		
	/**
	 * initialize game screen with Camera Handler
	 * 
	 * @param game
	 */
	public NinjaAdminScreen(CySwordMasterGame game) {
		super(game);
		adminCam = CameraHandler.createCamera2(ViewportMode.PIXEL_PERFECT,
				VIRTUAL_WIDTH, VIRTUAL_HEIGHT, Assets.pixelDensity);
		spriteBatch = new SpriteBatch();
		spriteBatch.setProjectionMatrix(adminCam.combined);
		touchPoint = new Vector3();
		
		createButtons(); // Creates all my buttons.
	}

	private void createButtons() {
		// Set up new buttons.
		bThree = new Button("03", Assets.textFont);
		bFour = new Button("04", Assets.textFont);
		bFive = new Button("05", Assets.textFont);
		bSix = new Button("06", Assets.textFont);
		bSeven = new Button("07", Assets.textFont);
		bEight = new Button("08", Assets.textFont);
		bNine = new Button("09", Assets.textFont);
		bTen = new Button("10", Assets.textFont);
		bEleven = new Button("11", Assets.textFont);
		
		// Set width & Height of all the buttons...
		bThree.setWidth(VIRTUAL_WIDTH / 4);
		bThree.setHeight(VIRTUAL_HEIGHT / 4);
		bFour.setWidth(VIRTUAL_WIDTH / 4);
		bFour.setHeight(VIRTUAL_HEIGHT / 4);
		bFive.setWidth(VIRTUAL_WIDTH / 4);
		bFive.setHeight(VIRTUAL_HEIGHT / 4);
		bSix.setWidth(VIRTUAL_WIDTH / 4);
		bSix.setHeight(VIRTUAL_HEIGHT / 4);
		bSeven.setWidth(VIRTUAL_WIDTH / 4);
		bSeven.setHeight(VIRTUAL_HEIGHT / 4);
		bEight.setWidth(VIRTUAL_WIDTH / 4);
		bEight.setHeight(VIRTUAL_HEIGHT / 4);
		bNine.setWidth(VIRTUAL_WIDTH / 4);
		bNine.setHeight(VIRTUAL_HEIGHT / 4);
		bTen.setWidth(VIRTUAL_WIDTH / 4);
		bTen.setHeight(VIRTUAL_HEIGHT / 4);
		bEleven.setWidth(VIRTUAL_WIDTH / 4);
		bEleven.setHeight(VIRTUAL_HEIGHT / 4);

		// Locate them on the screen.
		bThree.leftOn(0);
		bThree.bottomOn((3.0f + 2 * (VIRTUAL_HEIGHT / 4)));
		bFour.leftOn((VIRTUAL_WIDTH / 2) - (VIRTUAL_WIDTH / 8));
		bFour.bottomOn((3.0f + 2 * (VIRTUAL_HEIGHT / 4)));
		bFive.rightOn(VIRTUAL_WIDTH);
		bFive.bottomOn((3.0f + 2 *(VIRTUAL_HEIGHT / 4)));
		
		bSix.leftOn(0);
		bSix.bottomOn((2.0f + (VIRTUAL_HEIGHT / 4)));
		bSeven.leftOn((VIRTUAL_WIDTH / 2) - (VIRTUAL_WIDTH / 8));
		bSeven.bottomOn((2.0f + (VIRTUAL_HEIGHT / 4)));
		bEight.rightOn(VIRTUAL_WIDTH);
		bEight.bottomOn((2.0f + (VIRTUAL_HEIGHT / 4)));
		
		bNine.leftOn(0);
		bNine.bottomOn(1.0f);
		bTen.leftOn((VIRTUAL_WIDTH / 2) - (VIRTUAL_WIDTH / 8));
		bTen.bottomOn(1.0f);
		bEleven.rightOn(VIRTUAL_WIDTH);
		bEleven.bottomOn(1.0f);
	}


	@Override
	public void render(float delta) {
		updateButtons(delta);
		if(bThree.wasPressed()) {
			startThree();
		} else if(bFour.wasPressed()) {
			startFour();
		} else if(bFive.wasPressed()) {
			startFive();
		} else if(bSix.wasPressed()) {
			startSix();
		} else if(bSeven.wasPressed()) {
			startSeven();
		} else if(bEight.wasPressed()) {
			startEight();
		} else if(bNine.wasPressed()) {
			startNine();
		} else if(bTen.wasPressed()) {
			startTen();
		} else if(bEleven.wasPressed()) {
			startEleven();
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

	private void startThree() {
		StatusManager.setNinjaAmount(3);
		game.setScreen(game.playingScreen());
	}

	private void startFour() {
		StatusManager.setNinjaAmount(4);
		game.setScreen(game.playingScreen());
	}
	
	private void startFive() {
		StatusManager.setNinjaAmount(5);
		game.setScreen(game.playingScreen());
	}
	
	private void startSix() {
		StatusManager.setNinjaAmount(6);
		game.setScreen(game.playingScreen());
	}
	
	private void startSeven() {
		StatusManager.setNinjaAmount(7);
		game.setScreen(game.playingScreen());
	}
	
	private void startEight() {
		StatusManager.setNinjaAmount(8);
		game.setScreen(game.playingScreen());
	}
	
	private void startNine() {
		StatusManager.setNinjaAmount(9);
		game.setScreen(game.playingScreen());
	}
	
	private void startTen() {
		StatusManager.setNinjaAmount(10);
		game.setScreen(game.playingScreen());
	}
	
	private void startEleven() {
		StatusManager.setNinjaAmount(11);
		game.setScreen(game.playingScreen());
	}	
	
	
	private void updateButtons(float delta) {
		touchPoint = screenToViewport(Gdx.input.getX(), Gdx.input.getY());
		boolean justTouched = Gdx.input.justTouched();
		boolean isTouched = Gdx.input.isTouched();
		boolean justReleased = wasTouched && !isTouched;
		wasTouched = isTouched;
		
		bThree.update(delta, justTouched, isTouched, justReleased, touchPoint.x, touchPoint.y);
		bFour.update(delta, justTouched, isTouched, justReleased, touchPoint.x, touchPoint.y);
		bFive.update(delta, justTouched, isTouched, justReleased, touchPoint.x, touchPoint.y);
		bSix.update(delta, justTouched, isTouched, justReleased, touchPoint.x, touchPoint.y);
		bSeven.update(delta, justTouched, isTouched, justReleased, touchPoint.x, touchPoint.y);
		bEight.update(delta, justTouched, isTouched, justReleased, touchPoint.x, touchPoint.y);
		bNine.update(delta, justTouched, isTouched, justReleased, touchPoint.x, touchPoint.y);
		bTen.update(delta, justTouched, isTouched, justReleased, touchPoint.x, touchPoint.y);
		bEleven.update(delta, justTouched, isTouched, justReleased, touchPoint.x, touchPoint.y);
	}
	
	/**
	 * This is called when we are drawing everything to the screen.
	 */
	private void drawButtons() {
		bThree.draw(spriteBatch);
		bFour.draw(spriteBatch);
		bFive.draw(spriteBatch);
		bSix.draw(spriteBatch);
		bSeven.draw(spriteBatch);
		bEight.draw(spriteBatch);
		bNine.draw(spriteBatch);
		bTen.draw(spriteBatch);
		bEleven.draw(spriteBatch);
	}

	private Vector3 screenToViewport(float x, float y) {
		adminCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		return touchPoint;
	}

}
