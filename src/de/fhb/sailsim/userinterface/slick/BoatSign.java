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

	private final int BOAT_SIZE = 20;

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
		
		drawBoatTorso(perspective,graphics);
		drawSail(perspective, graphics);
		drawRuder(perspective, graphics);
		
		graphics.resetTransform();
	}

	private void drawBoatTorso(Perspective perspective, Graphics graphics) {

		try {
			Image image = new Image("graphics/myBoatSymbol.gif");
			image = image.getScaledCopy(BOAT_SIZE * 2, BOAT_SIZE);
			graphics.rotate(0, 0, 270);
			graphics.drawImage(image, -BOAT_SIZE, -BOAT_SIZE / 2);

		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	private void drawRuder(Perspective perspective, Graphics graphics) {
		// Transformationen auf Perspektive
		graphics.resetTransform();
		calcDrawPosition(perspective, graphics);
		graphics.rotate(0, 0, 270);
		
		graphics.setColor(this.passiveColor);
		graphics.setLineWidth(2);

		// TODO ist noch buggy, Vektor verhält sich nciht so wie er soll
		// 60 grad entspriht hierbei 90 grad ??? wtf
		Vector2f ruderDirection = new Vector2f(100, 0);
		double ausgangstellung = 180d;
		int ruderAngle = this.boatState.getRuderDeflection();
		ruderDirection.setTheta(180);

		if (ruderAngle <= 90 && ruderAngle > 0)
			ruderDirection.setTheta(ausgangstellung + ruderAngle);

		if (ruderAngle >= -90 && ruderAngle < 0)
			ruderDirection.setTheta(ausgangstellung + ruderAngle);

		// System.out.println("ruder at " + this.ruderAngle);
		// System.out.println("ruderDirection at " + ruderDirection.getTheta());

		ruderDirection = ruderDirection.getNormal().scale(40);
//		System.out.println("ruder length: " + ruderDirection.length());

		graphics.drawLine(-BOAT_SIZE / 2 - 10, 0, ruderDirection.x, ruderDirection.y);
	}

	private void drawSail(Perspective perspective, Graphics graphics) {

		// Transformationen auf Perspektive
		graphics.resetTransform();
		calcDrawPosition(perspective, graphics);
		
		try {
			Image image = null;
			
			int Xposition = -BOAT_SIZE + (int)(BOAT_SIZE/1.4);
			int Yposition = -BOAT_SIZE / 2;
			
			if(this.boatState.getSailDeflection()<0){
				image = new Image("graphics/sail_full_right.gif");	
				Yposition = Yposition - 2;
			}else{
				image = new Image("graphics/sail_full_left.gif");
				Yposition = Yposition + 2;
			}

			image = image.getScaledCopy(BOAT_SIZE * 2, BOAT_SIZE);
			graphics.rotate(0, 0, 90 + this.boatState.getSailDeflection());
			graphics.drawImage(image, Xposition, Yposition);

		} catch (SlickException e) {
			e.printStackTrace();
		}
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
