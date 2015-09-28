package gui;

import game.control.packets.Packet01Disconnect;
import gameworld.TestPush;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

public class StartUpScreen extends JPanel{

	private JButton start;
	private JFrame frame;

	public StartUpScreen() {

		setLayout(new BorderLayout());
		frame = new JFrame();
		start = new JButton("Start");

		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				//new MainFrame();
			}

		});

		frame.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println(e.getKeyCode());
				String[] login;
				switch(e.getKeyCode()){
				case 72: //h
					//TODO Host game
					login = showLoginWindow();
					TestPush host = new TestPush(true, login[0], login[1]);
					break;
				case 74: //j
					//TODO Join game
					login = showLoginWindow();
					TestPush client = new TestPush(false, login[0], login[1]);
					break;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

		});

		// add(start, BorderLayout.CENTER);

		frame.setSize(680, 420);
		frame.add(this);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.pack();
		frame.repaint();
	}

	public void paintComponent(Graphics g) {
		java.net.URL imageURL = getClass().getResource(
				"StealthTitle.png");

		BufferedImage image = null;

		try {
			image = ImageIO.read(imageURL);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		g.drawImage(image, 0, 0, null);
	}

	private String[] showLoginWindow(){
		JTextField username = new JTextField();
		JTextField password = new JPasswordField();
		Object[] message = {
		    "Username:", username,
		    "Password:", password
		};

		int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION) {
			return new String[] {username.getText(), password.getText()};
		} else {
		    System.out.println("Login canceled");
		}
		return null;
	}

	public static void main(String args[]) {
		new StartUpScreen();
	}
}
