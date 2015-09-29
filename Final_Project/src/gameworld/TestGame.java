package gameworld;

import game.control.GameClient;
import game.control.GameServer;

public class TestGame implements Runnable {

	private GameClient client;
	private GameServer server;

	private boolean running = false;

	/**
	 * Sets up the server/ client
	 */
	public synchronized void start() {

		running = true;

		// TODO server should only be created IFF the player hosts a game
		server = new GameServer(this);
		server.start();

		// Client should always be created
		client = new GameClient(this, "localhost");
		client.start();
	}

	/**
	 * Called to stop the game
	 */
	public synchronized void stop() {
		running = false;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public void tick() {

	}

}
