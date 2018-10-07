import java.util.ArrayList;
import java.util.Random;

public class RandomLane {
	public enum Obstacle {
		BUS, BULLDOZER, BIKE, RACECAR, LOG, LONGLOG, TURTLE;
		
		private static final Obstacle[] VALUES = values();
		private static final Random RANDOM = new Random();
		
		public static Obstacle getRandomObs() {
			return VALUES[RANDOM.nextInt(VALUES.length)];
		}
		
		public Sprite createObs(float x, float y, boolean moveRight) {
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
		
		public boolean onWater() {
			if (LOG.equals(this) || LONGLOG.equals(this) || 
					TURTLE.equals(this)) {
				return true;
			}
			return false;
		}
	}
	
	public static final int OBSTACLES = Obstacle.values().length;
	private static final Random RANDOM = new Random();
	private Obstacle obstacle;
	private float y;
	private ArrayList<Sprite> sprites;
	
	public RandomLane(float y) {
		this.obstacle = Obstacle.getRandomObs();
		this.y = y;
		this.sprites = createLane();
	}
	
	public RandomLane(int index, float y) {
		this.obstacle = Obstacle.values()[index];
		this.y = y;
		this.sprites = createLane();
	}
	
	public ArrayList<Sprite> getSprites(){
		return this.sprites;
	}
	
	private ArrayList<Sprite> createLane() {
		ArrayList<Sprite> sprites = new ArrayList<Sprite>();
		
		if (this.obstacle.onWater()) {
			// create water tiles
			for (int i=0; i<App.SCREEN_WIDTH; i+=App.TILE_SIZE) {
				sprites.add(Tile.createWaterTile(i, y));
			}
		}
		
		// create lane of obstacles
		boolean moveRight = RANDOM.nextBoolean();
		float x = RANDOM.nextFloat() * 500;
		float sep = RANDOM.nextFloat() * 10 + 5;
		
		int nObstacles = (int)((App.SCREEN_WIDTH-x) /(sep * App.TILE_SIZE)+1);
		
		for (int i=0; i<nObstacles; i++) {
			sprites.add(obstacle.createObs(x, y, moveRight));
			x += sep * App.TILE_SIZE;
		}
		
		return sprites;
	}
}
