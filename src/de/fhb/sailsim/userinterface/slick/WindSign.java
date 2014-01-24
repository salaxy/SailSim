package de.fhb.sailsim.userinterface.slick;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class WindSign {

	private final int X_POS = 60;
	private final int Y_POS = 60;
	private final int WIDTH = 40;
	private final int LENGTH = 70;

	// in km per hour, 0 to 65
	private double strength = 0;
	// 0 to 359 degree, 0=north, 90=east, 180=south, 270=west
	private double direction = 0;

	private int windToBoat = 180;

	public WindSign(double strength, double direction) {
		super();
		this.strength = strength;
		this.direction = direction;
	}

	public double getStrength() {
		return strength;
	}

	public void setStrength(double strength) {
		if (strength >= 0 && strength <= 65) {
			this.strength = strength;
		}
	}

	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		if (direction >= 0 && direction <= 359) {
			this.direction = direction;
		}
	}

	public int getWindToBoat() {
		return windToBoat;
	}

	public void setWindToBoat(int windToBoat) {
		this.windToBoat = windToBoat;
	}

	public void paint(Perspective perspective, Graphics graphics, boolean b) {

		// TODO später noch schöner machen, Kreis mit Windrichtungen drumrum

		Image image = null;
		try {
			image = new Image("graphics/myWindSymbol.gif");
			image = image.getScaledCopy((int) (WIDTH + this.strength), LENGTH);
			graphics.rotate(X_POS + image.getCenterOfRotationX(),
					Y_POS + image.getCenterOfRotationY(),
					(float) this.direction + 180);
			graphics.drawImage(image, this.X_POS, this.Y_POS);
			// TODO Pfeil verschiebt sich etwas, wenn er breiter wird
		} catch (SlickException e) {
			e.printStackTrace();
		}
		graphics.resetTransform();

		displayInformationText(graphics);
	}

	private void displayInformationText(Graphics graphics) {
		graphics.setColor(Color.green);
		// graphics.rotate(0, 0, 0);
		// graphics.scale(0.9f, 0.9f);
		graphics.drawString("Direction: " + this.getDirection(), X_POS, Y_POS
				+ LENGTH);
		graphics.drawString("Strength: " + this.getStrength(), X_POS, Y_POS
				+ LENGTH + 20);
		graphics.drawString("WindToBoat: " + this.getWindToBoat(), X_POS, Y_POS
				+ LENGTH + 40);

		graphics.resetTransform();
	}

}
