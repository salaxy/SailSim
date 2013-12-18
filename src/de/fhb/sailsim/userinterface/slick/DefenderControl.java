package de.fhb.sailsim.userinterface.slick;

import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

/**
 * Diese Klasse stellt die Verbindung zwischen Anzeige, also dem PApplet dar und
 * dem eigentlichen Spielinhalt wie die Units, Spiellogik usw. (und noch
 * zukünftige dinge)
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 */
public class DefenderControl {

	// Spielkonstanten
	public static final int MOUSE_LEFT = 0;
	public static final int MOUSE_RIGHT = 1;

	// Konstanten fuer Spielerzugehoerigheit
	public static final int PLAYER_ONE_ID = 0;
	public static final int PLAYER_TWO_ID = 1;
	public static final int PLAYER_SYSTEM_ID = 2;

	// Ursprungspunkte fuer Spielerviews
	public final static Vector2f ORIGIN_POSITION_LEFT = new Vector2f(0f, 0f);
	public final static Vector2f ORIGIN_POSITION_RIGHT = new Vector2f(512f,
			768f);

	/**
	 * Globale Liste an zu zeichnende Objekte/Units
	 */
	private CopyOnWriteArrayList<Unit> globalUnits;

	/**
	 * Spielerobjekt Spieler Eins
	 */
	private Player playerOne;

	/**
	 * Spielerobjekt Spieler System
	 */
	private Player playerSystem;

	/**
	 * Spielkarte
	 */
	private Gamemap map;

	private Unit boatDrawing;

	public Unit getBoatDrawing() {
		return boatDrawing;
	}

	public DefenderControl() {

		// map init
		map = new Gamemap();

		// die beiden Spieler initialisieren
		playerOne = new Player(this, 0, 1.5f, new Vector2f(0f, 0f), Color.blue,
				PLAYER_ONE_ID);
		globalUnits = new CopyOnWriteArrayList<Unit>();

		boatDrawing = new Unit(100, 100, Unit.MODE_NORMAL, this);

	}

	public Gamemap getMap() {
		return map;
	}

	public CopyOnWriteArrayList<Unit> getGlobalUnits() {
		return globalUnits;

	}

	public Player getPlayerSystem() {
		return playerSystem;
	}

	/**
	 * Zeichnet beide Spielfelder und Inhalte
	 */
	public void drawAll(Graphics graphics) {

		// Linke Seite zeichnen
		// zeichenbereich setzen
		// graphics.setClip(0, 0, 510, 768);

		// Feld zeichnen
		this.map.zeichne(graphics, playerOne);

		// units playerOne zeichen
		for (Unit unit : globalUnits) {
			unit.paint(this.playerOne, graphics, false);
		}

		// info zeichnen
		graphics.setColor(Color.black);
		graphics.rotate(0, 0, 90);
		// graphics.scale(1.2f, 1.2f);
		// display.drawString("Aktuelles Gebäude: " +
		// menuePlayerOne.getActualBuildingName(), 100, -15);
		graphics.drawString("Credits: " + playerOne.getCredits(), 100, -15);

		graphics.resetTransform();
		// zeichenbereich leoschen
		graphics.clearClip();

	}

	public void updateGame() {
		// TODO stelle ist wichtig
		// Berechnen der Positionen aller Units
		for (Unit unit : globalUnits) {
			unit.update();
		}
	}

	public Player getPlayerOne() {
		return playerOne;
	}

	// /**
	// * Zoomen der Anzeige
	// *
	// * @param oldx
	// * @param oldy
	// * @param newx
	// * @param newy
	// */
	// public void zoomInterface(int oldx, int oldy, int newx, int newy) {
	//
	// Vector2f old=GraphicTools.calcInputVector(new Vector2f(newx,
	// newy), this.playerOne);
	//
	// this.getPlayerOne().setActualZoom(this.getPlayerOne().getActualZoom() +
	// (newy - oldy) * 0.01f);
	//
	// Vector2f neu=GraphicTools.calcInputVector(new Vector2f(newx,
	// newy), this.playerOne);
	//
	// // Experiment Zoompunkt setzen
	// Vector2f difference=VectorHelper.sub(old,neu);
	// this.getPlayerOne().setOriginOffset(this.getPlayerOne().getOriginOffset().sub(difference));
	// this.getPlayerOne().getOriginOffset().scale(1 +(( oldy - newy ) *
	// 0.01f));
	// }

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

}
