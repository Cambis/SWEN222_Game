package game.control;

import game.control.packets.Packet;
import game.control.packets.Packet.PacketType;
import game.control.packets.Packet00Login;
import gameworld.TestPush;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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

	private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();

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
	 *
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

			parsePacket(packet.getData(), packet.getAddress(), packet.getPort());

			// String message = new String(packet.getData());
			// System.out.println("CLIENT: " + message);
			//
			// // Message has some empty spaces on the
			// // end of it so we have to trim it
			// if (message.trim().equals("Callum smells bad")) {
			// sendData("Fuck, I know aye".getBytes(), packet.getAddress(),
			// packet.getPort());
			// }
		}
	}

	/**
	 * Parse a packet type
	 * @param data
	 * @param address
	 * @param port
	 */
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketType type = Packet.lookupPacket(message.substring(0, 2));

		switch (type) {
		case DISCONNECT:
			break;
		case INVALID:
			break;
		case LOGIN:
			Packet00Login packet = new Packet00Login(data);
			System.out.println("[ " + address.getHostAddress() + " " + port
					+ " ]" + packet.getUsername() + " has connected...");
			PlayerMP player = new PlayerMP(address, port);
			connectedPlayers.add(player);
			break;
		default:
			break;

		}
	}

	/**
	 * Send data to a client
	 *
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

	/**
	 * Send data to all clients on the server
	 * @param data
	 */
	public void sendDataToAllClients(byte[] data) {
		for (PlayerMP p : connectedPlayers)
			sendData(data, p.getIpAddress(), p.getPort());
	}
}
