/**
 * Project for SWEN20003: Object Oriented Software Development 2018
 * by Xuanken Tay, University of Melbourne
 */

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.*;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Abstract game state class containing attributes for data passing between
 * states.
 */
public abstract class State extends BasicGameState {
	/** x position to render current level */
	public static final int LEVEL_X = 0;
	/** y position to render current level */
	public static final int LEVEL_Y = 20;
	/** index of name of sprite in line from csv */
	public static final int CELL_NAME = 0;
	/** index of x position of sprite in line from csv */
	public static final int CELL_X = 1;
	/** index of y position of sprite in line from csv */
	public static final int CELL_Y = 2;
	/** index of (boolean isMoveRight) of sprite in line from csv */
	public static final int CELL_MOVE_RIGHT = 3;

	
	/** total time in playing the game */
	private float totalTime;

	/** history of all previous game state */
	private ArrayList<Integer> historyState = new ArrayList<>();

	/** fonts for rendering level and time */
	private Font font = new Font("Verdana", Font.BOLD, 20);
	
	/** nicer fonts for rendering level and time */
	private TrueTypeFont trueTypeFont = new TrueTypeFont(font, true);

	
	
	
	/** Initialise the state (meant to be overriden).
	 * @param gc The Slick game container object.
	 * @param sbg The game holding this state.
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) 
			throws SlickException {
		return;
	}
	
	/**
	 * Update the game for a frame.
	 * @param gc    The Slick game container object.
	 * @param sbg   The game holding this state.
	 * @param delta Time passed since last frame (milliseconds).
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) 
			throws SlickException {
		// increment the total time
		totalTime += delta * App.MILLISECOND;
	}

	/**
	 * Render the entire screen, so it reflects the current game state.
	 * 
	 * @param gc  The Slick game container object.
	 * @param sbg The game holding this state.
	 * @param g   The Slick graphics object, used for drawing.
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
			throws SlickException {
		// render total time
		renderString(String.format("Time: %.1f", totalTime), 0, 0, Color.green);
	}

	/**
	 * Enter normal game mode (Proj2).
	 * 
	 * @param gc  The Slick game container object.
	 * @param sbg The game holding this state.
	 */
	public void enterNormal(GameContainer gc, StateBasedGame sbg) {
		enterState(gc, sbg, new NormalGameState());
	}

	/**
	 * Enter endless game mode (Extension)
	 * 
	 * @param gc  The Slick game container object.
	 * @param sbg The game holding this state.
	 */
	public void enterEndless(GameContainer gc, StateBasedGame sbg) {
		enterState(gc, sbg, new EndlessGameState());
	}

	/**
	 * Enter gameover screen.
	 * 
	 * @param gc    The Slick game container object.
	 * @param sbg   The game holding this state.
	 * @param level Total level passed.
	 */
	public void enterGameOver(GameContainer gc, StateBasedGame sbg, int level) {
		enterState(gc, sbg, new GameOverState());
		((GameOverState) sbg.getState(GameOverState.ID)).setLastLevel(level);
	}

	/**
	 * Get the total gameplay time.
	 * 
	 * @return float Gameplay time.
	 */
	public float getTime() {
		return totalTime;
	}

	/**
	 * Get all previous game state.
	 * 
	 * @return ArrayList<Integer> An array list containing previous states.
	 */
	public ArrayList<Integer> getHistory() {
		return historyState;
	}

	/**
	 * Configure player from last game state.
	 * 
	 * @param x New x position for player.
	 * @param y New y position for player.
	 * @return Player Same copy of player (singleton).
	 */
	public Player setupPlayer(float x, float y) {
		// configure player from last game state
		Player player = Player.getPlayer();
		player.setMove(x, y);
		player.setRespawnPosition(x, y);
		return player;
	}

	/**
	 * Helper function to render a multiline string.
	 * 
	 * @param text  String to be rendered.
	 * @param x     Render at x position.
	 * @param y     Render at y position.
	 * @param color Color of the font.
	 */
	public void renderString(String text, float x, float y, Color color) {
		for (String line : text.split("\n")) {
			trueTypeFont.drawString(x, y, line, color);
			y += trueTypeFont.getHeight();
		}
	}

	/**
	 * read in csv from "assets"
	 * 
	 * @param level The level or string indicating which csv to read from.
	 * @return ArrayList<Sprite> Sprites to be created from csv.
	 */
	public ArrayList<Sprite> readCsv(String level) {
		ArrayList<Sprite> sprites = new ArrayList<Sprite>();
		String infile = "assets/levels/" + level + ".lvl";

		try (BufferedReader br = new BufferedReader(new FileReader(infile))) {
			String text;
			while ((text = br.readLine()) != null) {
				String cells[] = text.split(",");

				// read components of each line
				String objectName = cells[CELL_NAME];
				float x = Float.parseFloat(cells[CELL_X]);
				float y = Float.parseFloat(cells[CELL_Y]);
				boolean moveRight = false;
				try {
					moveRight = Boolean.parseBoolean(cells[CELL_MOVE_RIGHT]);
				} catch (Exception e) {
					;
				}

				switch (objectName) {
				// is a tile
				case "grass":
					sprites.add(Tile.createGrassTile(x, y));
					break;
				case "water":
					sprites.add(Tile.createWaterTile(x, y));
					break;
				case "tree":
					sprites.add(Tile.createTreeTile(x, y));
					break;

				// is a vehicle
				case "bus":
					sprites.add(Vehicle.createBus(x, y, moveRight));
					break;
				case "racecar":
					sprites.add(Vehicle.createRacecar(x, y, moveRight));
					break;
				case "bike":
					sprites.add(Vehicle.createBike(x, y, moveRight));
					break;
				case "bulldozer":
					sprites.add(Vehicle.createBulldozer(x, y, moveRight));
					break;

				// is water transport
				case "log":
					sprites.add(WaterTransport.createLog(x, y, moveRight));
					break;
				case "longLog":
					sprites.add(WaterTransport.createLonglog(x, y, moveRight));
					break;
				case "turtle":
					sprites.add(WaterTransport.createTurtle(x, y, moveRight));
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sprites;
	}

	
	private void enterState(GameContainer gc, StateBasedGame sbg, 
			GameState nextState) {
		// enter another state and pass data
		historyState.add(sbg.getCurrentStateID());
		sbg.addState(nextState);
		try {
			// initialise state
			nextState.init(gc, sbg);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		;
		sbg.enterState(nextState.getID(), new EmptyTransition(), 
						new VerticalSplitTransition());
		// pass data
		((State) nextState).totalTime = this.totalTime;
		((State) nextState).historyState.addAll(this.historyState);
	}

}