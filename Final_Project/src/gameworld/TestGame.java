package gameworld;

import java.awt.event.WindowEvent;

import game.control.GameClient;
import game.control.GameServer;
import game.control.PlayerMP;
import game.control.packets.Packet00Login;
import game.control.packets.Packet01Disconnect;
import game.model.Player;
import gui.MainFrame;
import gui.WindowHandler;

public class TestGame implements Runnable {

	public static final boolean DEBUG = true;

	private GameClient client;
	private GameServer server;
	private boolean isHost = false;
	private boolean running = false;

	// Ticks during the game
	private Thread thread;

	// Primary game display
	private MainFrame mainFrame;

	private WindowHandler windowHandler;

	/**
	 * We only need one player here, the full list of players is stored in
	 * GameServer
	 **/
	private Player player;

	public TestGame(boolean isHost, String username) {
		this.isHost = isHost;
		player = new PlayerMP(username, 0, 0, 0, null, -1);
		init();
	}

	private void init() {
		mainFrame = new MainFrame();

		windowHandler = new WindowHandler(mainFrame) {
			@Override
			public void windowClosing(WindowEvent e) {
				Packet01Disconnect packet = new Packet01Disconnect(
						player.getUsername());
				packet.writeData(client);
				stop();
			}
		};
	}

	/**
	 * Sets up the server/ client
	 */
	public synchronized void start() {

		running = true;

		thread = new Thread(this, "Test");
		thread.start();

		// Server is created if user is a host
		if (isHost) {
			server = new GameServer(this);
			server.start();
		}

		// Client should always be created
		client = new GameClient("localhost", this);
		client.start();

		// Login to the server
		Packet00Login login = new Packet00Login(player.getUsername());
		if (server != null)
			server.addConnection((PlayerMP) player, login);
		login.writeData(client);
	}

	/**
	 * Called to stop the game
	 */
	public synchronized void stop() {

		running = false;

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (DEBUG)
			System.out.println("THREAD IS "
					+ (thread.isAlive() ? "ALIVE" : "DEAD"));
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public void tick() {

	}
}
