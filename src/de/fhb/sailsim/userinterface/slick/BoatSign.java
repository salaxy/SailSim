package de.fhb.sailsim.userinterface.slick;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import de.fhb.sailsim.boat.BoatState;
import de.fhb.sailsim.worldmodel.Enviroment;

/**
 * BoatSign represents the boat on the display
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 */
public class BoatSign extends DrawingOnMap {

	private final int BOAT_SIZE = 20;

	private BoatState boatState;

	private Enviroment envirmoent;

	/**
	 * Constructor, sets boatState
	 * 
	 * @param boatState
	 * @param enviroment
	 */
	public BoatSign(BoatState boatState, Enviroment enviroment) {
		super(MODE_NORMAL);
		this.boatState = boatState;
		this.envirmoent = enviroment;
	}

	/**
	 * draws figure in normal state
	 * 
	 * @param perspective
	 * @param graphics
	 */
	protected void drawNormalAppearance(Perspective perspective, Graphics graphics) {

		drawBoatTorso(perspective, graphics);
		drawSail(perspective, graphics);
		drawRuder(perspective, graphics);
		drawAppentWindArrow(perspective, graphics);
		drawTrueWindArrow(perspective, graphics);

		graphics.resetTransform();
	}

	private void drawAppentWindArrow(Perspective perspective, Graphics graphics) {

		graphics.resetTransform();
		calcDrawPosition(perspective, graphics);

		double direction = this.boatState.getSeeminglyWind().getDirection();

		if (Double.isNaN(direction)) {
			direction = 0;
		}

		try {
			Image image = new Image("graphics/apparentWind.gif");
			image = image.getScaledCopy(20, 40);
			graphics.rotate(0, 0, (float) direction);
			// graphics.rotate(-BOAT_SIZE, -BOAT_SIZE / 2, (float) direction);
			// graphics.drawImage(image,-2*BOAT_SIZE, -4*BOAT_SIZE);
			graphics.drawImage(image, -BOAT_SIZE / 2, -3 * BOAT_SIZE);
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

	private void drawTrueWindArrow(Perspective perspective, Graphics graphics) {

		graphics.resetTransform();
		calcDrawPosition(perspective, graphics);

		double direction = this.envirmoent.getWindState().getWindToBoat();

		if (Double.isNaN(direction)) {
			direction = 0;
		}

		try {
			Image image = new Image("graphics/trueWind.gif");
			image = image.getScaledCopy(20, 40);
			graphics.rotate(0, 0, 180 - (float) direction);
			// graphics.rotate(-BOAT_SIZE, -BOAT_SIZE / 2, (float) direction);
			// graphics.drawImage(image,-2*BOAT_SIZE, -4*BOAT_SIZE);
			graphics.drawImage(image, -BOAT_SIZE / 2, -4 * BOAT_SIZE);
		} catch (SlickException e) {
			e.printStackTrace();
		}

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

		graphics.resetTransform();
		calcDrawPosition(perspective, graphics);

		int Xposition = 0;
		int Yposition = 0 + BOAT_SIZE - 2;

		try {
			Image image = new Image("graphics/ruder.gif");
			image = image.getScaledCopy(BOAT_SIZE, BOAT_SIZE / 4);
			graphics.rotate(Xposition, image.getCenterOfRotationY() + Yposition,
					(float) (90 + this.boatState.getRuderDeflection()));
			graphics.drawImage(image, Xposition, Yposition);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	private void drawSail(Perspective perspective, Graphics graphics) {

		graphics.resetTransform();
		calcDrawPosition(perspective, graphics);

		try {
			Image image = null;

			int Xposition = -BOAT_SIZE + (int) (BOAT_SIZE / 1.4);
			int Yposition = -BOAT_SIZE / 2;
			int deflection = this.boatState.getSailDeflection();

			if (deflection < 5 && deflection > -5) {
				image = new Image("graphics/sail_flaped.gif");
				Yposition = Yposition + 1;
			} else if (deflection < 0) {
				image = new Image("graphics/sail_full_right.gif");
				Yposition = Yposition - 2;
			} else {
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
		// Eigendrehung hinzurechnen
		graphics.rotate(0, 0, (float) this.boatState.getDirectionValue());
	}

}
