package net.team20.cyswordmastergame;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

/**
 * 
 * @author Sam Reeves
 *
 * A lot of this code was taken from the tutorial: http://obviam.net/index.php/getting-started-in-android-game-development-with-libgdx-create-a-working-prototype-in-a-day-tutorial-part-1/
 * This, however, will ultimately get updated to suit our specfic gaming needs. We just used the tutorial to get a base start to putting libgdx programming into Android.
 */
public class CySwordMasterGameActivity extends AndroidApplication {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		config.useWakelock = true;
		config.useGL20 = true;
		initialize(new CySwordMasterGame(), config);
	}
	
}
