package game.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class MainFrame extends JFrame {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Screen screen;
	private BufferedImage image;
	private JTextArea textArea;

	public static final int WIDTH = 1024;
	public static final int HEIGHT = 768;

	public MainFrame() {

		setLayout(new BorderLayout());
		screen = new Screen();
		setResizable(false);


		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFocusable(false);
		textArea.setBackground(new Color(40, 40, 50));
		textArea.setForeground(new Color(195, 245, 165));
		textArea.setRows(5);

		getContentPane().add(screen, BorderLayout.CENTER);
		getContentPane().add(textArea, BorderLayout.SOUTH);

		setSize(WIDTH, HEIGHT);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		repaint();
	}

	public Dimension2D getImageSize() {
		return screen.getSize();
	}

	public void setImage(BufferedImage image) {
		screen.setImage(image);
	}

	public void println(String s) {
		String[] text = textArea.getText().split("\n");
		String newText = "";
		for (int i = 4; i >= 0; --i){
			if (text.length -1 - i < 0){
				newText += "\n";
			} else{
				newText += text[text.length - 1 - i] + "\n";
			}
		}
		newText += s;
		textArea.setText(newText);
	}
}