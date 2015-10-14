package game.test;

import static org.junit.Assert.*;
import game.control.GameClient;
import game.control.GameServer;
import game.control.PlayerMP;
import game.control.packets.Packet00Login;
import game.control.packets.Packet01Disconnect;
import game.control.packets.Packet02Move;
import game.logic.Player;
import game.logic.StealthGame;

import org.junit.Ignore;
import org.junit.Test;

/**
 * JUnit testing for the client/ server. Please note, you might have to test each
 * of these tests individually, otherwise you might throw a
 * java.net.BindException or a NullPointerException.
 *
 * @author Cameron Bryers 300326848 MMXV
 *
 */
public class NetworkTester {

	@Test
	public void testLogin() {

		Thread thread = new Thread("testLogin");
		thread.start();
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

		try {
			thread.join();
			host.stop();
			client.stop();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testLoginMultiple() {

		Thread thread = new Thread("testLogin");
		thread.start();
		StealthGame host = StealthGame.host("Host", 4);
		host.start();

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

		try {
			thread.join();
			host.stop();
			client1.stop();
			client2.stop();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testLoginMax() {

		Thread thread = new Thread("testLogin");
		thread.start();
		StealthGame host = StealthGame.host("Host", 4);
		host.start();

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

		assertTrue(socketServer.getPlayerMP("Host") != null);
		assertTrue(socketServer.getPlayerMP("Client1") != null);
		assertTrue(socketServer.getPlayerMP("Client2") != null);
		assertTrue(socketServer.getPlayerMP("Client3") != null);
		assertTrue(socketServer.getPlayerMP("Client4") != null);
		assertTrue(socketServer.getPlayerMP("Client5") == null);

		try {
			thread.join();
			host.stop();
			client1.stop();
			client2.stop();
			client3.stop();
			client4.stop();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDisconnect() {

		Thread thread = new Thread("testDisconnect");
		thread.start();
		StealthGame host = StealthGame.host("Host", 4);
		host.start();


		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		StealthGame client = StealthGame.client("Client", "localhost");
		client.start();

		Packet01Disconnect packet = new Packet01Disconnect(
		client.getPlayer().getUsername());

		GameClient socketClient = client.getClient();
		packet.writeData(socketClient);

		assertTrue(host.getServer().getPlayerMP("Host") != null);
		assertTrue(host.getServer().getPlayerMP("Client") == null);

		try {
			thread.join();
			host.stop();
			client.stop();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testMove() {

		Thread thread = new Thread("testMove");
		thread.start();
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

		try {
			thread.join();
			host.stop();
			client.stop();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDamage() {

		Thread thread = new Thread("testMove");
		thread.start();
		StealthGame host = StealthGame.host("Host", 4);
		host.start();

		StealthGame client = StealthGame.client("Client", "localhost");
		client.start();

		GameServer socketServer = host.getServer();
		GameClient socketClient = client.getClient();
	}
}
