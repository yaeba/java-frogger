/**
 * Project for SWEN20003: Object Oriented Software Development 2018
 * by Xuanken Tay, University of Melbourne
 */

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

/**
 * A vehicle that inherits from MovingObject.
 */
public class Vehicle extends MovingObject {
	/** image path of bus */
	private static final String BUS_PATH = "assets/bus.png";
	/** image path of racecar */
	private static final String RACECAR_PATH = "assets/racecar.png";
	/** image path of bike*/
	private static final String BIKE_PATH = "assets/bike.png";
	/** image path of bulldozer */
	private static final String BULLDOZER_PATH = "assets/bulldozer.png";
	/** speed of bus */
	private static final float BUS_SPEED = 0.15f;
	/** speed of racecar */
	private static final float RACECAR_SPEED = 0.5f;
	/** speed of bike */
	private static final float BIKE_SPEED = 0.2f;
	/** speed of bulldozer */
	private static final float BULLDOZER_SPEED = 0.05f;
	
	
	/** Static method to create a bus.
	 * @param x X position.
	 * @param y Y position
	 * @param moveRight Boolean indicating it is moving right.
	 * @return Vehicle Bus.
	 */
	public static Vehicle createBus(float x, float y, boolean moveRight) {
		return new Vehicle(BUS_PATH, x, y, BUS_SPEED, moveRight, 
						new String[] {LETHAL, FLIPPABLE});
	}
	
	/** Static method to create a racecar.
	 * @param x X position.
	 * @param y Y position
	 * @param moveRight Boolean indicating it is moving right.
	 * @return Vehicle Racecar.
	 */
	public static Vehicle createRacecar(float x, float y, boolean moveRight) {
		return new Vehicle(RACECAR_PATH, x, y, RACECAR_SPEED, moveRight, 
						new String[] {LETHAL, FLIPPABLE});
	}
	
	/** Static method to create a bike.
	 * @param x X position.
	 * @param y Y position
	 * @param moveRight Boolean indicating it is moving right.
	 * @return Vehicle Bike.
	 */
	public static Vehicle createBike(float x, float y, boolean moveRight) {
		return new Vehicle(BIKE_PATH, x, y, BIKE_SPEED, moveRight, 
						new String[] {LETHAL, REVERSIBLE, FLIPPABLE});
	}
	
	/** Static method to create a bulldozer.
	 * @param x X position.
	 * @param y Y position
	 * @param moveRight Boolean indicating it is moving right.
	 * @return Vehicle Bulldozer.
	 */
	public static Vehicle createBulldozer(float x, float y,
			boolean moveRight) {
		return new Vehicle(BULLDOZER_PATH, x, y, BULLDOZER_SPEED, moveRight, 
						new String[] {SOLID, FLIPPABLE});
	}


	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		// add on to how sprite moves
		
		if (hasTag(REVERSIBLE)) {
			// reverse direction if necessary (only for bike(
			handleReverse();
		}
		
		// continue moving as usual
		super.update(gc, delta);
		
	}
	
	private Vehicle(String imgPath, float x, float y, float speed,
			boolean moveRight, String[] tags) {
		super(imgPath, x, y, speed, moveRight, tags);
	}
	
	
	private void handleReverse() {
		// method that handles reversible moving object
		float width = getWidth(), x = getX();
		if ((!isMovingRight()  && x < width/2)
				|| (isMovingRight() && x > App.SCREEN_WIDTH - width/2)) {
			reverseDir();
		}
	}
}
