package de.fhb.calc;

import java.util.ArrayList;

import de.fhb.sailsim.boat.BoatState;
import de.fhb.sailsim.worldmodel.Enviroment;

public class PolarPlotModel extends CalculationModel {
	
	private ArrayList<PolarData> polarPlot = new ArrayList<PolarData>();

	@Override
	public void calculateNextState(BoatState boat, Enviroment world) {
		// TODO Auto-generated method stub
		
	}
	
	public double interpolateMaxV(PolarData valueMin, PolarData valueMax){
		//TODO
		return 0;
	}
	
	public void readInPolarPlotAsCsv(PolarData valueMin, PolarData valueMax){
		//TODO


	}	

}
