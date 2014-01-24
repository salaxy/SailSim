package de.fhb.sailsim.userinterface.slick;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class WindSign {
	
	private final int X_POS=60;
	private final int Y_POS=60;
	private final int WIDTH=40;
	private final int LENGTH=70;

	// in km per hour, 0 to 65
	private double strength = 0;
	// 0 to 359 degree, 0=north, 90=east, 180=south, 270=west
	private double direction = 0;

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

	public void paint(Perspective perspective, Graphics graphics, boolean b) {
		
		// Transformationen auf Perspektive
//		calcDrawPosition(perspective, graphics);
		


		
		// farben setzen
		graphics.setColor(Color.black);
		Image image = null;

		try {
			image = new Image("graphics/myWindSymbol.gif");
			image = image.getScaledCopy(WIDTH, LENGTH);
			graphics.rotate(X_POS+image.getCenterOfRotationX(),Y_POS+ image.getCenterOfRotationY(), (float) this.direction+180);
			graphics.drawImage(image, this.X_POS, this.Y_POS);
			
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		
		// zurücksetzen der Umgebung, Seiteneffekte vermeiden
		graphics.resetTransform();
		
		
	}
	
	private void calcDrawPosition(Perspective perspective, Graphics graphics) {
//		GraphicTools.calcDrawTransformationForSlick(perspective, graphics, position);
		// eigendrehung hinzurechnen
//		graphics.rotate(0, 0, this.actualAngle);
	}
}
