package de.fhb.sailsim.userinterface.slick;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import de.fhb.sailsim.boat.BoatState;

public class BoatInformation {

	private final int X_POS = 60;
	private final int Y_POS = 200;

	private BoatState boatState;

	public BoatInformation(BoatState boatState) {
		super();
		this.boatState = boatState;
	}

	/**
	 * draw Absolute Boat Information
	 * 
	 * @param perspective
	 * @param graphics
	 * @param b
	 */
	public void paint(Perspective perspective, Graphics graphics) {

		graphics.setColor(Color.green);
		// graphics.scale(0.9f, 0.9f);
		DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
		dfs.setDecimalSeparator('.');
		DecimalFormat f = new DecimalFormat("#0.00", dfs);
		
		graphics.drawString(
				"Boat proV: " + f.format(this.boatState.getCurrentPropulsionVelocity()),
				X_POS, Y_POS);
		graphics.drawString(
				"Boat spinV: " + f.format(this.boatState.getCurrentSpinVelocity()),
				X_POS, Y_POS + 20);
		// graphics.drawString("Boat proA: " + this.windState.getWindToBoat(),
		// X_POS, Y_POS + 40);

		graphics.resetTransform();
	}

}
