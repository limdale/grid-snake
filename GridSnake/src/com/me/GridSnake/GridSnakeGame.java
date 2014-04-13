package com.me.GridSnake;

import com.badlogic.gdx.Game;

public class GridSnakeGame extends Game {

	MainMenuScreen mainMenuScreen;
	GameScreen gameScreen;
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		
		 mainMenuScreen = new MainMenuScreen(this);
		 gameScreen = new GameScreen(this);
         setScreen(mainMenuScreen);  
	}
	
	public void startGame()
	{
		setScreen(gameScreen);  
	}

}
