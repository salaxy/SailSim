package de.fhb.sailsim.boat;

import org.newdawn.slick.geom.Vector2f;

public class BoatState {

	private Vector2f position = new Vector2f(0, 0);

	private Vector2f direction = new Vector2f(0, 1);

	private Vector2f ruderDirection = new Vector2f(1, 0);

	private GPS gpsPostition;

	private Compass compass;

	/** meter per secound */
	private double currentPropulsionVelocity;

	private double maxVelocity = 0.01;

	public double getMaxVelocity() {
		return maxVelocity;
	}

	public void setMaxVelocity(double maxVelocity) {
		this.maxVelocity = maxVelocity;
	}

	private double currentSpinVelocity;

	/** 0 to 9 , = 0 deegree deflection, 9 = 90 degree deflection */
	private int sailWinch;

	/** from 90 degree to - 90 degree */
	private int ruderPostion = 0;

	public BoatState(GPS gpsPostition, Compass compass,
			double currentPropulsionVelocity, double currentSpinVelocity,
			int sailWinch, int ruderPostion) {
		super();
		this.gpsPostition = gpsPostition;
		this.compass = compass;
		this.currentPropulsionVelocity = currentPropulsionVelocity;
		this.currentSpinVelocity = currentSpinVelocity;
		this.sailWinch = sailWinch;
		this.ruderPostion = ruderPostion;
	}

	/**
	 * Constructor wirth standardfields
	 */
	public BoatState() {
		super();
		this.gpsPostition = new GPS();
		this.compass = new Compass();
		this.currentPropulsionVelocity = 0d;
		this.currentSpinVelocity = 0d;
		this.sailWinch = 0;
		this.ruderPostion = 0;
	}

	public BoatState(float xCoord, float yCoord) {
		this();
		position = new Vector2f(xCoord, yCoord);
	}

	public GPS getGpsPostition() {
		return gpsPostition;
	}

	public void setGpsPostition(GPS gpsPostition) {
		this.gpsPostition = gpsPostition;
	}

	public Compass getCompass() {
		return compass;
	}

	public void setCompass(Compass compass) {
		this.compass = compass;
	}

	public double getCurrentPropulsionVelocity() {
		return currentPropulsionVelocity;
	}

	public void setCurrentPropulsionVelocity(double currentPropulsionVelocity) {
		this.currentPropulsionVelocity = currentPropulsionVelocity;
	}

	public double getCurrentSpinVelocity() {
		return currentSpinVelocity;
	}

	public void setCurrentSpinVelocity(double currentSpinVelocity) {
		this.currentSpinVelocity = currentSpinVelocity;
	}

	public int getSailWinch() {
		return sailWinch;
	}

	public void setSailWinch(int sailWinch) {
		this.sailWinch = sailWinch;
	}

	public int getRuderPostion() {
		return ruderPostion;
	}

	public void setRuderPostion(int ruderPostion) {
		if (ruderPostion <= 90 && ruderPostion >= -90) {
			this.ruderPostion = ruderPostion;
		}
	}

	public Vector2f getDirection() {
		return direction;
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public void setDirection(Vector2f direction) {
		this.direction = direction.normalise();
	}

	// next methods for manual control
	// *****************************************************

	public void turnRight() {
		// TODO delete direction here, direction is only calculated by executing
		// simulation routine
		// this.direction.add(45d);
		this.setRuderPostion(ruderPostion + 5);
	}

	public void turnLeft() {
		// this.direction.sub(45d);
		this.setRuderPostion(ruderPostion - 5);
	}

	public void speedUp() {
		this.currentPropulsionVelocity += 0.01f;
	}

	public void speedDown() {
		this.currentPropulsionVelocity -= 0.01f;
	}

	public boolean isMoving() {
		if (this.getCurrentPropulsionVelocity() > 0d) {
			return true;
		} else {
			return false;
		}
	}

}
