/**
 * Project for SWEN20003: Object Oriented Software Development 2018
 * by Xuanken Tay, University of Melbourne
 */


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import java.awt.Font;


/**
 * Game Over State for the game
 */
public class GameOverState extends State {
	/** The ID given to this state */
	public static final int ID = 3;
	public static final float[] GAME_OVER = {320, 0, 0.5f};
	public static final float[] SAD_FROG = {0, 150, 3f};
	public static final float[] BOILING_FROG = {700, 450, 1.8f};
	public static final float CHANGE_PERIOD = 0.5f;
	public static final String COMMENT = "They say real frog plays this "
									+ "poorly\nMy grandma's still playing "
									+ "until now\nPress ENTER to exit\n";
	
	private Image gameOver, boilingFrog;
	private Image sadFrog1, sadFrog2;
	private Image showing;
	private TrueTypeFont trueTypeFont;
	private float timeElapsed = 0;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		sadFrog1 = new Image("assets/sadfrog1.jpeg");
		sadFrog2 = new Image("assets/sadfrog2.jpg");
		showing = sadFrog1;
		boilingFrog = new Image("assets/boilingfrog.png");
		gameOver=  new Image("assets/gameover.jpg");
		Font font = new Font("Verdana", Font.BOLD, 20);
		trueTypeFont = new TrueTypeFont(font, true);
	}

	

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
		timeElapsed += delta * App.MILLISECOND;
		if (timeElapsed > CHANGE_PERIOD) {
			toggleImage();
			timeElapsed = 0;
		}
		Input input = gc.getInput();
		if (input.isKeyPressed(Input.KEY_ENTER)) {
			gc.exit();
		}
		
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		gameOver.draw(GAME_OVER[0], GAME_OVER[1], GAME_OVER[2]);
		boilingFrog.draw(BOILING_FROG[0], BOILING_FROG[1], BOILING_FROG[2]);
		
		showing.draw(SAD_FROG[0], SAD_FROG[1], SAD_FROG[2]);
		
		String text = String.format("YOU HAVE WASTED %.2f SECONDS", getTime())
					+ " OF YOUR LIFE\n";
		text += COMMENT;
		drawString(trueTypeFont, text, 0, 0);
	}

	@Override
	public int getID() {
		return ID;
	}

	private void drawString(TrueTypeFont font, String text, float x, float y) {
		for (String line : text.split("\n")) {
			font.drawString(x, y+=font.getHeight(), line);
		}
	}
	
	private void toggleImage() {
		showing = showing.equals(sadFrog1) ? sadFrog2 : sadFrog1;
	}
}