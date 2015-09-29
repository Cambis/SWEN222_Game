package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

/**
 * This packet is sent when a player is engaging another player.
 *
 * ENGAGE - 03 + String username
 *
 * @author Bieleski, Bryers, Gill & Thompson MMXV.
 *
 */
public class Packet03Engage extends Packet {

	private String username;

	public Packet03Engage(byte data[]) {
		super(03);
		this.username = readData(data);
	}

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

	public final String getUsername() {
		return username.trim();
	}
}
