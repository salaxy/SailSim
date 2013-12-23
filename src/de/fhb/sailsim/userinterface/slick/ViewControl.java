package de.fhb.sailsim.userinterface.slick;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import de.fhb.sailsim.control.SimulationControl;

/**
 * Diese Klasse stellt die Verbindung zwischen Anzeige und Simulationslogik dar
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 */
public class ViewControl {

	private SimulationControl simulation;
	
	public static final int MOUSE_LEFT = 0;
	public static final int MOUSE_RIGHT = 1;

	// Ursprungspunkte fuer Spielerviews
	public final static Vector2f ORIGIN_POSITION_LEFT = new Vector2f(0f, 0f);

	private Perspective perspectiveOne;
	private Map map;
	private BoatSign boatSymbol;

	public BoatSign getBoatSymbol() {
		return boatSymbol;
	}

	public void setBoatSymbol(BoatSign boatSymbol) {
		this.boatSymbol = boatSymbol;
	}

	public SimulationControl getSimulation() {
		return simulation;
	}

	public ViewControl() {
		map = new Map();
		perspectiveOne = new Perspective(this, 0, 1.5f, new Vector2f(0f, 0f),
				Color.blue);
		boatSymbol = new BoatSign(0, 0, BoatSign.MODE_NORMAL, this);
		simulation = new SimulationControl();
	}

	public void drawViewContent(Graphics graphics) {
		// zeichenbereich setzen
		// graphics.setClip(0, 0, 510, 768);

		this.map.paint(graphics, perspectiveOne);
		this.boatSymbol.paint(this.perspectiveOne, graphics, false);

		// info zeichnen
		graphics.setColor(Color.black);
		graphics.rotate(0, 0, 90);
		graphics.scale(1.2f, 1.2f);
		graphics.drawString("Test-Information: " + perspectiveOne.getCredits(),
				100, -15);

		graphics.resetTransform();
		// zeichenbereich leoschen
		// graphics.clearClip();
	}

	/**
	 * Zoomen der Anzeige
	 * 
	 * @param oldx
	 * @param oldy
	 * @param newx
	 * @param newy
	 */
	public void zoomInterface(int oldx, int oldy, int newx, int newy) {

		Vector2f old = GraphicTools.calcInputVector(new Vector2f(newx, newy),
				this.perspectiveOne);

		this.getPlayerOne().setActualZoom(
				this.getPlayerOne().getActualZoom() + (newy - oldy) * 0.01f);

		Vector2f neu = GraphicTools.calcInputVector(new Vector2f(newx, newy),
				this.perspectiveOne);

		// Experiment Zoompunkt setzen
		Vector2f difference = VectorHelper.sub(old, neu);
		this.getPlayerOne().setOriginOffset(
				this.getPlayerOne().getOriginOffset().sub(difference));
		this.getPlayerOne().getOriginOffset()
				.scale(1 + ((oldy - newy) * 0.01f));
	}

	/**
	 * Schieben der anzeige
	 * 
	 * @param oldx
	 * @param oldy
	 * @param newx
	 * @param newy
	 */
	public void schiebeInterface(float oldx, float oldy, float newx, float newy) {

		Vector2f tempVec = this.getPlayerOne().getOriginOffset().copy();
		tempVec.y = tempVec.y + newy - oldy;
		tempVec.x = tempVec.x + newx - oldx;
		
		this.getPlayerOne().setOriginOffset(tempVec);
	}

	public BoatSign getBoatDrawing() {
		return boatSymbol;
	}

	public void updateGame() {
		this.simulation.execute(SlickView.CALCULATION_TIME);
		this.boatSymbol.setPosition(this.simulation.getBoatState().getPosition());
		this.boatSymbol.setDirection(this.simulation.getBoatState().getDirection());
//		this.getBoatDrawing().update();
	}

	public Perspective getPlayerOne() {
		return perspectiveOne;
	}

	public Map getMap() {
		return map;
	}

}
