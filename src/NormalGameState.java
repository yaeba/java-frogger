/**
 * Project for SWEN20003: Object Oriented Software Development 2018
 * by Xuanken Tay, University of Melbourne
 */


import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Graphics;
import java.util.ArrayList;
import java.util.Random;

/**
 * Normal game mode for the game (Proj 2).
 * Reads level information from csv, handles initialisation, 
 * input and rendering.
 */
public class NormalGameState extends State {
	/** ID given to this state */
	public static final int ID = 1;
	/** player starting x position */
	public static final float PLAYER_X = 512;
	/** player starting y position */
	public static final float PLAYER_Y = 720;
	/** number of levels in this game state */
	public static final int MAX_LEVELS = 2;
	/** lower bound seconds extralife appears */
	public static final int EXTRALIFE_MIN = 25;
	/** upper bound seconds extralife appears */
	public static final int EXTRALIFE_MAX = 35;
	
	
	/** The game world */
	private World world;
	
	/** Singleton player */
	private Player player;
	
	/** Current level */
	private int level = 0;
	
	/** Time extralife appears */
	private float extraLifeStart;
	
	/** Status to set extralife */
	private boolean toSetExtraLife = false;

	
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) 
			throws SlickException {
		
		// get singleton player
		player = setupPlayer(PLAYER_X, PLAYER_Y);
		world = createLevel();
	}

	

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) 
			throws SlickException {
		
		// handle creation and destruction of extra life
		handleExtraLife();
		
		// update the world and all sprites
        world.update(gc, delta);
        

        // check if level complete or game over
        if (world.isLevelCompleted()) {
        	this.level++;
        	if (this.level == MAX_LEVELS) {
        		enterEndless(gc, sbg);;
        	}
        	else {
        		// create a World of new level
        		init(gc, sbg);
        	}
        } else if (world.isGameOver()) {
        	enterGameOver(gc, sbg, ++this.level);
        }
        
        // update the time
        super.update(gc, sbg, delta);
	}
	
	

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
			throws SlickException {
		// render world and level string
		world.render(g);
		renderString("Level: "+(level+1)+" (Proj2)", LEVEL_X, LEVEL_Y, 
				Color.orange);
		// draw time
		super.render(gc, sbg, g);
	}



	@Override
	public int getID() {
		return ID;
	}
	
	
	/** Get current level.
	 * @return int Current level.
	 */
	public int getLevel() {
		return level;
	}
	


	private World createLevel() {
		// create a world containing player, all sprites and goals
		try {
			// read in sprites from csv and find goals
			ArrayList<Sprite> sprites = readCsv(Integer.toString(level));
			Goal[] goals = findGoals(sprites);
			
			setExtraLifeStart();
			return new World(this.player, sprites, goals);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	private void handleExtraLife() {
		if (getTime() > extraLifeStart && toSetExtraLife) {
			// time to create an extralife
			world.createExtraLife();
			toSetExtraLife = false;
		} else if (!toSetExtraLife && !world.hasExtraLife()) {
			// extra life has disappeared
			setExtraLifeStart();
		}
	}
	
	
	private void setExtraLifeStart() {
		// set the time for extralife to appear
		int bound = EXTRALIFE_MAX - EXTRALIFE_MIN + 1;
		extraLifeStart = new Random().nextInt(bound)+EXTRALIFE_MIN+getTime();
		toSetExtraLife = true;
	}
	

	private Goal[] findGoals(ArrayList<Sprite> sprites) throws SlickException {
		// identify goals (as horizontal spaces between tree sprites)
		ArrayList<Goal> goals = new ArrayList<Goal>();
		float prevX, prevY;
		prevX = prevY = 0;
		
		for (Sprite sprite : sprites) {
			// for tree tiles
			if (sprite instanceof Tile && sprite.hasTag(Sprite.SOLID)) {
				float diffX = sprite.getX() - prevX;
				if (diffX > App.TILE_SIZE && prevY == sprite.getY()) {
					// found a spot for goal
					Goal goal = Goal.createGoal(prevX + diffX/2, prevY);
					goals.add(goal);
				}
				prevY = sprite.getY();
				prevX = sprite.getX();
			}
		}
		return goals.toArray(new Goal[0]);
	}
}
