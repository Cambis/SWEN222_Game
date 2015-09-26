package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

/**
 * This packet is sent to the server when a player connects to a game.
 *
 * LOGIN - 00 + (String username)
 *
 * @author Bieleski, Bryers, Gill & Thompson MMXV.
 *
 */
public class Packet00Login extends Packet {

	private final String username;

	/**
	 * Constructor intended for sending data
	 *
	 * @param data
	 */
	public Packet00Login(byte[] data) {
		super(00);
		this.username = readData(data);
	}

	/**
	 * Constructor intended for receiving data
	 *
	 * @param username
	 */
	public Packet00Login(String username) {
		super(00);
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
		return ("00" + this.username).getBytes();
	}

	public String getUsername() {
		return username.trim();
	}
}
