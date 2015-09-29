package gameworld;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Scanner;

import game.control.GameClient;
import game.control.GameServer;
import game.control.Player;
import game.control.PlayerMP;
import game.control.packets.Packet00Login;
import game.control.packets.Packet01Disconnect;

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

	public TestPush(boolean isHost, String user, String pass) {
		init(isHost, user, pass);
	}

	private void init(boolean isHost, String user, String pass) {

		if (isHost) {
			// server = GameServer.testServer(this);
			server.start();
		}

		// client = GameClient.testClient("localhost", this);
		client.start();

		if (isHost)
			player = new PlayerMP(user, null, -1);
		else
			player = new PlayerMP(user, null, -1);

		Packet00Login login = new Packet00Login(player.getUsername());
		if (server != null)
			server.addConnection((PlayerMP) player, login);
		login.writeData(client);
	}



}
