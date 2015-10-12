package game.control;

import game.control.packets.Packet;
import game.control.packets.Packet.PacketType;
import game.control.packets.Packet00Login;
import game.control.packets.Packet01Disconnect;
import game.control.packets.Packet02Move;
import game.control.packets.Packet03Engage;
import game.control.packets.Packet04Damage;
import game.control.packets.Packet05Heal;
import game.control.packets.Packet06Interact;
import game.control.packets.Packet07Equip;
import game.control.packets.Packet10Pickup;
import game.control.packets.Packet22LoadLevel;
import game.control.packets.Packet23RecieveID;
import game.control.packets.Packet24TeamAssign;
import game.logic.StealthGame;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a server that the clients connect to in a multiplayer game.
 *
 * @author Cameron Bryers 300326848 MMXV
 *
 */
public class GameServer extends Thread {

	// Debugging Mode
	private static final boolean DEBUG = false;

	// Maximum amount of players allowed to connect
	public static final int MAX_PLAYERS = StealthGame.MAX_PLAYERS;

	// Minimum amount of players required to run the game
	public static final int MIN_PLAYERS = StealthGame.MIN_PLAYERS;

	// Used for generating unique IDs
	public static final int ID_PREFIX = 10000;

	// Number of players needed to start the game
	private int numOfPlayers;

	// True IFF game has started
	private boolean gameStarted = false;

	// Receives packets from the clients
	private DatagramSocket socket;

	// Reference to the host's game clas
	private StealthGame game;

	// Players connected to the server
	private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();

	/**
	 * Creates a new server that clients can connect to.
	 *
	 * @param game
	 *            - reference to the host's game
	 * @param numOfPlayers
	 *            - number of players required to run the game
	 */
	public GameServer(StealthGame game, int numOfPlayers) {

		this.game = game;
		this.numOfPlayers = numOfPlayers;

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
					"res/levels/pool.lvl");
			sendData(level.getData(), player.getIpAddress(), player.getPort());

			addConnection(player, (Packet00Login) packet);

			// Send prompt to the client when the min amount of players is
			// reached
			if (!gameStarted && connectedPlayers.size() >= numOfPlayers) {
				gameStarted = true;
				assignTeams();
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

		case PICKUP:
			packet = new Packet10Pickup(data);
			handlePickup((Packet10Pickup) packet);
			break;

		case EQUIP:
			packet = new Packet07Equip(data);
			handleEquip((Packet07Equip) packet);
			break;

		default:
			break;
		}
	}

	private void assignTeams() {

		// Create a randomly sorted list so the teams are chosen at random
		List<PlayerMP> playersList = new ArrayList<PlayerMP>(connectedPlayers);
		Collections.shuffle(playersList);

		// Put the information into an array
		String[] players = new String[playersList.size()];
		for (int i = 0; i < players.length; i++) {
			players[i] = playersList.get(i).getUsername();
		}

		// System.out.println("Players: " + playersList.size());
		// Put the players in teams 0: GUARD, 1: SPY
		String[] teams = new String[players.length];
		int mid = teams.length / 2;

		for (int i = 0; i < teams.length; i++) {
			if (i < mid)
				teams[i] = "0";
			else
				teams[i] = "1";
		}

		// Send the team assignment to all players on the server.
		Packet24TeamAssign packet = new Packet24TeamAssign(players, teams);
		packet.writeData(this);
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
		// System.out.println(player.getUsername());

		for (PlayerMP p : connectedPlayers) {
			// if (p.getUsername().equals(game.getPlayer().getUsername()))
			// break;
			if (p.getUsername().equalsIgnoreCase(player.getUsername())) {
				if (DEBUG)
					System.out.println("User already in " + p.getUsername());
				if (p.getIpAddress() == null)
					p.setIpAddress(player.getIpAddress());

				if (p.getPort() == -1)
					p.setPort(player.getPort());

				alreadyConnected = true;
			} else {

				// Relay to the current connected player that there is a new
				// player
				// sendData(packet.getData(), p.getIpAddress(), p.getPort());
				sendDataToAllClients(packet.getData());

				// Packet23RecieveID id = new Packet23RecieveID(p.getID());

				// sendData(id.getData(), p.getIpAddress(), p.getPort());

				// Relay to the new player that the currently connect player
				// exists
				packet = new Packet00Login(p.getUsername(), p.getID(),
						p.getX(), p.getY(), p.getRotation());
				sendData(packet.getData(), player.getIpAddress(),
						player.getPort());
			}
		}
		if (!alreadyConnected) {
			// player.setID(ID_PREFIX + connectedPlayers.size());
			// if (!hostConnected) {
			// ((PlayerMP) game.player).setID(ID_PREFIX
			// + connectedPlayers.size());
			// hostConnected = true;
			// }
			connectedPlayers.add(player);
			// System.out.println("Adding player " + player.getUsername() + ": "
			// + player.getID());
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

		// System.out.println("PACKET ID: " + packet.getID());
		// PlayerMP player = getPlayerMP(packet.getID());

		// if (player == null) {
		PlayerMP player = getPlayerMP(packet.getUsername());
		// }

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

	private void handlePickup(Packet10Pickup packet) {

		// If there is no player get out of this method
		if (getPlayerMP(packet.getUsername()) == null)
			return;

		PlayerMP player = getPlayerMP(packet.getUsername());

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
