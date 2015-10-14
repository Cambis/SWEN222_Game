package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

/**
 * This packet is sent to the server when a player connects to a game.
 *
 * LOGIN - 00 + (String username) + "," + int id + "," + double x + "," + double
 * z + "," + double rotation
 *
 * @author Cameron Bryers 300326848 MMXV
 *
 */
public class Packet00Login extends Packet {

	private final String username;
	private final double x, z, rotation;
	private final int uid;

	/**
	 * Constructor intended for sending data
	 *
	 * @param data
	 *            - message to be sent
	 */
	public Packet00Login(byte[] data) {
		super(00);

		String[] dataArray = readData(data).split(",");

		this.username = dataArray[0];
		this.uid = Integer.parseInt(dataArray[1]);
		this.x = Double.parseDouble(dataArray[2]);
		this.z = Double.parseDouble(dataArray[3]);
		this.rotation = Double.parseDouble(dataArray[4]);
	}

	/**
	 * Constructor intended for receiving data.
	 *
	 * @param username
	 *            - username of the player
	 * @param uid
	 *            - ID of the player
	 * @param x
	 *            - player's x position (Renderer)
	 * @param z
	 *            - player's z position (Renderer)
	 * @param rotation
	 *            - player's rotation around y (Renderer)
	 */
	public Packet00Login(String username, int uid, double x, double z,
			double rotation) {
		super(00);
		this.username = username;
		this.uid = uid;
		this.x = x;
		this.z = z;
		this.rotation = rotation;
	}

	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());
	}

	@Override
	public void writeData(GameServer server) {
		server.sendDataToAllClients(getData());
	}

	@Override
	public byte[] getData() {
		return ("00" + this.username + "," + uid + "," + x + "," + z + "," + rotation)
				.getBytes();
	}

	/**
	 * Get the player's username.
	 *
	 * @return player's username
	 */
	public String getUsername() {
		return username.trim();
	}

	/**
	 * Gets player's x position (Renderer).
	 *
	 * @return player's x position
	 */
	public final double getX() {
		return x;
	}

	/**
	 * Gets player's z position (Renderer).
	 *
	 * @return player's z position
	 */
	public final double getZ() {
		return z;
	}

	/**
	 * Gets player's rotation around the y axis (Renderer)
	 *
	 * @return player's rotation
	 */
	public final double getRotation() {
		return rotation;
	}

	/**
	 * Gets player's unique ID.
	 *
	 * @return player's ID
	 */
	public final int getID() {
		return uid;
	}
}
