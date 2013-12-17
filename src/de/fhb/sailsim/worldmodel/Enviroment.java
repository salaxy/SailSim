package de.fhb.sailsim.worldmodel;

public class Enviroment {
	
	private Wind wind;
	private CompassSimulation compass;
	
	public Enviroment() {
		super();
		
		wind= new Wind();
		compass= new CompassSimulation();
	}
	public Wind getWind() {
		return wind;
	}
	public void setWind(Wind wind) {
		this.wind = wind;
	}
	public CompassSimulation getCompass() {
		return compass;
	}
	public void setCompass(CompassSimulation compass) {
		this.compass = compass;
	}

}
