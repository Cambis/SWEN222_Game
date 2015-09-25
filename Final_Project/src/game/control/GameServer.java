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
	public GameServer(/** Game game **/) {

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
	 *
	 * @param data
	 * @param address
	 * @param port
	 */
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketType type = Packet.lookupPacket(message.substring(0, 2));
		Packet packet = null;
		// System.out.println("TYPE: " + type.toString());

		switch (type) {
		case DISCONNECT:
			break;
		case INVALID:
			break;
		case LOGIN:
			packet = new Packet00Login(data);
			String check = ("[ " + address.getHostAddress() + " " + port
					+ " ] " + ((Packet00Login) packet).getUsername()
					+ " has connected...");
			System.out.println(check);
			PlayerMP player = new PlayerMP(
					((Packet00Login) packet).getUsername(), address, port);
			addConnection(player, (Packet00Login) packet);
			break;
		default:
			break;

		}
	}

	public void addConnection(PlayerMP player, Packet00Login packet) {

		// Check that the connection doesn't already exist
		boolean alreadyConnected = false;

		for (PlayerMP p : connectedPlayers)
			System.out.println(p.getUsername());

		for (PlayerMP p : connectedPlayers) {
			if (p.getUsername().equalsIgnoreCase(player.getUsername())) {
				System.out.println("User already in " + p.getUsername());
				if (p.getIpAddress() == null)
					p.setIpAddress(player.getIpAddress());

				if (p.getPort() == -1)
					p.setPort(player.getPort());

				alreadyConnected = true;
			} else {
				System.out.println("In here");
				// Relay to the current connected player that there is a new
				// player
				sendData(packet.getData(), p.getIpAddress(), p.getPort());

				// Relay to the new player that the currently connect player
				// exists
				packet = new Packet00Login(p.getUsername());
				sendData(packet.getData(), player.getIpAddress(),
						player.getPort());
			}
		}
		if (!alreadyConnected) {
			connectedPlayers.add(player);
			System.out.println("Adding player " + connectedPlayers.size());
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
	 *
	 * @param data
	 */
	public void sendDataToAllClients(byte[] data) {
		for (PlayerMP p : connectedPlayers)
			sendData(data, p.getIpAddress(), p.getPort());
	}
}
