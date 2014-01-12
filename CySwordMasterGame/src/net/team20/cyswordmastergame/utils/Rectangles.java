package net.team20.cyswordmastergame.utils;

import com.badlogic.gdx.math.Rectangle;

import static net.team20.cyswordmastergame.utils.MathUtils.*;
/** helper function class of rectangles
 * 
 * @author Arkiny
 * 
 */
public class Rectangles {

        private Rectangles () {
        }

        /** set rectangle with its coordinates and size
         * 
         * @param r
         * @param x
         * @param y
         * @param w
         * @param h
         */
        public static void setRectangle(Rectangle r, float x, float y, float w, float h) {
                r.x = x;
                r.y = y;
                r.width = w;
                r.height = h;
        }

        /**Union the rectangles
         * 
         * @param a
         * @param b
         * @param result
         * @return
         */
        public static Rectangle union (Rectangle a, Rectangle b, Rectangle result) {
                result.x = min(a.x, b.x);
                result.y = min(a.y, b.y);
                result.width = max(a.x + a.width, b.x + b.width) - result.x;
                result.height = max(a.y + a.height, b.y + b.height) - result.y;
                return result;
        }
}