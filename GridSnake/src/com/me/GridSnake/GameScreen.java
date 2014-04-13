package com.me.GridSnake;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;


public class GameScreen implements Screen {

	GridSnakeGame game; // Note it's "MyGame" not "Game"
	private FPSLogger fpsLogger;

    // constructor to keep a reference to the main Game class
     public GameScreen(GridSnakeGame game){
             this.game = game;
             fpsLogger = new FPSLogger();
             //create();
     }
	
     public enum Move {
 	    LEFT, RIGHT, UP, DOWN, NONE
 	}
 	
 	public class SnakeBlock extends Actor {
         Texture texture = new Texture(Gdx.files.internal("data/block.png"));
         Texture headTexture = new Texture(Gdx.files.internal("data/boxCoinAlt.png"));
         Texture tailTexture = new Texture(Gdx.files.internal("data/boxCoinAlt_disabled.png"));
         int indexX;
         int indexY;
         float blockWidth;
         float blockHeight;
         boolean isHead = false;
         boolean isTail = false;
         ArrayList<Move> moveList;
         Move previousMove = Move.NONE;

         public SnakeBlock(int x, int y){
             this.indexX = x;
             this.indexY = y;
             moveList = new ArrayList<Move>();
         }
         
         public SnakeBlock(int x, int y, float w, float h)
         {
         	this.indexX = x;
             this.indexY = y;
             this.blockWidth = w;
             this.blockHeight = h;
             setBounds(x*w, y*h, w, h);
             moveList = new ArrayList<Move>();
         }
         
         public SnakeBlock(SnakeBlock head, Move[] offsetMoves)
         {
         	this.indexX = head.indexX;
             this.indexY = head.indexY;
             this.blockWidth = head.blockWidth;
             this.blockHeight = head.blockHeight;
             setBounds(indexX*blockWidth,indexY*blockHeight,blockWidth,blockHeight);
             moveList = new ArrayList<Move>();
             
             for (Move currentMove : offsetMoves)
             {
             	moveSnakeBlock(currentMove);
             	moveList.add(oppositeMove(currentMove));
             }
         }
         
         public SnakeBlock(SnakeBlock block)
         {
         	this.indexX = block.indexX;
             this.indexY = block.indexY;
             this.blockWidth = block.blockWidth;
             this.blockHeight = block.blockHeight;
             setBounds(indexX*blockWidth,indexY*blockHeight,blockWidth,blockHeight);
             moveList = new ArrayList<Move>();
             
             for (Move currentMove : block.moveList)
             	moveList.add(currentMove); 
         }
         
         public void changeHeadStatus(boolean b)
         {
         	this.isHead = b;
         }
         
         public void changeTailStatus(boolean b)
         {
         	this.isTail = b;
         }
         
         public Move oppositeMove(Move m)
         {
         	if(m==Move.LEFT)
         		return Move.RIGHT;
         	if(m==Move.RIGHT)
         		return Move.LEFT;
         	if(m==Move.UP)
         		return Move.DOWN;
         	if(m==Move.DOWN)
         		return Move.UP;
         	return Move.NONE;
         }
         
         public void addMove(Move m)
         {
         	moveList.add(m);
         }
         
         public void popFromMoveStack()
         {
         	Move newMove = moveList.remove(0);
         	moveSnakeBlock(newMove);
         }
         
         public void moveSnakeBlock(Move move)
         {
         	switch (move)
         	{
         	case LEFT:
         		this.indexX -= 1;
         		break;
         	case RIGHT:
         		this.indexX += 1;
         		break;
         	case UP:
         		this.indexY += 1;
         		break;
         	case DOWN:
         		this.indexY -= 1;
         		break;
 			default:
 				break;
         	}
         	this.previousMove = move;
         }
         
         @Override
         public void draw(SpriteBatch batch, float alpha){
         	batch.setColor(1.0f, 1.0f, 1.0f, alpha/255);
         	if (this.isHead)
         		batch.draw(headTexture,indexX*getWidth(),indexY*getHeight(),getWidth(), getHeight());
         	else if(this.isTail)
         		batch.draw(tailTexture,indexX*getWidth(),indexY*getHeight(),getWidth(), getHeight());        		
         	else
         		batch.draw(texture,indexX*getWidth(),indexY*getHeight(),getWidth(), getHeight());        		
         	batch.setColor(Color.WHITE);
             //System.out.println(indexX*getX()+" "+indexY*getY());
         }
         
     }
 	
 	public class Snake extends Actor
 	{
 		ArrayList<SnakeBlock> snakeBlockList;
 		Grid grid;
 		SnakeBlock snakeHead;
 		SnakeBlock snakeTail;
 		int currentX, currentY;
 		Move previousMove = Move.NONE;
 		
 		float maxLife = 3f;
 		float currentLife = 3f;
 		
 		boolean isAlive = true;
 		
 		public Snake(Grid g)
 		{
 			this.grid = g;
 			this.currentX = 0;
 			this.currentY = 0;
 			snakeHead = new SnakeBlock(0,0,BLOCK_WIDTH,BLOCK_HEIGHT);
 			snakeHead.changeHeadStatus(true);
 			snakeBlockList = new ArrayList<SnakeBlock>();
 			snakeBlockList.add(snakeHead);
 			
 			Move[] moveArray = new Move[]{Move.RIGHT};
 			SnakeBlock s1 = new SnakeBlock(snakeHead,moveArray);
 			moveArray = new Move[]{Move.RIGHT,Move.RIGHT};
 			SnakeBlock s2 = new SnakeBlock(snakeHead,moveArray);
 			snakeBlockList.add(s1);
 			snakeBlockList.add(s2);
 			snakeTail = s2;
 			snakeTail.changeTailStatus(true);
 		}
 		
 		public Snake(Grid g, int startX, int startY)
 		{
 			this.grid = g;
 			this.currentX = startX;
 			this.currentY = startY;
 			snakeHead = new SnakeBlock(startX,startY,BLOCK_WIDTH,BLOCK_HEIGHT);
 			snakeBlockList = new ArrayList<SnakeBlock>();
 			snakeBlockList.add(snakeHead);
 		}
 		
 		public Move getMoveType(int oldX, int oldY, int newX, int newY)
 		{
 			if(oldX > newX)
 				return Move.LEFT;
 			if(oldX < newX)
 				return Move.RIGHT;
 			if(oldY > newY)
 				return Move.DOWN;
 			if(oldY < newY)
 				return Move.UP;
 			return Move.NONE;
 		}
 		
 		public Boolean checkValidMove(int newX, int newY)
 		{
 			Move currentMove = getMoveType(currentX,currentY,newX,newY);
 			if(snakeHead.previousMove == Move.LEFT && currentMove==Move.RIGHT)
 				return false;
 			if(snakeHead.previousMove == Move.RIGHT && currentMove==Move.LEFT)
 				return false;
 			if(snakeHead.previousMove == Move.DOWN && currentMove==Move.UP)
 				return false;
 			if(snakeHead.previousMove == Move.UP && currentMove==Move.DOWN)
 				return false;
 			return true;
 		}
 		
 		public void checkMove(int newX, int newY)
 		{
 			if((newX != currentX || newY != currentY) && checkValidMove(newX,newY) && isAlive)
 			{
 				Move currentMove = getMoveType(currentX,currentY,newX,newY);
 				moveBlocks(currentMove);
 				//System.out.println(currentMove);
 				currentX = newX; currentY = newY;
 				currentLife = maxLife;
 			}
 		}
 		
 		public void checkKill()
 		{
 			boolean kill = false;
 			for(SnakeBlock currentSnakeBlock : snakeBlockList)
 			{
 				if(!currentSnakeBlock.equals(snakeHead) && !currentSnakeBlock.equals(snakeTail))
 					if(snakeHead.indexX == currentSnakeBlock.indexX && snakeHead.indexY == currentSnakeBlock.indexY)
 					{
 						kill = true;
 						break;
 					}
 			}
 			if (kill)
 			{
 				grid.stopGame();
 			}
 		}
 		
 		public void moveBlocks(Move move)
 		{
 			previousMove = move;
 			for(SnakeBlock currentSnakeBlock : snakeBlockList)
 			{
 				currentSnakeBlock.addMove(move);
 				currentSnakeBlock.popFromMoveStack();
 			}
 		}

 		public float getAlpha(float life)
 		{
 			return ((255/maxLife)*life);
 		}
 		
 		
 		@Override
 		public void act(float delta)
 		{
 			if(grid.isPlaying)
 			{
 				currentLife -= delta;
 				if (currentLife < 0)
 				{
 					currentLife = 0;
 					grid.stopGame();
 				}
 				checkMove(grid.getIndex_X(),grid.getIndex_Y());
 				checkKill();
 			}
 		}
 		
 		
 		@Override
 		public void draw(SpriteBatch batch, float alpha)
 		{
 			
 			// get life's corresponding alpha value
 			float newAlpha = getAlpha(currentLife);
 			for(SnakeBlock currentSnakeBlock : snakeBlockList)
 			{
 				currentSnakeBlock.draw(batch, newAlpha);
 			}
         }
 		
 		public void elongate()
 		{ 			
 			SnakeBlock lastBlock = snakeBlockList.get(snakeBlockList.size()-1);
 			SnakeBlock newBlock = new SnakeBlock(lastBlock);
 			newBlock.moveSnakeBlock(newBlock.oppositeMove(lastBlock.previousMove));
 			newBlock.moveList.add(0,lastBlock.previousMove);
 			snakeBlockList.add(newBlock);
 			lastBlock.changeTailStatus(false);
 			newBlock.changeTailStatus(true);
 			snakeTail = newBlock;
 		}
 		
 		public void reduceTail()
 		{
 			System.out.println("EAT TAIL");
 			snakeBlockList.remove(snakeBlockList.size()-1);
 			snakeTail = snakeBlockList.get(snakeBlockList.size()-1);
 			snakeTail.changeTailStatus(true);
 		}
 		
 		public void resetSnake()
 		{
 			this.currentX = 0;
 			this.currentY = 0;
 			snakeBlockList.clear();
 			snakeHead = new SnakeBlock(0,0,BLOCK_WIDTH,BLOCK_HEIGHT);
 			snakeHead.changeHeadStatus(true);
 			snakeBlockList.add(snakeHead);
 			
 			Move[] moveArray = new Move[]{Move.RIGHT};
 			SnakeBlock s1 = new SnakeBlock(snakeHead,moveArray);
 			moveArray = new Move[]{Move.RIGHT,Move.RIGHT};
 			SnakeBlock s2 = new SnakeBlock(snakeHead,moveArray);
 			snakeBlockList.add(s1);
 			snakeBlockList.add(s2);
 			snakeTail = s2;
 			snakeTail.changeTailStatus(true);
 			
 			currentLife = 3f;
 			System.out.println("jhk"+snakeHead.indexX);
 		}
 		
 	}
 	
 	public class Food extends Actor
 	{
 		Texture texture = new Texture(Gdx.files.internal("data/bonus.png"));
         int indexX;
         int indexY;
         float blockWidth;
         float blockHeight;

         public Food(int x, int y, float w, float h)
         {
         	this.indexX = x;
             this.indexY = y;
             this.blockWidth = w;
             this.blockHeight = h;
             setBounds(x*w, y*h, w, h);
         }

         @Override
         public void draw(SpriteBatch batch, float alpha){
             batch.draw(texture,indexX*getWidth(),indexY*getHeight(),getWidth(), getHeight());
             //System.out.println(indexX*getX()+" "+indexY*getY());
         }
         
 	}
 	
 	public class FoodSpawner extends Actor
 	{
 		ArrayList<Food> foodList;
 		ArrayList<SnakeBlock> snakeBlockList;
 		Grid grid;
 		Snake snake;
 		
 		public FoodSpawner(Grid g, Snake s)
 		{
 			this.grid = g;
 			this.snake = s;
 			foodList = new ArrayList<Food>();
 		}
 		
 		public void spawnFood()
 		{
 			snakeBlockList = snake.snakeBlockList;
 			Random rand = new Random();
 			
 			trialLoop:
 			while(true)
 			{
 				int newFoodX = rand.nextInt(grid.BLOCK_NUMBER_X);
 				int newFoodY = rand.nextInt(grid.BLOCK_NUMBER_Y);
 				boolean newFood = false;
 				
 				for (SnakeBlock currentSnakeBlock : snakeBlockList)
 				{
 					if(currentSnakeBlock.indexX == newFoodX && currentSnakeBlock.indexY == newFoodY)
 					{
 						continue trialLoop;
 					}
 				}
 				
 				foodList.add(new Food(newFoodX,newFoodY,BLOCK_WIDTH,BLOCK_HEIGHT));
 				break;
 			}
 		}
 		
 		public ArrayList<Food> getFoods()
 		{
 			return this.foodList;
 		}
 		
 		@Override
 		public void draw(SpriteBatch batch, float alpha)
 		{
 			for(Food f : foodList)
 			{
 				f.draw(batch, alpha);
 			}
         }
 		
 	}
 	
 	public class GridBox extends Actor
 	{
 		float x = 0;
 		float y = 0;
 		int index_x;
 		int index_y;
 		float width = 20;
 		float height = 20;
 		TextureRegion region;
 		Texture texture = new Texture(Gdx.files.internal("data/stoneWall.png"));
 		
 		public GridBox(int indexX, int indexY)
 		{
 			index_x = indexX;
 			index_y = indexY;
 			this.x = indexX*width;
 			this.y = indexY*height;
 			setBounds(this.x,this.y,this.width,this.height);
 			//region = new TextureRegion(this.width,this.height);
 		}
 		
 		public GridBox(int indexX, int indexY, float w, float h)
 		{
 			index_x = indexX;
 			index_y = indexY;
 			this.width = w;
 			this.height = h;
 			this.x = indexX*w;
 			this.y = indexY*h;
 			setBounds(this.x,this.y,this.width,this.height);
 			//region = new TextureRegion(this.width,this.height);
 		}
 		
 		
 		@Override
         public void draw(SpriteBatch batch, float alpha){
 			super.draw(batch, alpha);
 			//Color color = getColor();
 			//batch.setColor(56, 56, 56, 255);
             //batch.draw(region, getX(), getY(), getOriginX(), getOriginY(),
             //        getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
 			batch.draw(texture, this.x,this.y,this.width,this.height);
         }
 		
 		public int getIndex_x()
 		{
 			return this.index_x;
 		}
 		
 		public int getIndex_y()
 		{
 			return this.index_y;
 		}
 	}
 	
 	public class Grid extends Actor
 	{
 		int current_index_x;
 		int current_index_y;
 		float width = 20;
 		float height = 20;
 		int BLOCK_NUMBER_X;
 		int BLOCK_NUMBER_Y;
 		boolean isPlaying = false;
 		Texture texture = new Texture(Gdx.files.internal("data/stoneWall.png"));
 		//Sound eatSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bite.mp3"));
 		
 		Snake snake;
 		FoodSpawner foodSpawner;
 		GameUI gameUI;
 		Stage stage;
 		
 		int points = 0;
 		
 		public Grid(int x, int y, float w, float h)
 		{
 			BLOCK_NUMBER_X = x;
 			BLOCK_NUMBER_Y = y;
 			width = w;
 			height = h;
 			setBounds(0,0,this.width,this.height);
 			
 			addListener(new InputListener()
 			{
 	            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) 
 	            {
 	            	Grid target = (Grid)event.getTarget();
 	            	GridBox currentBox = target.getCurrentBox(x, y);
 	            	current_index_x = currentBox.index_x;
 	            	current_index_y = currentBox.index_y;
 	            	//System.out.println(currentBox.getIndex_x() + " " + currentBox.getIndex_y());

 	                target.tryToStartGame();
 	                
 	            	return true;
 	            }
 	            
 	            public void touchDragged(InputEvent event, float x, float y, int pointer)
 	            {
 	            	Grid target = (Grid)event.getTarget();
 	            	if (target.isPlaying)
 	            	{
 	            		GridBox currentBox = target.getCurrentBox(x, y);
 	            	}
 	            	//System.out.println(currentBox.getIndex_x() + " " + currentBox.getIndex_y());
 	            }
 	            
 	            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
 	            {
 	            	Grid target = (Grid)event.getTarget();
 	            	target.stopGame();
 	            }
 	            
 	        });
 		}
 		
 		
 		@Override
         public void draw(SpriteBatch batch, float alpha){
 			super.draw(batch, alpha);
 			for(int x=0; x< BLOCK_NUMBER_X; x++)
 	        {
 	        	for(int y=0; y< BLOCK_NUMBER_Y; y++)
 	        		batch.draw(texture, x*BLOCK_WIDTH, y*BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
 	        }
         }
 		
 		// this method changes the current_index_x/y already, no need to return anything actually
 		public GridBox getCurrentBox(float coordX, float coordY)
 		{
 			int index_x = (int)(coordX / BLOCK_WIDTH);
 			int index_y = (int)(coordY / BLOCK_HEIGHT);
 			
 			current_index_x = index_x;
 			current_index_y = index_y;
 			
 			return new GridBox(index_x,index_y);
 		}
 		
 		public int getIndex_X()
 		{
 			return current_index_x;
 		}
 		
 		public int getIndex_Y()
 		{
 			return current_index_y;
 		}
 		
 		@Override
 		public void act(float delta)
 		{
 			
 			if(this.isPlaying)
 			{
 				snake.checkMove(getIndex_X(),getIndex_Y());
 				checkEatTail();
 				checkEat();
 			}
 			
 			if(!snake.isAlive)
 				this.isPlaying = false;
             //checkDead();
             //Actor myhit = snakeHead.hit(snakeHead.indexX*snakeHead.blockWidth, snakeHead.indexY*snakeHead.blockHeight, true);
             //System.out.println(myhit);
 		}
 		
 		public void tryToStartGame()
 		{
 			if(current_index_x == snake.snakeHead.indexX && current_index_y == snake.snakeHead.indexY)
 			{
 				this.isPlaying = true;
 				snake.isAlive = true;
 				gameUI.start();
 				
 			}
 		}
 		
 		public void stopGame()
 		{
 			if(this.isPlaying == true)
 			{
 				this.isPlaying = false;
 				snake.isAlive = false;
 				//snake.resetSnake();
 				gameUI.stop();
 				points = 0;
 				showScore();

 			}
 		}
 		
 		public void checkEat()
 		{
 			SnakeBlock snakeHead = snake.snakeHead;
 			boolean eat = false;
 			/*for(Food f : foodSpawner.foodList)
 			{
 				if(f.indexX == snakeHead.indexX && f.indexY == snakeHead.indexY)
 				{
 					eat = true;
 					System.out.println("EAT");
 				}
 			}*/
 			
 			
 			for (Iterator<Food> it = foodSpawner.foodList.iterator(); it.hasNext(); ) 
 			{
 			    Food f = it.next();
 			    if(f.indexX == snakeHead.indexX && f.indexY == snakeHead.indexY)
 				{
 					eat = true;
 					it.remove();
 					System.out.println("EAT");
 				}
 			}
 			
 			if (eat)
 			{
 				snake.elongate();
 				foodSpawner.spawnFood();
 				points++;
 				gameUI.updateScore(points);
 				//eatSound.play();
 			}
 		}
 		
 		public void checkEatTail()
 		{
 			SnakeBlock snakeHead = snake.snakeHead;
 			SnakeBlock snakeTail = snake.snakeTail;
 			
 			System.out.println(snakeHead.indexX + " HEAD " + snakeHead.indexY);
 			System.out.println(snakeTail.indexX + " TAIL " + snakeTail.indexY);
 			
 			if(snakeHead.indexX == snakeTail.indexX && snakeHead.indexY == snakeTail.indexY)
 			{
 				snake.reduceTail();
 			}
 		}
 		
 		public void showScore()
 		{
	         Skin uiSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
		         
	         ScoreDialog scoreDialog = new ScoreDialog("Score", uiSkin, grid);
	         scoreDialog.setPosition((this.width/2)-(scoreDialog.getWidth()/2), 0);
	         MoveToAction move = new MoveToAction();
	         move.setPosition((this.width/2)-(scoreDialog.getWidth()/2), this.height/2);
	         move.setDuration(0.25f);
	         scoreDialog.addAction(move);
	         
	         stage.addActor(scoreDialog);
 		}
 		
 		public void setSnake(Snake s)
 		{
 			this.snake = s;
 		}
 		
 		public void setFoodSpawner(FoodSpawner f)
 		{
 			this.foodSpawner = f;
 		}
 		
 		public void setGameUI(GameUI ui)
 		{
 			this.gameUI = ui;
 		}
 		
 		public void setStage(Stage s)
 		{
 			this.stage = s;
 		}
 		
 		
 	}
 	
 	
 	private Stage stage;
     float STAGE_WIDTH;
     float STAGE_HEIGHT;
     float BLOCK_WIDTH;
     float BLOCK_HEIGHT;
     int BLOCK_NUMBER_X = 5;
     int BLOCK_NUMBER_Y = 5;
     Grid grid;
     GameUI gameUI;
     
     public void create() {   
    	 
    	 Gdx.graphics.setVSync(false);    
    	 
     	STAGE_WIDTH = Gdx.graphics.getWidth();
         STAGE_HEIGHT = Gdx.graphics.getHeight();
         
         if (STAGE_WIDTH < STAGE_HEIGHT)
         	STAGE_HEIGHT = STAGE_WIDTH;
         else
         	STAGE_WIDTH = STAGE_HEIGHT;
         
         BLOCK_WIDTH = STAGE_WIDTH / BLOCK_NUMBER_X;
         BLOCK_HEIGHT = STAGE_HEIGHT / BLOCK_NUMBER_Y;
     	
         Texture.setEnforcePotImages(false);
         
         System.out.println(BLOCK_WIDTH);
         
         stage = new Stage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
         Gdx.input.setInputProcessor(stage);
         
         grid = new Grid(BLOCK_NUMBER_X,BLOCK_NUMBER_Y,STAGE_WIDTH,STAGE_HEIGHT);
         grid.setTouchable(Touchable.enabled);
         stage.addActor(grid);
         
         Snake snake = new Snake(grid);
         FoodSpawner foodSpawner = new FoodSpawner(grid,snake);
         foodSpawner.spawnFood();
         
         gameUI = new GameUI(grid);
         gameUI.stop();
         
         grid.setSnake(snake);
         grid.setFoodSpawner(foodSpawner);
         grid.setGameUI(gameUI);
         grid.setStage(stage);
         
         stage.addActor(snake);
         stage.addActor(foodSpawner);
         stage.addActor(gameUI);
         
         //WindowStyle newStyle = new WindowStyle();
         //newStyle.titleFont = new BitmapFont();      
     }

     @Override
     public void dispose() {
     }

     @Override
     public void render(float delta) {    
         Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
         
      // output the current FPS
        fpsLogger.log();
         
         stage.act(Gdx.graphics.getDeltaTime());
         
         gameUI.debug(); // turn on all debug lines (table, cell, and widget)
         stage.draw();
         Table.drawDebug(stage);
         
         stage.draw();
     }

     @Override
     public void resize(int width, int height) {
     }

     @Override
     public void pause() {
     }

     @Override
     public void resume() {
     }

	@Override
	public void show() {
		// TODO Auto-generated method stub
		create();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

}
