package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

/**
 * This packet is sent when a player equips an item.
 *
 * EQUIP - 07 + String username + "," + objectID
 *
 * @author Cameron Bryers 300326848 MMXV
 *
 */
public class Packet07Equip extends Packet {

	private String username;
	private int objectID;

	/**
	 * Constructor intended for sending data
	 *
	 * @param data
	 *            - message to be sent
	 */
	public Packet07Equip(byte data[]) {

		super(07);

		// Having commas in the byte array makes it easier to read the
		// information off it
		String[] dataArray = readData(data).split(",");

		// Read the information
		this.username = dataArray[0];
		this.objectID = Integer.parseInt(dataArray[1]);
	}

	/**
	 * Constructor intended for receiving data.
	 *
	 * @param username
	 *            - username of player
	 * @param objectID
	 *            - object that the player is interacting with
	 */
	public Packet07Equip(String username, int objectID) {
		super(07);
		this.username = username;
		this.objectID = objectID;
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
		return ("07" + username + "," + objectID).getBytes();
	}

	/**
	 * Get the player's username.
	 *
	 * @return player's username
	 */
	public final String getUsername() {
		return username.trim();
	}
}
