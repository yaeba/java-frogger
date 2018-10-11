/**
 * Project for SWEN20003: Object Oriented Software Development 2018
 * by Xuanken Tay, University of Melbourne
 */

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;


/**
 * An abstract moving object inherits from Sprite. Contains method of
 * updating how it moves.
 */
public abstract class MovingObject extends Sprite {
	/** Moving speed */
	private float speed;
	
	/** Moving direction */
	private boolean moveRight;
	
	
	/** Constructor.
	 * @param imgPath Path to sprite's image.
	 * @param x Starting x position of sprite.
	 * @param y Starting y position of sprite.
	 * @param speed Speed of sprite.
	 * @param moveRight Moving direction sprite.
	 * @param tags Tags associated with sprite.
	 */
	public MovingObject(String imgPath, float x, float y, float speed, 
			boolean moveRight, String[] tags) {
		super(imgPath, x, y, tags);
		this.speed = speed;
		this.moveRight = moveRight;
	}
	
	
	@Override
	public void update(GameContainer gc, int delta) 
			throws SlickException {
		// update an obstacle's position
		move(delta);
	}
	
	
	@Override
	public void render(Graphics g) throws SlickException {
		// some sprites can be flipped according to moving direction
		if (!isMovingRight() && hasTag(FLIPPABLE)) {
			
			// render horizontally flipped image
			g.drawImage(getImage().getFlippedCopy(true, false), 
						getX()-getWidth()/2, getY()-getHeight()/2);
		
		} else {
			super.render(g);
		}
	}
	
	
	/** Getter of moving direction.
	 * @return boolean Moving right?
	 */
	public boolean isMovingRight() {
		return this.moveRight;
	}
	
	
	/** Getter of moving speed.
	 * @return float Moving speed.
	 */
	public float getSpeed() {
		return this.speed;
	}
	
	
	/** Reverse the direction of sprite. */
	public void reverseDir() {
		this.moveRight = !this.moveRight;
	}
	
	/** Solid sprite pushes and updates position of player.
	 * @param player Reference to player.
	 */
	public void pushPlayer(Player player) {
		float sep = getWidth()/2 + getWidth()/2;
		sep = isMovingRight() ? sep : -1 * sep;
		player.setMove(getX()+sep, player.getY());
		if (!player.onScreen()) {
			killPlayer(player);
		}
	}
	
	
	private void move(int delta) {
		// method that moves any moving object and respawn from either sides
		float toX = getX();
		
		// moving left
		if (!moveRight) {
			toX -= delta * speed;
			// respawn from right end if half of it went past window
			if (toX + getWidth()/2 <= 0) {
				toX = App.SCREEN_WIDTH + getWidth()/2;
			}
		}
		// moving right
		else {
			toX += delta * speed;
			// respawn from left end if half of it went past window
			if (toX - getWidth()/2 >= App.SCREEN_WIDTH) {
				toX = -getWidth()/2;
			}
		}
	
		setMove(toX, getY());
	}
}
