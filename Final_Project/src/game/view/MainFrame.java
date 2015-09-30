package game.view;

import game.control.packets.Packet01Disconnect;
import game.model.StealthGame;
import gameworld.TestPush;
import renderer.*;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class MainFrame extends JFrame {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JFrame frame;

	private Renderer renderer;
	private StealthGame game;

	private Screen screen;

	public static final int WIDTH = 1080;
	public static final int HEIGHT = 720;

	public MainFrame(Renderer r) {

		this.renderer = r;

		setLayout(new BorderLayout());
		frame = new JFrame("Stealth");

		Screen screen = new Screen();
		add(screen, BorderLayout.CENTER);

		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.pack();
		repaint();
	}

	/**
	 * Draws buffered Image
	 * @param g
	 */
	public void paintComponent(Graphics g) {
		screen.setImage(renderer.render());
	}
}