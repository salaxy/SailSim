package de.fhb.sailsim.userinterface;

import javafx.scene.layout.AnchorPane;

/**
*
* @author Andy Klay <klay@fh-brandenburg.de>
* 
*/
public class MotionPane extends AnchorPane {

	private double myHeight = 300;
	private double myWidth = 300;
	
	/**
	 * Konstruktor mit default HTML-Content
	 */
	public MotionPane() {
		this.setStyle("-fx-background-color: #00FFFF;");
//		motionSpace.setStyle("-fx-background-color: linear-gradient(#112200, #be1d00);");
		
	}

	/**
	 * construktor
	 * @param height
	 * @param width
	 */
	public MotionPane(double height, double width) {
		this();
		myHeight = height;
		myWidth = width;	
	}


	@Override
	protected void layoutChildren() {
//		layoutInArea(newsContentPane, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER,
//				VPos.CENTER);
	}

	@Override
	protected double computePrefWidth(double height) {
		return myWidth;
	}

	@Override
	protected double computePrefHeight(double width) {
		return myHeight;
	}

	/**
	 * setzen der Größe des Elements
	 * @param height
	 * @param width
	 */
	public void setSize(double height, double width) {
		myHeight = height;
		myWidth = width;
	}

}