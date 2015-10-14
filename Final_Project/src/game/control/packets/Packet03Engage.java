package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

/**
 * This packet is sent when a player is engaging another player.
 *
 * ENGAGE - 03 + String username
 *
 * @author Cameron Bryers 300326848 MMXV
 *
 */
public class Packet03Engage extends Packet {

	private String username;

	/**
	 * Constructor intended for sending data.
	 *
	 * @param data
	 *            - message to be sent
	 */
	public Packet03Engage(byte data[]) {
		super(03);
		this.username = readData(data);
	}

	/**
	 * Constructor intended for receiving data.
	 * 
	 * @param username
	 *            - username of the player
	 */
	public Packet03Engage(String username) {
		super(03);
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
		return ("03" + username).getBytes();
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
