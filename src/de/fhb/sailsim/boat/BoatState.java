package de.fhb.sailsim.boat;

import org.newdawn.slick.geom.Vector2f;

public class BoatState {

	private final int RUDER_STEP = 5;
	private final float SPEEDUP_STEP = 0.1f;

	public static final int MAX_RUDER_AMPLITUDE = 60;

	private Vector2f position = new Vector2f(0, 0);

	private Vector2f direction = new Vector2f(0, 1);

	private double directionValue = 180;

	private GPS gpsPostition;

	private Compass compass;

	/** meter per secound */
	private double currentPropulsionVelocity;

	/** degree per secound */
	private double currentSpinVelocity;

	/** 0 to 9 , = 0 deegree deflection, 9 = 90 degree deflection */
	private int sailWinch;

	/** from 60 degree to - 60 degree */
	private int ruderDeflection = 0;

	/** from 60 degree to - 60 degree */
	private int sailDeflection = 0;
	
	/**start coordinates */
	private float startX = 0f;
	private float startY = 0f;

	public BoatState(GPS gpsPostition, Compass compass, double currentPropulsionVelocity,
			double currentSpinVelocity, int sailWinch, int ruderPostion) {
		super();
		this.gpsPostition = gpsPostition;
		this.compass = compass;
		this.currentPropulsionVelocity = currentPropulsionVelocity;
		this.currentSpinVelocity = currentSpinVelocity;
		this.sailWinch = sailWinch;
		this.ruderDeflection = ruderPostion;
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
		this.ruderDeflection = 0;
	}

	public BoatState(float xCoord, float yCoord) {
		this();
		this.position = new Vector2f(xCoord, yCoord);
		this.startX = xCoord;
		this.startY = yCoord;
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

	public int getRuderDeflection() {
		return ruderDeflection;
	}

	public void setRuderDeflection(int ruderPostion) {
		if (ruderPostion <= MAX_RUDER_AMPLITUDE && ruderPostion >= -MAX_RUDER_AMPLITUDE) {
			this.ruderDeflection = ruderPostion;
		}
	}

	public int getSailDeflection() {
		return sailDeflection;
	}

	public void setSailDeflection(int sailDeflection) {
		this.sailDeflection = sailDeflection;
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

	public double getDirectionValue() {
		return directionValue;
	}

	public void setDirectionValue(double directionValue) {
		if (directionValue >= 360d) {
			this.directionValue = directionValue % 360;
		} else {
			this.directionValue = directionValue;
		}
		if (this.directionValue < 0) {
			this.directionValue = 360 + this.directionValue;
		}
	}

	// next methods for manual control
	// *****************************************************

	public void turnRight() {
		this.setRuderDeflection(ruderDeflection + RUDER_STEP);
	}

	public void turnLeft() {
		this.setRuderDeflection(ruderDeflection - RUDER_STEP);
	}

	public void directRight() {
		this.direction.add(-5d);
		this.setDirectionValue(directionValue - RUDER_STEP);
	}

	public void directLeft() {
		this.direction.add(5d);
		this.setDirectionValue(directionValue + RUDER_STEP);
	}

	public void speedUp() {
		this.currentPropulsionVelocity += SPEEDUP_STEP;
	}

	public void speedDown() {
		this.currentPropulsionVelocity -= SPEEDUP_STEP;
	}

	public boolean isMoving() {
		if (this.getCurrentPropulsionVelocity() > 0d) {
			return true;
		} else {
			return false;
		}
	}

	public void stop() {
		this.currentPropulsionVelocity = 0.0f;
	}

	public void setBack() {
		this.position = new Vector2f(startX, startY);
		this.currentPropulsionVelocity = 0.0f;
		this.currentSpinVelocity= 0f;
		this.sailDeflection=0;
		this.directionValue=180;
		this.direction = new Vector2f(0, 1);
	}

}
