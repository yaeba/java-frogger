/**
 * Project for SWEN20003: Object Oriented Software Development 2018
 * by Xuanken Tay, University of Melbourne
 */

import org.newdawn.slick.GameContainer;	
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.Arrays;
import utilities.BoundingBox;


/**
 * Abstract Sprite class.
 * Contains general methods such as getters, setters and handles rendering.
 */
public abstract class Sprite {
	/** tag indicating sprite can kill player */
	public static final String LETHAL = "lethal";
	/** tag indicating sprite is solid */
	public static final String SOLID = "solid";
	/** tag indicating sprite is floating */
	public static final String FLOATING = "floating";
	/** tag indicating sprite can reverse direction */
	public static final String REVERSIBLE = "reversible";
	/** tag indicating sprite can dive */
	public static final String DIVEABLE = "diveable";
	/** tag indicating (some) sprites can be flipped horizontally */
	public static final String FLIPPABLE = "flippable";

	
	
	/** image of sprite */
	private Image image;
	
	/** x, y position of sprite */
	private float x, y;
	
	/** tags (properties) associated with sprite */
	private ArrayList<String> tags;
	
	/** for collision detection */
	private BoundingBox bounds;
	
	
	/** Constructor.
	 * @param imgPath Path to sprite's image.
	 * @param x Starting x position of sprite.
	 * @param y Starting y position of sprite.
	 */
	public Sprite(String imgPath, float x, float y) {
		createSprite(imgPath, x, y);
	}
	

	/** Constructor of sprite with predefined tags.
	 * @param imgPath Path to sprite's image.
	 * @param x Starting x position of sprite.
	 * @param y Starting y position of sprite.
	 * @param tags Tags associated with sprite.
	 */
	public Sprite(String imgPath, float x, float y, String[] tags) {
		createSprite(imgPath, x, y);
		this.tags = createTags(tags);
	}
	
	
	/** Dummy update method to be overriden by subclasses.
	 * @param gc The Slick game container.
	 * @param delta Time passed since last frame (milliseconds).
	 */
	public void update(GameContainer gc, int delta) 
			throws SlickException {
		return;
	}
	
	
	/** Render the sprite from top left corner.
     * @param g The Slick graphics object, used for drawing.
     */
	public void render(Graphics g) throws SlickException {
		g.drawImage(image, x-image.getWidth()/2, y-image.getHeight()/2);
	}
		
	
	/** Setter for position of sprite, and update bounds.
	 * @param x Destination x position.
	 * @param y Destination y position.
	 */
	public void setMove(float x, float y) {
		this.x = x;
		this.y = y;
		this.bounds.setX(x);
		this.bounds.setY(y);
	}
	
	
	/** Getter for x position of sprite.
	 * @return float Current x position of sprite.
	 */
	public float getX() {
		return this.x;
	}
	
	/** Getter for y position of sprite.
	 * @return float Current y position of sprite.
	 */
	public float getY() {
		return this.y;
	}

	/** Getter for image of sprite.
	 * @return Image image.
	 */
	public Image getImage() {
		return this.image;
	}
	
	/** Getter for image width of sprite.
	 * @return float Image width.
	 */
	public float getWidth() {
		return this.image.getWidth();
	}
	
	/** Getter for image height of sprite.
	 * @return float Image height.
	 */
	public float getHeight() {
		return this.image.getHeight();
	}
	
	/** Check if sprite has certain tag.
	 * @param tag Tag of interest.
	 * @return boolean Presence of certain tag.
	 */
	public boolean hasTag(String tag) {
		for (String s : this.tags) {
			if (s.equals(tag)) {
				return true;
			}
		}
		return false;
	}
	
	/** Add a tag to sprite.
	 * @param tag Tag to be added.
	 */
	public void addTag(String tag) {
		this.tags.add(tag);
	}
	
	/** Remove a tag from sprite.
	 * @param tag Tag to be removed.
	 */
	public void removeTag(String tag) {
		this.tags.remove(tag);
	}
	
	/** Check if this sprite collides with another sprite.
	 * @param other Another sprite.
	 * @return boolean Are they in collision?
	 */
	public boolean collidesWith(Sprite other) {
		if (this.bounds.intersects(other.bounds)) {
				return true;
		}

		return false;
	}
	
	/** Some sprite can kill player.
	 * @param player Reference to player.
	 */
	public void killPlayer(Player player) {
		player.die();
	}
	
	/** Check if sprite is on screen entirely.
	 * @return boolean Sprite is on screen.
	 */
	public boolean onScreen() {
		return onScreen(getX(), getY());
	}
	
	/** Check if positions are on screen entirely.
	 * @param x X position of sprite.
	 * @param y Y position of sprite.
	 * @return boolean Sprite is on screen.
	 */
	public boolean onScreen(float x, float y) {
		float width = image.getWidth();
		return (x - width/2 >= 0)
				&& (x + width/2 <= App.SCREEN_WIDTH)
				&& (y - width/2 >= 0)
				&& (y + width/2 <= App.SCREEN_HEIGHT);
	}
	

	
	private void createSprite(String imgPath, float x, float y) {
		// create sprites with empty tags
		try {
			this.image = new Image(imgPath);
			this.x = x;
			this.y = y;
			this.tags = new ArrayList<>();
			this.bounds = new BoundingBox(this.image, this.x, this.y);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	private ArrayList<String> createTags(String[] tags){
		// convert string array of tags to array list of tags
		return new ArrayList<>(Arrays.asList(tags));
	}
}
