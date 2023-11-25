package com.mygdx.shawnskates;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.helper.Constants;
import com.mygdx.shawnskates.Launcher;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(Constants.FOREGROUND_FPS);
		config.setTitle(Constants.TITLE);
		config.setWindowedMode(Constants.WIDTH, Constants.HEIGHT);
		new Lwjgl3Application(new Launcher(), config);
	}
}
