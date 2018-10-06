/**
 * Project for SWEN20003: Object Oriented Software Development 2018
 * by Xuanken Tay, University of Melbourne
 */

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

/**
 * Represent an extra life object that inherits from Sprite.
 */
public class ExtraLife extends Sprite {
	/** image path of extra life object */
	private static final String EXTRALIFE_PATH = "assets/extralife.png";
	/** Time interval between moving */
	private static final int MOVE_PERIOD = 2;
	/** Time of disappearing */
	private static final int DESTROY_TIME = 14;

	
	/** Log that it is on */
	private WaterTransport onLog;
	
	/** Time since last moved */
	private float timeLastMoved = 0;
	
	/** Time since created */
	private float timeCreated = 0;
	
	/** Moving right? */
	private boolean stepRight = true;
	
	/** Variable to record separation from log */
	private float sep = 0;
	
	/** Status */
	private boolean destroyed = false;
	
	
	/** Static method to create an extra life.
	 * @param x X position of extra life.
	 * @param y Y position of extra life.
	 * @param log The log extra life is created on.
	 * @return ExtraLife Extra life object.
	 */
	public static ExtraLife createExtraLife(float x, float y,
			WaterTransport log) {
		return new ExtraLife(EXTRALIFE_PATH, x, y, log);
	}
	
	/** Constructor.
	 * @param imgPath Image path.
	 * @param x Starting x position.
	 * @param y Starting y position.
	 * @param log The log it is created on.
	 */
	public ExtraLife(String imgPath, float x, float y,
			WaterTransport log) {
		super(imgPath, x, y);
		this.onLog = log;
	}
	
	
	/** Update method of extra life.
	 * @param gc The Slick game container.
	 * @param delta Time passed since last frame (milliseconds).
	 */
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		// unlike sprite, extra life can move
		
		timeLastMoved += delta * App.MILLISECOND;
		timeCreated += delta * App.MILLISECOND;
		
		// move together with log
		setMove(onLog.getX() + this.sep, getY());
		
		// check if it needs to move by itself or be destroyed
		if (timeCreated >= DESTROY_TIME) {
			destroy();
		} else if (timeLastMoved >= MOVE_PERIOD) {
			// move by itself
			float x = getX();
			float step = stepRight ? getWidth() : -1 * getWidth();
			float toX = x + step;
			
			float logStart = onLog.getX() - onLog.getWidth()/2;
			float logEnd = onLog.getX() + onLog.getWidth()/2;
			if (toX < logStart || toX > logEnd) {
				// reached end, move in opposite direction instead
				toX = x - step;
				stepRight = !stepRight;
			}
			setMove(toX, getY());
			timeLastMoved = 0;
		}
		this.sep = getX() - onLog.getX();
		super.update(gc, delta);
	}
	
	
	/** Add live to player and destroy itself.
	 * @param player Player of interest.
	 */
	public void activateEffect(Player player) {
		player.addLives();
		destroy();
	}

	/** Getter for status of extra life.
	 * @return boolean Is extra life destroyed?
	 */
	public boolean isDestroyed() {
		return this.destroyed;
	}
	
	/** Set status to destroyed */
	private void destroy() {
		this.destroyed = true;
	}
}
