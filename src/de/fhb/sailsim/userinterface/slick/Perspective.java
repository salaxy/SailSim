package de.fhb.sailsim.userinterface.slick;

import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

/**
 * Dies Klasse beinhaltet Einstellungen der zu zeichneden Perspektive
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 * 
 */
public class Perspective {

	/**
	 * generelle Drehung der View
	 */
	private float generalAngle;

	/**
	 * Position des Ursprungs auf dem Bildschirm
	 */
	private Vector2f originPosition;

	private final float ZOOM_MIN = 0.3f;
	private final float ZOMM_MAX = 0.55f;
	private final float VIEW_BORDER = 1000;

	/**
	 * Relative Position der Sicht im Verhältnis zum Ursprung (originPosition)
	 */
	private Vector2f originOffset = new Vector2f(0, 0);

	/**
	 * Zoomfaktor der aktuellen view
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
	private CopyOnWriteArrayList<BoatSign> activeUnits;

	public Perspective(ViewControl gamelogic, float generalAngle,
			float actualZoom, Vector2f originPosition, Color unitColor) {

		this.generalAngle = generalAngle;
		this.actualZoom = actualZoom;
		this.originPosition = originPosition;
		this.originOffset = new Vector2f(0, 0);
		this.activeUnits = new CopyOnWriteArrayList<BoatSign>();
		this.unitColor = unitColor;
	}

	public Color getUnitColor() {
		return unitColor;
	}

	public CopyOnWriteArrayList<BoatSign> getActiveUnits() {
		return activeUnits;
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
		if (viewPosition.length() < VIEW_BORDER) {
			this.originOffset = viewPosition;
		}
	}

	public float getActualZoom() {
		return actualZoom;
	}

	public void setActualZoom(float actualZoom) {

		// wenn minimum oder maximum nicht überschritten
		if (!(actualZoom > ZOMM_MAX || actualZoom < ZOOM_MIN)) {
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
