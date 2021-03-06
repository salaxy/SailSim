package de.fhb.sailsim.userinterface.slick;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * 
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 * 
 */
public class SlickView extends BasicGameState {

	public static final int FRAMERATE = 25;
	// berechne milisekunden f�r Echtzeitberechnungen
	// daher immer 1 Sekunde durch Framerate
	public static final int CALCULATION_TIME = 1000 / FRAMERATE;

	/**
	 * Bildschirmbreite
	 */
	public static final int WIDTH = 1024;

	/**
	 * Bildschirmh�he
	 */
	public static final int HEIGHT = 768;

	/**
	 * GameState ID (nicht ver�ndern!)
	 */
	public static final int ID = 0;

	/**
	 * Control des Gesamtprogramms
	 */
	private ViewControl control;

	private int mouseButton = 0;

	/**
	 * initialisierung
	 */
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {

		control = new ViewControl();

		gc.setShowFPS(true);
		gc.setTargetFrameRate(FRAMERATE);
	}

	/**
	 * Berechnung, die jeden Frame gemacht werden m�ssen.
	 */
	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {

		// Abfangen der Eingabeger�te
		// Input input = gc.getInput();
		this.control.update();
	}

	/**
	 * Rendern eines Frames
	 */
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {

		g.setAntiAlias(true);
		g.setBackground(Color.white);
		
		this.control.drawViewContent(g);
	}

	/**
	 * What happens when mouse was clicked
	 */
	public void mouseClicked(int button, int x, int y, int clickCount) {

		// Klickvektor holen
		Vector2f clickVector = new Vector2f(x, y);
		Vector2f mapCoords = GraphicTools.calcInputVector(clickVector, control.getPerspectiveOne());
	}

	/**
	 * What happens when mouse is moving
	 */
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {

		if (mouseButton == 0) {
			control.slipViewPoint(oldx, oldy, newx, newy);
		}

		if (mouseButton == 1) {
			 control.zoomInterface(oldx, oldy, newx, newy);
		}
	}

	public void keyPressed(int key, char c) {

		switch (key) {
		case Input.KEY_H:
			System.out.println("pressed KEY_H");
			control.getSimulationControl().getBoatState().speedUp();
			break;
		case Input.KEY_B:
			System.out.println("pressed KEY_B");
			control.getSimulationControl().getBoatState().speedDown();
			break;
		case Input.KEY_RIGHT:
			System.out.println("pressed KEY_RIGHT");
			control.getSimulationControl().getBoatState().turnRight();
			break;
		case Input.KEY_LEFT:
			System.out.println("pressed KEY_LEFT");
			control.getSimulationControl().getBoatState().turnLeft();
			break;
		case Input.KEY_N:
			System.out.println("pressed KEY_N");
			control.getSimulationControl().getBoatState().directRight();
			break;
		case Input.KEY_M:
			System.out.println("pressed KEY_M");
			control.getSimulationControl().getBoatState().directLeft();
			break;
		case Input.KEY_SPACE:
			System.out.println("pressed KEY_SPACE");
			control.getSimulationControl().getBoatState().stop();
			break;
		case Input.KEY_W:
			System.out.println("pressed KEY_W");
			control.getSimulationControl().getEnviroment().getWindState().turnLeft();
			break;
		case Input.KEY_E:
			System.out.println("pressed KEY_E");
			control.getSimulationControl().getEnviroment().getWindState().turnRight();
			break;
		case Input.KEY_R:
			System.out.println("pressed KEY_R");
			control.getSimulationControl().getEnviroment().getWindState().raiseStrength();
			break;
		case Input.KEY_F:
			System.out.println("pressed KEY_F");
			control.getSimulationControl().getEnviroment().getWindState().reduceStrength();
			break;
		case Input.KEY_Z:
			System.out.println("pressed KEY_Z");
			control.getSimulationControl().getBoatState().setBack();
			break;
		case Input.KEY_A:
			System.out.println("pressed KEY_A");
			control.getPerspectiveOne().setBootOnCentral();
			break;
		case Input.KEY_I:
			System.out.println("pressed KEY_I");
			control.getPerspectiveOne().zoomIn();
			break;
		case Input.KEY_O:
			System.out.println("pressed KEY_O");
			control.getPerspectiveOne().zoomOut();
			break;
		}
	}

	/**
	 * What happens when mousewhell is scrolled
	 */
	public void mouseWheelMoved(int change) {
		//TODO Map zoom in and out
	}

	public void mousePressed(int button, int x, int y) {
		mouseButton = button;
	}

	@Override
	public int getID() {
		return ID;
	}

}
