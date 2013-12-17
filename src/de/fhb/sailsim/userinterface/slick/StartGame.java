package de.fhb.sailsim.userinterface.slick;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class StartGame extends StateBasedGame {

	/**
	 *  Konstruktor
	 */
	public StartGame() {
		super("SailSim v.0.0.1");
	}

	/**
	 * Hier werden die GameStates bekannt gemacht.
	 */
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		addState(new DefenderViewSlick());
	}

	/**
	 * Main-Methode
	 * 
	 * @param args
	 * @throws SlickException 
	 */
	public static void main(String[] args) throws SlickException {
        AppGameContainer defender = new AppGameContainer(new StartGame());
        defender.setDisplayMode(DefenderViewSlick.WIDTH, DefenderViewSlick.HEIGHT, false);
        defender.setVSync(true);
        defender.setShowFPS(false);
        defender.setAlwaysRender(true);
        //TODO schickes Icon
//        defender.setIcon("data/logos/DefenderTouchDeluxe32.png");
        defender.start();
	}
}
