package com.me.GridSnake;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "GridSnake";
		cfg.useGL20 = false;
		//cfg.width = 240;
		//cfg.height = 320;
		//cfg.width = 320;
		//cfg.height = 480;
		cfg.width = 480;
		cfg.height = 640;
		//cfg.width = 640;
		//cfg.height = 800;
		
		new LwjglApplication(new GridSnakeGame(), cfg);
	}
}
