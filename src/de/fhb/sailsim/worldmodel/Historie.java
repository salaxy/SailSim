package de.fhb.sailsim.worldmodel;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import de.fhb.sailsim.userinterface.slick.GraphicTools;
import de.fhb.sailsim.userinterface.slick.Perspective;

public class Historie {
	
	private ArrayList<Vector2f> positions = new ArrayList<Vector2f>();

	public ArrayList<Vector2f> getPositions() {
		return positions;
	}

	public void setPositions(ArrayList<Vector2f> positions) {
		this.positions = positions;
	}
	
	public void paint(Perspective perspective, Graphics graphics){
		
		graphics.setColor(Color.gray);
		GraphicTools.calcDrawTransformationForSlick(perspective, graphics, new Vector2f());

		for(Vector2f point : positions){
			graphics.drawOval(point.x, point.y, 1, 1);
		}
		
		graphics.resetTransform();

	}

}
