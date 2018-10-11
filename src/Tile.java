/**
 * Project for SWEN20003: Object Oriented Software Development 2018
 * by Xuanken Tay, University of Melbourne
 */


/**
 * Represent a single Tile. Inherits from Sprite.
 */
public class Tile extends Sprite {
	/** image path of grass tile */
	public static final String GRASS_PATH = "assets/grass.png";
	/** image path of water tile */
	public static final String WATER_PATH = "assets/water.png";
	/** image path of tree tile */
	public static final String TREE_PATH = "assets/tree.png";
	
	
	
	
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
	
	private Tile(String imgPath, float x, float y, String[] tags) {
		super(imgPath, x, y, tags);
	}

	

}
