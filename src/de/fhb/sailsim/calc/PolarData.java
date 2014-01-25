package de.fhb.sailsim.calc;

public class PolarData {

	private int windSpeed;
	private double maxV;
	private int angleToWind;
	
	public PolarData(int windSpeed, double maxV, int angleToWind) {
		super();
		this.windSpeed = windSpeed;
		this.maxV = maxV;
		this.angleToWind = angleToWind;
	}
	
	public int getWindSpeed() {
		return windSpeed;
	}
	
	public void setWindSpeed(int windSpeed) {
		this.windSpeed = windSpeed;
	}
	
	public double getMaxV() {
		return maxV;
	}
	
	public void setMaxV(double maxV) {
		this.maxV = maxV;
	}
	
	public int getAngleToWind() {
		return angleToWind;
	}
	
	public void setAngleToWind(int angleToWind) {
		this.angleToWind = angleToWind;
	}
	
}
