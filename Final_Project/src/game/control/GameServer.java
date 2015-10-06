package game.control;

import game.control.packets.Packet;
import game.control.packets.Packet.PacketType;
import game.logic.StealthGame;
import game.control.packets.Packet00Login;
import game.control.packets.Packet01Disconnect;
import game.control.packets.Packet02Move;
import game.control.packets.Packet03Engage;
import game.control.packets.Packet04Damage;
import game.control.packets.Packet05Heal;
import game.control.packets.Packet06Interact;
import game.control.packets.Packet07Equip;
import game.control.packets.Packet20GameStart;
import game.control.packets.Packet22LoadLevel;
import game.control.packets.Packet23RecieveID;
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

	private static final boolean DEBUG = StealthGame.DEBUG;
	public static final int MIN_PLAYERS = 1;

	public static final int ID_PREFIX = 10000;

	private boolean gameStarted = false;

	private DatagramSocket socket;

	private StealthGame game;

	private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();
	private boolean hostConnected = false;

	public GameServer(StealthGame game) {

		this.game = game;

		// Setup socket
		try {
			/* might have to change port 1331 */
			this.socket = new DatagramSocket(1331);
		} catch (SocketException e) {
			e.printStackTrace();
		}
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
		if (DEBUG)
			System.out.println("TYPE: " + type.toString());

		switch (type) {
		case INVALID:
			break;
		case LOGIN:
			packet = new Packet00Login(data);
			System.out.println("[ " + address.getHostAddress() + " " + port
					+ " ] " + ((Packet00Login) packet).getUsername()
					+ " has connected...");
			PlayerMP player = new PlayerMP(
					((Packet00Login) packet).getUsername(), address, port);

			// XXX addConnection was here

			// Send level filepath
			Packet22LoadLevel level = new Packet22LoadLevel(
					"res/Levels/test1.lvl");
			sendData(level.getData(), player.getIpAddress(), player.getPort());

			addConnection(player, (Packet00Login) packet);

			// Send prompt to the client when the min amount of players is
			// reached
			// XXX: probably wont use this anymore
			if (!gameStarted && connectedPlayers.size() >= MIN_PLAYERS) {
				gameStarted = true;
				// sendDataToAllClients("20".getBytes());
			}
			break;

		case DISCONNECT:
			packet = new Packet01Disconnect(data);
			System.out.println("[ " + address.getHostAddress() + " " + port
					+ " ] " + ((Packet01Disconnect) packet).getUsername()
					+ " has left...");
			break;

		case MOVE:
			packet = new Packet02Move(data);
			handleMove((Packet02Move) packet);
			break;

		case ENGAGE:
			packet = new Packet03Engage(data);
			handleEngage((Packet03Engage) packet);
			break;

		case DAMAGE:
			packet = new Packet04Damage(data);
			handleDamage((Packet04Damage) packet);
			break;

		case HEAL:
			packet = new Packet05Heal(data);
			handleHeal((Packet05Heal) packet);
			break;

		case INTERACT:
			packet = new Packet06Interact(data);
			handleInteract((Packet06Interact) packet);
			break;

		case EQUIP:
			packet = new Packet07Equip(data);
			handleEquip((Packet07Equip) packet);
			break;

		default:
			break;
		}
	}

	/**
	 * Add a player that is trying to login to the server
	 *
	 * @param player
	 * @param packet
	 */
	public void addConnection(PlayerMP player, Packet00Login packet) {

		// Check that the connection doesn't already exist
		boolean alreadyConnected = false;

		// for (PlayerMP p : connectedPlayers)
		// System.out.println(p.getUsername());

		for (PlayerMP p : connectedPlayers) {
			if (p.getUsername().equalsIgnoreCase(player.getUsername())) {
				// System.out.println("User already in " + p.getUsername());
				if (p.getIpAddress() == null)
					p.setIpAddress(player.getIpAddress());

				if (p.getPort() == -1)
					p.setPort(player.getPort());

				alreadyConnected = true;
			} else {

				// Relay to the current connected player that there is a new
				// player
				sendData(packet.getData(), p.getIpAddress(), p.getPort());

				// Relay to the new player that the currently connect player
				// exists
				packet = new Packet00Login(p.getUsername(), p.getID(),
						p.getX(), p.getY(), p.getRotation());
				sendData(packet.getData(), player.getIpAddress(),
						player.getPort());
			}
		}
		if (!alreadyConnected) {
			player.setID(ID_PREFIX + connectedPlayers.size());
//			if (!hostConnected) {
//				((PlayerMP) game.player).setID(ID_PREFIX
//						+ connectedPlayers.size());
//				hostConnected = true;
//			}
			connectedPlayers.add(player);
			System.out.println("Adding player " + player.getUsername() + ": "
					+ player.getID());
			System.out.println("IP: " );
			Packet23RecieveID id = new Packet23RecieveID(ID_PREFIX + connectedPlayers.size());
			
			// sendData(id.getData(), player.getIpAddress(), player.getPort());
		}
	}

	/**
	 * Remove a player from a game
	 *
	 * @param packet
	 */
	public void removeConnection(Packet01Disconnect packet) {
		connectedPlayers.remove(getPlayerMP(packet.getUsername()));
		packet.writeData(this);
	}

	/**
	 * Handles a move command from the client
	 *
	 * @param packet
	 */
	private void handleMove(Packet02Move packet) {

		// If there is no player get out of this method
		if (getPlayerMP(packet.getUsername()) == null)
			return;

		System.out.println("PACKET ID: " + packet.getID());
		PlayerMP player = getPlayerMP(packet.getID());

		if (player == null) {
			player = getPlayerMP(packet.getUsername());
		}

		player.setX(packet.getX());
		player.setY(packet.getZ());
		player.setRot(packet.getDirection());
		packet.writeData(this);
	}

	private void handleEngage(Packet03Engage packet) {

		// If there is no player get out of this method
		if (getPlayerMP(packet.getUsername()) == null)
			return;

		PlayerMP player = getPlayerMP(packet.getUsername());

		// TODO update player fields here

		packet.writeData(this);
	}

	private void handleDamage(Packet04Damage packet) {

		// If there is no player get out of this method
		if (getPlayerMP(packet.getHitPlayer()) == null)
			return;

		PlayerMP player = getPlayerMP(packet.getHitPlayer());

		// TODO update player fields here

		packet.writeData(this);
	}

	private void handleHeal(Packet05Heal packet) {

		// If there is no player get out of this method
		if (getPlayerMP(packet.getUsername()) == null)
			return;

		PlayerMP player = getPlayerMP(packet.getUsername());

		// TODO update player fields here

		packet.writeData(this);
	}

	private void handleInteract(Packet06Interact packet) {

		// If there is no player get out of this method
		if (getPlayerMP(packet.getUsername()) == null)
			return;

		PlayerMP player = getPlayerMP(packet.getUsername());

		// TODO update player fields here

		packet.writeData(this);

	}

	private void handleEquip(Packet07Equip packet) {

		// If there is no player get out of this method
		if (getPlayerMP(packet.getUsername()) == null)
			return;

		PlayerMP player = getPlayerMP(packet.getUsername());

		// TODO update player fields here

		packet.writeData(this);

	}

	/**
	 * Returns a player given a user name
	 *
	 * @param username
	 * @return
	 */
	public PlayerMP getPlayerMP(String username) {
		for (PlayerMP p : connectedPlayers)
			if (p.getUsername().equals(username))
				return p;

		return null;
	}

	public PlayerMP getPlayerMP(int id) {
		for (PlayerMP p : connectedPlayers)
			if (p.getID() == id)
				return p;
		return null;
	}

	/**
	 * Send data to a specific client
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

	/**
	 * Get IP address so clients can connect to it.
	 *
	 * @return
	 */
	public String getIPAddress() {
		return socket.getInetAddress().getHostAddress();
	}

}
