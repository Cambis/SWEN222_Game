package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

/**
 * This packet is sent when a client has moved.
 *
 * MOVE - 02 + "," + String username + "," + int id + "," + int pos.x + "," +
 * int pos.y + "," + int numOfSteps + "," + double z + "," + double direction
 *
 * @author Cameron Bryers 300326848 MMXV
 *
 */
public class Packet02Move extends Packet {

	private String username;
	private int uid;
	private double x, y, z;

	private boolean isMoving;
	private double direction;

	/**
	 * Constructor intended for sending data
	 *
	 * @param data
	 *            - message to be sent
	 */
	public Packet02Move(byte[] data) {

		super(02);

		// Having commas in the byte array makes it easier to read the
		// information off it
		String[] dataArray = readData(data).split(",");

		// Read the information
		this.username = dataArray[0];
		this.uid = Integer.parseInt(dataArray[1]);

		this.x = Double.parseDouble(dataArray[2]);
		this.z = Double.parseDouble(dataArray[3]);
		this.y = Double.parseDouble(dataArray[4]);

		this.isMoving = Integer.parseInt(dataArray[5]) == 1;
		this.direction = Double.parseDouble(dataArray[6]);
	}

	/**
	 * Constructor intended for receiving data
	 *
	 * @param username
	 *            - username of player
	 * @param id
	 *            - id of the player
	 * @param x
	 *            - player's x position (Tile)
	 * @param y
	 *            - player's y position (Tile)
	 * @param z
	 *            - player's z position (Tile)
	 * @param isMoving
	 *            - true if player is moving
	 * @param direction
	 *            - player's rotation around z (Tile)
	 */
	public Packet02Move(String username, int id, double x, double y, double z,
			boolean isMoving, double direction) {
		super(02);
		this.username = username;
		this.uid = id;
		this.x = x;
		this.z = y;
		this.y = z;
		this.isMoving = isMoving;
		this.direction = direction;
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
		return ("02" + username + "," + uid + "," + x + "," + z + "," + y + ","
				+ (isMoving ? 1 : 0) + "," + direction).getBytes();
	}

	/**
	 * Get the player's username.
	 *
	 * @return player's username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Get the player's x position (Tile)
	 *
	 * @return player's x position
	 */
	public double getX() {
		return x;
	}

	/**
	 * Get the player's z position (Tile)
	 *
	 * @return player's z position
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Get the player's y position (Tile)
	 *
	 * @return player's y position
	 */
	public double getY() {
		return y;
	}

	/**
	 * Get the player's rotation around z (Tile)
	 *
	 * @return player's rotation around z
	 */
	public double getDirection() {
		return direction;
	}
}