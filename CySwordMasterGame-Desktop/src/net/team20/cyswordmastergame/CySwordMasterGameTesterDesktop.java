package net.team20.cyswordmastergame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class CySwordMasterGameTesterDesktop {
	public static void main(String[] args) {
		new LwjglApplication(new CySwordMasterGame(true), "CySwordMaster", 800, 480, false);
	}
}