package de.fhb.calc;

import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;

import de.fhb.sailsim.boat.BoatState;
import de.fhb.sailsim.userinterface.slick.SlickView;
import de.fhb.sailsim.userinterface.slick.VectorHelper;
import de.fhb.sailsim.worldmodel.Enviroment;

public class PolarPlotModel extends CalculationModel {

	//pixel/meter per sec
//	private final double ACCELERATION=0.00005d;
	private final double ACCELERATION=0.0000d;
	
	//in grad pro sekunde
	private final double ANGLE_VELOCITY=10d;
	
	private ArrayList<PolarData> polarPlot = new ArrayList<PolarData>();
	
	private final double zielWinkel=0;

	@Override
	public void calculateNextState(BoatState boat, Enviroment world, long time) {
		
		//gesucht zurückgelegter Weg
		double s;
		
		//Beschleunigung m pro sekunde quadrat
		double a=this.ACCELERATION/SlickView.FRAMERATE;
		// zeit in milisecounds
		long t=time;
		t=50; //entspricht 20fps
		//anfangsgeschwindigkeit meter pro sekunde
		double v0=boat.getCurrentPropulsionVelocity()/SlickView.FRAMERATE;
		
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
		double winkelgeschwindigkeit=this.ANGLE_VELOCITY/SlickView.FRAMERATE;
		int ruderAngle=boat.getRuderPostion();
		
		if(ruderAngle<=90 && ruderAngle>0)
			rotateV=-winkelgeschwindigkeit;
		
		if(ruderAngle>=-90 && ruderAngle<0)
			rotateV=winkelgeschwindigkeit;
		
		//berechnen des Bewegungsvektors
		Vector2f newPosition;
		newPosition = VectorHelper.add(boat.getPosition(),VectorHelper.mult(boat.getDirection().normalise(), (float)s));
		
		//berechnen des neuen Winkels
//		Vector2f newDirection=boat.getDirection().copy();
		
		if(rotateS>=0){
			boat.getDirection().add(rotateV);
		}else{
			boat.getDirection().sub(rotateV);
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
