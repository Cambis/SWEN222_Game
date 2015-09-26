package game.control;

import game.control.packets.Packet;
import game.control.packets.Packet00Login;
import game.control.packets.Packet.PacketType;
import game.control.packets.Packet01Disconnect;
import game.control.packets.Packet02Move;
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
	public GameClient(String ipAddress /** , Game game */
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
			// Packet and data to be send to server
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);

			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.parsePacket(packet.getData(), packet.getAddress(),
					packet.getPort());

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

		switch (type) {
		case INVALID:
			break;

		case LOGIN:
			packet = new Packet00Login(data);
			handleLogin((Packet00Login) packet, address, port);
			break;

		case DISCONNECT:
			packet = new Packet01Disconnect(data);
			System.out.println("[" + address.getHostAddress() + ":" + port
					+ "] " + ((Packet01Disconnect) packet).getUsername()
					+ " has left the world...");
			// TODO remove player from game world
			break;

		case MOVE:
			packet = new Packet02Move(data);
			handleMove((Packet02Move) packet);
			break;

		default:
			break;

		}
	}

	/**
	 * Add a new player to the game
	 *
	 * @param packet
	 * @param address
	 * @param port
	 */
	private void handleLogin(Packet00Login packet, InetAddress address, int port) {

		System.out.println("[ " + address.getHostAddress() + " " + port + " ] "
				+ ((Packet00Login) packet).getUsername()
				+ " has joined the game...");

		PlayerMP player = new PlayerMP(packet.getUsername(), address, port);

		// TODO Add player to game
	}

	/**
	 * Handles a move from the server
	 * @param packet
	 */
	private void handleMove(Packet02Move packet) {
		// TODO Auto-generated method stub
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
