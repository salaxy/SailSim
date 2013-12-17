package de.fhb.sailsim.boat;

public class BoatState {
	
	private GPS gpsPostition;
	
	private Compass compass;
	
	/** meter per secound */
	private double currentPropulsionVelocity;

	private double currentSpinVelocity;
	
	/** 0 to 9 , = 0 deegree deflection, 9 = 90 degree deflection*/ 
	private int sailWinch;
	
	/** from 90 degree to - 90 degree*/
	private int ruderPostion;
	
	
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
		this.gpsPostition = gpsPostition;
		this.compass = compass;
		this.currentPropulsionVelocity = currentPropulsionVelocity;
		this.currentSpinVelocity = currentSpinVelocity;
		this.sailWinch = sailWinch;
		this.ruderPostion = ruderPostion;
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
		this.ruderPostion = ruderPostion;
	}
	
}
