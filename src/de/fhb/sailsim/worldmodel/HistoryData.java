package de.fhb.sailsim.worldmodel;

import org.newdawn.slick.geom.Vector2f;

public class HistoryData {

	private Vector2f point;
	private Vector2f direction;
	
	public HistoryData(Vector2f point, Vector2f direction) {
		super();
		this.point = point.copy();
		this.direction = direction.copy();
	}

	public Vector2f getPoint() {
		return point;
	}

	public Vector2f getDirection() {
		return direction;
	}
	
}
