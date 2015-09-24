package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

public class Packet00Login extends Packet {

	private final String username;

	/**
	 * Retrieving data
	 * @param data
	 */
	public Packet00Login(byte[] data) {
		super(00);
		this.username = readData(data);
	}

	/**
	 * Sending data
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
