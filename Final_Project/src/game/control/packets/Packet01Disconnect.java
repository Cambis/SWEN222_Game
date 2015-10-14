package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

/**
 * This packet is sent to the server when a player disconnects from the game.
 *
 * DISCONNECT - 01 + String username
 *
 * @author Bieleski, Bryers, Gill & Thompson MMXV.
 *
 */
public class Packet01Disconnect extends Packet {

	private String username;

	/**
	 * Constructor intended for sending data
	 *
	 * @param data
	 */
	public Packet01Disconnect(byte data[]) {
		super(01);
		this.username = readData(data);
	}

	/**
	 * Constructor intended for receiving data
	 *
	 * @param username
	 *            - username of player
	 */
	public Packet01Disconnect(String username) {
		super(01);
		this.username = username;
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
		return ("01" + username).getBytes();
	}

	/**
	 * Gets username of player disconnect.
	 *
	 * @return username of player
	 */
	public final String getUsername() {
		return username.trim();
	}
}
