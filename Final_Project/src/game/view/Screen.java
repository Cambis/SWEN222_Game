package game.view;

import game.logic.StealthGame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Used to display the scene from the renderer.
 *
 * @author Cameron Bryers 300326848 MMXV
 *
 */
public class Screen extends JPanel {

	private static final boolean DEBUG = StealthGame.DEBUG;
	private static final long serialVersionUID = 5869047725511908879L;
	private BufferedImage image = new BufferedImage(1080, 720,
			BufferedImage.TYPE_INT_RGB);

	public Screen() {
		setName("Stealth");
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}

	/**
	 * Set the image to display.
	 *
	 * @param image
	 *            - current render of the scene
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
		repaint();
	}
}
