package de.fhb.sailsim.userinterface.slick;

import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

/**
 * Dies Klasse beinhaltet einstellungen und Positionen eines Spielers
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 * 
 */
public class Player {

	/**
	 * generelle Drehung der View des Spielers
	 */
	private float generalAngle;

	/**
	 * Position des Ursprungs auf dem Bildschirm fuer den Spieler
	 */
	private Vector2f originPosition;

	private final float zoomMin = 0.3f;
	private final float zoomMax = 0.55f;

	/**
	 * Relative Position der Sicht im Verhältnis zum Ursprung (originPosition)
	 * des Spielers als Vektor
	 */
	private Vector2f originOffset = new Vector2f(0, 0);

	// /**
	// * Absoluter Sichtpunkt auf der karte
	// */
	// private Vector2f absolutViewPoint= new Vector2f(0,0);
	//
	// public Vector2f getAbsolutViewPoint() {
	// return absolutViewPoint;
	// }

	// public void setAbsolutViewPoint(Vector2f absolutViewPoint) {
	// this.absolutViewPoint = absolutViewPoint;
	// }

	/**
	 * Zoomfaktor der aktuellen Spielersicht
	 */
	private float actualZoom;

	/**
	 * Einheitenfarbe des Spielers
	 */
	private Color unitColor;

	/**
	 * Ressourceneinheiten des Spielers
	 */
	private int credits = 500;

	/**
	 * Liste der aktivierten Units des Spielers
	 */
	private CopyOnWriteArrayList<Unit> activeUnits;

	private int id;

	public Player(ViewControl gamelogic, float generalAngle,
			float actualZoom, Vector2f originPosition, Color unitColor, int id) {

		this.id = id;
		this.generalAngle = generalAngle;
		this.actualZoom = actualZoom;
		this.originPosition = originPosition;
		this.originOffset = new Vector2f(0, 0);
		this.activeUnits = new CopyOnWriteArrayList<Unit>();
		this.unitColor = unitColor;
	}

	public Color getUnitColor() {
		return unitColor;
	}

	public CopyOnWriteArrayList<Unit> getActiveUnits() {
		return activeUnits;
	}

	public int getId() {
		return id;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	/**
	 * Gibt den Ursprungsvector des Players zurueck
	 * 
	 * @return Vector2f
	 */
	public Vector2f getOriginPosition() {
		return originPosition;
	}

	/**
	 * Gibt den Sichtvektor zureuck
	 * 
	 * @return Vector2f
	 */
	public Vector2f getOriginOffset() {
		return originOffset;
	}

	/**
	 * setzt den Sichtvektor
	 * 
	 * @param viewPosition
	 */
	public void setOriginOffset(Vector2f viewPosition) {
		if (viewPosition.length() < 700) {
			this.originOffset = viewPosition;
		}
	}

	public float getActualZoom() {
		return actualZoom;
	}

	public void setActualZoom(float actualZoom) {

		// wenn minimum oder maximum nicht überschritten
		if (!(actualZoom > zoomMax || actualZoom < zoomMin)) {
			this.actualZoom = actualZoom;
		}

	}

	public float getGeneralAngle() {
		return generalAngle;
	}

	public void addCredits(int credits) {
		this.credits = this.credits + credits;
	}

}
