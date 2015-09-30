package game.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Screen screen;
	private BufferedImage image;

	public static final int WIDTH = 1080;
	public static final int HEIGHT = 720;

	public MainFrame() {

		setLayout(new BorderLayout());
		setName("Stealth");
		screen = new Screen();
		getContentPane().add(screen, BorderLayout.CENTER);

		setSize(WIDTH, HEIGHT);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		repaint();
	}

	public void setImage(BufferedImage image) {
		screen.setImage(image);
	}
}