package de.fhb.sailsim.userinterface.slick;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import de.fhb.sailsim.control.SimulationControl;
import de.fhb.sailsim.worldmodel.BoatHistory;

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

	private Perspective perspective;
	private Map map;
	private BoatSign boatSymbol;
	private BoatInformation boatInformation;
	private WindSign windSymbol;

	private BoatHistory historie;

	public BoatSign getBoatSymbol() {
		return boatSymbol;
	}

	public SimulationControl getSimulationControl() {
		return simulation;
	}

	public ViewControl() {
		map = new Map();
		perspective = new Perspective(this, 0, 0.6f, new Vector2f(0f, 0f),
				Color.blue);
		simulation = new SimulationControl();
		
		boatInformation =  new BoatInformation(simulation.getBoatState());
		boatSymbol = new BoatSign(simulation.getBoatState());
		windSymbol = new WindSign(simulation.getEnviroment().getWindState());
		historie = new BoatHistory();
	}

	public void drawViewContent(Graphics graphics) {
		// zeichenbereich setzen
		// graphics.setClip(0, 0, 510, 768);

		this.map.paint(graphics, perspective);
		
		this.historie.paint(this.perspective, graphics);
		this.boatSymbol.paint(this.perspective, graphics);
		this.windSymbol.paint(this.perspective, graphics);
		this.boatInformation.paint(this.perspective, graphics);

		// info zeichnen
		graphics.setColor(Color.black);
		graphics.rotate(0, 0, 90);
		graphics.scale(1.2f, 1.2f);
		graphics.drawString("Test-Information: " + perspective.getCredits(),
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
				this.perspective);

		this.getPlayerOne().setActualZoom(
				this.getPlayerOne().getActualZoom() + (newy - oldy) * 0.01f);

		Vector2f neu = GraphicTools.calcInputVector(new Vector2f(newx, newy),
				this.perspective);

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
		//calculate states
		this.simulation.execute(SlickView.CALCULATION_TIME);
		//add position to history
		this.historie.addPosition(this.simulation.getBoatState());
	}

	public Perspective getPlayerOne() {
		return perspective;
	}

	public Map getMap() {
		return map;
	}

}
