package fr.leroideskiwis.ktp.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import fr.leroideskiwis.ktp.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();

		cfg.title = "KillThePlayer (created by LeRoiDesKiwis)";
		cfg.useGL30 = false;
		cfg.width = 1050;
		cfg.height = 601;
		cfg.resizable = false;
		
		new LwjglApplication(new Main(), cfg);
	}
}
