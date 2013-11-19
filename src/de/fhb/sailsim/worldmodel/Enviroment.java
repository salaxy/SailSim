package de.fhb.sailsim.worldmodel;

public class Enviroment {
	
	private Wind wind;
	private Compass compass;
	
	public Enviroment() {
		super();
		
		wind= new Wind();
		compass= new Compass();
	}
	public Wind getWind() {
		return wind;
	}
	public void setWind(Wind wind) {
		this.wind = wind;
	}
	public Compass getCompass() {
		return compass;
	}
	public void setCompass(Compass compass) {
		this.compass = compass;
	}

}
