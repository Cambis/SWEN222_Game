package game.control;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Represents a server in a multiplayer game.
 * 
 * @author Bieleski, Bryers, Gill & Thompson MMXVI.
 *
 */
public class GameServer {

	private DatagramSocket socket;

	// TODO this class needs to be made
	// private Game game

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
			System.out.println("CLIENT: " + data);
			
			if (message.equals("ping"))
				sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
		}
	}

	public void sendData(byte[] data, InetAddress ipAddress, int port) {

		/* port 1331 might have to be changed */
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);

		// Send packet
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
