package game.view;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Screen extends JPanel {

	private static final long serialVersionUID = 5869047725511908879L;

	public void paintComponent(Graphics g) {

		BufferedImage image = new BufferedImage(1080, 720, BufferedImage.TYPE_INT_RGB);

		g.drawImage(image, 0, 0, null);
	}
}
