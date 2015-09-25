package gameworld;

import java.util.Scanner;

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

	public TestPush(boolean isHost) {
		init(isHost);
	}

	private void init(boolean isHost) {

		if (isHost) {
			server = GameServer.testServer(this);
			server.start();
		}
		client = GameClient.testClient("localhost", this);
		client.start();

		if (isHost)
			player = new PlayerMP("Host", null, -1);
		else
			player = new PlayerMP("Client", null, -1);

		Packet00Login login = new Packet00Login(player.getUsername());
		if (server != null)
			server.addConnection((PlayerMP) player, login);
		login.writeData(client);
	}
	// public TestPush() {
	//
	// this.client = GameClient.testClient("localhost", this);
	// client.start();
	//
	// // client.sendData("Callum smells bad".getBytes());
	//
	// }
	//
	// private void init() {
	// player = new PlayerMP("T" + Math.random(), null, -1);
	// Packet00Login login = new Packet00Login(player.getUsername());
	// if (server != null)
	// server.addConnection((PlayerMP) player, login);
	// login.writeData(client);
	// }
	//
	// public static TestPush Host() {
	// TestPush test = new TestPush();
	// test.server = GameServer.testServer(test);
	// test.server.start();
	// test.init();
	// return test;
	// }
	//
	// public static TestPush Client() {
	// TestPush test = new TestPush();
	// test.init();
	// return test;
	// }
	//
	// public static void main(String args[]) {
	// new TestPush();
	// }

}
