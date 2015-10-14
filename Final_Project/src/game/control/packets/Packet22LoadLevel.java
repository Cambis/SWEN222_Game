package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

/**
 * This packet is sent when the server tells the client which level to load.
 *
 * LOAD_LEVEL = 22 + String filename
 *
 * @author Cameron Bryers 300326848 MMXV
 *
 */
public class Packet22LoadLevel extends Packet {

	private final String filename;

	/**
	 * Constructor intended for sending data
	 *
	 * @param data
	 *            - message to be sent
	 */
	public Packet22LoadLevel(byte data[]) {
		super(22);
		this.filename = readData(data);
	}

	public Packet22LoadLevel(String filename) {
		super(22);
		this.filename = filename;
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
		return ("22" + filename).getBytes();
	}

	/**
	 * Get the level to load.
	 *
	 * @return path of the level in res
	 */
	public String getFilename() {
		return filename.trim();
	}

}
