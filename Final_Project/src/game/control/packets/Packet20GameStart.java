package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

/**
 * This packet is sent when the game is ready to start.
 *
 * GAME_START = 20
 *
 * @author Bieleski, Bryers, Gill & Thompson MMXV.
 *
 */
public class Packet20GameStart extends Packet {

	public Packet20GameStart() {
		super(20);
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
		return "20".getBytes();
	}

}
