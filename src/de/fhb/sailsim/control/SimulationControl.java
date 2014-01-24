package de.fhb.sailsim.control;

import de.fhb.calc.CalculationModel;
import de.fhb.calc.PolarPlotModel;
import de.fhb.sailsim.boat.BoatState;
import de.fhb.sailsim.worldmodel.Enviroment;
import de.fhb.sailsim.worldmodel.WindState;

public class SimulationControl {

	// Simulation issues
	private CalculationModel calculationModel;
	private Enviroment enviroment;
	private BoatState boatState;
	private WindState windState;

	public SimulationControl() {
		super();

		calculationModel = new PolarPlotModel();
		enviroment = new Enviroment();
		boatState = new BoatState(100, 100);
		windState = new WindState(0, 0);
	}
	
	public WindState getWindState() {
		return windState;
	}

	public Enviroment getEnviroment() {
		return enviroment;
	}

	public BoatState getBoatState() {
		return boatState;
	}

	/**
	 * 
	 * @param time
	 *            - timestep for calculation
	 */
	public void execute(long time) {
		calculationModel.calculateNextState(boatState, enviroment, time);
	}

}
