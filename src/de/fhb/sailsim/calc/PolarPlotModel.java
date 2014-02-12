package de.fhb.sailsim.calc;

import java.util.HashMap;

import org.newdawn.slick.geom.Vector2f;

import de.fhb.sailsim.boat.BoatState;
import de.fhb.sailsim.userinterface.slick.SlickView;
import de.fhb.sailsim.userinterface.slick.VectorHelper;
import de.fhb.sailsim.worldmodel.Enviroment;

public class PolarPlotModel extends CalculationModel {

	// pixel/meter per sec
	private final double FALL_BACK_ACCELERATION = -0.00004d;
	private final double MIN_VELOCITY = 0.1d;

	// in grad pro sekunde für eien geschwindigkeit von 10 m/s
	private final double ANGLE_VELOCITY = 40d;
	private final double REFERENCE_PROPULSION_VELOCITY = 1d;
	private final double MAX_VELOCITY = 5.0f;

	// private ArrayList<PolarData> polarPlot = new ArrayList<PolarData>();
	private HashMap<Integer, HashMap<Integer, Double>> bigMap = new HashMap<Integer, HashMap<Integer, Double>>();

	@Override
	public void calculateNextState(BoatState boat, Enviroment env, long time) {

		// gesucht zurückgelegter Weg
		double s;

		// Beschleunigung m pro sekunde quadrat
		// double a = this.ACCELERATION / SlickView.FRAMERATE; //linear,
		// depricated
		double a = this.calculateActualAcceleration(boat, env, SlickView.FRAMERATE);

		// zeit in sekunden
		long t = time; // t=1000/SlickView.FRAMERATE;
		// anfangsgeschwindigkeit meter pro sekunde
		double propulsionV = boat.getCurrentPropulsionVelocity();

		// aktuelle geschwindigkeit für ein Frame
		double v = propulsionV / SlickView.FRAMERATE;

		// berechne zurückgelegten Weg
		// eigentlich s = (0.5 * a) * (t * t) + (v * t) + s0;
		// aber berechnung hier nur für die nächste Teilstrecke
		s = (0.5 * a) * (t * t) + (v * t);

		if (boat.getCurrentPropulsionVelocity() <= MAX_VELOCITY) {
			// boot geschwindigkeit erhöhen
			// berechne neue geschwindigkeit
			// TODO wieder einkommentieren
			v = (a + FALL_BACK_ACCELERATION / SlickView.FRAMERATE) * t + v;
			if (v > 0) {
				boat.setCurrentPropulsionVelocity(v * SlickView.FRAMERATE);
			} else {
				boat.setCurrentPropulsionVelocity(0);
			}
		}

		// calc angle velocity in depency of propulsion velocity
		// TODO der Wendekreis bleibt immer gleich egal welche geschwindigkeit,
		// ist das wirklich so, ist das real???
		double rotateV = (propulsionV * ANGLE_VELOCITY) / REFERENCE_PROPULSION_VELOCITY;
		rotateV = rotateV / SlickView.FRAMERATE;
		int ruderAngle = boat.getRuderDeflection();

		if (ruderAngle < 0) {
			rotateV = -rotateV;
		}// else rotateV=rotateV

		// verhalten in Abhängigkeit davon wie stark das Ruder eingelschagen ist
		int ruderHelper = ruderAngle;
		if (ruderAngle < 0) {
			ruderHelper = -ruderAngle;
		}
		rotateV = (ruderHelper * rotateV) / BoatState.MAX_RUDER_AMPLITUDE;

		// System.out.println("rotateV: " + rotateV);

		// berechnen des neuen Winkels
		if (ruderAngle != 0) {
			boat.getDirection().add(rotateV);
			boat.setDirectionValue(boat.getDirectionValue() + rotateV);
		}

		// sync boatState
		// berechnen der neuen Postionsvektors
		Vector2f newPosition;
		newPosition = VectorHelper.add(boat.getPosition(),
				VectorHelper.mult(boat.getDirection().normalise(), (float) s));
		boat.setPosition(newPosition);

		boat.setCurrentSpinVelocity(rotateV * SlickView.FRAMERATE);

		calcAngleDifference(boat, env, time);
		calcSailDeflection(boat, env);

	}

	private double calculateActualAccelerationDummy(BoatState boat, Enviroment env, int framerate) {
		// here pick up values from polar-modell

		int absoluteBoatToWind = env.getWindState().getWindToBoat();
		float acceleration = 0;

		if (env.getWindState().getWindToBoat() < 0) {
			absoluteBoatToWind = -env.getWindState().getWindToBoat();
		}

		if (absoluteBoatToWind <= 5) {
			acceleration = 1.0f;
		} else if (absoluteBoatToWind <= 15) {
			acceleration = 1.5f;
		} else if (absoluteBoatToWind <= 30) {
			acceleration = 1.7f;
		} else if (absoluteBoatToWind <= 45) {
			acceleration = 1.5f;
		} else if (absoluteBoatToWind <= 60) {
			acceleration = 1.25f;
		} else if (absoluteBoatToWind <= 75) {
			acceleration = 1.1f;
		} else if (absoluteBoatToWind <= 90) {
			acceleration = 1.0f;
		} else if (absoluteBoatToWind <= 105) {
			acceleration = 0.75f;
		} else if (absoluteBoatToWind <= 120) {
			acceleration = 0.5f;
		} else if (absoluteBoatToWind <= 135) {
			acceleration = 0.375f;
		} else if (absoluteBoatToWind <= 150) {
			acceleration = 0.25f;
		} else if (absoluteBoatToWind <= 165) {
			acceleration = 0.125f;
		} else if (absoluteBoatToWind <= 170) {
			acceleration = 0.1f;
		} else if (absoluteBoatToWind <= 175) {
			acceleration = 0.05f;
		} else if (absoluteBoatToWind <= 180) {
			acceleration = 0.0f;
		}

		acceleration = acceleration / 10000;

		// return this.ACCELERATION / SlickView.FRAMERATE;
		return acceleration / framerate;
	}

	private void calcSailDeflection(BoatState boat, Enviroment env) {

		// calculate sailDeflection
		int boatToWind = env.getWindState().getWindToBoat();
		int newSailAngle = boat.getSailDeflection();

		if (boatToWind >= 0 && boatToWind <= 180) {
			newSailAngle = (int) ((180 - boatToWind) / 2);
		} else {
			// if(boatToWind<0 && boatToWind>=-180)
			newSailAngle = -(int) ((180 + boatToWind) / 2);
		}
		boat.setSailDeflection(newSailAngle);

		double boatAngle = boat.getDirectionValue();
		double windAngle = env.getWindState().getDirection();
		int sailAngle = boat.getSailDeflection();

		// falsche Segel stellungen durch Wende korrigieren bzw. patenthalse
		// if(boatAngle>0){
		if (windAngle >= boatAngle + 60 && windAngle <= 360) {
			this.invert(boat);
		}

	}

	private void invert(BoatState boat) {
		boat.setSailDeflection(-boat.getSailDeflection());
	}

	private double interpolateMaxV(PolarData valueMin, PolarData valueMax) {
		// TODO
		return 0;
	}

	public void readInPolarPlotAsCsv(PolarData valueMin, PolarData valueMax) {
		// TODO

	}

	// public void calculateMaxVelocityDepencyOfWinddirection(BoatState boat,
	// Enviroment env, long time){
	// //TODO
	//
	// }

	public void calcAngleDifference(BoatState boat, Enviroment env, long time) {
		int boatDirection = (int) boat.getDirectionValue();
		// int boatTheta = (int) boat.getDirection().getTheta();
		int windDirection = (int) env.getWindState().getDirection();
		int diff = windDirection - boatDirection;

		if (diff < 0) {
			diff = -diff;
		}

		if (diff > 180) {
			diff = 180 - diff;
			if (diff < 0) {
				diff = -(180 + diff);
			}
		}
		env.getWindState().setWindToBoat(diff);

		// System.out.println("boat: " + boatDirection);
		// System.out.println("boat theta: " + boatTheta);
		// System.out.println("wind: " + windDirection);
	}

	private double calculateActualAcceleration(BoatState boat, Enviroment env, int framerate) {
		// here pick up values from polar-modell

		int absoluteBoatToWind = 180 - env.getWindState().getWindToBoat();
		int windAcceleration = (int) env.getWindState().getStrength();
		float acceleration = 0;

		if (env.getWindState().getWindToBoat() < 0) {
			absoluteBoatToWind = 180 + env.getWindState().getWindToBoat();
		}

		HashMap<Integer, Double> nextHigherMap = new HashMap<Integer, Double>();
		HashMap<Integer, Double> nextLowerMap = new HashMap<Integer, Double>();

		int nextHigherAngle = 0;
		int nextLowerAngle = 0;

		// find lower map
		for (int i = absoluteBoatToWind; i >= 0; i--) {
			nextLowerMap = bigMap.get(new Integer(i));
			if (nextLowerMap != null) {
				System.out.println("next lower windmap found");
				nextLowerAngle = i;
				break;
			}
		}

		// find higher map
		for (int i = absoluteBoatToWind; i <= 180; i++) {
			nextHigherMap = bigMap.get(new Integer(i));
			if (nextHigherMap != null) {
				System.out.println("next higher windmap found");
				nextHigherAngle = i;
				break;
			}
		}

		double lowerAngleWindHigh = 0;
		double lowerAngleWindLow = 0;

		double higherAngleWindHigh = 0;
		double higherAngleWindLow = 0;

		// find wind values in lower map
		for (int i = windAcceleration; i <= 40; i++) {
			lowerAngleWindHigh = nextLowerMap.get(new Integer(i));
			if (nextLowerMap != null) {
				System.out.println("next lower windmap found");
				break;
			}
		}
		for (int i = windAcceleration; i >= 0; i--) {
			lowerAngleWindLow = nextLowerMap.get(new Integer(i));
			if (nextLowerMap != null) {
				System.out.println("next lower windmap found");
				break;
			}
		}
		// find wind values in higher map
		for (int i = windAcceleration; i <= 40; i++) {
			higherAngleWindHigh = nextHigherMap.get(new Integer(i));
			if (nextLowerMap != null) {
				System.out.println("next lower windmap found");
				break;
			}
		}
		for (int i = windAcceleration; i >= 0; i--) {
			higherAngleWindLow = nextHigherMap.get(new Integer(i));
			if (nextLowerMap != null) {
				System.out.println("next lower windmap found");
				break;
			}
		}
		// syso alle vier werte
		// 
		//TODO hier weiter machen morgen
		acceleration = this.interpolateMaxV(nextHigherAngle, nextLowerAngle, lowerAngleWindHigh,
				lowerAngleWindLow, higherAngleWindHigh, higherAngleWindLow);

		// finde Wind Geschwindigkeiten in den Maps

		bigMap.get(30);

		if (absoluteBoatToWind <= 5) {
			acceleration = 1.0f;
		} else if (absoluteBoatToWind <= 15) {
			acceleration = 1.5f;
		} else if (absoluteBoatToWind <= 30) {
			acceleration = 1.7f;
		} else if (absoluteBoatToWind <= 45) {
			acceleration = 1.5f;
		} else if (absoluteBoatToWind <= 60) {
			acceleration = 1.25f;
		} else if (absoluteBoatToWind <= 75) {
			acceleration = 1.1f;
		} else if (absoluteBoatToWind <= 90) {
			acceleration = 1.0f;
		} else if (absoluteBoatToWind <= 105) {
			acceleration = 0.75f;
		} else if (absoluteBoatToWind <= 120) {
			acceleration = 0.5f;
		} else if (absoluteBoatToWind <= 135) {
			acceleration = 0.375f;
		} else if (absoluteBoatToWind <= 150) {
			acceleration = 0.25f;
		} else if (absoluteBoatToWind <= 165) {
			acceleration = 0.125f;
		} else if (absoluteBoatToWind <= 170) {
			acceleration = 0.1f;
		} else if (absoluteBoatToWind <= 175) {
			acceleration = 0.05f;
		} else if (absoluteBoatToWind <= 180) {
			acceleration = 0.0f;
		}

		acceleration = acceleration / 10000;

		// return this.ACCELERATION / SlickView.FRAMERATE;
		return acceleration / framerate;
	}

	private float interpolateMaxV(int nextHigherAngle, int nextLowerAngle,
			double lowerAngleWindHigh, double lowerAngleWindLow, double higherAngleWindHigh,
			double higherAngleWindLow) {
		// TODO Auto-generated method stub

		return nextLowerAngle;

	}

	public void createTestPolar() {

		// HashMap<Integer, HashMap<Integer, Double>> bigMap = new
		// HashMap<Integer, HashMap<Integer, Double>>();

		HashMap<Integer, Double> hm00 = new HashMap<Integer, Double>();
		hm00.put(3, 1d);
		hm00.put(6, 2d);
		hm00.put(9, 4d);
		hm00.put(12, 10d);
		hm00.put(20, 12d);
		hm00.put(30, 14d);
		hm00.put(40, 16d);
		bigMap.put(0, hm00);

		HashMap<Integer, Double> hm30 = new HashMap<Integer, Double>();
		hm30.put(3, 2d);
		hm30.put(6, 4d);
		hm30.put(9, 8d);
		hm30.put(12, 10d);
		hm30.put(20, 12d);
		hm30.put(30, 14d);
		hm30.put(40, 16d);
		bigMap.put(30, hm30);

		HashMap<Integer, Double> hm60 = new HashMap<Integer, Double>();
		hm60.put(3, 2d);
		hm60.put(6, 4d);
		hm60.put(9, 8d);
		hm60.put(12, 10d);
		hm60.put(20, 12d);
		hm60.put(30, 14d);
		hm60.put(40, 16d);
		bigMap.put(60, hm60);

		HashMap<Integer, Double> hm90 = new HashMap<Integer, Double>();
		hm90.put(3, 2d);
		hm90.put(6, 4d);
		hm90.put(9, 8d);
		hm90.put(12, 10d);
		hm90.put(20, 12d);
		hm90.put(30, 14d);
		hm90.put(40, 16d);
		bigMap.put(90, hm90);

		HashMap<Integer, Double> hm120 = new HashMap<Integer, Double>();
		hm120.put(3, 3d);
		hm120.put(6, 6d);
		hm120.put(9, 9d);
		hm120.put(12, 14d);
		hm120.put(20, 20d);
		hm120.put(30, 27d);
		hm120.put(40, 36d);
		bigMap.put(120, hm120);

		HashMap<Integer, Double> hm180 = new HashMap<Integer, Double>();
		hm180.put(3, 2d);
		hm180.put(6, 4d);
		hm180.put(9, 8d);
		hm180.put(12, 10d);
		hm180.put(20, 12d);
		hm180.put(30, 14d);
		hm180.put(40, 16d);
		bigMap.put(180, hm180);

	}

	public PolarPlotModel() {
		super();
		createTestPolar();
	}

}
