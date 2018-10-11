
/**
 * Project for SWEN20003: Object Oriented Software Development 2018	
 * by Xuanken Tay, University of Melbourne
 */

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Main class for the game. Startup the game.
 */
public class App extends StateBasedGame {
	/** screen width, in pixels */
	public static final int SCREEN_WIDTH = 1024;
	/** screen height, in pixels */
	public static final int SCREEN_HEIGHT = 768;
	/** tile size, in pixels */
	public static final int TILE_SIZE = 48;
	/** one millisecond */
	public static final float MILLISECOND = 0.001f;

	/** Constructor */
	public App() {
		super("Shadow Leap");
	}

	/**
	 * Start-up method. Creates the game and runs it.
	 * 
	 * @param args Command-line arguments (ignored).
	 */
	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new App());
		app.setShowFPS(false);
		app.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
		app.start();
	}

	/**
	 * Initialise the list of states making up this game. Successive state will
	 * be added as game advances to save memory.
	 * 
	 * @param gc The container holding the game.
	 */
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		addState(new MenuState());
	}

}