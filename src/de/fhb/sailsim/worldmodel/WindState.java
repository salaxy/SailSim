package de.fhb.sailsim.worldmodel;

/**
 * This Class represents the global wind properties
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 */
public class WindState {

	// in km per hour, 0 to 65
	private double strength = 0;
	// 0 to 359 degree, 0=north, 90=east, 180=south, 270=west
	private double direction = 0;

	public WindState(double strength, double direction) {
		super();
		this.strength = strength;
		this.direction = direction;
	}

	public WindState() {
		super();
		this.strength = 0d;
		this.direction = 0d;
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

	public void turnLeft() {
		this.setDirection(direction+5);
		
	}

	public void turnRight() {
		this.setDirection(direction-5);		
	}

}
