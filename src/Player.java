/**
 * Project for SWEN20003: Object Oriented Software Development 2018
 * by Xuanken Tay, University of Melbourne
 */

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;


/**
 * Singleton class, represent a player (frog) that inherits from Sprite. 
 * Handles inputs and update position of player.
 */
public class Player extends Sprite {
	/** image path of player (frog) */
	public static final String PLAYER_PATH = "assets/frog.png";
	/** player's default respawn x position */
	public static final float RESPAWN_X = 512;
	/** player's default respawn y position */
	public static final float RESPAWN_Y = 720;
	/** player initial lives */
	public static final int PLAYER_LIVES = 3;
	
	
	/** static variable single instance of Player */
	private static Player player = null;
	
	/** lives player has */
	private int lives;
	
	/** object player is riding on */
	private WaterTransport rideOn;
	
	/** previous legal position of player */
	private float prevX, prevY;
	
	/** x position to return to after dying */
	private float respawnX = RESPAWN_X;
	
	/** y position to return to after dying */
	private float respawnY = RESPAWN_Y;

	
	/** Static method to create a player.
	 * @param x X position of player.
	 * @param y Y position of player.
	 * @param lives Number of lives player has.
	 * @return Player Player created.
	 */
	public static Player createPlayer(float x, float y) {
		if (player == null) {
			player = new Player(PLAYER_PATH, x, y, PLAYER_LIVES);
		}
		return player;
	}
	
	/** Get the singleton player.
	 * @return Player The singleton player
	 */
	public static Player getPlayer() {
		return player;
	}
	

	@Override
	public void update(GameContainer gc, int delta) {
		// update position of player
		
		// move together if player is riding on something
		if (hasTag(FLOATING)) {
			moveByRiding(delta);
		}
		
		// receive inputs and move player
		moveByInput(gc.getInput());
	}
	
	@Override
	public void setMove(float x, float y) {
		prevX = getX();
		prevY = getY();
		super.setMove(x, y);
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
	
	/** Setter for number lives
	 * @param lives Lives for player/
	 */
	public void setLives(int lives) {
		this.lives = lives;
	}
	
	/** Die and reset to starting position. */
	public void die() {
		respawn();
		this.lives--;
	}
	
	/** Spawn player */
	public void respawn() {
		setMove(respawnX, respawnY);
	}
	
	/** Handles collision of player with different sprites.
	 * @param other Another sprite in contact with player.
	 */
	public void onCollision(Sprite other) {
		// in contact with lethal sprite
		if (other.hasTag(LETHAL)) {
			onCollisionLethal(other);
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
	
	
	/** Set restart position after dying (required by different states.
	 * @param x Respawn x position.
	 * @param y Respawn y position.
	 */
	public void setRespawnPosition(float x, float y) {
		this.respawnX = x;
		this.respawnY = y;
	}
	
	
	
	private void moveByRiding(int delta) {
		// update player position with object it is riding.
		float sep = rideOn.getSpeed() * delta;
		sep = rideOn.isMovingRight() ? sep : -1 * sep;
		float toX = getX() + sep;
		if (canMove(toX, getY())) {
			setMove(getX() + sep, getY());
		}
	}
	
	
	private void moveByInput(Input input) {
		// update player position with input received.
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
			setMove(toX, toY);
		}
	}
	

	private void onCollisionLethal(Sprite other) {
		// collide with lethal sprite
		if (!hasTag(FLOATING)) {
			other.killPlayer(this);
		}
	}
	
	private void onCollisionSolid(Sprite other) {
		// collide with solid sprite
		if (prevX != getX() || prevY != getY()) {
			// prevent moving into solid object
			reset_pos();
		} else {
			// pushed by solid object
			MovingObject solid = (MovingObject) other;
			solid.pushPlayer(this);
		}
	}
	
	private void onCollisionFloating(Sprite other) {
		// collide with floating sprite
		this.rideOn = (WaterTransport) other;
		if (!hasTag(FLOATING)) {
			addTag(FLOATING);
		}
	}
	
	private void onCollisionGoal(Sprite other) {
		// collide with a goal
		Goal goal = (Goal) other;
		if (goal.isFilled()) {
			// already filled
			die();
		} else {
			goal.fillGoal(this);
		}
	}

	
	private void onCollisionExtraLife(Sprite other) {
		// add a live
		ExtraLife extraLife = (ExtraLife) other;
		extraLife.activateEffect(this);
	}
	
	private void reset_pos() {
		// return to previous legal position
		setMove(prevX, prevY);
	}
	

	private Player(String imgPath, float x, float y, int lives) {
		super(imgPath, x, y);
		this.lives = lives;
		this.prevX = x;
		this.prevY = y;
	}
}
