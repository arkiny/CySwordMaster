package net.team20.cyswordmastergame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class CySwordMasterGameDesktop {
	public static void main(String[] args) {
		new LwjglApplication(new CySwordMasterGame(false), "CySwordMaster", 800, 480, false);
	}
}


/*
import net.team20.swordmaster.Swordmaster;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class DesktopStarter {

	private final static int WINDOW_WIDTH = 800;
	private final static int WINDOW_HEIGHT = 480;

	public static void main (String[] args) {
		new LwjglApplication(new Swordmaster(), "Swordmaster", WINDOW_WIDTH, WINDOW_HEIGHT, false);
	}
}
*/