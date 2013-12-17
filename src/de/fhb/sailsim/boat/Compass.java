package de.fhb.sailsim.boat;

/**
*
* @author Andy Klay <klay@fh-brandenburg.de>
* 
*/
public class Compass {

	private double azimuth;
	private double pitch;
	private double roll;
	
	public Compass() {
		this(0,0,0);
	}
	
	public Compass(double azimuth, double pitch, double roll) {
		super();
		this.azimuth = azimuth;
		this.pitch = pitch;
		this.roll = roll;
	}
	public double getAzimuth() {
		return azimuth;
	}
	public void setAzimuth(double azimuth) {
		this.azimuth = azimuth;
	}
	public double getPitch() {
		return pitch;
	}
	public void setPitch(double pitch) {
		this.pitch = pitch;
	}
	public double getRoll() {
		return roll;
	}
	public void setRoll(double roll) {
		this.roll = roll;
	}
	
}
