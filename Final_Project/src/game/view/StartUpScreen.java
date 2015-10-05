package game.view;

import game.control.packets.Packet01Disconnect;
import game.logic.StealthGame;
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

public class StartUpScreen extends JPanel {

	public static boolean LOCAL = true;

	private final int WIDTH = 680;
	private final int HEIGHT = 420;
	private final int BTN_WIDTH = 250;
	private final int BTN_HEIGHT = 80;

	private GuiButton joinBtn;
	private GuiButton hostBtn;
	private GuiButton[] buttons = new GuiButton[2];

	private JFrame frame;

	public StartUpScreen() {

		setLayout(new BorderLayout());
		frame = new JFrame();

		// setup btn
		joinBtn = new GuiButton("Join", BTN_WIDTH, BTN_HEIGHT);
		hostBtn = new GuiButton("Host", BTN_WIDTH, BTN_HEIGHT);
		buttons[0] = joinBtn;
		buttons[1] = hostBtn;

		frame.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println(e.getKeyCode());
				switch (e.getKeyCode()) {
				case 72: // h
					host();
					break;
				case 74: // j
					join();
					break;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

		});
		// add mouse listener
		frame.addMouseListener(new MouseListener());
		frame.addMouseMotionListener(new MouseMotionListener());
		// add(start, BorderLayout.CENTER);

		// add buttons
		joinBtn.setPos((WIDTH / 2) - (BTN_WIDTH / 2), HEIGHT - BTN_HEIGHT - 60);
		hostBtn.setPos((WIDTH / 2) - (BTN_WIDTH / 2), HEIGHT - BTN_HEIGHT * 2
				- 80);

		frame.setSize(WIDTH, HEIGHT);
		frame.add(this);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.pack();
		frame.repaint();
	}

	public void paintComponent(Graphics g) {
		java.net.URL imageURL = getClass().getResource("StealthTitle.png");

		BufferedImage image = null;

		try {
			image = ImageIO.read(imageURL);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		g.drawImage(image, 0, 0, null);
		joinBtn.draw(g);
		hostBtn.draw(g);
	}

	private String[] showLoginWindow() {
		JTextField username = new JTextField();
		JTextField ip = new JTextField();
		Object[] message = { "Username:", username, "IP:", ip };

		int option = JOptionPane.showConfirmDialog(null, message, "Login",
				JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION) {
			return new String[] { username.getText(), ip.getText() };
		} else {
			System.out.println("Login canceled");
		}
		return null;
	}

	private void join() {
		String[] login = showLoginWindow();
		if(login==null){
			return;
		}
		// TestPush client = new TestPush(false, login[0], login[1]);
		StealthGame game = new StealthGame(false, login[0], login[1]);
		game.start();
		// new MainFrame();
		frame.dispose();
	}

	private void host() {
		String[] login = showLoginWindow();
		if(login==null){
			return;
		}
		login[1] = (login[1] == null) ? "localhost" : login[1];
		StealthGame game = new StealthGame(true, login[0], login[1]);
		game.start();
		// TestPush host = new TestPush(true, login[0], login[1]);
		frame.dispose();
	}

	public static void main(String args[]) {
		new StartUpScreen();
	}

	private class MouseListener implements java.awt.event.MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			for (int i = 0; i < buttons.length; i++) {
				if (buttons[i].isHovered()) {
					switch (buttons[i].getName()) {
					case "Join":
						join();
						break;
					case "Host":
						host();
						break;
					}
				}
			}

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	}

	private class MouseMotionListener implements
			java.awt.event.MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseMoved(MouseEvent e) {
			for (int i = 0; i < buttons.length; i++) {
				buttons[i].checkHovered(e.getX(), e.getY());
			}
			frame.repaint();
		}

	}
}
