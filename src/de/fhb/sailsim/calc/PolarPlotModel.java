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

	// in grad pro sekunde für eine geschwindigkeit von 10 m/s
	private final double ANGLE_VELOCITY = 40d;
	private final double REFERENCE_PROPULSION_VELOCITY = 1d;
	private final double MAX_VELOCITY = 5.0f;

	// Polar data
	private HashMap<Integer, HashMap<Integer, Double>> bigMap = new HashMap<Integer, HashMap<Integer, Double>>();

	public PolarPlotModel() {
		super();
		createTestPolar();
	}

	@Override
	public void calculateNextState(BoatState boat, Enviroment env, long time) {

		// gesucht zurückgelegter Weg
		double s;

		// Beschleunigung m pro sekunde quadrat
		// double a = this.ACCELERATION / SlickView.FRAMERATE; //linear,
		// depricated
		double a = this.calculateMaxVelocityFromPolar(boat, env, SlickView.FRAMERATE);

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

	private double calculateMaxVelocityFromPolar(BoatState boat, Enviroment env, int framerate) {
		// here pick up values from polar-modell

		int absoluteBoatToWind = 180 - env.getWindState().getWindToBoat();
		int windAcceleration = (int) env.getWindState().getStrength();
		float acceleration = 0;

		if (env.getWindState().getWindToBoat() < 0) {
			absoluteBoatToWind = 180 + env.getWindState().getWindToBoat();
		}

//		HashMap<Integer, Double> nextHigherMap = new HashMap<Integer, Double>();
//		HashMap<Integer, Double> nextLowerMap = new HashMap<Integer, Double>();
		
		HashMap<Integer, Double> nextHigherMap = null;
		HashMap<Integer, Double> nextLowerMap = null;

		int nextHigherAngle = 0;
		int nextLowerAngle = 0;

		double lowerAngleWindHighValue = 0;
		double lowerAngleWindLowValue = 0;
		double lowerAngleWindHighKey = 0;
		double lowerAngleWindLowKey = 0;

		double higherAngleWindHighValue = 0;
		double higherAngleWindLowValue = 0;
		double higherAngleWindHighKey = 0;
		double higherAngleWindLowKey = 0;

		// find lower map
		for (int i = absoluteBoatToWind; i >= 0; i--) {
			nextLowerMap = bigMap.get(new Integer(i));
			if (nextLowerMap != null) {
//				 System.out.println("next lower windmap found");
				nextLowerAngle = i;
				break;
			}
		}

		// find higher map
		for (int i = absoluteBoatToWind; i <= 180; i++) {
			nextHigherMap = bigMap.get(new Integer(i));
			if (nextHigherMap != null) {
				// System.out.println("next higher windmap found");
				nextHigherAngle = i;
				break;
			}
		}

		// find velocities in maps
		// find wind=key velocity=values in lower map
		for (int i = windAcceleration; i <= 40; i++) {
			lowerAngleWindHighValue = nextLowerMap.get(new Integer(i));
			lowerAngleWindHighKey = i;
			if (lowerAngleWindHighValue != 0) {
				break;
			}
		}
		
		for (int i = windAcceleration; i >= 0; i--) {
			lowerAngleWindLowValue = nextLowerMap.get(new Integer(i));
			lowerAngleWindLowKey = i;
			if (lowerAngleWindLowValue != 0) {
				break;
			}
		}

		// find wind=key velocity=values in higher map
		for (int i = windAcceleration; i <= 40; i++) {
			higherAngleWindHighValue = nextHigherMap.get(new Integer(i));
			higherAngleWindHighKey = i;
			if (higherAngleWindHighValue != 0) {
				break;
			}
		}
		for (int i = windAcceleration; i >= 0; i--) {
			higherAngleWindLowValue = nextHigherMap.get(new Integer(i));
			higherAngleWindLowKey = i;
			if (higherAngleWindLowValue != 0) {
				break;
			}
		}

		System.out.println("**** interpolation beginns ****");
		System.out.println("absoluteBoatToWind: " + absoluteBoatToWind + ", " + "windStrength"
				+ windAcceleration);
		double intrapoltedHigherAngleWind = interpolateValueFromValuesBetweenDiffKey(
				windAcceleration, higherAngleWindHighValue, higherAngleWindLowValue,
				higherAngleWindHighKey, higherAngleWindLowKey);
		System.out.println(" intrapoltedHigherAngleWind: " + intrapoltedHigherAngleWind
				+ " from Values: " + higherAngleWindHighValue + ", " + higherAngleWindLowValue
				+ "windstrenght:" + higherAngleWindHighKey + ", " + higherAngleWindLowKey);

		double intrapoltedLowerAngleWind = interpolateValueFromValuesBetweenDiffKey(
				windAcceleration, lowerAngleWindHighValue, lowerAngleWindLowValue,
				lowerAngleWindHighKey, lowerAngleWindLowKey);
		System.out.println(" intrapoltedLowerAngleWind: " + intrapoltedLowerAngleWind
				+ " from Values: " + lowerAngleWindHighValue + ", " + lowerAngleWindLowValue
				+ "windstrenght:" + lowerAngleWindHighKey + ", " + lowerAngleWindLowKey);

		double finalIntrapoltedVelocity = interpolateValueFromValuesBetweenDiffKey(
				absoluteBoatToWind, intrapoltedHigherAngleWind, intrapoltedLowerAngleWind,
				nextHigherAngle, nextLowerAngle);
		System.out.println(" finalIntrapoltedVelocity: " + finalIntrapoltedVelocity
				+ " from Values: " + intrapoltedHigherAngleWind + ", " + intrapoltedLowerAngleWind
				+ "angles:" + nextHigherAngle + ", " + nextLowerAngle);

		System.out.println("interpolation : " + "absWindToBoat: " + absoluteBoatToWind
				+ "absWindToBoat: " + absoluteBoatToWind + "absWindToBoat: " + absoluteBoatToWind
				+ "absWindToBoat: " + absoluteBoatToWind);

		acceleration = acceleration / 10000;
		return acceleration / framerate;
	}

	private double interpolateValueFromValuesBetweenDiffKey(int windAcceleration,
			double angleWindHighValue, double angleWindLowValue, double angleWindHighKey,
			double angleWindLowKey) {

		if (angleWindHighKey == angleWindLowKey) {
			return angleWindHighValue;
		}

		double diffWindKeys = angleWindHighKey - angleWindLowKey;
		double diffWindValue = angleWindHighValue - angleWindLowValue;
		double diffActualWindStrength = windAcceleration - angleWindLowKey;
		double interpolatedWindVelocity = angleWindLowValue
				+ (diffActualWindStrength * (diffWindValue / diffWindKeys));
		return interpolatedWindVelocity;
	}

	public void createTestPolar() {

		HashMap<Integer, Double> hm00 = new HashMap<Integer, Double>();
//		hm00.put(0, 0d);
		hm00.put(2, 1d);
		hm00.put(5, 2d);
		hm00.put(10, 4d);
		hm00.put(12, 10d);
		hm00.put(20, 12d);
		hm00.put(30, 14d);
		hm00.put(40, 16d);
		bigMap.put(0, hm00);

		HashMap<Integer, Double> hm30 = new HashMap<Integer, Double>();
//		hm30.put(0, 0d);
		hm30.put(2, 2d);
		hm30.put(5, 4d);
		hm30.put(10, 8d);
		hm30.put(12, 10d);
		hm30.put(20, 12d);
		hm30.put(30, 14d);
		hm30.put(40, 16d);
		bigMap.put(30, hm30);

		HashMap<Integer, Double> hm60 = new HashMap<Integer, Double>();
//		hm60.put(0, 0d);
		hm60.put(2, 2d);
		hm60.put(5, 4d);
		hm60.put(10, 8d);
		hm60.put(12, 10d);
		hm60.put(20, 12d);
		hm60.put(30, 14d);
		hm60.put(40, 16d);
		bigMap.put(60, hm60);

		HashMap<Integer, Double> hm90 = new HashMap<Integer, Double>();
//		hm90.put(0, 0d);
		hm90.put(2, 2d);
		hm90.put(5, 4d);
		hm90.put(10, 8d);
		hm90.put(12, 10d);
		hm90.put(20, 12d);
		hm90.put(30, 14d);
		hm90.put(40, 16d);
		bigMap.put(90, hm90);

		HashMap<Integer, Double> hm120 = new HashMap<Integer, Double>();
//		hm120.put(0, 0d);
		hm120.put(2, 3d);
		hm120.put(5, 6d);
		hm120.put(10, 9d);
		hm120.put(12, 14d);
		hm120.put(20, 20d);
		hm120.put(30, 27d);
		hm120.put(40, 36d);
		bigMap.put(120, hm120);

		HashMap<Integer, Double> hm180 = new HashMap<Integer, Double>();
		hm180.put(0, 0d);
		hm180.put(2, 2d);
		hm180.put(5, 4d);
		hm180.put(10, 8d);
		hm180.put(12, 10d);
		hm180.put(20, 12d);
		hm180.put(30, 14d);
		hm180.put(40, 16d);
		bigMap.put(180, hm180);

	}

	private double calculateActualAccelerationDummy(BoatState boat, Enviroment env, int framerate) {

		int absoluteBoatToWind = env.getWindState().getWindToBoat();
		float velocity = 0;

		if (env.getWindState().getWindToBoat() < 0) {
			absoluteBoatToWind = -env.getWindState().getWindToBoat();
		}

		if (absoluteBoatToWind <= 5) {
			velocity = 1.0f;
		} else if (absoluteBoatToWind <= 15) {
			velocity = 1.5f;
		} else if (absoluteBoatToWind <= 30) {
			velocity = 1.7f;
		} else if (absoluteBoatToWind <= 45) {
			velocity = 1.5f;
		} else if (absoluteBoatToWind <= 60) {
			velocity = 1.25f;
		} else if (absoluteBoatToWind <= 75) {
			velocity = 1.1f;
		} else if (absoluteBoatToWind <= 90) {
			velocity = 1.0f;
		} else if (absoluteBoatToWind <= 105) {
			velocity = 0.75f;
		} else if (absoluteBoatToWind <= 120) {
			velocity = 0.5f;
		} else if (absoluteBoatToWind <= 135) {
			velocity = 0.375f;
		} else if (absoluteBoatToWind <= 150) {
			velocity = 0.25f;
		} else if (absoluteBoatToWind <= 165) {
			velocity = 0.125f;
		} else if (absoluteBoatToWind <= 170) {
			velocity = 0.1f;
		} else if (absoluteBoatToWind <= 175) {
			velocity = 0.05f;
		} else if (absoluteBoatToWind <= 180) {
			velocity = 0.0f;
		}

		velocity = velocity / 10000;
		return velocity / framerate;
	}

}