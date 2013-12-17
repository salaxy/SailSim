package de.fhb.sailsim.userinterface.slick;

import org.newdawn.slick.geom.Vector2f;

public class VectorHelper {

	/**
	 * a - b = c
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static Vector2f sub(Vector2f a, Vector2f b) {
		Vector2f c = a.copy();
		c.sub(b);
		return c;
	}

	public static Vector2f add(Vector2f a, Vector2f b) {
		Vector2f c = a.copy();
		c.add(b);
		return c;
	}

	public static Vector2f mult(Vector2f a, float b) {
		Vector2f c = a.copy();
		c.scale(b);
		return c;
	}

	public static float distance(Vector2f a, Vector2f b) {
		Vector2f c = a.copy();
		float distance = c.distance(b);
		return distance;
	}

	public static float angleBetween(Vector2f a, Vector2f b) {
		double angleA = a.getTheta();
		double angelB = b.getTheta();
		double difference = angleA - angelB;

		return (float) difference;
	}
}
