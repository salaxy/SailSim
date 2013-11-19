package de.fhb.sailsim.userinterface;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MotionPane extends AnchorPane {

	private double myHeight = 300;
	private double myWidth = 300;

	private final Rectangle rect;
	private int rectsize=15;
	
	/**
	 * Konstruktor mit default HTML-Content
	 */
	public MotionPane() {
		this.setStyle("-fx-background-color: blue;");
		
		rect = new Rectangle(25,25,50, 50);
        rect.setArcHeight(rectsize);
        rect.setArcWidth(15);
        rect.setFill(Color.CRIMSON);
        rect.setTranslateX(50);
        rect.setTranslateY(75);
        this.getChildren().add(rect); 
        
        this.setOnKeyPressed(new EventHandler<KeyEvent>() {

            public void handle(KeyEvent key) {

                if(key.getCharacter().equals("w")){
                	rectsize++;
                	rect.setArcHeight(rectsize);
                	System.out.println("w pressded");
                }

            }

        });
	}

	/**
	 * Konstruktor mit default HTML-Content
	 * und Größen-Angabe
	 * @param height
	 * @param width
	 */
	public MotionPane(double height, double width) {
		this();
		myHeight = height;
		myWidth = width;
		
		
	}

 public void keyInput(){
	 
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

	public void changeBlock() {
    	rectsize++;
    	rect.setHeight(rectsize);
	}

	
}