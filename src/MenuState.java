/**
 * Project for SWEN20003: Object Oriented Software Development 2018
 * by Xuanken Tay, University of Melbourne
 */


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Graphics;
import java.util.ArrayList;

/**
 * Menu for the game. Creates a simple interactive World as Menu.
 * Player can enter into Normal or Endless mode.
 */
public class MenuState extends State {
	/** The ID given to this state */
	public static final int ID = 0;
	/** Player starting x position */
	public static final float PLAYER_X = App.SCREEN_WIDTH/2;
	/** Player starting y position */
	public static final float PLAYER_Y = App.SCREEN_HEIGHT/2+100+App.TILE_SIZE;
	/** Lower and upper bounds of tree x position */
	public static final float[] TREE_X = {368, 704};
	/** Lower and upper bounds of tree y position */
	public static final float[] TREE_Y = {244, 724};
	/** X position to enter normal mode */
	public static final float NORMAL_GOAL_X = 464;
	/** Y position to enter normal mode */
	public static final float NORMAL_GOAL_Y = 292;
	/** X position to enter endless mode */
	public static final float ENDLESS_GOAL_X = 560;
	/** Y position to enter endless mode */
	public static final float ENDLESS_GOAL_Y = 292;
	/** Offset between texts */
	public static final float TEXT_OFFSET = App.TILE_SIZE/2;
	

	/** Singleton player */
	private Player player;
	
	/** Simple interactive world */
	private World world;
	
	/** Goal to enter normal mode */
	private Goal normalGoal = Goal.createGoal(NORMAL_GOAL_X, NORMAL_GOAL_Y);
	
	/** Goal to enter endless mode */
	private Goal endlessGoal = Goal.createGoal(ENDLESS_GOAL_X, ENDLESS_GOAL_Y);


	
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) 
			throws SlickException {
		// create singleton player
		this.player = Player.createPlayer(PLAYER_X, PLAYER_Y);
		this.player.setRespawnPosition(PLAYER_X, PLAYER_Y);
		this.world = createMenuWorld();
	}


	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) 
			throws SlickException {
		world.update(gc, delta);
		
		// check if game is over
		if (world.isGameOver()) {
			enterGameOver(gc, sbg, 0);
		}
		if (normalGoal.isFilled()) {
			enterNormal(gc, sbg);
		} else if (endlessGoal.isFilled()) {
			enterEndless(gc, sbg);
		}
	}
	

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
			throws SlickException {
		
		world.render(g);
		g.drawString("Proj2", NORMAL_GOAL_X-TEXT_OFFSET, NORMAL_GOAL_Y);
		g.drawString("Endless", ENDLESS_GOAL_X-TEXT_OFFSET, ENDLESS_GOAL_Y);
		super.render(gc, sbg, g);
	}

	
	
	@Override
	public int getID() {
		return ID;
	}
	
	
	private World createMenuWorld() {
		// create the menu world
		ArrayList<Sprite> sprites = createTiles();
		Goal[] goals = new Goal[] {normalGoal, endlessGoal};
		try {
			return new World(this.player, sprites, goals);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	private ArrayList<Sprite> createTiles() {
		// create all the tree tiles and goals in menu
		float tile = App.TILE_SIZE;
		ArrayList<Sprite> sprites = new ArrayList<Sprite>();
		for (float y=TREE_Y[0]; y<TREE_Y[1]; y+=tile) {
			for (float x=TREE_X[0]; x<TREE_X[1]; x+=tile) {
				if (y == TREE_Y[0] || y == TREE_Y[1]-tile
					|| x == TREE_X[0] || x == TREE_X[1]-tile) {
					sprites.add(Tile.createTreeTile(x, y));
				}
				else if (y == TREE_Y[0]+tile
						|| y == TREE_Y[1]-2*tile
						|| y == TREE_Y[1]-3*tile) {
					if (y == TREE_Y[0]+tile && 
							(x == NORMAL_GOAL_X || x == ENDLESS_GOAL_X)) {
						sprites.add(Tile.createGrassTile(x, y));
					} else {
						sprites.add(Tile.createWaterTile(x, y));
					}
				}
			}
		}
		
		return sprites;
	}
}