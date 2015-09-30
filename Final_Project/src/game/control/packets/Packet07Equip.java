package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

/**
 * This packet is sent when a player equips an item.
 *
 * EQUIP - 07 + String username + "," + objectID
 *
 * @author Bieleski, Bryers, Gill & Thompson MMXV.
 *
 */
public class Packet07Equip extends Packet {

	private String username;
	private int objectID;

	public Packet07Equip(byte data[]) {

		super(07);

		// Having commas in the byte array makes it easier to read the
		// information off it
		String[] dataArray = readData(data).split(",");

		// Read the information
		this.username = dataArray[0];
		this.objectID = Integer.parseInt(dataArray[1]);
	}

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

	public final String getUsername() {
		return username.trim();
	}

	public final int getObjectID() {
		return objectID;
	}

}
