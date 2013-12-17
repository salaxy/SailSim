package de.fhb.sailsim.userinterface.slick;

import java.util.Vector;
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



	int width = 1024;
	int height = 768;

	// Spielkonstanten
	public static final int MOUSE_LEFT = 0;
	public static final int MOUSE_RIGHT = 1;

	// Konstanten fuer Spielerzugehoerigheit
	public static final int PLAYER_ONE_ID = 0;
	public static final int PLAYER_TWO_ID = 1;
	public static final int PLAYER_SYSTEM_ID = 2;

	// Ursprungspunkte fuer Spielerviews
	public final static Vector2f ORIGIN_POSITION_LEFT = new Vector2f(512f, 0f);
	public final static Vector2f ORIGIN_POSITION_RIGHT = new Vector2f(512f, 768f);

	/**
	 * Globale Liste an zu zeichnende Objekte/Units
	 */
	private CopyOnWriteArrayList<Unit> globalUnits;

	/**
	 * Spielerobjekt Spieler Eins
	 */
	private Player playerOne;

	/**
	 * Spielerobjekt Spieler Zwei
	 */
	private Player playerTwo;

	/**
	 * Spielerobjekt Spieler System
	 */
	private Player playerSystem;

	/**
	 * Spielkarte
	 */
	private Gamemap map;



	 /**
	 * Gamemap
	 */
	 private Gamemap gameMap;


	public DefenderControl() {

		// map init
		map = new Gamemap();

		// die beiden Spieler initialisieren
		playerOne = new Player(this, 90, 0.5f, ORIGIN_POSITION_LEFT, Color.blue, PLAYER_ONE_ID);
		playerTwo = new Player(this, 270, 0.5f, ORIGIN_POSITION_RIGHT, Color.green, PLAYER_TWO_ID);
		playerTwo.setOriginOffset(new Vector2f(0,320));
		playerSystem = new Player(this, 0, 1f, ORIGIN_POSITION_RIGHT, Color.black, PLAYER_SYSTEM_ID);

		globalUnits = new CopyOnWriteArrayList<Unit>();

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
		graphics.setClip(0, 0, 510, 768);

		// Feld zeichnen
		this.map.zeichne(graphics, playerOne);

		// units playerOne zeichen
		for (Unit unit : globalUnits) {
				// zeichne Aktiviert
				unit.paint(this.playerOne, graphics, true);
				// zeichen normal
				unit.paint(this.playerOne, graphics, false);
		}

		// info zeichnen
		graphics.setColor(Color.black);
		graphics.rotate(0, 0, 90);
//		graphics.scale(1.2f, 1.2f);
		// display.drawString("Aktuelles Gebäude: " +
		// menuePlayerOne.getActualBuildingName(), 100, -15);

		graphics.resetTransform();
		// zeichenbereich leoschen
		graphics.clearClip();

		// Rechte Seite zeichnen
		// zeichenbereich setzen
		graphics.setClip(512, 0, 514, 768);

		// Feld zeichnen
		this.map.zeichne(graphics, playerTwo);
		graphics.resetTransform();


		// info zeichnen
		graphics.setColor(Color.black);
		graphics.translate(510, 768);
		graphics.rotate(0, 0, -90);
		// display.scale(1.05f, 1.05f);

		graphics.drawString("Credits: " + playerTwo.getCredits(), 25, 490);
		// display.drawString("Aktuelles Gebäude: " +
		// menuePlayerTwo.getActualBuildingName(), 100, 490);

		graphics.resetTransform();
		// zeichenbereich leoschen
		graphics.clearClip();

		// Trennlinie zeichnen
		graphics.setColor(Color.black);
		graphics.drawLine(511f, 0f, 511f, 768f);
		graphics.drawLine(512f, 0f, 512f, 768f);
		graphics.drawLine(513f, 0f, 513f, 768f);

	}

	public void updateGame() {
		//TODO stelle ist wichtig
		// Berechnen der Positionen aller Units
		for (Unit unit : globalUnits) {
			unit.update();
		}
	}


	public Player getPlayerOne() {
		return playerOne;
	}

	public Player getPlayerTwo() {
		return playerTwo;
	}

//	private void playBackgroundSound() {
//		try {
//			new SampleThread("/data/sounds/background.mp3", 10.0f, true).start();
//		} catch (FormatProblemException e) {
//			e.printStackTrace();
//		}
//	}



	

	/**
	 * Zoomen der Anzeige
	 * 
	 * @param oldx
	 * @param oldy
	 * @param newx
	 * @param newy
	 */
	public void zoomInterface(int oldx, int oldy, int newx, int newy) {
		if (newx < 512) {

//			 Vector2f old=GraphicTools.calcInputVector(new Vector2f(newx,
//			 newy), this.playerOne);
//
//			this.getPlayerOne().setActualZoom(this.getPlayerOne().getActualZoom() + (newy - oldy) * 0.001f);
//
//			 Vector2f neu=GraphicTools.calcInputVector(new Vector2f(newx,
//			 newy), this.playerOne);
////			 Experiment Zoompunkt setzen
//			 Vector2f difference=VectorHelper.sub(old,neu);
//			 this.getPlayerOne().setOriginOffset(this.getPlayerOne().getOriginOffset().sub(difference));
//			 this.getPlayerOne().getOriginOffset().scale(1 +(( oldy - newy ) *
//			 0.01f));

		} else {
			this.getPlayerTwo().setActualZoom(this.getPlayerTwo().getActualZoom() + (newy - oldy) * 0.001f);
		}
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

		if (newx < 512) {
			
			Vector2f tempVec = this.getPlayerOne().getOriginOffset().copy();
			tempVec.y = tempVec.y + newy - oldy;
			tempVec.x = tempVec.x + newx - oldx;
			
			this.getPlayerOne().setOriginOffset(tempVec);
		} else {
			
			Vector2f tempVec = this.getPlayerTwo().getOriginOffset().copy();
			tempVec.y = tempVec.y + newy - oldy;
			tempVec.x = tempVec.x + newx - oldx;
			
			this.getPlayerTwo().setOriginOffset(tempVec);
		}
	}


	public void schiebeInterfacePlayerTwo(float oldx, float oldy, float newx, float newy){
		if(newx>=512) {
		
		Vector2f tempVec = this.getPlayerTwo().getOriginOffset().copy();
		tempVec.y = tempVec.y + newy - oldy;
		tempVec.x = tempVec.x + newx - oldx;
		
		this.getPlayerTwo().setOriginOffset(tempVec);
	}
	}
	
	/**
	 * Creating some Test Units
	 */
	public void createTestUnits() {
		
		new Unit(100, 200, Unit.MODE_NORMAL, this);

	}
	
	/**
	 * Creating some Test Units
	 */
	public void createPraesentationUnits() {

		
		new Unit(900, 1120, Unit.MODE_NORMAL, this);

	}
}
