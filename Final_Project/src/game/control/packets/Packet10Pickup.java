package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

/**
 * Sent when a player picks up an item
 *
 * @author Cameron Bryers 300326848 MMXV
 *
 */
public class Packet10Pickup extends Packet {

	private String username;
	private int tileID, itemID;

	/**
	 * Constructor intended for sending data.
	 *
	 * @param data
	 *            - message to be sent
	 */
	public Packet10Pickup(byte[] data) {
		super(10);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.tileID = Integer.parseInt(dataArray[1]);
		this.itemID = Integer.parseInt(dataArray[2]);
	}

	/**
	 * Constructor intended for receiving data
	 *
	 * @param username
	 *            - username of player
	 * @param tileID
	 *            - tile that the object is on
	 * @param itemID
	 *            - ID of the item
	 */
	public Packet10Pickup(String username, int tileID, int itemID) {
		super(10);
		this.username = username;
		this.tileID = tileID;
		this.itemID = itemID;
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
		return ("10" + username + "," + tileID + "," + itemID).getBytes();
	}

	/**
	 * Get the player's username.
	 *
	 * @return player's username
	 */
	public final String getUsername() {
		return username;
	}

	/**
	 * Get the tile that the item is on.
	 *
	 * @return ID of the tile
	 */
	public final int getTileID() {
		return tileID;
	}

	/**
	 * Get the object that the player is interacting with.
	 *
	 * @return ID of the object
	 */
	public final int getItemID() {
		return itemID;
	}
}
