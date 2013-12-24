package de.fhb.calc;

import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;

import de.fhb.sailsim.boat.BoatState;
import de.fhb.sailsim.userinterface.slick.VectorHelper;
import de.fhb.sailsim.worldmodel.Enviroment;

public class PolarPlotModel extends CalculationModel {
	
	private final double ACCELERATION=0.00005d;
	
	private ArrayList<PolarData> polarPlot = new ArrayList<PolarData>();

	@Override
	public void calculateNextState(BoatState boat, Enviroment world, long time) {
		
		//gesucht zurückgelegter Weg
		double s;
		
		//Beschleunigung m pro sekunde quadrat
		double a=this.ACCELERATION;
		// zeit in milisecounds
		long t=time;
		t=50; //entspricht 20fps
		//anfangsgeschwindigkeit meter pro sekunde
		double v0=boat.getCurrentPropulsionVelocity();
		
		//neue geschwindigkeit nach t
		double v=v0;
		
		//berechne zurückgelegten Weg
		s= (0.5*a) * (t*t) + (v0*t);

		if(boat.getCurrentPropulsionVelocity()<=boat.getMaxVelocity()){
			//boot geschwindigkeit erhöhen
			//berechne neue geschwindigkeit
			v= a * t +v0;
			boat.setCurrentPropulsionVelocity(v);
		}
		
		//Drehwinkel und dreh geschwindigkeit bestimmen
		double rotateV=0;
		double rotateS=0;
		//TODO Berechnungen ergänzen
		//als ergebnis wird roateV des bootes gesetzt
		
		//berechnen des Bewegungsvektors
		Vector2f newPosition;
		newPosition = VectorHelper.add(boat.getPosition(),VectorHelper.mult(boat.getDirection().normalise(), (float)s));
		
		//berechnen des neuen Winkels
//		Vector2f newDirection=boat.getDirection().copy();
		
		if(rotateS>=0){
			boat.getDirection().add(rotateS);
		}else{
			boat.getDirection().sub(rotateS);
		}
		
		boat.setPosition(newPosition);
//		boat.setDirection(newDirection);
	}
	
	public double interpolateMaxV(PolarData valueMin, PolarData valueMax){
		//TODO
		return 0;
	}
	
	public void readInPolarPlotAsCsv(PolarData valueMin, PolarData valueMax){
		//TODO

	}

}
