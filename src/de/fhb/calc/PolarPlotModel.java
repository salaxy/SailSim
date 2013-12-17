package de.fhb.calc;

import java.util.ArrayList;

import de.fhb.sailsim.boat.BoatState;
import de.fhb.sailsim.worldmodel.Enviroment;

public class PolarPlotModel extends CalculationModel {
	
	private ArrayList<PolarData> polarPlot = new ArrayList<PolarData>();

	@Override
	public void calculateNextState(BoatState boat, Enviroment world, long time) {
		
		//gesucht zurückgelegter Weg
		double s;
		
		//Beschleunigung m pro sekunde quadrat
		double a=2;
		// zeit in milisecounds
		long t=time;
		t=50; //entspricht 20fps
		//anfangsgeschwindigkeit meter pro sekunde
		double v0=boat.getCurrentPropulsionVelocity();
		
		//neue geschwindigkeit nach t
		double v=v0;
		
		//berechne zurückgelegten Weg
		s= (0.5*a) * (t*t) + (v0*t);

		if(boat.getCurrentPropulsionVelocity()>=boat.getMaxVelocity()){
			//boot geschwindigkeit erhöhen
			//berechne neue geschwindigkeit
			v= a * t +v0;
		}
		
	}
	
	public double interpolateMaxV(PolarData valueMin, PolarData valueMax){
		//TODO
		return 0;
	}
	
	public void readInPolarPlotAsCsv(PolarData valueMin, PolarData valueMax){
		//TODO


	}

	@Override
	public void calculateNextState(BoatState boat, Enviroment world) {
		// TODO Auto-generated method stub
		
	}


}
