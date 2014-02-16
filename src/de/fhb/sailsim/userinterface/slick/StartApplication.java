package de.fhb.sailsim.userinterface.slick;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class StartApplication extends StateBasedGame {

	/**
	 *  Konstruktor
	 */
	public StartApplication() {
		super("SailSim v.0.3.0");
	}

	/**
	 * Hier werden die GameStates bekannt gemacht.
	 */
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		addState(new SlickView());
	}

	/**
	 * Main-Methode
	 * 
	 * @param args
	 * @throws SlickException 
	 */
	public static void main(String[] args) throws SlickException {
        AppGameContainer simulatorView = new AppGameContainer(new StartApplication());
        simulatorView.setDisplayMode(SlickView.WIDTH, SlickView.HEIGHT, false);
        simulatorView.setVSync(true);
        simulatorView.setShowFPS(false);
        simulatorView.setAlwaysRender(true);
        simulatorView.setIcon("graphics/sailsim_icon_32.gif");
        simulatorView.start();
	}
}
