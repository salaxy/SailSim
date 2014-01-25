package de.fhb.sailsim.userinterface.slick;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import de.fhb.sailsim.boat.BoatState;

/**
 * BoatSign represents the boat on the display
 * 
 */
public class BoatSign extends DrawingOnMap {

	private final int SIZE = 20;

	private BoatState boatState;

	public BoatSign(BoatState boatState) {
		super(MODE_NORMAL);
		this.boatState = boatState;
	}

	/**
	 * zeichne Figur im Normalen Zustand
	 * 
	 * @param perspective
	 * @param graphics
	 */
	protected void drawNormalAppearance(Perspective perspective,
			Graphics graphics) {

		// farben setzen
		graphics.setColor(this.passiveColor);
		Image image = null;

		try {
			image = new Image("graphics/myBoatSymbol.gif");
			image = image.getScaledCopy(SIZE * 2, SIZE);
			graphics.rotate(0, 0, 270);
			graphics.drawImage(image, -SIZE, -SIZE / 2);

		} catch (SlickException e) {
			e.printStackTrace();
		}

		// Zeichne Baum
		// TODO in depence of sail angle
		graphics.setLineWidth(3);
		graphics.drawLine(0, 0, -SIZE + 10, 0);

		// Zeichne Ruder
		graphics.setLineWidth(2);

		// TODO ist noch buggy, Vektor verhält sich nciht so wie er soll
		// 60 grad entspriht hierbei 90 grad ??? wtf
		Vector2f ruderDirection = new Vector2f(100, 0);
		double ausgangstellung = 180d;
		int ruderAngle = this.boatState.getRuderPostion();
		ruderDirection.setTheta(180);

		if (ruderAngle <= 90 && ruderAngle > 0)
			ruderDirection.setTheta(ausgangstellung + ruderAngle);

		if (ruderAngle >= -90 && ruderAngle < 0)
			ruderDirection.setTheta(ausgangstellung + ruderAngle);

		// System.out.println("ruder at " + this.ruderAngle);
		// System.out.println("ruderDirection at " + ruderDirection.getTheta());

		ruderDirection = ruderDirection.getNormal().scale(40);
		System.out.println("ruder length: " + ruderDirection.length());

		graphics.drawLine(-SIZE / 2 - 10, 0, ruderDirection.x, ruderDirection.y);
		graphics.resetTransform();
		// System.out.println("draw at " + this.position.toString());
	}

	/**
	 * Berechnet Zeichnenposition und setzt
	 * Abbildungsmatrix(Transformationsmatix)
	 * 
	 * @return
	 */
	protected void calcDrawPosition(Perspective perspective, Graphics graphics) {
		GraphicTools.calcDrawTransformationForSlick(perspective, graphics,
				this.boatState.getPosition());
		// eigendrehung hinzurechnen
		graphics.rotate(0, 0, (float) this.boatState.getDirectionValue());
	}

}
