package de.fhb.sailsim.userinterface;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Ellipse;

/**
*
* @author Andy Klay <klay@fh-brandenburg.de>
* 
*/
public class BoatSymbol extends Group{
	
	double positionX=0;
	double positionY=0;	
	
	final int BOAT_LENGTH=20*10;
	final int BOAT_WIDE=5*10;
	
	private MotionPane parent;
	
	//Darstellung zurückgelegter Weg
	private ArrayList<Lines> wayLines= new ArrayList<Lines>();

	//Segelmast position von 90 bis - 90 grad
	private double poleAmplitude;
	private Ellipse body;
	
	//rotqation angle positive left, negative right 
	private Arc rotationAngleLine;
	private int actualRotationAngle=0;
	
	private void draw(){
		body = new Ellipse(this.positionX,this.positionY,BOAT_LENGTH, BOAT_WIDE);	
		this.getChildren().add(body);
		
		double startAngle= 0;
		rotationAngleLine = new Arc(positionX,positionY,200,100,startAngle,-90);
		rotationAngleLine.setStroke(Color.DODGERBLUE);
		rotationAngleLine.setStrokeWidth(5d);
		rotationAngleLine.setFill(null);
		this.getChildren().add(rotationAngleLine);
	}

	public BoatSymbol(MotionPane parent, double positionX, double positionY) {
		super();
		this.parent= parent;
		this.positionX = positionX;
		this.positionY = positionY;
		draw();
	}

	public double getPositionX() {
		return positionX;
	}

	public void setPositionX(double positionX) {
		this.positionX = positionX;
		body.setLayoutX(positionX);
		rotationAngleLine.setLayoutX(positionX);
		//...
	}

	public double getPositionY() {
		return positionY;
	}

	public void setPositionY(double positionY) {
		this.positionY = positionY;
		body.setLayoutY(positionY);
		rotationAngleLine.setLayoutY(positionY);
	}

	public ArrayList<Lines> getWayLines() {
		return wayLines;
	}
	
}
