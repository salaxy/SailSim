package de.fhb.sailsim.worldmodel;

public class Enviroment {
	
	private WindState wind;
	private CompassSimulation compass;
	
	public Enviroment() {
		super();
		
		wind= new WindState();
		compass= new CompassSimulation();
	}
	
	public WindState getWindState () {
		return wind;
	}
	
	public CompassSimulation getCompass() {
		return compass;
	}
	
}
