package gameworld;

import game.control.GameClient;
import game.control.GameServer;
import game.control.Player;
import game.control.PlayerMP;
import game.control.packets.Packet00Login;

/**
 * Just used for testing the client/ server
 *
 * @author Bieleski, Bryers, Gill & Thompson MMXV.
 *
 */
public class TestPush {

	private GameServer server;
	private GameClient client;
	public Player player;

	public TestPush() {

		this.server = GameServer.testServer(this);
		server.start();
		this.client = GameClient.testClient("localhost", this);
		client.start();

		// client.sendData("Callum smells bad".getBytes());

		player = new PlayerMP("Test", null, -1);
		Packet00Login login = new Packet00Login(player.getUsername());
		server.addConnection((PlayerMP) player, login);
		login.writeData(client);
	}

	public static void main(String args[]) {
		new TestPush();
	}
}
