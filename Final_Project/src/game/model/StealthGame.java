package game.model;

import java.awt.event.WindowEvent;

import renderer.*;
import game.control.GameClient;
import game.control.GameServer;
import game.control.PlayerMP;
import game.control.packets.Packet00Login;
import game.control.packets.Packet01Disconnect;
import game.view.MainFrame;
import game.view.WindowHandler;

public class StealthGame implements Runnable {

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

	private Level level;

	// We only need a reference to the player on the client here
	private Player player;

	public StealthGame(boolean isHost, String username) {
		this.isHost = isHost;
		player = new PlayerMP(username, 0, 0, 0, null, -1);
		init();
	}

	private void init() {
		Renderer r = new Renderer(MainFrame.WIDTH, MainFrame.HEIGHT);
		mainFrame = new MainFrame(r);

		// Set up window handler
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

	public void addPlayer(Player p){
		level.addPlayer(p);
	}

	public void removePlayer(Player p) {
		level.removePlayer(p);
	}

	public void loadLevel(String filepath) {
		level.loadRooms(filepath);
	}
	/**
	 * Called to run the game, the server should send a packet to tell the game
	 * to start
	 */
	@Override
	public void run() {

	}

	public void tick() {

	}
}
