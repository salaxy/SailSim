package de.fhb.sailsim.boat;

public class GPS {
	
	private double latitude;
	private double longitude;
	
	public GPS() {
		this(0,0);
	}
	
	public GPS(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
