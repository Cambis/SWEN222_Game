package game.view;

import game.control.packets.Packet01Disconnect;
import gameworld.TestPush;

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

	private Screen screen;

	public static final int WIDTH = 1080;
	public static final int HEIGHT = 720;

	public MainFrame() {

		setLayout(new BorderLayout());
		frame = new JFrame("Stealth");

		frame.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println(e.getKeyCode());

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});

		Screen screen = new Screen();
		add(screen, BorderLayout.CENTER);

		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.pack();
		repaint();
	}
}