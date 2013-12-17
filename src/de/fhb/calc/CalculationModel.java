package de.fhb.calc;

import de.fhb.sailsim.boat.BoatState;
import de.fhb.sailsim.worldmodel.Enviroment;

public abstract class CalculationModel {

	public abstract void calculateNextState(BoatState boat, Enviroment world, long time);
	
}
