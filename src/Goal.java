/**
 * Project for SWEN20003: Object Oriented Software Development 2018
 * by Xuanken Tay, University of Melbourne
 */

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;


/**
 * Represent a goal to be filled in. Inherits from Sprite.
 */
public class Goal extends Sprite {
	/** image path of filled goal */
	private static final String PLAYER_PATH = "assets/frog.png";
	
	/** goal status */
	private boolean filled = false;
	
	/** Static method to create a goal.
	 * @param x X position of goal.
	 * @param y Y position of goal.
	 * @return Goal Created goal.
	 */
	public static Goal createGoal(float x, float y) {
		return new Goal(PLAYER_PATH, x, y);
	}
	
	
	/** Constructor.
	 * @param imgPath Path to sprite's image.
	 * @param x Starting x position of sprite.
	 * @param y Starting y position of sprite.
	 */
	public Goal(String imgPath, float x, float y){
		super(imgPath, x, y);
	}

	
	/** Render the sprite from top left corner.
     * @param g The Slick graphics object, used for drawing.
     */
	@Override
	public void render(Graphics g) throws SlickException {
		if (filled) {
			super.render(g);
		}
	}
	
	/** Method to fill goal with player.
	 * @param player Player to fill in goal.
	 */
	public void fillGoal(Player player) {
		filled = true;
		player.respawn();
	}
	
	/** Return status of goal.
	 * @return boolean Goal status.
	 */
	public boolean isFilled() {
		return filled;
	}
}
