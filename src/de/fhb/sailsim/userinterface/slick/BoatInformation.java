package de.fhb.sailsim.userinterface.slick;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import de.fhb.sailsim.boat.BoatState;

public class BoatInformation {

	private final int X_POS = 20;
	private final int Y_POS = 220;

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
		DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
		dfs.setDecimalSeparator('.');
		DecimalFormat f2 = new DecimalFormat("#0.00", dfs);
		DecimalFormat f0 = new DecimalFormat("#0", dfs);

		//draw boat information
		graphics.drawString("Boatstate", X_POS, Y_POS);
		graphics.drawString("proV: " + f2.format(this.boatState.getCurrentPropulsionVelocity())
				+ " m/s", X_POS, Y_POS + 20);
		graphics.drawString(
				"spinV: " + f2.format(this.boatState.getCurrentSpinVelocity()) + " d/s", X_POS,
				Y_POS + 40);
		graphics.drawString("Direction: " + f0.format(this.boatState.getDirectionValue()) + " d",
				X_POS, Y_POS + 60);
		graphics.drawString("Sailangle: " + this.boatState.getSailDeflection() + " d", X_POS,
				Y_POS + 80);
		graphics.drawString("WS Strength: " + f2.format(this.boatState.getApparentWind().getStrength()) + " m/s", X_POS,
				Y_POS + 100);
		graphics.drawString("WS Direction: " + f0.format(this.boatState.getApparentWind().getDirection()) + " d", X_POS,
				Y_POS + 120);

		//draw measurement line
		graphics.setLineWidth(3);
		int measureLine = 160;
		int measureLength = (int) (100 * perspective.getActualZoom());
		graphics.drawString("Measure 100 m", X_POS, Y_POS + measureLine + 5);
		graphics.drawLine(X_POS, Y_POS + measureLine, X_POS + measureLength, Y_POS + measureLine);
		graphics.drawLine(X_POS, Y_POS + measureLine - 5, X_POS, Y_POS + measureLine + 5);
		graphics.drawLine(X_POS + measureLength, Y_POS + measureLine - 5, X_POS + measureLength,
				Y_POS + measureLine + 5);

		graphics.resetTransform();
	}

}
