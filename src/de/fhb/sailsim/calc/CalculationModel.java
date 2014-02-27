package de.fhb.sailsim.calc;

import de.fhb.sailsim.boat.BoatState;
import de.fhb.sailsim.worldmodel.Enviroment;

/**
 * CalculationModel calculates the next state of model of simulation
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 * 
 */
public abstract class CalculationModel {

	/**
	 * calculates the next state of simulationmodel
	 * 
	 * @param boat
	 * @param world
	 * @param time
	 */
	public abstract void calculateNextState(BoatState boat, Enviroment world, long time);

}
