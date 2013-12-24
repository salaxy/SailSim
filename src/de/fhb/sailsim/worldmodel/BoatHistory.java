package de.fhb.sailsim.worldmodel;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import de.fhb.sailsim.boat.BoatState;
import de.fhb.sailsim.userinterface.slick.GraphicTools;
import de.fhb.sailsim.userinterface.slick.Perspective;
import de.fhb.sailsim.userinterface.slick.VectorHelper;

public class BoatHistory {
	
	private final float LINE_LENGTH = 10;
	private final float LINE_WIDTH = 2;
	private final Color LINE_COLOR = Color.black;
	private final long TIME_STEP_MILLIS = 1000;

	private long lastTimeStap=System.currentTimeMillis();

	private ArrayList<HistoryData> dataSet = new ArrayList<HistoryData>();

	public ArrayList<HistoryData> getPositions() {
		return dataSet;
	}

	public void paint(Perspective perspective, Graphics graphics) {

		graphics.setColor(LINE_COLOR);
		graphics.setLineWidth(LINE_WIDTH);
		GraphicTools.calcDrawTransformationForSlick(perspective, graphics, new Vector2f());

		for (HistoryData data : dataSet) {

			Vector2f point= data.getPoint();
			Vector2f direction = data.getDirection();			

			Vector2f newPoint= data.getPoint().copy().add(VectorHelper.mult(direction, this.LINE_LENGTH)); 
			graphics.drawLine(point.x, point.y, newPoint.x, newPoint.y);
		}

		graphics.resetTransform();
	}

	public void addPosition(BoatState boat) {
		
		if(boat.isMoving()&&(System.currentTimeMillis()-this.lastTimeStap)>=this.TIME_STEP_MILLIS){
			lastTimeStap=System.currentTimeMillis();
			
			this.getPositions().add(new HistoryData(boat.getPosition(), boat.getDirection()));
		}

	}

}
