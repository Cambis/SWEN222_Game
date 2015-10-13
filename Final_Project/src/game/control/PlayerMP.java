package game.control;

import game.logic.Player;

import java.net.InetAddress;

/**
 * This class should do the multiplayer connecting side, player logic should be
 * left to Player.java.
 *
 * @author Cameron Bryers 300326848 MMXV
 *
 */
public class PlayerMP extends Player {

	private InetAddress ipAddress;
	private int port;
	private int uid = 00000;

	/**
	 * Default constructor, creates a Player that can be sent over the server.
	 *
	 * @param username
	 *            - username of the player
	 * @param x
	 *            - player's x position (Tile x, Renderer x)
	 * @param y
	 *            - player's y position (Tile y, Renderer z)
	 * @param rotation
	 *            - players rotation around: (Tile z) (Renderer y)
	 * @param ipAddress
	 *            - IP address of client
	 * @param port
	 *            - port of client
	 */
	public PlayerMP(String username, double x, double y, double rotation,
			InetAddress ipAddress, int port) {
		super(username, 0.2, 0.2, rotation);
		this.ipAddress = ipAddress;
		this.port = port;
	}

	/**
	 * Creates a player with its position and rotation defaulted to (0, 0, 0).
	 *
	 * @param username
	 *            - username of the player
	 * @param ipAddress
	 *            - IP address of client
	 * @param port
	 *            - port of client
	 */
	public PlayerMP(String username, InetAddress ipAddress, int port) {
		this(username, 0, 0, 0, ipAddress, port);
	}

	/**
	 * Set IP address of the player.
	 *
	 * @param ipAddress
	 *            - new IP address of the player
	 */
	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * Get the IP address of the player.
	 *
	 * @return current IP Address of the player
	 */
	public final InetAddress getIpAddress() {
		return ipAddress;
	}

	/**
	 * Set the port of the player.
	 *
	 * @param port
	 *            - new port of the player
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Get the port of the player.
	 *
	 * @return current port of the player
	 */
	public final int getPort() {
		return port;
	}

	/**
	 * Set the ID of the player.
	 *
	 * @param id
	 *            - new ID for the player
	 */
	public void setID(int id) {
		uid = id;
	}

	/**
	 * Get the ID of the player.
	 *
	 * @return current ID of the player
	 */
	public final int getID() {
		return uid;
	}
}
