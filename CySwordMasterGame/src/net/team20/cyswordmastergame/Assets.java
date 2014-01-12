package net.team20.cyswordmastergame;

import net.team20.cyswordmastergame.manager.Config;
import net.team20.cyswordmastergame.utils.CameraHandler;
import net.team20.cyswordmastergame.utils.CollisionGeometry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;

import static net.team20.cyswordmastergame.utils.Rectangles.*;

/**
 * Asset class which control all images, sounds & other resources
 *
 */
public class Assets {
    private static final float PLAYER_BORDER_WIDTH = Config.asFloat("Player.borderWidthPercent", 25.0f);
    private static final float PLAYER_BORDER_HEIGHT = Config.asFloat("Player.borderHeightPercent", 6.7f);
    private static final float PLAYER_FRAME_DURATION = Config.asFloat("Player.frameDuration", 0.2f);

    private static final float NINJA_FRAME_DURATION = Config.asFloat("Ninja.frameDuration", 0.1f);

    private static TextureAtlas atlas;

    public static final float VIRTUAL_WIDTH = 30.0f;
    public static final float VIRTUAL_HEIGHT = 20.0f;

    public static TextureRegion pureWhiteTextureRegion;
    public static TextureRegion playerWalkingRight1;
    public static TextureRegion playerWalkingRight2;
    public static TextureRegion playerWalkingLeft1;
    public static TextureRegion playerWalkingLeft2;
    public static TextureRegion ninjaLeft1;
    public static TextureRegion ninjaLeft2;

    public static TextureRegion ninjaRight1;
    public static TextureRegion ninjaRight2;

    public static TextureRegion playerShotLeft1;
    public static TextureRegion playerShotRight1;

    public static TextureRegion pauseButton;

    public static Animation playerWalkingRightAnimation;
    public static Animation playerWalkingLeftAnimation;

    public static Animation ninjaWalkingLeftAnimation;
    public static Animation ninjaWalkingRightAnimation;
    public static Animation ninjaScanningAnimation;

    public static Animation nemesisAnimation;

    public static BitmapFont scoreFont;
    public static BitmapFont textFont;
    public static BitmapFont flyupFont;

    public static float playerWidth;
    public static float playerHeight;
    public static float ninjaWidth;
    public static float ninjaHeight;
    public static float playerShotWidth;
    public static float playerShotHeight;
    
    public static CollisionGeometry playerGeometry;

    public static float pixelDensity;

    /**
     * Load assets with configured properties.
     */
    public static void load () {
            pixelDensity = calculatePixelDensity();
            if (System.getProperty("eclipse") != null){
            	String textureDir = "data/textures/" + (int)pixelDensity;
                String textureFile = textureDir + "/pack";
                atlas = new TextureAtlas(Gdx.files.internal(textureFile), Gdx.files.internal(textureDir));
            }
            else{            
            	String textureDir = "data/textures/" + (int)pixelDensity;
                String textureFile = textureDir + "/pack";
                atlas = new TextureAtlas(Gdx.files.internal(textureFile), Gdx.files.internal(textureDir));
            }            
            loadTextures();
            createAnimations();
            loadFonts();
            initialiseGeometries();
    }

    /**
     * Load textures from asset list
     */
    private static void loadTextures () {
            pureWhiteTextureRegion = atlas.findRegion("8x8");
            playerWalkingRight1 = atlas.findRegion("playerRight1");
            playerWalkingRight2 = atlas.findRegion("playerRight2");
            playerWalkingLeft1 = atlas.findRegion("playerLeft1");
            playerWalkingLeft2 = atlas.findRegion("playerLeft2");
            ninjaLeft1 = atlas.findRegion("ninjaLeft1");
            ninjaLeft2 = atlas.findRegion("ninjaLeft2");
            ninjaRight1 = atlas.findRegion("ninjaRight1");
            ninjaRight2 = atlas.findRegion("ninjaRight2");
            playerShotLeft1 = atlas.findRegion("PlayerShotLeft01");
            playerShotRight1 = atlas.findRegion("PlayerShotRight01");
            pauseButton = atlas.findRegion("pause");
    }

    /**
     * get best density and return it with float numbers
     * @return calculated best density
     */
    private static float calculatePixelDensity () {
            FileHandle textureDir = Gdx.files.internal("data/textures");
            FileHandle[] availableDensities = textureDir.list();
            FloatArray densities = new FloatArray();
            for (int i = 0; i < availableDensities.length; i++) {
                    try {
                            float density = Float.parseFloat(availableDensities[i].name());
                            densities.add(density);
                    } catch (NumberFormatException ex) {
                            // Ignore anything non-numeric, such as ".svn" folders.
                    }
            }
            densities.shrink(); // Remove empty slots to get rid of zeroes.
            densities.sort(); // Now the lowest density comes first.
            return CameraHandler.bestDensity(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, densities.items);
    }

    /**
     * Create sprite animation from textures
     */
    private static void createAnimations () {
            playerWalkingRightAnimation = new Animation(PLAYER_FRAME_DURATION, Assets.playerWalkingRight1, Assets.playerWalkingRight2);
            playerWalkingLeftAnimation = new Animation(PLAYER_FRAME_DURATION, Assets.playerWalkingLeft1, Assets.playerWalkingLeft2);

            ninjaWalkingLeftAnimation = new Animation(NINJA_FRAME_DURATION, ninjaLeft1, ninjaLeft2);
            ninjaWalkingRightAnimation = new Animation(NINJA_FRAME_DURATION, ninjaRight1, ninjaRight2);
    }
    
    /**
     * load fonts from asset list 
     */
    private static void loadFonts () {
            
            FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("data/fonts/amiga4everpro2.ttf"));
            
            scoreFont = fontGenerator.generateFont(15);
            textFont = fontGenerator.generateFont(15);
            flyupFont = fontGenerator.generateFont(15);
            
            fontGenerator.dispose();           
            
            scoreFont.setScale(1.0f / pixelDensity);
            textFont.setScale(1.0f / pixelDensity);
            flyupFont.setScale(1.0f / pixelDensity);
    }
    
    /**
     * initialize geometries with sprite texture size
     */
    private static void initialiseGeometries () {
            playerWidth = toWidth(playerWalkingRight1);
            playerHeight = toHeight(playerWalkingRight1);
            ninjaWidth = toWidth(ninjaRight1);
            ninjaHeight = toHeight(ninjaRight1);
            
            playerShotWidth = toWidth(playerShotLeft1);
            playerShotHeight = toHeight(playerShotLeft1);

            Array<Rectangle> playerRectangles = new Array<Rectangle>();
            Rectangle r = new Rectangle();
            float x = (playerWidth * PLAYER_BORDER_WIDTH / 100.0f) / 2.0f;
            float y = (playerHeight * PLAYER_BORDER_HEIGHT / 100.0f) / 2.0f;
            float w = playerWidth - 2 * x;
            float h = playerHeight - 2 * y;
            setRectangle(r, x, y, w, h);
            playerRectangles.add(r);
            playerGeometry = new CollisionGeometry(playerRectangles);
    }

    /**
     * adjust width with pixeldensity
     * @param region
     * @return float number of width
     */
    private static float toWidth (TextureRegion region) {
            return region.getRegionWidth() / pixelDensity;
    }

    /**
     * adjust height with pexel density
     * @param region
     * @return float number of height
     */
    private static float toHeight (TextureRegion region) {
            return region.getRegionHeight() / pixelDensity;
    }
}