package gameworld;

import javax.swing.JOptionPane;

import game.control.GameClient;
import game.control.GameServer;

/**
 * Just used for testing the client/ server
 *
 * @author Bieleski, Bryers, Gill & Thompson MMXV.
 *
 */
public class TestPush {

	private GameServer server;
	private GameClient client;

	public TestPush() {

		this.server = GameServer.testServer(this);
		server.start();
		this.client = GameClient.testClient("localhost", this);
		client.start();

		client.sendData("Callum smells bad".getBytes());
	}

	public static void main(String args[]) {
		new TestPush();
	}
}
