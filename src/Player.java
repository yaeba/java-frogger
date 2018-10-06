/**
 * Project for SWEN20003: Object Oriented Software Development 2018
 * by Xuanken Tay, University of Melbourne
 */

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;


/**
 * Represent a player (frog) that inherits from Sprite. Handles inputs and
 * update position of player.
 */
public class Player extends Sprite {
	/** image path of player (frog) */
	private static final String PLAYER_PATH = "assets/frog.png";
	/** player's default respawn x position */
	public static final float RESPAWN_X = 512;
	/** player's default respawn y position */
	public static final float RESPAWN_Y = 720;
	
	/** lives player has */
	private int lives;
	
	/** object player is riding on */
	private WaterTransport rideOn;
	
	/** previous legal position of player */
	private float prevX, prevY;
	
	/** position to return to after dying */
	private float respawnX = RESPAWN_X;
	private float respawnY = RESPAWN_Y;
	
	/** Static method to create a player.
	 * @param x X position of player.
	 * @param y Y position of player.
	 * @param lives Number of lives player has.
	 * @return Player Player created.
	 */
	public static Player createPlayer(float x, float y, int lives) {
		return new Player(PLAYER_PATH, x, y, lives);
	}
	
	
	/** Constructor.
	 * @param imgPath Path to sprite's image.
	 * @param x Starting x position of sprite.
	 * @param y Starting y position of sprite.
	 * @param lives Number of lives player has.
	 */
	public Player(String imgPath, float x, float y, int lives) {
		super(imgPath, x, y);
		this.lives = lives;
		this.prevX = x;
		this.prevY = y;
	}
	
	
	/** Update method of player.
	 * @param gc The Slick game container.
	 * @param delta Time passed since last frame (milliseconds).
	 */
	@Override
	public void update(GameContainer gc, int delta) {
		// update position of player
		
		// move together if player is riding on something
		if (hasTag(FLOATING)) {
			moveByRiding(delta);
		}
		
		// receive inputs and move player
		Input input = gc.getInput();
		moveByInput(input);
	}
	
	
	/** Check if player still on screen after moving.
	 * @param toX Destination x.
	 * @param toY Destination y.
	 * @return boolean Legal move?
	 */
	public boolean canMove(float toX, float toY) {
		return onScreen(toX, toY);
	}
	
	
	/** Getter of current number of lives.
	 * @return int Number of lives.
	 */
	public int getLives() {
		return this.lives;
	}
	
	/** Method to add 1 live to player. */
	public void addLives() {
		this.lives++;
	}
	
	/** Method to kill player and reset to starting position. */
	public void dies() {
		setMove(respawnX, respawnY);
		this.lives--;
	}
	
	
	/** Handles collision of player with different sprites.
	 * @param other Another sprite in contact with player.
	 */
	public void onCollision(Sprite other) {
		// in contact with lethal sprite
		if (other.hasTag(LETHAL)) {
			onCollisionLethal();
		}
		
		// in contact with solid sprite
		if (other.hasTag(SOLID)) {
			onCollisionSolid(other);
		} 
		
		// in contact with floating sprite
		if (other.hasTag(FLOATING)) {
			onCollisionFloating(other);
		}
		
		// reached a goal
		if (other instanceof Goal) {
			onCollisionGoal(other);
		} 
		
		// activate an extra life
		if (other instanceof ExtraLife) {
			onCollisionExtraLife(other);
		}
	}
	
	
	/** Set restart position after dying 
	 * @param x Respawn x position
	 * @param y Respawn y position
	 */
	public void setRespawnPosition(float x, float y) {
		this.respawnX = x;
		this.respawnY = y;
	}
	
	
	/** Update player position with object it is riding. */
	private void moveByRiding(int delta) {
		float sep = rideOn.getSpeed() * delta;
		sep = rideOn.isMovingRight() ? sep : -1 * sep;
		float toX = getX() + sep;
		if (canMove(toX, getY())) {
			setMove(getX() + sep, getY());
		}
	}
	
	
	/** Update player position with input received. */
	private void moveByInput(Input input) {
		float toX = getX(), toY = getY();
		float step = getWidth();
		if (input.isKeyPressed(Input.KEY_UP)) {
			toY -= step;
		} else if (input.isKeyPressed(Input.KEY_DOWN)) {
			toY += step;
		} else if (input.isKeyPressed(Input.KEY_LEFT)) {
			toX -= step;
		} else if (input.isKeyPressed(Input.KEY_RIGHT)) {
			toX += step;
		}
		// make sure it is not moving out of window
		if (canMove(toX, toY)) {
			prevX = getX();
			prevY = getY();
			setMove(toX, toY);
		}
	}
	
	/** Collides with lethal sprite */
	private void onCollisionLethal() {
		if (!hasTag(FLOATING)) {
			dies();
		}
	}
	
	/** Collides with solid sprite */
	private void onCollisionSolid(Sprite other) {
		if (prevX != getX() || prevY != getY()) {
			// prevent moving into solid object
			reset_pos();
		} else {
			// pushed by solid object
			MovingObject solid = (MovingObject) other;
			float sep = solid.getWidth()/2 + this.getWidth()/2;
			sep = solid.isMovingRight() ? sep : -1 * sep;
			setMove(solid.getX()+sep, getY());
			if (!onScreen()) {
				dies();
			}
		}
	}
	
	/** Collides with floating sprite */
	private void onCollisionFloating(Sprite other) {
		this.rideOn = (WaterTransport) other;
		if (!hasTag(FLOATING)) {
			addTag(FLOATING);
		}
	}
	
	/** Collides with goal */
	private void onCollisionGoal(Sprite other) {
		Goal goal = (Goal) other;
		if (goal.isFilled()) {
			// already filled
			dies();
		} else {
			goal.fillGoal(this);
		}
	}
	
	/** Collides with extra life */
	private void onCollisionExtraLife(Sprite other) {
		ExtraLife extraLife = (ExtraLife) other;
		extraLife.activateEffect(this);
	}
	
	/** Reset to previous legal position (prevent moving into solid object) */
	private void reset_pos() {
		setMove(prevX, prevY);
	}
	
	
}
