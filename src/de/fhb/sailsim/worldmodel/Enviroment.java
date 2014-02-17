package de.fhb.sailsim.worldmodel;

public class Enviroment {

	private WindState wind;
	private CompassState compass;

	public Enviroment() {
		super();

		wind = new WindState();
		compass = new CompassState();
	}

	public WindState getWindState() {
		return wind;
	}

	public CompassState getCompass() {
		return compass;
	}

}
