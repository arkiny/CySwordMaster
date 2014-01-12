package net.team20.cyswordmastergame.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Config class which for game configuration
 *
 */
public class Config {

	private static final String PROPERTIES_FILE = "data/swordmaster.properties";	
	private static Properties properties;

	private static Properties instance () {
		if (null == properties) {
			properties = new Properties();			
			FileHandle fh = Gdx.files.internal(PROPERTIES_FILE);
			InputStream inStream = fh.read();
			try {
				properties.load(inStream);
				inStream.close();
			} catch (IOException e) {
				if (inStream != null) {
					try {
						inStream.close();
					} catch (IOException ex) {
					}
				}
			}
		}
		return properties;
	}

	/**
	 * Get instance as integer
	 * @param name
	 * @param fallback
	 * @return
	 */
	public static int asInt (String name, int fallback) {
		String v = instance().getProperty(name);
		if (v == null) return fallback;
		return Integer.parseInt(v);
	}

	/**
	 * Get instance as float number
	 * @param name
	 * @param fallback
	 * @return
	 */
	public static float asFloat (String name, float fallback) {
		String v = instance().getProperty(name);
		if (v == null) return fallback;
		return Float.parseFloat(v);
	}

	/**
	 * Get instance as String
	 * @param name
	 * @param fallback
	 * @return
	 */
	public static String asString (String name, String fallback) {
		String v = instance().getProperty(name);
		if (v == null) return fallback;
		return v;
	}
}
