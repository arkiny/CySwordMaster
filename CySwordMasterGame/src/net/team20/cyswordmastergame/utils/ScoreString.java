package net.team20.cyswordmastergame.utils;

/** ScoreString class which will show up in the scorescreen
 * 
 * @author Arkiny
 * @author Phil
 */
public class ScoreString implements CharSequence {

	public static final int DEFAULT_CHARS = 6;

	private final char[] score;
	private final int start;

	/**
	 * initialize score string with default characters
	 */
	public ScoreString () {
		this(DEFAULT_CHARS);
	}

	/**
	 * make new score string with number of characters
	 * @param numChars
	 */
	public ScoreString (int numChars) {
		score = new char[numChars];
		int n = 1;
		for (int i = 0; i < numChars - 1; i++) {
			n *= 10;
		}
		start = n;
	}

	@Override
	public char charAt (int index) {
		return score[index];
	}

	@Override
	public int length () {
		return score.length;
	}

	@Override
	public CharSequence subSequence (int start, int end) {
		// Don't care. Call at your own risk.
		return null;
	}

	/** Sets this <code>ScoreString</code> to hold the given integer value.
	 * 
	 * @param v the value that the ScoreString will hold. */
	public void setScore (int v) {
		for (int n = start, i = 0; i < score.length; n /= 10, i++) {
			int j = (v / n) % 10;
			score[i] = (char)('0' + j);
		}
	}
}
