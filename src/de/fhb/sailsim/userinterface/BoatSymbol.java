package de.fhb.sailsim.userinterface;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.shape.Ellipse;

/**
*
* @author Andy Klay <klay@fh-brandenburg.de>
* 
*/
public class BoatSymbol extends Group{
	
	double positionX=0;
	double positionY=0;
	
	//zurückgelegter Weg
	private ArrayList<Lines> wayLines= new ArrayList<Lines>();
	
	int actualRotationAngel=0;
	
	final int BOAT_LENGTH=10;
	final int BOAT_WIDE=5;
	
	private MotionPane parent;
	
	//Segelmast position von 90 bis - 90 grad
	private double poleAmplitude;
	private Ellipse body;
	
	
	public void draw(){
		

		
		
		
		
		
	}

	public BoatSymbol(MotionPane parent, double x, double y) {
		super();
		this.parent= parent;
		
		body = new Ellipse(x,y,BOAT_LENGTH, BOAT_WIDE);	
		parent.getChildren().add(body);
	}

	public double getPositionX() {
		return positionX;
	}

	public void setPositionX(double positionX) {
		this.positionX = positionX;
		body.setLayoutX(positionX);
		//...
	}

	public double getPositionY() {
		return positionY;

	}

	public void setPositionY(double positionY) {
		this.positionY = positionY;
		body.setLayoutY(positionY);
	}
}
