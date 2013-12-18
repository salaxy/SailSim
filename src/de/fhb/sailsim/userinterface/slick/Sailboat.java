package de.fhb.sailsim.userinterface.slick;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

/**
 * Klasse für die Units
 */
public class Sailboat {

	protected ViewControl gamelogic;

	/**
	 * Referenz auf Spielkarte
	 */
	protected Map map;

	/**
	 * beinhaltet alle Einheiten die existent sind
	 */
	public static int idCounter = 0;

	public static final int MODE_NORMAL = 0;
	public static final int MODE_ROTATE = 1;
	public static final int MODE_PULSE = 2;
	public static final int MODE_ROTATE_AND_PULSE = 3;
	public static final int MODE_HALO = 4;
	public static final int MODE_PULSE_IF_ACTIVE = 5;

	/**
	 * Skala nach der Einheit pulsiert
	 */
	protected float[] pulseSkala = { 1.0f, 0.9f, 0.8f, 0.7f, 0.6f, 0.5f };

	/**
	 * Skala fuer den Radius des Halo
	 */
	protected float[] haloSkala;

	/**
	 * aktuelle Position der Einheit
	 */
	protected Vector2f position;

	/**
	 * legt die erste Blickrichtungfest
	 */
	protected Vector2f direction = new Vector2f(0, 1);

	/**
	 * aktueller rotationsgrad (aktuelle Drehung der Rotation)
	 */
	protected float rotatingAngle = 0;

	/**
	 * aktuelle Skalierung fuer das Pulsieren
	 */
	protected int pulseStat = 0;

	/**
	 * aktuelle Skalierung fuer den Halo
	 */
	protected int haloStat = 0;

	/**
	 * legt das erscheinungbild der Einheit fest
	 */
	protected int mode;

	protected boolean isMoving = false;

	/**
	 * sagt aus um welchen längenfaktor der Schweif erscheint
	 */
	protected int schweiflaenge = 40;

	/**
	 * Blickrichtung
	 */
	protected float actualAngle = 0;

	/**
	 * Geschwindigkeitsfaktor der Rotation
	 */
	protected float rotationSpeed = 10f;

	/**
	 * Bewegungsgeschwindigkeit
	 */
	// bei zu großen werten von movementSpeed kann das objekt zum schwingen
	// kommen
	protected float movementSpeed = 2f;

	/**
	 * Farbe in der Einheit gezeichnet wird, wenn Einheit Aktiv ist, also
	 * Einheit Steuerbar ist
	 */
	protected Color activeColor = Color.red;

	/**
	 * Farbe in der Einheit gezeichnet wird, wenn Einheit NICHT Aktiv ist
	 */
	protected Color passiveColor = Color.black;

	/**
	 * Zielvektor der Einheit
	 */
	protected Vector2f destinationVector;

	/**
	 * 
	 * @param x
	 * @param y
	 * @param mode
	 * @param player
	 * @param gamelogic
	 * @param unitColor
	 */
	public Sailboat(int x, int y, int mode, ViewControl gamelogic) {

		this.mode = mode;
		this.position = new Vector2f(x, y);
		this.destinationVector = new Vector2f(x, y);
		this.berechneNeueBlickrichtung();
		this.initHaloSkala();
		this.passiveColor = Color.black;
		this.gamelogic = gamelogic;
		this.map = gamelogic.getMap();

		// fuegt sich selbst zur globalen Menge der Einheiten hinzu
		this.gamelogic.getGlobalUnits().add(this);
	}

	/**
	 * initialisiert die Haloskala
	 */
	protected void initHaloSkala() {
		haloSkala = new float[50];
		int i = 0;
		for (float f = 1.01f; i < haloSkala.length; f += 0.1f) {
			haloSkala[i] = f;
			i++;
		}
	}

	/**
	 * zeichnet die Einheit, wird je Frame 1 mal aufgerufen!
	 * 
	 * @param player
	 * @param graphics
	 * @param focus
	 *            - wird Unit Aktiv gezeichnet oder nicht
	 */
	public void paint(Perspective player, Graphics graphics, boolean drawActive) {

		// zeichne Schweif wenn in bewegung
		if (this.isMoving) {
			drawTail(player, graphics);

		}

		// Umrechnung auf Spielersicht
		// Transformationen
		calcDrawPosition(player, graphics);

		if (drawActive) {

			this.drawActiveAppearance(player, graphics);

		} else {

			// entscheide ueber erscheinungsbild
			switch (mode) {
			case MODE_ROTATE:
				this.drawRotateAppearance(player, graphics);
				break;
			case MODE_PULSE:
				this.drawPulseAppearance(player, graphics);
				break;
			case MODE_ROTATE_AND_PULSE:
				this.drawRotateAndPulseAppearance(player, graphics);
				this.drawHalo(player, graphics);
				break;
			case MODE_HALO:
				this.drawHalo(player, graphics);
				this.drawNormalAppearance(player, graphics);
				break;
			case MODE_NORMAL:
				this.drawNormalAppearance(player, graphics);
				break;
			case MODE_PULSE_IF_ACTIVE:
				this.drawPulseIfActive(player, graphics);
				break;
			}
		}

		// zurücksetzen der Umgebung, Seiteneffekte vermeiden
		graphics.resetTransform();

	}

	/**
	 * Zeichne Figur im Aktiven Zustand
	 * 
	 * @param player
	 * @param graphics
	 */
	public void drawActiveAppearance(Perspective player, Graphics graphics) {

		graphics.setColor(this.activeColor);
		this.drawFigure(graphics);
		graphics.resetTransform();
	}

	/**
	 * zeichne Figur im Normalen Zustand
	 * 
	 * @param player
	 * @param graphics
	 */
	protected void drawNormalAppearance(Perspective player, Graphics graphics) {

		// Umrechnung auf Spielersicht

		// farben setzen
		graphics.setColor(this.passiveColor);

		// zeichnen
		this.drawFigure(graphics);
		graphics.resetTransform();
	}

	/**
	 * zeichne Figur im Normalen Zustand, wenn Aktiv dann pulsierend
	 * 
	 * @param player
	 * @param graphics
	 */
	protected void drawPulseIfActive(Perspective player, Graphics graphics) {

		this.drawNormalAppearance(player, graphics);
	}

	/**
	 * zeichne Figur im pulsierend
	 * 
	 * @param player
	 * @param graphics
	 */
	protected void drawPulseAppearance(Perspective player, Graphics graphics) {

		// solange die skala noch nicht durchlaufen ist
		if (pulseStat < pulseSkala.length - 1) {
			pulseStat++;
		} else {
			// sonst wieder von vorne anfangen
			pulseStat = 0;
		}

		// skalieren
		graphics.scale(pulseSkala[pulseStat], pulseSkala[pulseStat]);

		graphics.setColor(this.passiveColor);
		this.drawFigure(graphics);

		graphics.resetTransform();
	}

	/**
	 * zeichne Halo
	 * 
	 * @param player
	 * @param graphics
	 */
	protected void drawHalo(Perspective player, Graphics graphics) {

		// Umrechnung auf Spielersicht
		this.calcDrawPosition(player, graphics);

		// solange die skala noch nicht durchlaufen ist
		if (haloStat < haloSkala.length - 1) {
			haloStat++;
		} else {
			// sosnt wieder von vorne anfangen
			haloStat = 0;
		}

		// skalieren
		graphics.scale(haloSkala[haloStat], haloSkala[haloStat]);

		// zeichnen
		graphics.setColor(Color.black);
		graphics.drawOval(-10, -10, 20, 20);

		graphics.resetTransform();
	}

	/**
	 * zeichne Figur im rotierend
	 * 
	 * @param player
	 * @param graphics
	 */
	protected void drawRotateAppearance(Perspective player, Graphics graphics) {

		// solange die skala noch nicht durchlaufen ist
		if (rotatingAngle < 360) {
			rotatingAngle += this.rotationSpeed;
		} else {
			// sosnt wieder von vorne anfangen
			rotatingAngle = 0;
		}

		// skalieren
		graphics.rotate(0, 0, rotatingAngle);

		graphics.setColor(this.passiveColor);
		this.drawFigure(graphics);

		graphics.resetTransform();

	}

	/**
	 * zeichne Figur im rotierend und pulsierend
	 * 
	 * @param player
	 * @param graphics
	 */
	protected void drawRotateAndPulseAppearance(Perspective player, Graphics graphics) {

		// solange die skala noch nicht durchlaufen ist
		if (rotatingAngle < (float) Math.PI * 2) {
			rotatingAngle += this.rotationSpeed;
		} else {
			// sosnt wieder von vorne anfangen
			rotatingAngle = 0;
		}

		// skalieren
		graphics.rotate(0, 0, rotatingAngle);

		// solange die skala noch nicht durchlaufen ist
		if (pulseStat < pulseSkala.length - 1) {
			pulseStat++;
		} else {
			// sosnt wieder von vorne anfangen
			pulseStat = 0;
		}

		// skalieren
		graphics.scale(pulseSkala[pulseStat], pulseSkala[pulseStat]);

		// gefuelllt zeichnen
		graphics.setColor(this.passiveColor);
		this.drawFigure(graphics);

		// Umgebung zurücksetzen
		graphics.resetTransform();

	}

	/**
	 * zeichnen des normalen Erscheinungs bildes ohne Effekte
	 */
	protected void drawFigure(Graphics graphics) {

		graphics.scale(2, 2);
		ArrayList<Vector2f> vektoren = new ArrayList<Vector2f>();
		vektoren.add(new Vector2f(0, -8));
		vektoren.add(new Vector2f(-8, 8));
		vektoren.add(new Vector2f(-4, 6));
		vektoren.add(new Vector2f(0, 8));
		vektoren.add(new Vector2f(0, -8));
		vektoren.add(new Vector2f(8, 8));
		vektoren.add(new Vector2f(4, 6));
		vektoren.add(new Vector2f(0, 8));

		GraphicTools.zeicheFigurNachVektoren(vektoren, graphics);

		graphics.resetTransform();
	}

	/**
	 * Berechnen des neuen Position, wenn in Bewegung
	 */
	public void update() {

		Vector2f newPosition;

		// wenn aktuelle position noch weit weg vom ziel, dann weiter bewegen
		// if (position.distance(destinationVector) > 3) {

		// neue Position erechnen, normierten Richtungsvector zur position
		// hinzurechnen
		newPosition = VectorHelper.add(this.position,
				VectorHelper.mult(direction, movementSpeed));

		// neue Position setzen
		this.position = newPosition;

		isMoving = true;

		// } else {
		// isMoving = false;
		// }

	}

	/**
	 * Zeichne Schweif
	 */
	protected void drawTail(Perspective player, Graphics graphics) {

		// Zielpunkt hinter der Einheit berechnen
		// end Vektor fuer jeweiligen Spieler berechnen
		Vector2f ende = direction.copy();
		// Vektor auf laenge 1 kuerzen
		ende.normalise();
		// Richtungsvektor umkehren zum schweif
		ende.scale(schweiflaenge * -2);

		// Berechne Zeichnenposition und setzte
		// Abblidungsmatrix(Transformationsmatix)
		calcDrawPosition(player, graphics);

		// Eigendrehung ausgleichen (wird in calcDrawPostion gesetzt)
		graphics.rotate(0, 0, -this.actualAngle);

		graphics.setColor(Color.orange);
		// linien zeichnen
		graphics.drawLine(0, 0, ende.x / 2, ende.y / 2);
		graphics.drawLine(1, 1, ende.x / 2, ende.y / 2);
		graphics.drawLine(-1, -1, ende.x / 2, ende.y / 2);

		graphics.drawLine(ende.x / 2, ende.y / 2, ende.x, ende.y);
		graphics.drawLine(ende.x * 1.1f, ende.y * 1.1f, ende.x * 1.2f,
				ende.y * 1.2f);
		graphics.drawLine(ende.x * 1.3f, ende.y * 1.3f, ende.x * 1.4f,
				ende.y * 1.4f);

		graphics.resetTransform();
	}

	/**
	 * legt das erscheinungbild der Einheit neu fest
	 * 
	 * @param mode
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

	/**
	 * Berechnet Blickrichtung der Einheit nach dem Bewegungsvektor
	 */
	protected void berechneNeueBlickrichtung() {
		// berechne neue Blickrichtung
		actualAngle = VectorHelper.angleBetween(direction, new Vector2f(0, -1));
	}

	public Vector2f getPosition() {
		return position;
	}

	/**
	 * Berechnet Zeichnenposition und setzt
	 * Abblidungsmatrix(Transformationsmatix)
	 * 
	 * @return
	 */
	private void calcDrawPosition(Perspective player, Graphics graphics) {
		GraphicTools.calcDrawTransformationForSlick(player, graphics, position);
		// eigendrehung hinzurechnen
		graphics.rotate(0, 0, this.actualAngle);
	}

	public void turnRight() {
		this.actualAngle += 5;
		System.out.println("Direction vector: " + this.direction.toString());
		this.direction.add(5d);
		System.out.println(this.actualAngle);
	}

	public void turnLeft() {
		this.actualAngle -= 5;
		this.direction.sub(5d);
		System.out.println(this.actualAngle);
	}

	public void speedUp() {
		this.movementSpeed += 0.5f;
		System.out.println(this.movementSpeed);
	}

	public void speedDown() {
		this.movementSpeed -= 0.5f;
		System.out.println(this.movementSpeed);
	}
}
