package game.control;

import game.control.packets.Packet;
import game.control.packets.Packet00Login;
import game.control.packets.Packet.PacketType;
import gameworld.TestPush;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Represents a client in a multiplayer game.
 *
 * @author Bieleski, Bryers, Gill & Thompson MMXV.
 *
 */
public class GameClient extends Thread {

	private InetAddress ipAddress;
	private DatagramSocket socket;

	// TODO this class needs to be made
	// private Game game

	// Testing only
	private TestPush test;

	// TODO this constructor needs to take in a Game paramter
	public GameClient(String ipAddress /** , Game game **/
	) {

		// this.game = game;

		// Setup socket
		try {
			this.socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}

		// Setup ipAddress
		try {
			this.ipAddress = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Just used for testing
	 *
	 * @param ipAddress
	 * @param test
	 */
	public static GameClient testClient(String ipAddress, TestPush test) {
		GameClient testClient = new GameClient(ipAddress);
		testClient.test = test;
		return testClient;
	}

	/**
	 * Run the client
	 */
	public void run() {

		while (true) {
			System.out.println("Hello");
			// Packet and data to be send to server
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);

			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			parsePacket(packet.getData(), packet.getAddress(), packet.getPort());

		}
	}

	/**
	 * Parse a packet type
	 *
	 * @param data
	 * @param address
	 * @param port
	 */
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketType type = Packet.lookupPacket(message.substring(0, 2));
		Packet packet = null;
		System.out.println("TYPE: " + type.toString());

		switch (type) {
		case DISCONNECT:
			break;
		case INVALID:
			break;
		case LOGIN:
			packet = new Packet00Login(data);
			System.out.println("[ " + address.getHostAddress() + " " + port
					+ " ] " + ((Packet00Login) packet).getUsername()
					+ " has joined the game...");

			PlayerMP player = new PlayerMP(
					((Packet00Login) packet).getUsername(), address, port);

			// TODO Add player to game

			break;
		default:
			break;

		}
	}

	/**
	 * Send data to the server
	 *
	 * @param data
	 */
	public void sendData(byte[] data) {

		/* port 1331 might have to be changed */
		DatagramPacket packet = new DatagramPacket(data, data.length,
				ipAddress, 1331);

		// Send packet
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
