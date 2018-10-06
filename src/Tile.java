/**
 * Project for SWEN20003: Object Oriented Software Development 2018
 * by Xuanken Tay, University of Melbourne
 */

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

/**
 * Represent a single Tile. Inherits from Sprite.
 */
public class Tile extends Sprite {
	/** image path of grass tile */
	private static final String GRASS_PATH = "assets/grass.png";
	/** image path of water tile */
	private static final String WATER_PATH = "assets/water.png";
	/** image path of tree tile */
	private static final String TREE_PATH = "assets/tree.png";
	
	
	/** Static method to create a grass tile.
	 * @param x X position.
	 * @param y Y position
	 * @return Tile Grass tile.
	 */
	public static Tile createGrassTile(float x, float y) {
		return new Tile(GRASS_PATH, x, y, new String[0]);
	}
	
	
	/** Static method to create a water tile.
	 * @param x X position.
	 * @param y Y position
	 * @return Tile Water tile.
	 */
	public static Tile createWaterTile(float x, float y) {
		return new Tile(WATER_PATH, x, y, new String[] {LETHAL});
	}
	
	
	/** Static method to create a tree tile.
	 * @param x X position.
	 * @param y Y position
	 * @return Tile Tree tile.
	 */
	public static Tile createTreeTile(float x, float y) {
		return new Tile(TREE_PATH, x, y, new String[] {SOLID});
	}
	
	
	/** Constructor.
	 * @param imgPath Path to sprite's image.
	 * @param x Starting x position of sprite.
	 * @param y Starting y position of sprite.
	 * @param tags Tags of sprite.
	 */
	public Tile(String imgPath, float x, float y, String[] tags) {
		super(imgPath, x, y, tags);
	}

	
	/** Update method.
	 * @param gc The Slick game container.
	 * @param delta Time passed since last frame (milliseconds).
	 */
	@Override
	public void update(GameContainer gc, int delta) 
			throws SlickException {
		// tile has nothing to do with updating (at this stage)
		return;
	}	
	
}
