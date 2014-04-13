package com.me.GridSnake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.me.GridSnake.GameScreen.Grid;
import com.me.GridSnake.GameScreen.GridBox;

public class MainMenuScreen implements Screen {

	
	GridSnakeGame game;
	private Stage stage;
	
	private float BUTTON_WIDTH = 300f;
    private final float BUTTON_HEIGHT = 60f;
    private final float BUTTON_SPACING = 10f;

    // constructor to keep a reference to the main Game class
     public MainMenuScreen(GridSnakeGame game){
             this.game = game;
             //create();
     }

     public void create () {
    	 
    	 
     }

     
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Table.drawDebug(stage);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        
        
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		Texture.setEnforcePotImages(false);
		 
		
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        

        
        
        /// BUTTON
        
        Texture upTexture =  new Texture(Gdx.files.internal("data/grass_platform.png"));
        Texture downTexture =  new Texture(Gdx.files.internal("data/block.png"));

        TextureRegion upRegion = new TextureRegion(upTexture);
        TextureRegion downRegion = new TextureRegion(downTexture);
        BitmapFont buttonFont = new BitmapFont(Gdx.files.internal("data/digiffiti.fnt"),
        		Gdx.files.internal("data/digiffiti.png"),false);
        TextButtonStyle style = new TextButtonStyle();
        style.up = new TextureRegionDrawable(upRegion);
        style.down = new TextureRegionDrawable(downRegion);
        style.font = buttonFont;
        
        TextButton button1 = new TextButton("PLAY",style);
        button1.setWidth(BUTTON_WIDTH);
        button1.setHeight(BUTTON_HEIGHT);
        
        
        button1.addListener( new ClickListener()
        {
        	public void clicked(InputEvent event,float x,float y)
        	{
        		game.startGame();
        	}
        });

        
        LabelStyle labelStyle = new LabelStyle(buttonFont, Color.WHITE);
        
        //Label title = new Label( "GridSnake",labelStyle );
        Label title = new Label( "GRIDSNAKE",labelStyle );
        title.setFontScale(2f);
        
        Label subtitle = new Label("Ancient Mystic Wonder Games", labelStyle);
        
        
        
        table.add(title);
        table.row();
        table.add(subtitle);
        table.row();
        table.add(button1);
        stage.addActor(table);
        
        // Add widgets to the table here.
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
	}

}
