package net.team20.cyswordmastergame.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public final class CameraHandler {
	private CameraHandler () {
    }

    /** Creates an orthographic camera where the "play area" has the given viewport size. The viewport will either be stretched to
     * fill the entire window, or it will be scaled to maintain the aspect ratio.
     * 
     * @param stretched true if the "play area" is stretched to fill the entire window.
     * @param virtualWidth the width of the game screen in virtual pixels.
     * @param virtualHeight the height of the game screen in virtual pixels.
     * @return the new camera. */
    public static OrthographicCamera createCamera (boolean stretched, float virtualWidth, float virtualHeight) {
            // Get the viewport size.
            Vector2 viewportSize = viewportSize(stretched, virtualWidth, virtualHeight);
            float viewportWidth = viewportSize.x;
            float viewportHeight = viewportSize.y;

            // Create the camera, placing the origin at the bottom left of the viewport.
            OrthographicCamera camera = new OrthographicCamera(viewportWidth, viewportHeight);
            float xOrg = (viewportWidth - virtualWidth) / 2;
            float yOrg = (viewportHeight - virtualHeight) / 2;
            camera.position.set(viewportWidth / 2 - xOrg, viewportHeight / 2 - yOrg, 0);
            camera.update();
            return camera;
    }

    /** Calculates the dimensions of the viewport required to support the given virtual coordinates.
     * 
     * @param stretched true if the viewport should be stretched to fill the entire window.
     * @param virtualWidth the width of the viewport in virtual units.
     * @param virtualHeight the height of the viewport in virtual units.
     * @return the viewport's dimensions. */
    public static Vector2 viewportSize (boolean stretched, float virtualWidth, float virtualHeight) {
            float viewportWidth;
            float viewportHeight;
            if (stretched) {
                    // Stretch the camera to fill the entire screen.
                    viewportWidth = virtualWidth;
                    viewportHeight = virtualHeight;
            } else {
                    // Maintain the aspect ratio by letterboxing.
                    float aspect = virtualWidth / virtualHeight;
                    float physicalWidth = Gdx.graphics.getWidth();
                    float physicalHeight = Gdx.graphics.getHeight();
                    if (physicalWidth / physicalHeight >= aspect) {
                            // Letterbox left and right.
                            viewportHeight = virtualHeight;
                            viewportWidth = viewportHeight * physicalWidth / physicalHeight;
                    } else {
                            // Letterbox above and below.
                            viewportWidth = virtualWidth;
                            viewportHeight = viewportWidth * physicalHeight / physicalWidth;
                    }
            }
            return new Vector2(viewportWidth, viewportHeight);
    }

    public static enum ViewportMode {
            PIXEL_PERFECT, STRETCH_TO_ASPECT, STRETCH_TO_SCREEN
    }

    /**
     * Creates an orthographic camera where the "play area" has the given viewport mode with the density
     * @param viewportMode
     * @param virtualWidth
     * @param virtualHeight
     * @param density
     * @return
     */
    public static OrthographicCamera createCamera2 (ViewportMode viewportMode, float virtualWidth, float virtualHeight,
            float density) {
            // Get the viewport size.
    	 	viewportMode = ViewportMode.STRETCH_TO_SCREEN;
            Vector2 viewportSize = sizeToDensity(viewportMode, virtualWidth, virtualHeight, density);
            float viewportWidth = viewportSize.x;
            float viewportHeight = viewportSize.y;
            
   
            // Create the camera, placing the origin at the bottom left of the viewport.
            OrthographicCamera camera = new OrthographicCamera(viewportWidth, viewportHeight);
            float xOrg = (viewportWidth - virtualWidth) / 2;
            float yOrg = (viewportHeight - virtualHeight) / 2;
            camera.position.set(viewportWidth / 2 - xOrg, viewportHeight / 2 - yOrg, 0);
            camera.update();
            return camera;
    }

    /**
     * Creates an orthographic camera where the "play area" has the given viewport mode but size to density.
     * @param viewportMode
     * @param virtualWidth
     * @param virtualHeight
     * @param density
     * @return
     */
    public static Vector2 sizeToDensity (ViewportMode viewportMode, float virtualWidth, float virtualHeight, float density) {
            float viewportWidth = virtualWidth;
            float viewportHeight = virtualHeight;
            float physicalWidth = Gdx.graphics.getWidth();
            float physicalHeight = Gdx.graphics.getHeight();
            if (viewportMode == ViewportMode.PIXEL_PERFECT) {
                    float widthAtDensity = viewportWidth * density;
                    float heightAtDensity = viewportHeight * density;
                    viewportWidth *= (physicalWidth / widthAtDensity);
                    viewportHeight *= (physicalHeight / heightAtDensity);
            } else if (viewportMode == ViewportMode.STRETCH_TO_ASPECT) {
                    float aspect = virtualWidth / virtualHeight;
                    if (physicalWidth / physicalHeight >= aspect) {
                            // Letterbox left and right.
                            viewportHeight = virtualHeight;
                            viewportWidth = viewportHeight * physicalWidth / physicalHeight;
                    } else {
                            // Letterbox above and below.
                            viewportWidth = virtualWidth;
                            viewportHeight = viewportWidth * physicalHeight / physicalWidth;
                    }
            }
            return new Vector2(viewportWidth, viewportHeight);
    }

    /**
     * get the best densities from virtual resolutions
     * @param virtualWidth
     * @param virtualHeight
     * @param densities
     * @return
     */
    public static float bestDensity (float virtualWidth, float virtualHeight, float[] densities) {
            // Spell out that densities is assumed to be in ascending order.
            float bestDensity = densities[0];
            float physicalWidth = Gdx.graphics.getWidth();
            float physicalHeight = Gdx.graphics.getHeight();
            for (int i = 1; i < densities.length; i++) {
                    float density = densities[i];
                    float widthAtDensity = density * virtualWidth;
                    float heightAtDensity = density * virtualHeight;
                    if (widthAtDensity > physicalWidth || heightAtDensity > physicalHeight) {
                            break;
                    }
                    bestDensity = density;
            }
            return bestDensity;
    }
}
