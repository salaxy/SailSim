package de.fhb.sailsim.userinterface.slick;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class Map {

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
	public Map() {

		try {
			visibleMap = new Image("./maps/background.gif");
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
	public void paint(Graphics graphics, Perspective player) {

		GraphicTools.calcDrawTransformationForSlick(player, graphics,
				new Vector2f(0, 0));
		visibleMap.draw(0, 0);

		// Feldumrandung zeichnen
		// graphics.setColor(new Color(255, 255, 0,55));
		// graphics.fillRect(0f, 0f, 1024, 768);
		graphics.setBackground(new Color(10,10, 255,55));

		graphics.resetTransform();
	}

}
