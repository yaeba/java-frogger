/**
 * Project for SWEN20003: Object Oriented Software Development 2018
 * by Xuanken Tay, University of Melbourne
 */

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


import java.util.ArrayList;

public class EndlessGameState extends State {
	/** The ID given to this state */
	public static final int ID = 2;
	/** Lower and upper bounds of world */
	public static final float[] WORLD_Y = {96, 672};
	/** player starting x position */
	public static final float PLAYER_X = 504;
	/** player starting y position */
	public static final float PLAYER_Y = 720;
	/** Goal x */
	public static final float GOAL_X = 504;
	/** GOal y */
	public static final float GOAL_Y = 48;
	
	private World world;
	private Player player;
	private Goal goal;
	private int level = 0;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) 
			throws SlickException {
		this.player = Player.createPlayer(PLAYER_X, PLAYER_Y, App.PLAYER_LIVES);
		this.player.setRespawnPosition(PLAYER_X, PLAYER_Y);
		this.goal = Goal.createGoal(GOAL_X, GOAL_Y);
		this.world = createRandomWorld();
	}

	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) 
			throws SlickException {
		world.update(gc, delta);
		super.update(gc, sbg, delta);
		
		// check if level complete or game over
        if (world.isLevelCompleted()) {
        	this.level++;
        	System.out.println(level);
        	this.goal = Goal.createGoal(GOAL_X, GOAL_Y);
    		this.world = createRandomWorld();
        } else if (world.isGameOver()) {
        	enterState(sbg, GameOverState.ID, 0);
        }
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
		super.render(gc, sbg, g);
	}
	
	@Override
	public int getID() {
		return ID;
	}
	
	public void setPlayerLives(int lives) {
		player.setLives(lives);
	}
	
	private World createRandomWorld() {
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
		ArrayList<Sprite> sprites = readCsv("endless");
		
		for (float y=WORLD_Y[0]; y<=WORLD_Y[1]; y+=App.TILE_SIZE) {
			sprites.addAll(createRandomLane(y));
		}
		return sprites;
	}
	
	private ArrayList<Sprite> createRandomLane(float y) {
		RandomLane randomLane;
		if (level < 4) {
			randomLane = new RandomLane(level, y);
		} else {
			randomLane = new RandomLane(y);
		}
		return randomLane.getSprites();
	}
	
}
