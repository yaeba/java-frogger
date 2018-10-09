/**
 * Project for SWEN20003: Object Oriented Software Development 2018
 * by Xuanken Tay, University of Melbourne
 */


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Graphics;
import java.util.ArrayList;
import java.util.Random;

/**
 * Normal Game State for the game (Proj 2).
 * Reads level information from csv, handles initialisation, 
 * input and rendering.
 */
public class NormalGameState extends State {
	/** The ID given to this state */
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
	
	/** The player */
	private Player player;
	
	/** Current level */
	private int level = 0;
	
	private float timePassed = 0;
	
	private float extraLifeStart;
	private boolean toSetExtraLife = false;

	
	
	/** Initialise the game.
	 * @param gc The Slick game container object.
	 * @param sbg The game holding this state.
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
		// create player and world
		player = setupPlayer(PLAYER_X, PLAYER_Y);
		world = createLevel();
		world.setExtraLife(true);
	}

	
	/** Update the game for a frame.
     * @param gc The Slick game container object.
	 * @param sbg The game holding this state.
     * @param delta Time passed since last frame (milliseconds).
     */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) 
			throws SlickException {
		
		timePassed += delta * App.MILLISECOND;
		handleExtraLife();
		
		// update the world
        world.update(gc, delta);
        

        // check if level complete or game over
        if (world.isLevelCompleted()) {
        	this.level++;
        	if (this.level == MAX_LEVELS) {
        		enterEndless(gc, sbg);;
        	}
        	else {
        		// create a World of new level
        		this.world = createLevel();
        		this.world.setExtraLife(true);
        	}
        } else if (world.isGameOver()) {
        	enterGameOver(gc, sbg);
        }
        
        // update the time
        super.update(gc, sbg, delta);
	}
	
	
	/** Render the entire screen, so it reflects the current game state.
     * @param gc The Slick game container object.
	 * @param sbg The game holding this state.
     * @param g The Slick graphics object, used for drawing.
     */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
			throws SlickException {
		world.render(g);
		
		// draw time
		super.render(gc, sbg, g);
	}


	/** Get the ID of this state.
	 * @return int The game unique ID for this state.
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	
	/** Set lives of player (used for passing data across states.
	 * @param lives Remaining lives of player.
	 */
	public void setPlayerLives(int lives) {
		player.setLives(lives);
	}
	
	/** create a World that contains player, sprites and goals of current
	 * level.
	 */
	private World createLevel() {
		try {
			// read in sprites from csv and find goals
			ArrayList<Sprite> sprites = readCsv(Integer.toString(level));
			Goal[] goals = findGoals(sprites);
			
			World world = new World(this.player, sprites, goals);
			world.setExtraLife(true);
			setExtraLifeStart();
			timePassed = 0;
			return new World(this.player, sprites, goals);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void handleExtraLife() {
		if (timePassed > extraLifeStart && toSetExtraLife) {
			world.createExtraLife();
			toSetExtraLife = false;
		} else if (!toSetExtraLife && !world.hasExtraLife()) {
			// extra life has disappeared
			setExtraLifeStart();
		}
	}
	
	private void setExtraLifeStart() {
		int bound = EXTRALIFE_MAX - EXTRALIFE_MIN + 1;
		extraLifeStart = new Random().nextInt(bound) + EXTRALIFE_MIN + timePassed;
		toSetExtraLife = true;
	}
	
	/** identify goals (as horizontal spaces between tree sprites). */
	private Goal[] findGoals(ArrayList<Sprite> sprites) throws SlickException {
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
