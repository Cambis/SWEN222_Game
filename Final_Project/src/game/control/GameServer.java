package game.control;

import gameworld.TestPush;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Represents a server in a multiplayer game.
 *
 * @author Bieleski, Bryers, Gill & Thompson MMXV
 *
 *
 */
public class GameServer extends Thread {

	private DatagramSocket socket;

	// TODO this class needs to be made
	// private Game game

	// Testing only
	private TestPush test;

	// TODO this constructor needs to take in a Game paramter
	public GameServer() {

		// this.game = game;

		// Setup socket
		try {
			/* might have to change port 1331 */
			this.socket = new DatagramSocket(1331);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Used for testing only
	 * @param push
	 * @return
	 */
	public static GameServer testServer(TestPush push) {
		GameServer testServer = new GameServer();
		testServer.test = push;
		return testServer;
	}

	/**
	 * Run the server
	 */
	public void run() {

		while (true) {

			// Packet and data to be send to server
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);

			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}

			String message = new String(packet.getData());
			System.out.println("CLIENT: " + message);

			// Message has some empty spaces on the
			// end of it so we have to trim it
			if (message.trim().equals("Callum smells bad")) {
				sendData("Fuck, I know aye".getBytes(), packet.getAddress(),
						packet.getPort());
			}
		}
	}

	/**
	 * Send data to a client
	 * @param data
	 * @param ipAddress
	 * @param port
	 */
	public void sendData(byte[] data, InetAddress ipAddress, int port) {

		DatagramPacket packet = new DatagramPacket(data, data.length,
				ipAddress, port);

		// Send packet
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
