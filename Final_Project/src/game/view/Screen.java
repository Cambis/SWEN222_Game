package game.view;

import game.logic.StealthGame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Screen extends JPanel {

	private static final boolean DEBUG = StealthGame.DEBUG;
	private static final long serialVersionUID = 5869047725511908879L;
	private BufferedImage image = new BufferedImage(1080, 720, BufferedImage.TYPE_INT_RGB);

	public Screen() {
		setName("Stealth");
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}

	public void setImage(BufferedImage image) {
		// if (DEBUG) System.out.println("Setting image " + image.toString());
		this.image = image;
		repaint();
	}
}
