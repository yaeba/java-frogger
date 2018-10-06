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
import org.newdawn.slick.state.StateBasedGame;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;


public abstract class State extends BasicGameState {
	/** index of name of sprite in line from csv */
	public static final int CELL_NAME = 0;
	/** index of x position of sprite in line from csv */
	public static final int CELL_X = 1;
	/** index of y position of sprite in line from csv */
	public static final int CELL_Y = 2;
	/** index of (boolean isMoveRight) of sprite in line from csv */
	public static final int CELL_MOVE_RIGHT = 3;
	

	private float time;
	private Font font = new Font("Verdana", Font.BOLD, 20);
	private TrueTypeFont trueTypeFont = new TrueTypeFont(font, true);
	


	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) 
			throws SlickException {
		time += delta * App.MILLISECOND;
	}
	
	
	
	/** Render the time.
     * @param gc The Slick game container object.
	 * @param sbg The game holding this state.
     * @param g The Slick graphics object, used for drawing.
     */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
			throws SlickException {
		trueTypeFont.drawString(0, 0, String.format("Time: %.1f", time), 
							Color.green);
	}
	
	
	public void enterState(StateBasedGame sbg, int nextStateID, int lives) {
		sbg.enterState(nextStateID);
		State nextState = (State) sbg.getState(nextStateID);
		nextState.setPlayerLives(lives);
		nextState.setTime(time);
	}
	
	public void setPlayerLives(int lives) {
		// meant to be overriden
		return;
	}
	
	public void setTime(float time) {
		this.time = time;
	}
	
	public float getTime() {
		return time;
	}
	
	/** read in csv from "assets" 
	 * @param level The level or string indicating which csv to read from.
	 */
	public ArrayList<Sprite> readCsv(String level) {
		ArrayList<Sprite> sprites = new ArrayList<Sprite>();
		String infile = "assets/levels/" + level + ".lvl";
		
		try (BufferedReader br =
				new BufferedReader(new FileReader(infile))) {
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
						sprites.add(WaterTransport.createLonglog(x, y, 
								moveRight));
						break;
					case "turtle":
						sprites.add(WaterTransport.createTurtle(x, y,
								moveRight));
						break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sprites;
	}
}