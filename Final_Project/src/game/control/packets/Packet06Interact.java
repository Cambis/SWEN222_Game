package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

/**
 * This packet is sent when a player interacts with an object.
 *
 * INTERACT - 06 + String username + "," + int objectID
 *
 * @author Bieleski, Bryers, Gill & Thompson MMXV.
 *
 */
public class Packet06Interact extends Packet {

	private String username;
	private int objectID;

	public Packet06Interact(byte data[]) {

		super(06);

		// Having commas in the byte array makes it easier to read the
		// information off it
		String[] dataArray = readData(data).split(",");

		// Read the information
		this.username = dataArray[0];
		this.objectID = Integer.parseInt(dataArray[1]);
	}

	public Packet06Interact(String username, int objectID) {
		super(06);
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
		return ("06" + username + "," + objectID).getBytes();
	}

	public final String getUsername() {
		return username.trim();
	}

	public final int getObjectID() {
		return objectID;
	}

}
