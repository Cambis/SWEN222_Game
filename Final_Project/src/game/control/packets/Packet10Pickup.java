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
	private int ID;

	public Packet10Pickup(byte[] data) {
		super(10);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.ID = Integer.parseInt(dataArray[1]);
	}

	public Packet10Pickup(String username, int iD) {
		super(10);
		this.username = username;
		ID = iD;
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
		return ("10" + username + "," + ID).getBytes();
	}

	public final String getUsername() {
		return username;
	}

	public final int getID() {
		return ID;
	}

}
