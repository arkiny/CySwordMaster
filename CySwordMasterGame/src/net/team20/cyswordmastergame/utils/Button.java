package net.team20.cyswordmastergame.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.team20.cyswordmastergame.Assets;

/**
 * Button class for menu
 * @author Arkiny
 * @author Phil
 */
public class Button {
	private HAlignment alignment;
    private String text;
    private boolean wasPressed;
    private float x;
    private float y;
    private float w;
    private float h;
    private boolean activated;
    private boolean down;
    private BitmapFont font;
    private float textHeight;

    /**
     * constructor which make a button
     * @param text
     * @param font
     */
    public Button (String text, BitmapFont font) {
            this.text = text;
            this.wasPressed = false;
            this.activated = false;
            this.down = false;
            this.font = font;
            TextBounds bounds = Assets.textFont.getBounds(text);
            textHeight = bounds.height;
            w = bounds.width * 2;
            h = bounds.height * 2;
            alignment = HAlignment.CENTER;
    }

    /**
     * set button's width
     * @param width
     */
    public void setWidth (float width) {
            w = width;
    }

    /**
     * set button's height
     * @param height
     */
    public void setHeight (float height) {
            h = height;
    }

    /**
     * set alignment
     * @param alignment
     */
    public void setAlignment (HAlignment alignment) {
            this.alignment = alignment;
    }

    /**
     * live update the button
     * @param delta
     * @param justTouched
     * @param isTouched
     * @param justReleased
     * @param x
     * @param y
     */
    public void update (float delta, boolean justTouched, boolean isTouched, boolean justReleased, float x, float y) {
            wasPressed = false;
            if (justTouched && inBounds(x, y)) {
                    activated = true;
                    down = true;
            } else if (isTouched) {
                    down = activated && inBounds(x, y);
            } else if (justReleased) {
                    wasPressed = activated && inBounds(x, y);
                    activated = false;
                    down = false;
            } else {
                    activated = false;
            }
    }

   
    private boolean inBounds (float x, float y) {
            return x >= this.x && x < this.x + this.w && y >= this.y && y < this.y + this.h;
    }

    /**
     * draw a button to game screen
     * @param spriteBatch
     */
    public void draw (SpriteBatch spriteBatch) {
            Color oldColor = font.getColor();
            if (down) {
                    spriteBatch.setColor(Color.RED);
            } else {
                    spriteBatch.setColor(Color.DARK_GRAY);
            }
            spriteBatch.draw(Assets.pureWhiteTextureRegion, x, y, w, h);
            spriteBatch.setColor(Color.WHITE);
            if (down) {
                    font.setColor(oldColor.r / 2, oldColor.g / 2, oldColor.b / 2, oldColor.a);
            }
            float textX = x;
            float textY = y + h;
            textY -= (h - textHeight) / 2;
            font.drawWrapped(spriteBatch, text, textX, textY, w, alignment);
            font.setColor(oldColor);
    }

    /**
     * 
     * @return boolean that the button is pressed
     */
    public boolean wasPressed () {
            return this.wasPressed;
    }

    /**
     * right on
     * @param right
     */
    
    public void rightOn (float right) {
            x = right - w;
    }

    /**
     * left on
     * @param left
     */
    public void leftOn (float left) {
            x = left;
    }

    /**
     * center
     * @param centerX
     */
    public void centerHorizontallyOn (float centerX) {
            x = centerX - w / 2;
    }

    /**
     * Bottom on
     * @param bottom
     */
    public void bottomOn (float bottom) {
            y = bottom;
    }

    /**
     * top on
     * @param top
     */
    public void topOn (float top) {
            y = top - h;
    }

    /**
     * center vertically on
     * @param centerY
     */
    public void centerVerticallyOn (float centerY) {
            y = centerY - h / 2;
    }
}
