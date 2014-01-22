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
	
	//in grad pro sekunde für eien geschwindigkeit von 10 m/s
	private final double ANGLE_VELOCITY=40d;
	private final double REFERENCE_PROPULSION_VELOCITY=1d;
	
	private ArrayList<PolarData> polarPlot = new ArrayList<PolarData>();

	@Override
	public void calculateNextState(BoatState boat, Enviroment world, long time) {
		
		//gesucht zurückgelegter Weg
		double s;
		
		//Beschleunigung m pro sekunde quadrat
		double a=this.ACCELERATION/SlickView.FRAMERATE;
		// zeit in milisecounds
		long t=time; //t=1000/SlickView.FRAMERATE; 
		//anfangsgeschwindigkeit meter pro sekunde
		double propulsionV=boat.getCurrentPropulsionVelocity();
		
		//neue geschwindigkeit nach t
		double v=propulsionV/SlickView.FRAMERATE;
		
		//berechne zurückgelegten Weg
		s= (0.5*a) * (t*t) + (v*t);

		if(boat.getCurrentPropulsionVelocity()<=boat.getMaxVelocity()){
			//boot geschwindigkeit erhöhen
			//berechne neue geschwindigkeit
			v= a * t +v;
			boat.setCurrentPropulsionVelocity(v*SlickView.FRAMERATE);
		}
		
		//als ergebnis wird roateV des bootes gesetzt
		//calc angle velocity in depency of propulsion velocity

		double rotateV=(propulsionV * ANGLE_VELOCITY)/REFERENCE_PROPULSION_VELOCITY;
		int ruderAngle=boat.getRuderPostion();
		
		//verhalten in Abhängigkeit davon wie stark das Ruder eingelschagen ist
		//TODO nicht realistisch ...der Wendekreis bleibt immer gleich egal welche geschwindigkeit
		if(ruderAngle<0){
			int angleHelper=-ruderAngle;
			rotateV=(angleHelper*rotateV)/90;
		}
		
		rotateV=rotateV/SlickView.FRAMERATE;

		if(ruderAngle<=90 && ruderAngle>0){
			rotateV=-rotateV;
		}  //else rotateV=rotateV
		
		//berechnen des neuen Winkels
		if(ruderAngle!=0){
			boat.getDirection().add(rotateV);		
		}
		
		//berechnen der neuen Postionsvektors
		Vector2f newPosition;
		newPosition = VectorHelper.add(boat.getPosition(),VectorHelper.mult(boat.getDirection().normalise(), (float)s));
		boat.setPosition(newPosition);
	}
	
	public double interpolateMaxV(PolarData valueMin, PolarData valueMax){
		//TODO
		return 0;
	}
	
	public void readInPolarPlotAsCsv(PolarData valueMin, PolarData valueMax){
		//TODO

	}

}
