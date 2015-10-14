package game.test;

import static org.junit.Assert.*;
import game.control.GameClient;
import game.control.GameServer;
import game.control.PlayerMP;
import game.control.packets.Packet;
import game.control.packets.Packet00Login;
import game.control.packets.Packet01Disconnect;
import game.control.packets.Packet02Move;
import game.control.packets.Packet04Damage;
import game.control.packets.Packet05Heal;
import game.logic.Player;
import game.logic.StealthGame;

import org.junit.Ignore;
import org.junit.Test;

/**
 * JUnit testing for the client/ server. Please note, you might have to test
 * each of these tests individually, otherwise you might throw a
 * java.net.BindException or a NullPointerException.
 *
 * @author Cameron Bryers 300326848 MMXV
 *
 */
public class NetworkTester {

	/**
	 * Used to make the tests wait for the client and server to shut down.
	 */
	public final static long SLEEP = 0;

	/**
	 * Tests a single user login.
	 */
	@Test
	public void testLogin() {

		try {
			Thread.sleep(SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Set up host/ client
		StealthGame host = StealthGame.host("Host", 4);
		host.start();

		StealthGame client = StealthGame.client("Client", "localhost");
		client.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(host.getServer().getPlayerMP("Host") != null);
		assertTrue(host.getServer().getPlayerMP("Client") != null);

	}

	/**
	 * Tests two users logging in.
	 */
	@Test
	public void testLoginMultiple() {

		try {
			Thread.sleep(SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Set up host
		StealthGame host = StealthGame.host("Host", 4);
		host.start();

		// Set up clients
		StealthGame client1 = StealthGame.client("Client1", "localhost");
		client1.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		StealthGame client2 = StealthGame.client("Client2", "localhost");
		client2.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		GameServer socketServer = host.getServer();

		assertTrue(socketServer.getPlayerMP("Host") != null);
		assertTrue(socketServer.getPlayerMP("Client1") != null);
		assertTrue(socketServer.getPlayerMP("Client2") != null);

	}

	/**
	 * Tests too many clients trying to login.
	 */
	@Test
	public void testLoginMax() {

		try {
			Thread.sleep(SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Set up host
		StealthGame host = StealthGame.host("Host", 4);
		host.start();

		// Set up client
		StealthGame client1 = StealthGame.client("Client1", "localhost");
		client1.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		StealthGame client2 = StealthGame.client("Client2", "localhost");
		client2.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		StealthGame client3 = StealthGame.client("Client3", "localhost");
		client3.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		StealthGame client4 = StealthGame.client("Client4", "localhost");
		client4.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		StealthGame client5 = StealthGame.client("Client5", "localhost");
		client5.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		GameServer socketServer = host.getServer();

		// Try adding too many players
		assertTrue(socketServer.getPlayerMP("Host") != null);
		assertTrue(socketServer.getPlayerMP("Client1") != null);
		assertTrue(socketServer.getPlayerMP("Client2") != null);
		assertTrue(socketServer.getPlayerMP("Client3") != null);
		assertTrue(socketServer.getPlayerMP("Client5") == null);

	}

	/**
	 * Tests a client disconnecting from the server.
	 */
	@Test
	public void testDisconnect() {

		try {
			Thread.sleep(SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		StealthGame host = StealthGame.host("Host", 4);
		host.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		StealthGame client = StealthGame.client("Client", "localhost");
		client.start();

		Packet01Disconnect packet = new Packet01Disconnect(client.getPlayer()
				.getUsername());

		GameClient socketClient = client.getClient();
		packet.writeData(socketClient);

		assertTrue(host.getServer().getPlayerMP("Host") != null);
		assertTrue(host.getServer().getPlayerMP("Client") == null);

	}

	/**
	 * Tests a move packet over the server.
	 */
	@Test
	public void testMove() {

		try {
			Thread.sleep(SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		StealthGame host = StealthGame.host("Host", 4);
		host.start();

		StealthGame client = StealthGame.client("Client", "localhost");
		client.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		GameServer socketServer = host.getServer();
		GameClient socketClient = client.getClient();

		// Move the client (rotation of 2)
		Packet02Move packet = new Packet02Move("Client", 0, 1, 1, 0, true, 2);
		packet.writeData(socketClient);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Check that the rotation is now 2
		assertTrue(socketServer.getPlayerMP("Client").getRotation() == 2);
	}

	/**
	 * Tests a damage packet over the server.
	 */
	@Test
	public void testDamage() {

		try {
			Thread.sleep(SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		StealthGame host = StealthGame.host("Host", 4);
		host.start();

		StealthGame client = StealthGame.client("Client", "localhost");
		client.start();

		GameServer socketServer = host.getServer();
		GameClient socketClient = client.getClient();

		Packet04Damage packet = new Packet04Damage("Client", "Host", 1);
		packet.writeData(socketClient);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Assert health has been lost
		assertTrue(socketServer.getPlayerMP("Client").getHealth() == 99);
	}

	/**
	 * Tests a heal packet over the server.
	 */
	@Test
	public void testHeal() {

		try {
			Thread.sleep(SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		StealthGame host = StealthGame.host("Host", 4);
		host.start();

		StealthGame client = StealthGame.client("Client", "localhost");
		client.start();

		GameServer socketServer = host.getServer();
		GameClient socketClient = client.getClient();

		// Take damage
		Packet packet = new Packet04Damage("Client", "Host", 1);
		packet.writeData(socketClient);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Take health
		packet = new Packet05Heal("Client", 1);
		packet.writeData(socketClient);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Assert health has not changed
		assertTrue(socketServer.getPlayerMP("Client").getHealth() == 100);
	}
}
