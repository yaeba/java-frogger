/**
 * Project for SWEN20003: Object Oriented Software Development 2018
 * by Xuanken Tay, University of Melbourne
 */

import java.util.ArrayList;
import java.util.Random;

/** Represent a random lane.
 * Contains method to generate a lane with random chosen sprites
 * with different x and y positions.
 */
public class RandomLane {

	/** Local helper enum to draw a random sprite */
	public enum Obstacle {
		BUS, BULLDOZER, BIKE, RACECAR, LOG, LONGLOG, TURTLE;
		
		/** sprites that can be drawn */
		private static final Obstacle[] VALUES = values();
		/** random class to be used for drawing a sprite */
		private static final Random RANDOM = new Random();
		
		/** Static method to draw a random sprite.
		 * @return Obstacle One sprite from the enum.
		 */
		public static Obstacle getRandomObs() {
			return VALUES[RANDOM.nextInt(VALUES.length)];
		}
		
		/** Indicate if the randomly chosen sprite is floating on water.
		 * @return boolean Should sprite only be on water?
		 */
		public boolean onWater() {
			if (LOG.equals(this) || LONGLOG.equals(this) || 
					TURTLE.equals(this)) {
				return true;
			}
			return false;
		}
		
		private Sprite createObs(float x, float y, boolean moveRight) {
			// create a sprite
			if (BUS.equals(this)) {
				return Vehicle.createBus(x, y, moveRight);
			} else if (BULLDOZER.equals(this)) {
				return Vehicle.createBulldozer(x, y, moveRight);
			} else if (BIKE.equals(this)) {
				return Vehicle.createBike(x, y, moveRight);
			} else if (RACECAR.equals(this)) {
				return Vehicle.createRacecar(x, y, moveRight);
			} else if (LOG.equals(this)) {
				return WaterTransport.createLog(x, y, moveRight);
			} else if (LONGLOG.equals(this)) {
				return WaterTransport.createLonglog(x, y, moveRight);
			} else {
				return WaterTransport.createTurtle(x, y, moveRight);
			}
		}
	}
	
	/** range of position x values can take */
	public static final int X_RANGE = 500;
	/** range of separation between sprites can take */
	public static final int SEP_RANGE = 10;
	/** minimum separation between sprites can take */
	public static final int SEP_MIN = 5;
	/** type of sprite picked to be generated */
	private Obstacle obstacle;
	
	/** y position of lane */
	private float y;
	
	/** all sprites along the lane */
	private ArrayList<Sprite> sprites;
	
	
	/** Constructor that creates a lane of random sprites 
	 * @param y Y position of lane.
	 */
	public RandomLane(float y) {
		this.obstacle = Obstacle.getRandomObs();
		this.y = y;
		this.sprites = createLane();
	}
	
	/** Constructor that creates a lane of selected sprites.
	 * @param index Index of sprite in Obstacle enum class.
	 * @param y Y position of lane.
	 */
	public RandomLane(int index, float y) {
		this.obstacle = Obstacle.values()[index];
		this.y = y;
		this.sprites = createLane();
	}
	
	/** Get sprites of the lane created.
	 * @return ArrayList<Sprite> All sprites on the lane.
	 */
	public ArrayList<Sprite> getSprites(){
		return this.sprites;
	}
	
	
	private ArrayList<Sprite> createLane() {
		ArrayList<Sprite> sprites = new ArrayList<>();
		
		if (this.obstacle.onWater()) {
			// create water tiles
			for (int i=0; i<App.SCREEN_WIDTH; i+=App.TILE_SIZE) {
				sprites.add(Tile.createWaterTile(i, y));
			}
		}
		
		// create lane of obstacles
		boolean moveRight = Obstacle.RANDOM.nextBoolean();
		float x = Obstacle.RANDOM.nextFloat() * X_RANGE;
		float sep = Obstacle.RANDOM.nextFloat() * SEP_RANGE + SEP_MIN;
		
		int nObstacles = (int)((App.SCREEN_WIDTH-x) /(sep * App.TILE_SIZE)+1);
		
		for (int i=0; i<nObstacles; i++) {
			sprites.add(obstacle.createObs(x, y, moveRight));
			x += sep * App.TILE_SIZE;
		}
		
		return sprites;
	}
}
