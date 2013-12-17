package de.fhb.sailsim.userinterface.slick;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class Gamemap {

	/**
	 * Sichtbare Karte
	 */
	private Image visibleMap;

	/**
	 * Karte mit Informationen darüber, wer wo bauen kann, wo Bodeneinheiten
	 * passieren können usw.
	 */
	private Image informationalMap;

	/**
	 * Konstruktor
	 * 
	 * Lädt die Karte.
	 */
	public Gamemap() {

		try {		

			// Neue Map:
			 visibleMap = new Image("./maps/background.png");
//			 informationalMap = new Image("./maps/inewmap.png");

		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Zeichnet die Karte für einen angegebenen Spieler.
	 * 
	 * @param graphics
	 * @param player
	 */
	public void zeichne(Graphics graphics, Player player) {

		GraphicTools.calcDrawTransformationForSlick(player, graphics, new Vector2f(0, 0));
		visibleMap.draw();

		// Feldumrandung zeichnen
		// graphics.setColor(new Color(255, 255, 0,55));
		// graphics.fillRect(0f, 0f, 1024, 768);

		graphics.resetTransform();
	}



}
