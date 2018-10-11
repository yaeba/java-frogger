/**
 * Project for SWEN20003: Object Oriented Software Development 2018
 * by Xuanken Tay, University of Melbourne
 */

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;


/**
 * Endless game mode for the game (Extension).
 * Creates endless world for gameplay. First few levels are sprites of 
 * same type and the rest are randomly generated.
 */
public class EndlessGameState extends State {
	/** The ID given to this state */
	public static final int ID = 2;
	/** Lower and upper bounds of world's y */
	public static final float[] WORLD_Y = {96, 672};
	/** player starting x position */
	public static final float PLAYER_X = 504;
	/** player starting y position */
	public static final float PLAYER_Y = 720;
	/** x position of goal */
	public static final float GOAL_X = 504;
	/** y position of goal */
	public static final float GOAL_Y = 48;
	/** start of random level */
	public static final int RANDOM_START = 4;
	/** bonus level of extra lives */
	public static final int BONUS_LEVEL = 3;
	/** number of bonus extra lives */
	public static final int BONUS_LIVES = 15;
	/** comment to display in bonux level */
	public static final String BONUS_COMMENT = "YOU'LL NEED IT";
	/** comment's x position */
	public static final float COMMENT_X = 400;
	/** comment's y position */
	public static final float COMMENT_Y = 0;
	
	
	/** The game world */
	private World world;
	
	/** Singleton player */
	private Player player;
	
	/** A single goal in endless mode */
	private Goal goal;
	
	/** Current level */
	private int level = 0;
	
	/** Comment to display for every level */
	private String comment;
	
	/** The level that is bonus level */
	private int bonusLevel = BONUS_LEVEL;
	
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) 
			throws SlickException {
		// initialise
		player = setupPlayer(PLAYER_X, PLAYER_Y);
		goal = Goal.createGoal(GOAL_X, GOAL_Y);
		comment = readComment();
		world = initialiseWorld();
	}

	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) 
			throws SlickException {
		// update world and time
		world.update(gc, delta);
		super.update(gc, sbg, delta);
		
		// special key to randomise the current level
		if (gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
			shuffleWorld();
		}
		
		// check if level complete or game over
        if (world.isLevelCompleted()) {
        	this.level++;
        	init(gc, sbg);
        } else if (world.isGameOver()) {
        	enterGameOver(gc, sbg, ++this.level);
        }
	}
	
	

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
			throws SlickException {
		// render world and strings
		world.render(g);
		renderString("Level: "+(level+1)+" (Endless)", LEVEL_X, LEVEL_Y, 
						Color.orange);
		renderString(comment, COMMENT_X, COMMENT_Y, Color.cyan);
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
	
	
	private World initialiseWorld() {
		// create world in endless game mode
		if (level == bonusLevel) {
			world = createBonusWorld();
		} else {
			world = createRandomWorld();
		}
		return world;
	}
	
	private World createRandomWorld() {
		// create a random world
		ArrayList<Sprite> sprites = createSprites();
		World world = null;
		try {
			world = new World(player, sprites, new Goal[] {goal});
		} catch (SlickException e) {
			e.printStackTrace();
		}
		return world;
	}

	private ArrayList<Sprite> createSprites() {
		// create sprites required for random world
		ArrayList<Sprite> sprites = new ArrayList<>();
		
		for (float y=WORLD_Y[0]; y<=WORLD_Y[1]; y+=App.TILE_SIZE) {
			sprites.addAll(createRandomLane(y));
		}
		sprites.addAll(readCsv("endless"));
		return sprites;
	}
	
	private ArrayList<Sprite> createRandomLane(float y) {
		// draw a random lane
		RandomLane randomLane;
		if (level < RANDOM_START) {
			randomLane = new RandomLane(level, y);
		} else {
			randomLane = new RandomLane(y);
		}
		return randomLane.getSprites();
	}
	
	
	private World createBonusWorld() {
		// method to create bonus level of extra lives
		ArrayList<Sprite> sprites = new ArrayList<>();
		for (float y=WORLD_Y[0]; y<=WORLD_Y[1]; y+=App.TILE_SIZE) {
			for (float x=0; x<=App.SCREEN_WIDTH; x+=App.TILE_SIZE) {
				sprites.add(Tile.createWaterTile(x, y));
			}
		}
		sprites.addAll(readCsv("endless"));
		//create some logs for fun
		sprites.addAll(readCsv("bonus"));
		try {
			world = new World(player, sprites, new Goal[] {goal});
		} catch (SlickException e) {
			e.printStackTrace();
		}
		level--;
		bonusLevel *= bonusLevel;
		// create all the extra lives
		for (int i=0; i<BONUS_LIVES; i++) {
			world.createExtraLife();
		}
		return world;
	}
	
	private void shuffleWorld() {
		// randomise current level
		world = initialiseWorld();
	}
	
	private String readComment() {
		// read comments from csv
		if (level == bonusLevel) {
			return BONUS_COMMENT;
		} else if (level < RANDOM_START) {
			return readFromFile("assets/levels/endless.txt", level);
		} else {
			return readFromFile("assets/levels/endless.txt", RANDOM_START);
		}
	}
	
	private String readFromFile(String infile, int index) {
		// read (index)th String from a file
		String line = null;
		try (BufferedReader br =
				new BufferedReader(new FileReader(infile))) {
			for (int i=0; i<index; i++) {
				br.readLine();
			}
			line = br.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return line;
	}
}
