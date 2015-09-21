package game.control;

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
 * @author Bieleski, Bryers, Gill & Thompson MMXVI.
 *
 */
public class GameClient extends Thread {
	
	private InetAddress ipAddress;
	private DatagramSocket socket;
	
	// TODO this class needs to be made
	// private Game game
	
	// TODO this constructor needs to take in a Game paramter
	public GameClient(String ipAddress) {
		
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
			System.out.println("SERVER: " + new String(packet.getData()));}
	}
	
	public void sendData(byte[] data) {
		
		/* port 1331 might have to be changed */
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);

		// Send packet
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
