package de.flo.ballplatformer.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.flo.ballplatformer.PlatformerGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 900;
		config.height = 540;

		new LwjglApplication(new PlatformerGame(), config);
	}
}
