package de.fhb.sailsim.userinterface.slick;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 *
 */
public class DefenderViewSlick extends BasicGameState{

	/**
	 * Bildschirmbreite
	 */
    public static final int WIDTH = 1024;

    /**
     * Bildschirmhöhe
     */
    public static final int HEIGHT = 768;
    
    /**
     * GameState ID (nicht verändern!)
     */
    public static final int ID = 0; 
    
    
	/**
	 * Control des Gesamtprogramms
	 */
	private DefenderControl control;
	
	
	private int mouseButton=0; 

    /**
     * initialisierung
     */
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
    	 
    //gamelogic initialisieren  
    	//TODO
  	  control = new DefenderControl();
  	  control.createTestUnits();
  	  control.createTestUnits();
  	  
  	  gc.setShowFPS(true);
  	  gc.setTargetFrameRate(25);
  	  
    }

    /**
     * Berechnung, die jeden Frame gemacht werden müssen.
     */
    public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
    	
	 // Abfangen der Eingabegeräte
//    	Input input = gc.getInput();
    }

    /**
     * Rendern eines Frames
     */
    public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
    	
    	g.setAntiAlias(true);
    	g.setBackground(Color.white);

    	//alles zeichnen
    	//TODO
    	this.control.drawAll(g);  
    	this.control.updateGame();
    }

//    /**
//     * What happens when mouse was clicked
//     */
//    public void mouseClicked(int button, int x, int y, int clickCount){
//
//    	//Klickvektor holen
//    	Vector2f clickVector=new Vector2f(x,y);
//    	Vector2f mapCoords = GraphicTools.calcInputVector(clickVector, control.getPlayerOne());
//    	
//    }
	
    
    /**
     * What happens when mouse is moving
     */
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
    	
	    	if (mouseButton == 0) {
	    		control.schiebeInterface(oldx, oldy,newx, newy);		
	    	}
	   
	    	if (mouseButton == 1) {		
	    		control.zoomInterface(oldx, oldy, newx, newy);
	    	}			
    }
    
    /**
     * What happens when mousewhell is scrolled
     */
    public void mouseWheelMoved(int change){
    	
	}

	
	public void mousePressed(int button, int x, int y){	
    	mouseButton=button;
	}
	
	@Override
	public int getID() {
		return ID;
	}


}
