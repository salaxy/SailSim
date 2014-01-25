package de.fhb.sailsim.control;

import de.fhb.sailsim.boat.BoatState;
import de.fhb.sailsim.calc.CalculationModel;
import de.fhb.sailsim.calc.PolarPlotModel;
import de.fhb.sailsim.worldmodel.Enviroment;

public class SimulationControl {

	// Simulation issues
	private CalculationModel calculationModel;
	private Enviroment enviroment;
	private BoatState boatState;

	public SimulationControl() {
		super();

		calculationModel = new PolarPlotModel();
		enviroment = new Enviroment();
		boatState = new BoatState(300, 100);
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
