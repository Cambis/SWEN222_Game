package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

/**
 * This packet is sent when the game is over.
 *
 * GAME_OVER = 21 + String winner
 *
 * @author Bieleski, Bryers, Gill & Thompson MMXV.
 *
 */
public class Packet21GameOver extends Packet {

	private String winner;

	public Packet21GameOver(byte[] data) {
		super(21);
		this.winner = readData(data);
	}

	public Packet21GameOver(String winner) {
		super(21);
		this.winner = winner;
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
		return ("21" + winner).getBytes();
	}

}
