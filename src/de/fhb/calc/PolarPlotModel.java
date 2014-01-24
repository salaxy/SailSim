package de.fhb.calc;

import java.util.ArrayList;
import java.util.HashMap;

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
	public void calculateNextState(BoatState boat, Enviroment env, long time) {
		
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
			boat.setDirectionValue(boat.getDirectionValue()+rotateV);
		}
		
		//berechnen der neuen Postionsvektors
		Vector2f newPosition;
		newPosition = VectorHelper.add(boat.getPosition(),VectorHelper.mult(boat.getDirection().normalise(), (float)s));
		boat.setPosition(newPosition);
		
		this.calculateMaxVelocityDepencyOfWinddirection(boat, env, time);
	}
	
	private double interpolateMaxV(PolarData valueMin, PolarData valueMax){
		//TODO
		return 0;
	}
	
	public void readInPolarPlotAsCsv(PolarData valueMin, PolarData valueMax){
		//TODO

	}
	
	public void calculateMaxVelocityDepencyOfWinddirection(BoatState boat, Enviroment env, long time){
		
		int boatDirection=(int)boat.getDirectionValue();
		int boatTheta=(int)boat.getDirection().getTheta();
		int windDirection=(int) env.getWindState().getDirection();
		int diffence = windDirection - boatDirection ;
		
		if(diffence<0){
			diffence=-diffence;
		}
		
		if(windDirection>180){
			diffence=180-diffence;
		}
//		diffence=180-diffence;
		env.getWindState().setWindToBoat(diffence);
		
		System.out.println("boat: " + boatDirection);
		System.out.println("boat theta: " + boatTheta);
		System.out.println("wind: " + windDirection);
		System.out.println("boat to wind: " + diffence);

		

	}
	
	public void createTestPolar(){
		
		HashMap<Integer, HashMap<Integer, Double>> bigMap = new HashMap<Integer, HashMap<Integer, Double>>();
		
		HashMap<Integer, Double> hm30=new HashMap<Integer, Double>();
		hm30.put(3,2d);
		hm30.put(6,4d);
		hm30.put(9,8d);
		hm30.put(12,10d);
		hm30.put(20,12d);
		hm30.put(30,14d);
		hm30.put(40,16d);
		bigMap.put(30, hm30);
		
		HashMap<Integer, Double> hm60=new HashMap<Integer, Double>();
		hm60.put(3,2d);
		hm60.put(6,4d);
		hm60.put(9,8d);
		hm60.put(12,10d);
		hm60.put(20,12d);
		hm60.put(30,14d);
		hm60.put(40,16d);
		bigMap.put(60, hm60);
		
		HashMap<Integer, Double> hm90=new HashMap<Integer, Double>();
		hm90.put(3,2d);
		hm90.put(6,4d);
		hm90.put(9,8d);
		hm90.put(12,10d);
		hm90.put(20,12d);
		hm90.put(30,14d);
		hm90.put(40,16d);
		bigMap.put(90, hm90);
		
		HashMap<Integer, Double> hm120=new HashMap<Integer, Double>();
		hm120.put(3,3d);
		hm120.put(6,6d);
		hm120.put(9,9d);
		hm120.put(12,14d);
		hm120.put(20,20d);
		hm120.put(30,27d);
		hm120.put(40,36d);
		bigMap.put(120, hm120);
		
		HashMap<Integer, Double> hm180=new HashMap<Integer, Double>();
		hm180.put(3,2d);
		hm180.put(6,4d);
		hm180.put(9,8d);
		hm180.put(12,10d);
		hm180.put(20,12d);
		hm180.put(30,14d);
		hm180.put(40,16d);
		bigMap.put(180, hm180);
		
	}
	
	public void calculateIdealSailPosition(){
		
		
	}
	

}
