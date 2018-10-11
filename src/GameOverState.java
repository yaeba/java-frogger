/**
 * Project for SWEN20003: Object Oriented Software Development 2018
 * by Xuanken Tay, University of Melbourne
 */


import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import java.util.ArrayList;


/**
 * Game Over screen for the game.
 * Render some images and display score
 */
public class GameOverState extends State {
	/** ID given to this state */
	public static final int ID = 3;
	/** path to gameover image */
	public static final String GAMEOVER_IMG = "assets/gameover.jpg";
	/** path to sadfrog1 image */
	public static final String SADFROG_1 = "assets/sadfrog1.jpeg";
	/** path to sadfrog2 image */
	public static final String SADFROG_2 = "assets/sadfrog2.jpg";
	/** path to boiling frog image */
	public static final String BOILING_FROG_IMG = "assets/boilingfrog.png";
	/** array holding details for rendering gameover image */
	public static final float[] GAME_OVER = {320, 0, 0.5f};
	/** array holding details for rendering sadfrogs image */
	public static final float[] SAD_FROGS = {0, 150, 3f};
	/** array holding details for rendering boiling frog image */
	public static final float[] BOILING_FROG = {700, 450, 1.8f};
	/** time to toggle between sadfrog1 and sadfrog2 */
	public static final float CHANGE_PERIOD = 0.5f;
	/** x position to render score */
	public static final float SCORE_X = 600;
	/** y position to render score */
	public static final float SCORE_Y = 200;
	/** comment to be displayed */
	public static final String LAST_COMMENT = "They say real frog plays this "
									+ "poorly\nPress ENTER to exit\n";
	/** comment if player lost from menu */
	public static final String NULL_COMMENT = "YOU HAVE NOT EVEN PLAYED THE "
									+ "GAME, COWARD!";
	/** comment on time taken */
	public static final String TIME_COMMENT = "YOU HAVE WASTED %.2f SECONDS "
									+"OF YOUR LIFE\n";
	
	/** gameover image */
	private Image gameOver;
	/** sadfrog1 image */
	private Image sadFrog1;
	/** sadfrog2 image */
	private Image sadFrog2;
	/** boilingfrog image */
	private Image boilingFrog;
	/** image that is currently showing (sadfrog1 or 2). */
	private Image showing;
	/** level passed before losing */
	private int lastLevel;
	/** timer used for toggling images */
	private float imageTimer = 0;
	
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) 
			throws SlickException {
		// create all images
		sadFrog1 = new Image(SADFROG_1);
		sadFrog2 = new Image(SADFROG_2);
		showing = sadFrog1;
		boilingFrog = new Image(BOILING_FROG_IMG);
		gameOver=  new Image(GAMEOVER_IMG);
	}

	

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) 
			throws SlickException {
		
		imageTimer += delta * App.MILLISECOND;
		if (imageTimer > CHANGE_PERIOD) {
			toggleImage();
			imageTimer = 0;
		}
		Input input = gc.getInput();
		if (input.isKeyPressed(Input.KEY_ENTER)) {
			gc.exit();
		}
		
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
			throws SlickException {
		// render all images
		gameOver.draw(GAME_OVER[0], GAME_OVER[1], GAME_OVER[2]);
		boilingFrog.draw(BOILING_FROG[0], BOILING_FROG[1], BOILING_FROG[2]);
		
		showing.draw(SAD_FROGS[0], SAD_FROGS[1], SAD_FROGS[2]);
		
		drawText(0, 0);
		drawScore(SCORE_X, SCORE_Y);
	}

	@Override
	public int getID() {
		return ID;
	}

	/** Set the last level passed (for information passing across states).
	 * @param level Levels passed before losing.
	 */
	public void setLastLevel(int level) {
		lastLevel = level;
	}
	
	
	private void toggleImage() {
		showing = showing.equals(sadFrog1) ? sadFrog2 : sadFrog1;
	}

	private void drawText(float x, float y) {
		// draw the comment on screen
		String text;
		if (getTime() == 0) {
			text = NULL_COMMENT;
		} else {
			text = String.format(TIME_COMMENT, getTime());
			text += LAST_COMMENT;
		}
		renderString(text, x, y, Color.white);
	}
	
	private void drawScore(float x, float y) {
		// draw score of levels passed and time taken
		if (getTime() == 0) {
			return;
		}
		String score = "";
		ArrayList<Integer> history = getHistory();
		if (history.contains(NormalGameState.ID)) {
			score += "Proj2";
		}
		if (history.contains(EndlessGameState.ID)) {
			score += score.isEmpty() ? "Endless" : "+Endless";
		}
		score += String.format(": level %d, %.1fs", lastLevel, getTime());
		renderString("Score \n" + score, x, y, Color.cyan);
	}
	
}