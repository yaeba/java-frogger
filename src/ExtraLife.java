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
	/** Image path of extra life object */
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
	
	/** Indicate if it should step right */
	private boolean stepRight = true;
	
	/** Variable to record separation from log */
	private float sep;
	
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
	
	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		// unlike sprite, extra life can move
		
		timeLastMoved += delta * App.MILLISECOND;
		timeCreated += delta * App.MILLISECOND;

		
		// check if it needs be destroyed
		if (timeCreated >= DESTROY_TIME) {
			destroy();
		}
		
		move();
		
	}
	
	
	/** Add live to player and destroy itself.
	 * @param player Player of interest.
	 */
	public void activateEffect(Player player) {
		player.setLives(player.getLives()+1);
		destroy();
	}

	/** Getter for status of extra life.
	 * @return boolean Is extra life destroyed?
	 */
	public boolean isDestroyed() {
		return this.destroyed;
	}
	
	
	private ExtraLife(String imgPath, float x, float y,
			WaterTransport log) {
		super(imgPath, x, y);
		this.onLog = log;
	}
	
	
	private void destroy() {
		// set status to destroyed
		this.destroyed = true;
	}
	
	private void move() {
		// move together with log
		setMove(onLog.getX() + this.sep, getY());
		
		if (timeLastMoved >= MOVE_PERIOD) {
			// move by itself
			timeLastMoved = 0;
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
		}

		this.sep = getX() - onLog.getX();
	}

}
