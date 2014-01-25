package de.fhb.sailsim.userinterface.slick;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public class DrawingOnMap {

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
	 * Geschwindigkeitsfaktor der Rotation
	 */
	protected float rotationSpeed = 10f;

	/**
	 * Farbe in der Einheit gezeichnet wird, wenn Einheit Aktiv ist, also
	 * Einheit Steuerbar ist
	 */
	protected Color activeColor = Color.red;

	/**
	 * Farbe in der Einheit gezeichnet wird, wenn Einheit NICHT Aktiv ist
	 */
	protected Color passiveColor = Color.black;

	public DrawingOnMap(int mode) {
		super();
		this.mode = mode;
		this.initHaloSkala();
		this.passiveColor = Color.black;
	}

	/**
	 * zeichne Figur im pulsierend
	 * 
	 * @param perspective
	 * @param graphics
	 */
	protected void drawPulseAppearance(Perspective perspective,
			Graphics graphics) {

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
	 * @param perspective
	 * @param graphics
	 */
	protected void drawHalo(Perspective perspective, Graphics graphics) {

		// Umrechnung auf Spielersicht
		this.calcDrawPosition(perspective, graphics);

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
	 * @param perspective
	 * @param graphics
	 */
	protected void drawRotateAppearance(Perspective perspective,
			Graphics graphics) {

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
	 * @param perspective
	 * @param graphics
	 */
	protected void drawRotateAndPulseAppearance(Perspective perspective,
			Graphics graphics) {

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
	 * zeichne Figur im Normalen Zustand
	 * 
	 * @param perspective
	 * @param graphics
	 */
	protected void drawNormalAppearance(Perspective perspective,
			Graphics graphics) {
		// dummy, overwrite in subclasses
	}

	/**
	 * zeichnet die Einheit, wird je Frame 1 mal aufgerufen!
	 * 
	 * @param perspective
	 * @param graphics
	 * @param focus
	 *            - wird Unit Aktiv gezeichnet oder nicht
	 */
	public void paint(Perspective perspective, Graphics graphics) {

		// Transformationen auf Perspektive
		calcDrawPosition(perspective, graphics);

		this.drawNormalAppearance(perspective, graphics);

		// zurücksetzen der Umgebung, Seiteneffekte vermeiden
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
	 * Berechnet Zeichnenposition und setzt
	 * Abbildungsmatrix(Transformationsmatix)
	 * 
	 * @return
	 */
	protected void calcDrawPosition(Perspective perspective, Graphics graphics) {
		Vector2f position = new Vector2f(0, 0);
		GraphicTools.calcDrawTransformationForSlick(perspective, graphics,
				position);
		// eigendrehung hinzurechnen
		int eigendrehung = 90;
		graphics.rotate(0, 0, eigendrehung);
	}

}
