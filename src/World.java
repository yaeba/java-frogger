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
	/** x position of lives image */
	public static final float LIVES_X = 24;
	/** y position of lives image */
	public static final float LIVES_Y = 744;
	/** pixels between successive lives images */
	public static final float LIVES_SEP = 32;
	/** path to lives images */
	public static final String LIVES_PATH = "assets/lives.png";
	
	
	/** Array list containing all sprites */
	private ArrayList<Sprite> sprites;
	/** The player */
	private Player player;
	/** Lives image */
	private Image lives = new Image("assets/lives.png");
	/** Array of goals */
	private Goal[] goals;
	/** Level status */
	private boolean levelCompleted = false;
	/** Game status */
	private boolean gameOver = false;
	
	
	/** Constructor.
	 * @param player Player of the game.
	 * @param sprites Array list of all sprites.
	 * @param goals Array of goals.
	 */
	public World(Player player, ArrayList<Sprite> sprites, Goal[] goals) 
			throws SlickException {
		this.player = player;
		this.sprites = sprites;
		this.goals = goals;
	}
	
	
	/** Update the world for a frame.
     * @param gc The Slick game container object.
     * @param delta Time passed since last frame (milliseconds).
     */
	public void update(GameContainer gc, int delta) 
		throws SlickException {
		
		
		if (hasExtraLife()) {
			// handle the removal of extra life
			checkExtraLife();
		}
		
		// cheat code to fill goals (remove afterwards)
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			for (Goal goal : goals) {
				if (!goal.isFilled()) {
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
	public void createExtraLife() {
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
		
		ExtraLife extraLife = ExtraLife.createExtraLife(randomLog.getX(), 
									randomLog.getY(), randomLog);
		sprites.add(extraLife);
	}
	
	
	/** Check if world currently has extra life or not.
	 * @return boolean Presence of extra life.
	 */
	public boolean hasExtraLife() {
		for (Sprite sprite: this.sprites) {
			if (sprite instanceof ExtraLife) {
				return true;
			}
		}
		return false;
	}
	

	private void checkExtraLife() {
		// check if need to destroy any destroyed extra life
		sprites.removeIf(s -> (s instanceof ExtraLife && 
								((ExtraLife)s).isDestroyed()));
	}
	

	private void checkLevelCompleted() {
		// check if all goals have been filled
		for (Goal goal: this.goals) {
			if (!goal.isFilled()) {
				return;
			}
		}
		this.levelCompleted = true;
	}
	

	private void checkGameOver() {
		// check if player has no move lives
		if (player.getLives() < 0) {
			this.gameOver = true;
		}
	}
	
	
	private void checkFloating() {
		// update player's floating status
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
	

	private void renderLives(Graphics g) {
		// render lives on screen 
		for (int i=0; i<player.getLives(); i++) {
			float livesX = LIVES_X + i * LIVES_SEP;
			
			float livesY = LIVES_Y;
			g.drawImage(lives, livesX-lives.getWidth()/2, 
						livesY-lives.getHeight()/2);
		}
	}

}
