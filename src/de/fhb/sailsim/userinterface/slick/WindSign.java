package de.fhb.sailsim.userinterface.slick;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import de.fhb.sailsim.worldmodel.WindState;

public class WindSign {

	private final int X_POS = 60;
	private final int Y_POS = 60;
	private final int WIDTH = 40;
	private final int LENGTH = 70;

	private WindState windState;

	public WindSign(WindState windState) {
		super();
		this.windState = windState;
	}

	public void paint(Perspective perspective, Graphics graphics) {

		// TODO später noch schöner machen, Kreis mit Windrichtungen drumrum
		Image image = null;
		try {
			image = new Image("graphics/myWindSymbol.gif");
			image = image.getScaledCopy((int) (WIDTH + this.windState.getStrength()), LENGTH);
			graphics.rotate(X_POS + image.getCenterOfRotationX(),
					Y_POS + image.getCenterOfRotationY(),
					(float) this.windState.getDirection() + 180);
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
		graphics.drawString("Direction: " + this.windState.getDirection() + " degree", X_POS, Y_POS
				+ LENGTH);
		graphics.drawString("Strength: " + this.windState.getStrength() + " km/h", X_POS, Y_POS
				+ LENGTH + 20);
		graphics.drawString("WindToBoat: " + this.windState.getWindToBoat() + " degree", X_POS, Y_POS
				+ LENGTH + 40);

		graphics.resetTransform();
	}

}
