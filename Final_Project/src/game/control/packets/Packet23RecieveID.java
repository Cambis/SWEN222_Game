package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

/**
 * Sent when the client is given a unique ID.
 *
 * @author Cameron Bryers 300326848 MMXV
 *
 *         RECEIVE_ID = 23 + int ID
 */
public class Packet23RecieveID extends Packet {

	private int uid;

	/**
	 * Constructor intended for sending data.
	 *
	 * @param data
	 *            - message to be sent
	 */
	public Packet23RecieveID(byte[] data) {
		super(23);
		this.uid = Integer.parseInt(readData(data).trim());
	}

	/**
	 * Constructor intended for receiving data.
	 *
	 * @param id
	 *            - unique ID
	 */
	public Packet23RecieveID(int id) {
		super(23);
		this.uid = id;
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
		return ("23" + uid).getBytes();
	}

	/**
	 * Get the generated ID
	 *
	 * @return the uid
	 */
	public final int getID() {
		return uid;
	}
}
