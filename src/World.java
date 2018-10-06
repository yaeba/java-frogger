/**
 * Project for SWEN20003: Object Oriented Software Development 2018
 * by Xuanken Tay, University of Melbourne
 */


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import java.util.Random;
import java.util.ArrayList;


/**
 * World class for the game.
 * Handles update and rendering of whole world, as well as game physics,
 * mechanisms and rules.
 */
public class World {
	/** player starting x position */
	public static final float PLAYER_X = 512;
	/** player starting y position */
	public static final float PLAYER_Y = 720;
	/** x position of lives image */
	public static final float LIVES_X = 24;
	/** y position of lives image */
	public static final float LIVES_Y = 744;
	/** pixels between successive lives images */
	public static final float LIVES_SEP = 32;
	/** path to lives images */
	public static final String LIVES_PATH = "assets/lives.png";
	/** lower bound seconds extralife appears */
	public static final int EXTRALIFE_MIN = 25;
	/** upper bound seconds extralife appears */
	public static final int EXTRALIFE_MAX = 35;
	
	
	/** Array list containing all sprites */
	private ArrayList<Sprite> sprites;
	/** The player */
	private Player player;
	/** Extra life object */
	private ExtraLife extraLife = null;
	/** Lives image */
	private Image lives = new Image("assets/lives.png");
	/** Array of goals */
	private Goal[] goals;
	/** Level status */
	private boolean levelCompleted = false;
	/** Game status */
	private boolean gameOver = false;
	/** Time in world */
	private float timePassed = 0;
	/** Presence of extra life in world */
	private boolean setExtraLife = false;
	/** Time extra life appears */
	private int extraLifeStart;
	
	
	/** Constructor.
	 * @param player Player of the game.
	 * @param sprites Array list of all sprites.
	 * @param goals Array of goals.
	 */
	public World(Player player, ArrayList<Sprite> sprites, Goal[] goals) 
			throws SlickException {
		// initialise player, sprites, goals and time extra life starts
		
		this.player = player;
		this.sprites = sprites;
		this.goals = goals;
		
		int bound = EXTRALIFE_MAX - EXTRALIFE_MIN + 1;
		this.extraLifeStart = new Random().nextInt(bound) + EXTRALIFE_MIN;
	}
	
	
	/** Update the world for a frame.
     * @param gc The Slick game container object.
     * @param delta Time passed since last frame (milliseconds).
     */
	public void update(GameContainer gc, int delta) 
		throws SlickException {
		
		// update time passed
		timePassed += delta * App.MILLISECOND;
		

		// handle extra life
		if (setExtraLife) {
			checkExtraLife();
		}
		
		// cheat code to fill goals (remove afterwards)
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			for (Goal goal : this.goals) {
				if(!goal.isFilled()) {
					goal.fillGoal(player);
					break;
				}
			}
		}

		// update player with inputs
		player.update(gc, delta);
		

		// check if player is floating
		checkFloating();
		
		
		// update all sprites 
		for (Sprite sprite : this.sprites) {
			sprite.update(gc, delta);
			if (player.collidesWith(sprite)) {
				player.onCollision(sprite);
			}
		}

		
		// check if player has reached goal
		for (Goal goal : this.goals) {
			if (player.collidesWith(goal)) {
				player.onCollision(goal);
			}
		}
		
		// check if game is completed
		checkLevelCompleted();
		
		// check if player has no more lives
		checkGameOver();

	}
	
	
	/** Render the entire world.
     * @param g The Slick graphics object, used for drawing.
     */
	public void render(Graphics g) throws SlickException {
		
		// render all sprites
		for (Sprite sprite : this.sprites) {
			sprite.render(g);
		}
		
		// draw (filled) goals
		for (Goal goal : this.goals) {
			goal.render(g);
		}
		
		// draw player
		player.render(g);
		
		// draw lives
		renderLives(g);
	}

	/** Set if world should have extra life.
     * @param bool Boolean.
     */
	public void setExtraLife(boolean bool) {
		this.setExtraLife = bool;
	}
	
	/** Return level status.
	 * @return boolean Is level completed?
     */
	public boolean isLevelCompleted() {
		return this.levelCompleted;
	}
	
	/** Return game status.
	 * @return boolean Is game over?
     */
	public boolean isGameOver() {
		return this.gameOver;
	}
	
	/** Create extra life and add to array list of sprites */
	private void createExtraLife() {
		ArrayList<WaterTransport> list = new ArrayList<>();
		for (Sprite sprite : this.sprites) {
			if (sprite.hasTag(Sprite.FLOATING) &&
					!sprite.hasTag(Sprite.DIVEABLE)) {
				// found a log or long log
				list.add((WaterTransport)sprite);
			}
		}
		// choose a random log and create extra life on it
		int randint = new Random().nextInt(list.size());
		WaterTransport randomLog = list.get(randint);
		
		this.extraLife = ExtraLife.createExtraLife(randomLog.getX(), 
									randomLog.getY(), randomLog);
		sprites.add(this.extraLife);
	}
	
	/** Handles creation and destruction of extra life*/
	private void checkExtraLife() {
		// check if needs to create extra life
		if (extraLife == null && timePassed >= extraLifeStart) {
			createExtraLife();
		}
		// or need to destroy it
		if (extraLife != null && extraLife.isDestroyed()) {
			this.sprites.remove(extraLife);
		}
	}
	
	/** Check if all goals have been filled */
	private void checkLevelCompleted() {
		for (Goal goal: this.goals) {
			if (!goal.isFilled()) {
				return;
			}
		}
		this.levelCompleted = true;
	}
	
	/** Check if player has no move lives */
	private void checkGameOver() {
		if (player.getLives() == 0) {
			this.gameOver = true;
		}
	}
	
	/** Check if player is floating */
	private void checkFloating() {
		for (Sprite sprite : this.sprites) {
			if (sprite.hasTag(Sprite.FLOATING) && player.collidesWith(sprite)) {
				// player is riding something
				if (!player.hasTag(Sprite.FLOATING)) {
					player.addTag(Sprite.FLOATING);
				}
				return;
			}
		}
		// player is not floating
		if (player.hasTag(Sprite.FLOATING)) {
			player.removeTag(Sprite.FLOATING);
		}
	}
	
	/** Render lives on screen */
	private void renderLives(Graphics g) {
		for (int i=0; i<player.getLives(); i++) {
			float lives_x = LIVES_X + i * LIVES_SEP;
			float lives_y = LIVES_Y;
			g.drawImage(lives, lives_x-lives.getWidth()/2, 
						lives_y-lives.getHeight()/2);
		}
	}
}
