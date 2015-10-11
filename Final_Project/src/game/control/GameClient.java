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
import game.control.packets.Packet22LoadLevel;
import game.control.packets.Packet23RecieveID;
import game.control.packets.Packet24TeamAssign;
import game.logic.StealthGame;
import game.view.StartUpScreen;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Represents a client that connects to a host in a multiplayer game.
 *
 * @author Cameron Bryers 300326848 MMXV
 *
 */
public class GameClient extends Thread {

	// Debugging mode
	private static final boolean DEBUG = StealthGame.DEBUG;

	// IP address of the host
	private InetAddress ipAddress;

	// Receives packets from the server
	private DatagramSocket socket;

	// Reference to client's game class
	private StealthGame game;

	/**
	 * Creates a client that connects to a server.
	 *
	 * @param ipAddress
	 *            - IP address of the server, "localhost" if the client is also
	 *            the host
	 * @param game
	 *            - reference to the client's game class
	 */
	public GameClient(String ipAddress, StealthGame game) {

		this.game = game;

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
			game.stop();
			System.exit(0);
			new StartUpScreen();
		}

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

		if (StealthGame.DEBUG)
			System.out.println("Client TYPE: " + type.toString());

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
			game.removePlayer(((Packet01Disconnect) packet).getUsername());
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

		case GAME_START:
			// game.run();
			break;

		case GAME_OVER:
			game.stop();
			break;

		case LOAD_LEVEL:
			packet = new Packet22LoadLevel(data);
			handleLoadLevel((Packet22LoadLevel) packet);
			break;

		case RECIEVE_ID:
			packet = new Packet23RecieveID(data);
			handleReciveID((Packet23RecieveID) packet);
			break;

		case TEAM_ASSIGN:
			packet = new Packet24TeamAssign(data);
			handleTeamAssign((Packet24TeamAssign) packet);
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

		PlayerMP player = new PlayerMP(packet.getUsername(), packet.getX(),
				packet.getZ(), packet.getRotation(), address, port);

		// System.out.println("Adding player: " + packet.getUsername() + " "
		// + packet.getID());

		player.setID(packet.getID());
		game.addPlayer(player);
	}

	/**
	 * Handles a move from the server
	 *
	 * @param packet
	 */
	private void handleMove(Packet02Move packet) {
		game.movePlayer(packet.getUsername(), packet.getX(), packet.getZ(),
				packet.getDirection());
	}

	private void handleEngage(Packet03Engage packet) {
		// TODO If guard, the guard should fire his/ her gun.
	}

	private void handleDamage(Packet04Damage packet) {
		// TODO Decrease the health of the player
	}

	private void handleHeal(Packet05Heal packet) {
		// TODO Increase the health of the player
	}

	private void handleInteract(Packet06Interact packet) {
		System.out.println("In heres");
		game.handleInteract(packet.getUsername(), packet.getObjectID());
	}

	private void handleEquip(Packet07Equip packet) {

	}

	private void handleLoadLevel(Packet22LoadLevel packet) {
		// System.out.println(packet.getFilename());
		game.loadLevel(packet.getFilename());
	}

	private void handleReciveID(Packet23RecieveID packet) {
		((PlayerMP) game.getPlayer()).setID(packet.getID());
	}

	private void handleTeamAssign(Packet24TeamAssign packet) {
		game.setTeams(packet.getPlayers(), packet.getTeams());
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
