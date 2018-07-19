package com.solar.simulation.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.solar.simulation.View;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Solar System Simulation By Joshua Hernandez";
		config.width = 960;
		config.height = 540;
		new LwjglApplication(new View(), config);
	}
}
