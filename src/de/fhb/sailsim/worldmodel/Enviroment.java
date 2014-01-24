package de.fhb.sailsim.worldmodel;

public class Enviroment {
	
	private WindState wind;
	private CompassSimulation compass;
	
	public Enviroment() {
		super();
		
		wind= new WindState();
		compass= new CompassSimulation();
	}
	public WindState getWind() {
		return wind;
	}
	public void setWind(WindState wind) {
		this.wind = wind;
	}
	public CompassSimulation getCompass() {
		return compass;
	}
	public void setCompass(CompassSimulation compass) {
		this.compass = compass;
	}

}
