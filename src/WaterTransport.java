/**
 * Project for SWEN20003: Object Oriented Software Development 2018
 * by Xuanken Tay, University of Melbourne
 */

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * An object moving on water that inherits from MovingObject.
 */
public class WaterTransport extends MovingObject {
	/** image path of log */
	private static final String LOG_PATH = "assets/log.png";
	/** image path of long log */
	private static final String LONGLOG_PATH = "assets/longlog.png";
	/** image path of turtle */
	private static final String TURTLE_PATH = "assets/turtles.png";
	/** speed of log */
	private static final float LOG_SPEED = 0.1f;
	/** speed of long log */
	private static final float LONGLOG_SPEED = 0.07f;
	/** speed of turtle */
	private static final float TURTLE_SPEED = 0.085f;
	/** turtle diving interval */
	private static final int DIVE_PERIOD = 7;
	/** turtle diving duration */
	private static final int DIVE_FOR = 2;
	
	
	/** object is diving */
	private boolean isDiving = false;
	
	/** time passed since last dived */
	private float timeLastDived = 0;
	
	
	/** Static method to create a log.
	 * @param x X position.
	 * @param y Y position
	 * @param moveRight Boolean indicating it is moving right.
	 * @return WaterTransport Log.
	 */
	public static WaterTransport createLog(float x, float y, 
			boolean moveRight) {
		return new WaterTransport(LOG_PATH, x, y, LOG_SPEED, moveRight,
					new String[] {FLOATING});
	}
	
	
	/** Static method to create a long log.
	 * @param x X position.
	 * @param y Y position
	 * @param moveRight Boolean indicating it is moving right.
	 * @return WaterTransport Long log.
	 */
	public static WaterTransport createLonglog(float x, float y,
			boolean moveRight) {
		return new WaterTransport(LONGLOG_PATH, x, y, LONGLOG_SPEED, moveRight,
					new String[] {FLOATING});
	}
	
	
	/** Static method to create a turtle.
	 * @param x X position.
	 * @param y Y position
	 * @param moveRight Boolean indicating it is moving right.
	 * @return WaterTransport Turtle.
	 */
	public static WaterTransport createTurtle(float x, float y,
			boolean moveRight) {
		return new WaterTransport(TURTLE_PATH, x, y, TURTLE_SPEED, moveRight,
					new String[] {FLOATING, DIVEABLE, FLIPPABLE});
	}
	
	
	/** Constructor.
	 * @param imgPath Path to sprite's image.
	 * @param x Starting x position of sprite.
	 * @param y Starting y position of sprite.
	 * @param speed Speed of sprite.
	 * @param moveRight Moving direction sprite.
	 * @param tags Tags associated with sprite.
	 */
	public WaterTransport(String imgPath, float x, float y, float speed, 
			boolean moveRight, String[] tags) {
		super(imgPath, x, y, speed, moveRight, tags);
	}


	
	/** Update method of water transport.
	 * @param gc The Slick game container.
	 * @param delta Time passed since last frame (milliseconds).
	 */
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		// add on to how sprite moves
		
		if (hasTag(DIVEABLE)) {
			handleDiving(delta);
		}
		
		super.update(gc, delta);
		
	}
	
	
	/** Render the sprite from top left corner.
     * @param g The Slick graphics object, used for drawing.
     */
	@Override
	public void render(Graphics g) throws SlickException {
		// do not render diving sprite.
		if (!isDiving) {
			super.render(g);
		}
	}
	
	
	private void handleDiving(int delta) {
		timeLastDived += delta * App.MILLISECOND;
		
		if ((isDiving && timeLastDived >= DIVE_FOR) ||
				(!isDiving && timeLastDived >= DIVE_PERIOD)) {
			isDiving = !isDiving;
			updateFloatingTag();
			timeLastDived = 0;
		}
	}
	
	
	/** Method that alternate diving state of sprite. */
	private void updateFloatingTag() {
		if (hasTag(FLOATING)) {
			removeTag(FLOATING);
		} else {
			addTag(FLOATING);
		}
	}
}
