package game.view;

import game.logic.StealthGame;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Initial login window for host and clients.
 *
 * @author Cameron Bryers 300326848 MMXV
 * @author Callum Gill 300316407 MMXV
 *
 */
public class StartUpScreen extends JPanel {

	private static final long serialVersionUID = 8620016640945484361L;

	private final int WIDTH = 680;
	private final int HEIGHT = 420;
	private final int BTN_WIDTH = 250;
	private final int BTN_HEIGHT = 80;

	private GuiButton joinBtn;
	private GuiButton hostBtn;
	private GuiButton[] buttons = new GuiButton[2];

	private JFrame frame;

	/**
	 * Default constructor, creates new instance of this class.
	 */
	public StartUpScreen() {

		setLayout(new BorderLayout());
		frame = new JFrame();
		frame.setResizable(false);

		// setup btn
		joinBtn = new GuiButton("Join", BTN_WIDTH, BTN_HEIGHT);
		hostBtn = new GuiButton("Host", BTN_WIDTH, BTN_HEIGHT);
		buttons[0] = joinBtn;
		buttons[1] = hostBtn;

		frame.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
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

			}

		});

		// add mouse listener
		frame.addMouseListener(new MouseListener());
		frame.addMouseMotionListener(new MouseMotionListener());

		// add buttons
		joinBtn.setPos((WIDTH / 2) - (BTN_WIDTH / 2), HEIGHT - BTN_HEIGHT - 60);
		hostBtn.setPos((WIDTH / 2) - (BTN_WIDTH / 2), HEIGHT - BTN_HEIGHT * 2
				- 80);

		frame.setSize(WIDTH, HEIGHT);
		frame.add(this);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.repaint();
	}

	public void paintComponent(Graphics g) {
		java.net.URL imageURL = getClass().getResource("StealthTitle.png");

		BufferedImage image = null;

		try {
			image = ImageIO.read(imageURL);
		} catch (IOException e) {
			e.printStackTrace();
		}

		g.drawImage(image, 0, 0, null);
		joinBtn.draw(g);
		hostBtn.draw(g);
	}

	/**
	 * Shows the login window for the client
	 *
	 * @return array containing the username and the IP address
	 */
	private String[] showLoginWindowClient() {
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

	/**
	 * Shows the login window for the host.
	 *
	 * @return array containing the username and the amount of players
	 */
	private String[] showHostWindow() {
		JTextField username = new JTextField();
		JTextField players = new JTextField();
		Object[] message = { "Username:", username, "Amount of players:",
				players };

		int option = JOptionPane.showConfirmDialog(null, message, "Login",
				JOptionPane.OK_CANCEL_OPTION);

		if (option == JOptionPane.OK_OPTION) {

			if (players.getText() == null) {
				return new String[] { username.getText(), players.getText() };
			}

			// Need to check if the number of players is an integer
			try {
				int numOfPlayers = Integer.parseInt(players.getText());
			} catch (NumberFormatException e) {
				System.out.println("*** Invalid number: '" + players.getText()
						+ "'. Please enter an integer ***");
				return null;
			}

			return new String[] { username.getText(), players.getText() };
		} else {
			System.out.println("Login canceled");
		}
		return null;
	}

	/**
	 * Called when the player is not hosting.
	 */
	private void join() {
		String[] login = showLoginWindowClient();
		if (login == null) {
			return;
		}
		// TestPush client = new TestPush(false, login[0], login[1]);
		// StealthGame game = new StealthGame(login[0], login[1]);
		StealthGame game = StealthGame.client(login[0], login[1]);
		game.start();
		// new MainFrame();
		frame.dispose();
	}

	/**
	 * Called when the player is hosting.
	 */
	private void host() {
		String[] login = showHostWindow();
		if (login == null) {
			return;
		}

		if (login[1] == null) {
			login[1] = "2";
		}
		// StealthGame game = new StealthGame(true, login[0], "localhost");
		// StealthGame game = new StealthGame(login[0], "localhost",
		// Integer.parseInt(login[1]));
		StealthGame game = StealthGame.host(login[0],
				Integer.parseInt(login[1]));
		game.start();
		// TestPush host = new TestPush(true, login[0], login[1]);
		frame.dispose();
	}

	/**
	 * Runs the game
	 *
	 * @param args
	 */
	public static void main(String args[]) {
		new StartUpScreen();
	}

	/**
	 * Listens for actions on the buttons.
	 *
	 * @author Callum Gill 300316407 MMXV
	 *
	 */
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

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}
	}

	/**
	 * Listens for movement on the start up screen.
	 *
	 * @author Callum Gill 300316407 MMXV
	 *
	 */
	private class MouseMotionListener implements
			java.awt.event.MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {

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
