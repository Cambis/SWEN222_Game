package game.test;

import static org.junit.Assert.*;
import game.control.GameClient;
import game.control.GameServer;
import game.control.PlayerMP;
import game.control.packets.Packet00Login;
import game.control.packets.Packet01Disconnect;
import game.logic.Player;

import org.junit.Ignore;
import org.junit.Test;

/**
 * JUnit testing for the client/ server. Please note, you will have to test each
 * of these tests individually, otherwise you might throw
 * java.net.BindException.
 *
 * @author Cameron Bryers 300326848 MMXV
 *
 */
public class NetworkTester {

	private GameServer server;

	public NetworkTester() {

		// Set up the server/ client
		this.server = new GameServer(null, 1);

	}

	@Test
	public void testLoginServer() {

		GameClient client = new GameClient("localhost", null);

		// Create a player
		Player player = new PlayerMP("David", 0, 0, 0, null, -1);

		// Login to the server
		Packet00Login login = new Packet00Login(player.getUsername(), -1, 0, 0,
				0);
		if (server != null)
			server.addConnection((PlayerMP) player, login);
		login.writeData(client);

		assertTrue(server.getPlayerMP("David") != null);

	}

	@Test
	public void testDisconnect() {

		GameClient client = new GameClient("localhost", null);

		// Create a player
		Player player = new PlayerMP("Jim", 0, 0, 0, null, -1);

		// Login to the server
		Packet00Login login = new Packet00Login(player.getUsername(), -1, 0, 0,
				0);
		if (server != null)
			server.addConnection((PlayerMP) player, login);
		login.writeData(client);

		// Disconnect from the server
		Packet01Disconnect packet = new Packet01Disconnect(player.getUsername());
		server.removeConnection(packet);

		assertTrue(server.getPlayerMP("Jim") == null);
	}
}
