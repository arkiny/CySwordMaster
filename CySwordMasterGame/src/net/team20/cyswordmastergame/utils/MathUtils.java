package net.team20.cyswordmastergame.utils;
/** simple math utils for the game functions
 * 
 * @author Arkiny
 *
 */
public class MathUtils {
	/**
	 * return absolute of n
	 * @param n
	 * @return
	 */
    public static float abs (float n) {
            return (n >= 0.0f) ? n : -n;
    }

    /**
     * return sign of n
     * @param n
     * @return
     */
    public static float sgn (float n) {
            if (n > 0.0f)
                    return 1.0f;
            else if (n < 0.0f)
                    return -1.0f;
            else
                    return 0.0f;
    }

    /** return minimum of input float numbers
     * 
     * @param a
     * @param b
     * @return
     */
    public static float min (float a, float b) {
            return (a < b) ? a : b;
    }

    /** return maximum of input float numbers
     * 
     * @param a
     * @param b
     * @return
     */
    public static float max (float a, float b) {
            return (a > b) ? a : b;
    }

    /** return minimum of input integers
     * 
     * @param a
     * @param b
     * @return
     */
    public static int min (int a, int b) {
            return (a < b) ? a : b;
    }

    /** return maximum of input integers
     * 
     * @param a
     * @param b
     * @return
     */
    public static int max (int a, int b) {
            return (a > b) ? a : b;
    }
}
