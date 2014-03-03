package de.fhb.sailsim.calc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.newdawn.slick.geom.Vector2f;

import de.fhb.sailsim.boat.BoatState;
import de.fhb.sailsim.userinterface.slick.SlickView;
import de.fhb.sailsim.userinterface.slick.VectorHelper;
import de.fhb.sailsim.worldmodel.Enviroment;

/**
 * PolarPlotModel calculates the next state of model of simulation based on the
 * polarmap with maxmium velocities at a specific angle to wind and windstrength
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 * 
 */
public class PolarPlotModel extends CalculationModel {

	// meter(pixel) per sec
	private final double FALL_BACK_ACCELERATION = -0.00004d;
	private final int VELOCITY_DIVDER = 10000;

	// in grad pro sekunde für eine geschwindigkeit von 10 m/s
	private final double ANGLE_VELOCITY_REFERENCE = 40d;
	private final double REFERENCE_PROPULSION_VELOCITY = 1d;

	// Polar data stored in a Hashmap
	private HashMap<Integer, HashMap<Integer, Double>> polarMap = new HashMap<Integer, HashMap<Integer, Double>>();

	/**
	 * Constructur of the class with initialize a Testpolar in polarMap
	 */
	public PolarPlotModel() {
		super();

		InputStream input = null;

		try {
			// Load polars from properties
			loadPolarsFromProperties(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// createTestPolar();
		}

	}

	@Override
	public void calculateNextState(BoatState boat, Enviroment env, long time) {

		calcAngleWindToBoat(boat, env);
		calcAndSetSailDeflection(boat, env);
		calcAndSetApparentWind(boat, env);

		// Gesucht ist der zurückgelegte Weg s
		double s;

		// zeit in sekunden
		// t=1000/SlickView.FRAMERATE;
		long t = time;
		// Anfangsgeschwindigkeit in Meter pro Sekunde
		double propulsionV = boat.getCurrentPropulsionVelocity();

		// Beschleunigung Meter pro Sekunde quadrat in Abhängigkeit von MaxV
		double polarMaxV = this.interpolateMaxVelocityFromPolar(boat, env);
		double a = polarMaxV / VELOCITY_DIVDER / SlickView.FRAMERATE;

		System.out.println("beschleunigung: " + a);
		System.out.println("polarMaxV: " + polarMaxV);

		// aktuelle geschwindigkeit für ein Frame
		double v = propulsionV / SlickView.FRAMERATE;

		// Berechnung des zurückgelegten Weges
		// eigentlich s = (0.5 * a) * (t * t) + (v * t) + s0;
		// hier aber nur für die nächste Teilstrecke
		s = (0.5 * a) * (t * t) + (v * t);

		// Berechnung der neuen Geschwindigkeit
		// Beschränkung von V auf MaxV aus der Polarmap
		if ((v * SlickView.FRAMERATE) > polarMaxV) {

			// Entschleunigung wenn MaxV überschritten
			double fallbackAcceleration = FALL_BACK_ACCELERATION / SlickView.FRAMERATE;
			v = fallbackAcceleration * t + v;

			// Falls Geschwindigkeit negativ, setze 0
			if (v < 0) {
				v = 0;
			}
		} else {
			// sonst Beschleunigung
			v = a * t + v;
		}

		double rotateV = calcRotationValues(boat, s, propulsionV);
		syncBoatPosition(boat, s, v * SlickView.FRAMERATE, rotateV * SlickView.FRAMERATE);
	}

	/**
	 * calaculates seemingly wind direction and strength
	 * 
	 * @param boat
	 * @param env
	 */
	private void calcAndSetApparentWind(BoatState boat, Enviroment env) {

		// gegeben
		double wfDirection = boat.getDirectionValue();
		double wwDirection = env.getWindState().getDirection();

		double wfStrength = boat.getCurrentPropulsionVelocity();
		double wwStrength = env.getWindState().getStrength();

		// Fahrtwindstärke anpassen Fahrt von m/s in km/h
		// und Fahrtrichtung umkehren
		wfStrength = (wfStrength * 60 * 60) / 1000;
		wfDirection = (wfDirection + 180) % 360;
		// umwandeln in Bogenmaß!
		wfDirection = (2 * Math.PI) / 360 * wfDirection;
		wwDirection = (2 * Math.PI) / 360 * wwDirection;		
		System.out.println("wfDirection " + wfDirection);

		// Vektoren erstellen
		Vector2f wf = WindValuesToVector2f(wfDirection, wfStrength);
		Vector2f ww = WindValuesToVector2f(wwDirection, wwStrength);
		System.out.println("wf.x " + wf.x + " wf.y " + wf.y);
		System.out.println("ww.x " + ww.x + " ww.y " + ww.y);

		// neuen Vektor für den scheinbaren wind berechnen
		Vector2f ws = new Vector2f(wf.x + ww.x, wf.y + ww.y);
		System.out.println("ws.x " + ws.x + " ws.y " + ws.y);

		// berechnen von winkel und stärke
		double wsStrength = Math.sqrt((ws.x * ws.x) + (ws.y * ws.y));
		double cosAngle = ws.y / wsStrength;
		if (cosAngle >= 1d) {
			cosAngle = 1d;
		}
		if (cosAngle <= -1d) {
			cosAngle = -1d;
		}
		double wsDirection = Math.acos(cosAngle);

		System.out.println(ws.y / wsStrength);
		wsDirection = Math.acos((ws.x * ww.x)
				+ (ws.y * ww.y)
				/ (Math.sqrt((ww.x * ww.x) + (ww.y * ww.y)) * Math.sqrt((ws.x * ws.x)
						+ (ws.y * ws.y))));
		// wsDirection= ws.getTheta();

		// //Via Cosinussatz
		// double beta = (env.getWindState().getWindToBoat() + 180) % 360;
		// double a = wwStrength;
		// double c = wfStrength;
		// double b = Math.sqrt((a * a) + (c * c) - 2 * a * c * Math.cos(beta));
		// double cosGamma=((a * a) + (c * c) + (b * b)) / (2 * a * b);
		// double gamma = Math.acos(cosGamma);
		// System.out.println(gamma);
		// System.out.println(Math.abs(((a * a) + (c * c) + (b * b)) / (2 * a *
		// b)));
		// if(cosGamma>=1d){
		// cosGamma=1d;
		// }
		// if(cosGamma<=-1d){
		// cosGamma=-1d;
		// }
		// // berechnen von winkel und stärke
		// wsStrength = b;
		// wsDirection = env.getWindState().getWindToBoat() + gamma;

		// Formel von Wikipedia
		// // berechnen von winkel und stärke
		// double V = boat.getCurrentPropulsionVelocity();
		// double W = env.getWindState().getStrength();
		// double alpha = env.getWindState().getDirection();
		// double A = Math.sqrt((W * W) + (V * V) + ((2 * W * V) *
		// Math.cos(alpha)));
		//
		// wsStrength = A;
		// double cosAlpha=((W*Math.cos(alpha))+V)/A;
		// if(cosAlpha>=1d){
		// cosAlpha=1d;
		// }
		// if(cosAlpha<=-1d){
		// cosAlpha=-1d;
		// }
		// wsDirection = Math.acos(cosAlpha);

		// Umrechnung von Bogenmaß auf Gradmaß
		wsDirection = (360 / (2 * Math.PI)) * wsDirection;

		// setze Boat variablen
		boat.getSeeminglyWind().setDirection(wsDirection);
		boat.getSeeminglyWind().setStrength(wsStrength);
		System.out.println("Scheinbarer Wind: " + "wsStrength: " + wsStrength + " wsDirection: "
				+ wsDirection);
	}

	/**
	 * calculates a Vector2f from wind stength and direction
	 * 
	 * @param direction
	 *            - 0 to 360 degree
	 * @param strength
	 *            - velocity
	 * @return
	 */
	private Vector2f WindValuesToVector2f(double direction, double strength) {
		double x = Math.sin(direction);
		double y = Math.cos(direction);
		double normFactor = strength / Math.sqrt((x * x) + (y * y));
		return new Vector2f((float) (normFactor * x), (float) (normFactor * y));
		// double normFactor = strength / Math.sqrt(x * x + y * y);
		// return new Vector2f((float) ( x), (float) ( y));
	}

	/**
	 * setzt neue Bootsposition
	 * 
	 * @param boat
	 * @param s
	 * @param v
	 * @param rotateV
	 */
	private void syncBoatPosition(BoatState boat, double s, double v, double rotateV) {
		// sync velocities
		boat.setCurrentPropulsionVelocity(v);
		boat.setCurrentSpinVelocity(rotateV);

		// Berechnen des neuen Postionsvektors
		Vector2f newPosition;
		newPosition = VectorHelper.add(boat.getPosition(),
				VectorHelper.mult(boat.getDirection().normalise(), (float) s));
		boat.setPosition(newPosition);
	}

	/**
	 * calc angle rotateV and rotateS in depency of propulsion velocity in
	 * depency of Framerate
	 * 
	 * @param boat
	 * @param s
	 * @param propulsionV
	 * @return actualRotateV for a single timesnipet (actualRotateV/
	 *         SlickView.FRAMERATE)
	 */
	private double calcRotationValues(BoatState boat, double s, double propulsionV) {
		// Berechnung der Rotationsgeschwindigkeit abhängig von
		// Referenzgeschwindigkeiten
		int ruderAngle = boat.getRuderDeflection();
		double maxRotateV = (propulsionV * ANGLE_VELOCITY_REFERENCE)
				/ REFERENCE_PROPULSION_VELOCITY;
		maxRotateV = maxRotateV / SlickView.FRAMERATE;

		if (ruderAngle < 0) {
			maxRotateV = -maxRotateV;
		}// else rotateV=rotateV

		// Berechnung der Rotationsgeschw. in Abhängigkeit des Ruderwinkels
		double actualRotateV = (Math.abs(ruderAngle) * maxRotateV) / BoatState.MAX_RUDER_AMPLITUDE;
		// Wegberechnung s=v ohne (+a*t)
		double rotateS = actualRotateV;

		// berechnen des neuen Winkels
		if (ruderAngle != 0) {
			boat.getDirection().add(rotateS);
			boat.setDirectionValue(boat.getDirectionValue() + actualRotateV);
		}

		return actualRotateV;
	}

	/**
	 * calc and sets the sailDeflection
	 * 
	 * @param boat
	 * @param env
	 */
	private void calcAndSetSailDeflection(BoatState boat, Enviroment env) {
		int boatToWind = env.getWindState().getWindToBoat();
		int newSailAngle = boat.getSailDeflection();
		double boatAngle = boat.getDirectionValue();
		double windAngle = env.getWindState().getDirection();

		if (boatToWind >= 0) {
			newSailAngle = (int) ((180 - boatToWind) / 2);
		} else {
			newSailAngle = -(int) ((180 + boatToWind) / 2);
		}
		boat.setSailDeflection(newSailAngle);

		// falsche Segelstellungen durch Wende des Segels korrigieren
		if (windAngle >= boatAngle + 60 && windAngle <= 360) {
			this.invertSailDeflection(boat);
		}
	}

	private void invertSailDeflection(BoatState boat) {
		boat.setSailDeflection(-boat.getSailDeflection());
	}

	/**
	 * Berechnet den Winkel von Boot zum Wind im Wertebereich von 180 bis -180
	 * grad positiv = Wind von rechts, negativ = Wind von links
	 * 
	 * @param boat
	 * @param env
	 */
	private void calcAngleWindToBoat(BoatState boat, Enviroment env) {
		int boatDirection = (int) boat.getDirectionValue();
		int windDirection = (int) env.getWindState().getDirection();
		int absDiff = Math.abs(windDirection - boatDirection);

		// bilde absDiff auf den Wertebereich 180 bis -180 grad ab
		if (absDiff > 180) {
			absDiff = -(360 - absDiff);
		}
		env.getWindState().setWindToBoat(absDiff);

		System.out.println("calcAngleWindToBoat" + "(boat: " + boatDirection + ", wind: "
				+ windDirection + ", absDiff: " + absDiff + ")");
	}

	/**
	 * interpoliert maximal erreichbare Geschwindigkeit des Bootes zu einem
	 * bestimmten Winkel zum Wind
	 * 
	 * @param boat
	 * @param env
	 * @param framerate
	 * @return
	 */
	private double interpolateMaxVelocityFromPolar(BoatState boat, Enviroment env) {
		// here pick up values from polar-modell
		int absoluteBoatToWind = 180 - env.getWindState().getWindToBoat();
		int windAcceleration = (int) env.getWindState().getStrength();

		if (env.getWindState().getWindToBoat() < 0) {
			absoluteBoatToWind = 180 + env.getWindState().getWindToBoat();
		}

		System.out.println("**** interpolation beginns ****");
		System.out.println(" absoluteBoatToWind: " + absoluteBoatToWind + ", " + "windStrength: "
				+ windAcceleration);

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
			nextLowerMap = polarMap.get(new Integer(i));
			if (nextLowerMap != null) {
				// System.out.println("next lower windmap found");
				nextLowerAngle = i;
				break;
			}
		}

		// find higher map
		for (int i = absoluteBoatToWind; i <= 180; i++) {
			nextHigherMap = polarMap.get(new Integer(i));
			if (nextHigherMap != null) {
				// System.out.println("next higher windmap found");
				nextHigherAngle = i;
				break;
			}
		}

		// find velocities in maps
		// find wind=key velocity=values in lower map
		for (int i = windAcceleration; i <= 40; i++) {
			try {
				lowerAngleWindHighValue = nextLowerMap.get(new Integer(i));
				lowerAngleWindHighKey = i;
				if (lowerAngleWindHighValue != 0) {
					break;
				}
			} catch (NullPointerException e) {
				// System.err.println(e.getMessage());
			}
		}

		for (int i = windAcceleration; i >= 0; i--) {
			try {
				lowerAngleWindLowValue = nextLowerMap.get(new Integer(i));
				lowerAngleWindLowKey = i;
				if (lowerAngleWindLowValue != 0) {
					break;
				}
			} catch (NullPointerException e) {
				// System.err.println(e.getMessage());
			}
		}

		// find wind=key velocity=values in higher map
		for (int i = windAcceleration; i <= 40; i++) {
			try {
				higherAngleWindHighValue = nextHigherMap.get(new Integer(i));
				higherAngleWindHighKey = i;
				if (higherAngleWindHighValue != 0) {
					break;
				}
			} catch (NullPointerException e) {
				// System.err.println(e.getMessage());
			}
		}
		for (int i = windAcceleration; i >= 0; i--) {
			try {
				higherAngleWindLowValue = nextHigherMap.get(new Integer(i));
				higherAngleWindLowKey = i;
				if (higherAngleWindLowValue != 0) {
					break;
				}
			} catch (NullPointerException e) {
				// e.printStackTrace();
			}
		}

		double interpoltedHigherAngleWind = interpolateValueFromValuesBetweenDiffKey(
				windAcceleration, higherAngleWindHighValue, higherAngleWindLowValue,
				higherAngleWindHighKey, higherAngleWindLowKey);
		System.out.println(" intrapoltedHigherAngleWind: " + interpoltedHigherAngleWind
				+ " from Values: " + higherAngleWindHighValue + ", " + higherAngleWindLowValue
				+ " windstrenght: " + higherAngleWindHighKey + ", " + higherAngleWindLowKey);

		double interpolatedLowerAngleWind = interpolateValueFromValuesBetweenDiffKey(
				windAcceleration, lowerAngleWindHighValue, lowerAngleWindLowValue,
				lowerAngleWindHighKey, lowerAngleWindLowKey);
		System.out.println(" intrapoltedLowerAngleWind: " + interpolatedLowerAngleWind
				+ " from Values: " + lowerAngleWindHighValue + ", " + lowerAngleWindLowValue
				+ " windstrenght: " + lowerAngleWindHighKey + ", " + lowerAngleWindLowKey);

		double finalInterpolatedVelocity = interpolateValueFromValuesBetweenDiffKey(
				absoluteBoatToWind, interpoltedHigherAngleWind, interpolatedLowerAngleWind,
				nextHigherAngle, nextLowerAngle);
		System.out.println(" finalIntrapoltedVelocity: " + finalInterpolatedVelocity
				+ " from Values: " + interpoltedHigherAngleWind + ", " + interpolatedLowerAngleWind
				+ " angles: " + nextHigherAngle + ", " + nextLowerAngle);

		return finalInterpolatedVelocity;
	}

	/**
	 * interpolates linear between two key-value pairs
	 * 
	 * @param actualKey
	 * @param highValue
	 * @param lowValue
	 * @param highKey
	 * @param lowKey
	 * @return interpolatedValue
	 */
	private double interpolateValueFromValuesBetweenDiffKey(int actualKey, double highValue,
			double lowValue, double highKey, double lowKey) {

		if (highKey == lowKey) {
			return highValue;
		}

		double diffKeys = highKey - lowKey;
		double diffValue = highValue - lowValue;
		double diffActualToLowKey = actualKey - lowKey;
		double interpolatedValue = lowValue + (diffActualToLowKey * (diffValue / diffKeys));
		return interpolatedValue;
	}

	/**
	 * creates polardata
	 * 
	 * @deprecated only implemented for tests
	 * 
	 */
	private void createTestPolar() {

		HashMap<Integer, Double> hm00 = new HashMap<Integer, Double>();
		hm00.put(0, 0d);
		hm00.put(2, 0.05d);
		hm00.put(5, 0.07d);
		hm00.put(10, 0.1d);
		polarMap.put(0, hm00);

		HashMap<Integer, Double> hm30 = new HashMap<Integer, Double>();
		hm30.put(0, 0d);
		hm30.put(2, 0.25d);
		hm30.put(5, 0.5d);
		hm30.put(10, 1d);
		polarMap.put(30, hm30);

		HashMap<Integer, Double> hm60 = new HashMap<Integer, Double>();
		hm60.put(0, 0d);
		hm60.put(2, 0.625d);
		hm60.put(5, 1.25d);
		hm60.put(10, 2.5d);
		polarMap.put(60, hm60);

		HashMap<Integer, Double> hm90 = new HashMap<Integer, Double>();
		hm90.put(0, 0d);
		hm90.put(2, 0.5d);
		hm90.put(5, 1.0d);
		hm90.put(10, 2d);
		polarMap.put(90, hm90);

		HashMap<Integer, Double> hm120 = new HashMap<Integer, Double>();
		hm120.put(0, 0d);
		hm120.put(2, 0.85d);
		hm120.put(5, 1.25d);
		hm120.put(10, 2.5d);
		polarMap.put(120, hm120);

		HashMap<Integer, Double> hm150 = new HashMap<Integer, Double>();
		hm150.put(0, 0d);
		hm150.put(2, 0.9d);
		hm150.put(5, 1.7d);
		hm150.put(10, 3d);
		polarMap.put(150, hm150);

		HashMap<Integer, Double> hm180 = new HashMap<Integer, Double>();
		hm180.put(0, 0d);
		hm180.put(2, 0.5d);
		hm180.put(5, 1.0d);
		hm180.put(10, 2d);
		polarMap.put(180, hm180);
	}

	private void loadPolarsFromProperties(InputStream input) throws IOException {

		Properties prop = new Properties();
		input = new FileInputStream("polar.properties");
		prop.load(input);

		HashMap<Integer, Double> hm00 = new HashMap<Integer, Double>();
		hm00.put(0, Double.parseDouble(prop.getProperty("hm00_0")));
		hm00.put(2, Double.parseDouble(prop.getProperty("hm00_2")));
		hm00.put(5, Double.parseDouble(prop.getProperty("hm00_5")));
		hm00.put(10, Double.parseDouble(prop.getProperty("hm00_10")));
		polarMap.put(0, hm00);

		HashMap<Integer, Double> hm30 = new HashMap<Integer, Double>();
		hm30.put(0, Double.parseDouble(prop.getProperty("hm30_0")));
		hm30.put(2, Double.parseDouble(prop.getProperty("hm30_2")));
		hm30.put(5, Double.parseDouble(prop.getProperty("hm30_5")));
		hm30.put(10, Double.parseDouble(prop.getProperty("hm30_10")));
		polarMap.put(30, hm30);

		HashMap<Integer, Double> hm60 = new HashMap<Integer, Double>();
		hm60.put(0, Double.parseDouble(prop.getProperty("hm60_0")));
		hm60.put(2, Double.parseDouble(prop.getProperty("hm60_2")));
		hm60.put(5, Double.parseDouble(prop.getProperty("hm60_5")));
		hm60.put(10, Double.parseDouble(prop.getProperty("hm60_10")));
		polarMap.put(60, hm60);

		HashMap<Integer, Double> hm90 = new HashMap<Integer, Double>();
		hm90.put(0, Double.parseDouble(prop.getProperty("hm90_0")));
		hm90.put(2, Double.parseDouble(prop.getProperty("hm90_2")));
		hm90.put(5, Double.parseDouble(prop.getProperty("hm90_5")));
		hm90.put(10, Double.parseDouble(prop.getProperty("hm90_10")));
		polarMap.put(90, hm90);

		HashMap<Integer, Double> hm120 = new HashMap<Integer, Double>();
		hm120.put(0, Double.parseDouble(prop.getProperty("hm120_0")));
		hm120.put(2, Double.parseDouble(prop.getProperty("hm120_2")));
		hm120.put(5, Double.parseDouble(prop.getProperty("hm120_5")));
		hm120.put(10, Double.parseDouble(prop.getProperty("hm120_10")));
		polarMap.put(120, hm120);

		HashMap<Integer, Double> hm150 = new HashMap<Integer, Double>();
		hm150.put(0, Double.parseDouble(prop.getProperty("hm150_0")));
		hm150.put(2, Double.parseDouble(prop.getProperty("hm150_2")));
		hm150.put(5, Double.parseDouble(prop.getProperty("hm150_5")));
		hm150.put(10, Double.parseDouble(prop.getProperty("hm150_10")));
		polarMap.put(150, hm150);

		HashMap<Integer, Double> hm180 = new HashMap<Integer, Double>();
		hm180.put(0, Double.parseDouble(prop.getProperty("hm180_0")));
		hm180.put(2, Double.parseDouble(prop.getProperty("hm180_2")));
		hm180.put(5, Double.parseDouble(prop.getProperty("hm180_5")));
		hm180.put(10, Double.parseDouble(prop.getProperty("hm180_10")));
		polarMap.put(180, hm180);

	}

	/**
	 * @deprecated only implemented for tests
	 * @param boat
	 * @param env
	 * @param framerate
	 * @return propulsion velocity
	 */
	private double calcVelocityDummy(BoatState boat, Enviroment env, int framerate) {

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

		return velocity;
	}

}